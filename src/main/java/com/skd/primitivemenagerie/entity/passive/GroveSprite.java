// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.passive;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMobPassive;
import com.skd.mobweavecore.util.ColorUtil;
import com.skd.mobweavecore.util.EntityUtil;
import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.core.PMItems;
import com.skd.primitivemenagerie.core.PMSoundEvents;
import com.skd.primitivemenagerie.entity.ai.GroveSpriteTemptGoal;
import com.skd.primitivemenagerie.network.PrimitiveColorSapPayload;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Grove Sprite, ported from {@code EntityGroveSprite}. A small guardian of the forest that takes on
 * the colors of a nearby tree, plants saplings and can turn into a fire-fed "cinder" variant.
 *
 * <p>Simplifications: the DynamicTrees integration of the original is not ported (the Mobweave Core
 * tree search is used); the sap color and log type are stored on the held wonder-sap item through its
 * data components; the render-time pivot rotation of the leaf layer is done by the renderer.</p>
 *
 * @author Daveyx0
 */
public class GroveSprite extends PathfinderMob implements IMultiMobPassive {

    private static final EntityDataAccessor<Boolean> IS_CINDER =
            SynchedEntityData.defineId(GroveSprite.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SAPLING_AMOUNT =
            SynchedEntityData.defineId(GroveSprite.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockState> LEAVES =
            SynchedEntityData.defineId(GroveSprite.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<BlockState> LOG =
            SynchedEntityData.defineId(GroveSprite.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<BlockPos> LEAVES_POS =
            SynchedEntityData.defineId(GroveSprite.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> IS_BEGGING =
            SynchedEntityData.defineId(GroveSprite.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CAN_DESPAWN =
            SynchedEntityData.defineId(GroveSprite.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SAPLING_TIMER =
            SynchedEntityData.defineId(GroveSprite.class, EntityDataSerializers.INT);

    private float leavesR;
    private float leavesG;
    private float leavesB;
    private float logR;
    private float logG;
    private float logB;
    private float logTopR;
    private float logTopG;
    private float logTopB;
    private boolean changedColor;

    /** Color the wonder sap should carry, set through {@code PrimitiveColorSapPayload}. */
    private int sapColor = 0xFFFFFFFF;

    public GroveSprite(EntityType<? extends GroveSprite> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000298023224D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        int prio = 0;
        this.goalSelector.addGoal(++prio, new FloatGoal(this));
        this.goalSelector.addGoal(++prio, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(++prio, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(++prio, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(++prio, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(++prio, new AIManageSaplings(this));
        this.goalSelector.addGoal(++prio, new GroveSpriteTemptGoal(this, 1.1D, false,
                java.util.Set.of(this.getHeldItemForTempt(), new ItemStack(Items.BONE_MEAL))));
        this.goalSelector.addGoal(++prio, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    private ItemStack getHeldItemForTempt() {
        ItemStack held = this.getMainHandItem();
        return held.isEmpty() ? new ItemStack(Items.BONE_MEAL) : held.copy();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_CINDER, false);
        builder.define(SAPLING_AMOUNT, 0);
        builder.define(LEAVES, Blocks.OAK_LEAVES.defaultBlockState());
        builder.define(LOG, Blocks.OAK_LOG.defaultBlockState());
        builder.define(LEAVES_POS, BlockPos.ZERO);
        builder.define(CAN_DESPAWN, true);
        builder.define(IS_BEGGING, false);
        builder.define(SAPLING_TIMER, 0);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
            @Nullable SpawnGroupData spawnData) {
        if (!this.level().isClientSide) {
            this.determineLogAndLeaves();

            ItemStack sapling = this.getSaplingStack();

            if (this.getMainHandItem().isEmpty() && !sapling.isEmpty()) {
                this.setItemSlot(EquipmentSlot.MAINHAND, sapling);
            }

            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(PMItems.WONDER_SAP.get()));

            this.setSaplingAmount(1 + this.random.nextInt(4));
            this.setSaplingTimer(this.random.nextInt(1000) + 1000);
        }

        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    private void determineLogAndLeaves() {
        Object[] tree = EntityUtil.searchTree(this, 10);

        if (tree != null && tree.length > 0 && tree[0] != null && tree[1] != null) {
            this.setLog((BlockState) tree[0]);
            this.setLeaves((BlockState) tree[1]);
            this.setLeavesPos((BlockPos) tree[2]);
        }
    }

    private ItemStack getSaplingStack() {
        Block leavesBlock = this.getLeaves().getBlock();
        ResourceLocation leavesName = BuiltInRegistries.BLOCK.getKey(leavesBlock);

        if (leavesName != null && leavesName.getPath().endsWith("_leaves")) {
            String saplingPath = leavesName.getPath().substring(0, leavesName.getPath().length() - "_leaves".length())
                    + "_sapling";
            ResourceLocation saplingName = ResourceLocation.fromNamespaceAndPath(leavesName.getNamespace(), saplingPath);
            var item = BuiltInRegistries.ITEM.get(saplingName);

            if (item != null && item != Items.AIR) {
                return new ItemStack(item);
            }
        }

        return new ItemStack(this.getLog().getBlock());
    }

    @Override
    public void tick() {
        if (this.isOnFire()) {
            this.setCinderSprite(true);
            this.changedColor = false;
        }

        if (this.isCinderSprite()) {
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY() + this.getBbHeight() + 0.2F,
                    this.getZ(), 0.0D, 0.0D, 0.0D);
        } else {
            this.setSaplingTimer(this.getSaplingTimer() - 1);

            if (this.getSaplingTimer() <= 0) {
                this.setSaplingAmount(this.getSaplingAmount() + 1);

                if (this.level().isClientSide) {
                    for (int i = 0; i < 8; i++) {
                        this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                                this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                                this.getY() + this.random.nextFloat() + 1.0D,
                                this.getZ() + (this.random.nextFloat() - this.random.nextFloat()), 0.0D, 0.0D, 0.0D);
                    }
                }

                this.setSaplingTimer(this.random.nextInt(1000) + 1000);
            }
        }

        if (this.level().isClientSide && !this.changedColor) {
            this.changedColor = true;

            if (!this.isCinderSprite()) {
                this.setLeavesRGB(this.getColor(this.getLeaves(), this.getLeavesPos(), null));
                this.setLogRGB(this.getColor(this.getLog(), null, Direction.WEST));
                this.setLogTopRGB(this.getColor(this.getLog(), null, null));

                ItemStack clientSap = this.getOffhandItem();

                if (!clientSap.isEmpty()) {
                    int[] logTop = this.getColor(this.getLog(), null, null);
                    int packed = (logTop[0] << 16) | (logTop[1] << 8) | logTop[2];
                    this.setSapColor(packed);
                    PacketDistributor.sendToServer(new PrimitiveColorSapPayload(packed, this.getUUID()));
                }
            } else {
                this.setLeavesRGB(new int[] {177, 100, 0});
                this.setLogRGB(new int[] {90, 86, 80});
                this.setLogTopRGB(new int[] {102, 98, 94});
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TORCH));
            }
        }

        super.tick();
    }

    /** Client-side block-color lookup mirroring the original {@code getColor}. */
    private int[] getColor(BlockState state, @Nullable BlockPos pos, @Nullable Direction face) {
        if (state.getBlock() != Blocks.AIR) {
            int[] newColor = face != null
                    ? ColorUtil.getBlockStateColor(state, pos, this.level(), face, true)
                    : ColorUtil.getBlockStateColor(state, pos, this.level(), true);

            if (newColor != null) {
                if (ColorUtil.isColorInvalid(newColor)) {
                    if (state.getBlock() == this.getLeaves().getBlock()) {
                        newColor = new int[] {79, 146, 38, 255};
                    } else if (state.getBlock() == this.getLog().getBlock() && face == Direction.WEST) {
                        newColor = new int[] {70, 59, 46, 255};
                    } else {
                        newColor = new int[] {152, 126, 98, 255};
                    }
                }

                return newColor;
            }
        }

        return new int[] {255, 255, 255, 255};
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isCinderSprite()
                && (source.is(DamageTypeTags.IS_FIRE) || source.is(net.minecraft.world.damagesource.DamageTypes.IN_WALL))) {
            return false;
        }

        return super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean flag = super.doHurtTarget(target);

        if (flag && this.isCinderSprite()) {
            target.igniteForSeconds(8.0F);
        }

        return flag;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (this.isCinderSprite()) {
            return InteractionResult.PASS;
        }

        boolean flag = ItemStack.isSameItemSameComponents(itemstack, this.getMainHandItem());

        if (flag) {
            this.consumeItemFromStack(player, itemstack);

            if (this.canDespawn()) {
                this.setCanDespawn(false);
            }

            this.setSaplingAmount(this.getSaplingAmount() + 1);
            this.spawnHappyParticles();
            this.playSound(PMSoundEvents.ENTITY_GROVESPRITE_THANKS.get(), 1.0F, 1.0F);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (itemstack.is(Items.WHEAT_SEEDS)) {
            this.consumeItemFromStack(player, itemstack);

            for (int i = 0; i < 8; i++) {
                this.level().addParticle(ParticleTypes.HEART,
                        this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                        this.getY() + this.random.nextFloat() + 1.0D,
                        this.getZ() + (this.random.nextFloat() - this.random.nextFloat()), 0.0D, 0.0D, 0.0D);
            }

            this.playSound(PMSoundEvents.ENTITY_GROVESPRITE_THANKS.get(), 1.0F, 1.0F);
            this.setHealth(this.getMaxHealth());
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (itemstack.is(Items.BONE_MEAL) && this.getSaplingAmount() > 0) {
            this.consumeItemFromStack(player, itemstack);

            for (int i = 0; i < 8; i++) {
                this.level().addParticle(ParticleTypes.CRIT,
                        this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                        this.getY() + this.random.nextFloat() + 1.0D,
                        this.getZ() + (this.random.nextFloat() - this.random.nextFloat()), 0.0D, 0.0D, 0.0D);
            }

            this.setSaplingAmount(this.getSaplingAmount() - 1);
            this.playSound(PMSoundEvents.ENTITY_GROVESPRITE_THANKS.get(), 1.0F, 1.0F);

            if (!this.level().isClientSide) {
                ItemStack sap = this.getOffhandItem().copy();
                sap.setCount(this.random.nextInt(4) + 1);
                this.spawnAtLocation(sap);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    private void spawnHappyParticles() {
        for (int i = 0; i < 8; i++) {
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                    this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                    this.getY() + this.random.nextFloat() + 1.0D,
                    this.getZ() + (this.random.nextFloat() - this.random.nextFloat()), 0.0D, 0.0D, 0.0D);
        }
    }

    private void consumeItemFromStack(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);

        if (this.isCinderSprite()) {
            this.spawnAtLocation(new ItemStack(Items.COAL, this.random.nextInt(5)));
        }
    }

    @Override
    public float getVoicePitch() {
        return this.isCinderSprite()
                ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.5F
                : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return PMSoundEvents.ENTITY_GROVESPRITE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return PMSoundEvents.ENTITY_GROVESPRITE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return PMSoundEvents.ENTITY_GROVESPRITE_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    // ------------------------------------------------------------------------------------------
    // Synched state accessors
    // ------------------------------------------------------------------------------------------
    public void setCinderSprite(boolean cinder) {
        this.entityData.set(IS_CINDER, cinder);
    }

    public boolean isCinderSprite() {
        return this.entityData.get(IS_CINDER);
    }

    public void setSaplingAmount(int amount) {
        this.entityData.set(SAPLING_AMOUNT, amount);
    }

    public int getSaplingAmount() {
        return this.entityData.get(SAPLING_AMOUNT);
    }

    public void setSaplingTimer(int timer) {
        this.entityData.set(SAPLING_TIMER, timer);
    }

    public int getSaplingTimer() {
        return this.entityData.get(SAPLING_TIMER);
    }

    public void setLeaves(BlockState leaves) {
        this.entityData.set(LEAVES, leaves);
    }

    public BlockState getLeaves() {
        return this.entityData.get(LEAVES);
    }

    public void setLog(BlockState log) {
        this.entityData.set(LOG, log);
    }

    public BlockState getLog() {
        return this.entityData.get(LOG);
    }

    public void setLeavesPos(BlockPos pos) {
        this.entityData.set(LEAVES_POS, pos);
    }

    public BlockPos getLeavesPos() {
        return this.entityData.get(LEAVES_POS);
    }

    public void setIsBegging(boolean begging) {
        this.entityData.set(IS_BEGGING, begging);
    }

    public boolean isBegging() {
        return this.entityData.get(IS_BEGGING);
    }

    public void setCanDespawn(boolean canDespawn) {
        this.entityData.set(CAN_DESPAWN, canDespawn);
    }

    public boolean canDespawn() {
        return this.entityData.get(CAN_DESPAWN);
    }

    public void setSapColor(int color) {
        this.sapColor = color;

        ItemStack sap = this.getOffhandItem();

        if (sap.getItem() instanceof com.skd.primitivemenagerie.item.ItemGroveSpriteSap) {
            com.skd.primitivemenagerie.item.ItemGroveSpriteSap.setColor(sap, color);
            com.skd.primitivemenagerie.item.ItemGroveSpriteSap.setSapLogState(sap, this.getLog());
        }
    }

    public int getSapColor() {
        return this.sapColor;
    }

    public float[] getLeavesRGB() {
        return new float[] {this.leavesR, this.leavesG, this.leavesB};
    }

    public void setLeavesRGB(int[] rgb) {
        this.leavesR = rgb[0];
        this.leavesG = rgb[1];
        this.leavesB = rgb[2];
    }

    public float[] getLogRGB() {
        return new float[] {this.logR, this.logG, this.logB};
    }

    public void setLogRGB(int[] rgb) {
        this.logR = rgb[0];
        this.logG = rgb[1];
        this.logB = rgb[2];
    }

    public float[] getLogTopRGB() {
        return new float[] {this.logTopR, this.logTopG, this.logTopB};
    }

    public void setLogTopRGB(int[] rgb) {
        this.logTopR = rgb[0];
        this.logTopG = rgb[1];
        this.logTopB = rgb[2];
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("LeavesPos", NbtUtils.writeBlockPos(this.getLeavesPos()));
        compound.put("LeavesState", NbtUtils.writeBlockState(this.getLeaves()));
        compound.put("LogState", NbtUtils.writeBlockState(this.getLog()));
        compound.putInt("SaplingAmount", this.getSaplingAmount());
        compound.putInt("SaplingTimer", this.getSaplingTimer());
        compound.putBoolean("isCinder", this.isCinderSprite());
        compound.putBoolean("canDespawn", this.canDespawn());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setLeavesPos(NbtUtils.readBlockPos(compound, "LeavesPos").orElse(BlockPos.ZERO));
        this.setLeaves(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("LeavesState")));
        this.setLog(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("LogState")));
        this.setSaplingAmount(compound.getInt("SaplingAmount"));
        this.setSaplingTimer(compound.getInt("SaplingTimer"));
        this.setCinderSprite(compound.getBoolean("isCinder"));
        this.setCanDespawn(compound.getBoolean("canDespawn"));
    }

    /**
     * Sapling-management goal, ported from {@code EntityAIManageSaplings}. Walks to a growth target and
     * either bonemeals it or plants the held sapling.
     */
    static class AIManageSaplings extends MoveToBlockGoal {
        private final GroveSprite sprite;
        private int manageDelay;
        private boolean isNearGoal;
        private int type;
        private boolean placeSapling = true;
        private boolean hasChosen;

        public AIManageSaplings(GroveSprite sprite) {
            super(sprite, 0.699999988079071D, 16);
            this.sprite = sprite;
            this.manageDelay = sprite.getRandom().nextInt(100) + 100;
        }

        @Override
        public boolean canUse() {
            this.manageDelay--;
            return this.searchForDestination() && this.manageDelay <= 0;
        }

        @Override
        public void tick() {
            super.tick();
            this.sprite.getLookControl().setLookAt(this.blockPos.getX() + 0.5D, this.blockPos.getY(),
                    this.blockPos.getZ() + 0.5D, 10.0F, this.sprite.getMaxHeadXRot());

            if (this.sprite.distanceToSqr(Vec3.atCenterOf(this.blockPos.above())) < 12.0D) {
                this.isNearGoal = true;
            }

            if (this.isReachedTarget()) {
                if (this.type == 0) {
                    this.manageDelay = this.sprite.getRandom().nextInt(200) + 200;
                    BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), this.sprite.level(), this.blockPos, null);

                    if (this.sprite.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                                this.blockPos.getX() + 0.5F + (this.sprite.getRandom().nextFloat() - this.sprite.getRandom().nextFloat()),
                                this.blockPos.getY() + 0.5F,
                                this.blockPos.getZ() + 0.5F + (this.sprite.getRandom().nextFloat() - this.sprite.getRandom().nextFloat()),
                                10, 0.0D, 0.0D, 0.0D, 0.0D);
                    }

                    this.isNearGoal = false;
                } else {
                    this.manageDelay = this.sprite.getRandom().nextInt(300) + 300;
                    ItemStack heldItem = this.sprite.getMainHandItem();

                    if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem blockItem) {
                        this.sprite.level().setBlock(this.blockPos, blockItem.getBlock().defaultBlockState(), 3);
                        this.sprite.level().playSound(null, this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ(),
                                SoundEvents.GLASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        this.sprite.setSaplingAmount(this.sprite.getSaplingAmount() - 1);
                    }

                    this.isNearGoal = false;
                }

                this.hasChosen = false;
                this.placeSapling = true;
            }
        }

        private boolean searchForDestination() {
            int range = 16;
            BlockPos origin = this.sprite.blockPosition();
            boolean mobGriefing = this.sprite.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING);

            if ((!mobGriefing || !PMConfig.getGroveSpritesPlant())
                    && (this.sprite.getSaplingAmount() <= 0 || (!this.hasChosen && this.sprite.getRandom().nextInt(20) != 0))) {
                this.placeSapling = false;
            }

            this.hasChosen = true;

            for (int k = 0; k <= 1; k = k > 0 ? -k : 1 - k) {
                for (int l = 0; l < range; ++l) {
                    for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                        for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                            BlockPos candidate = origin.offset(i1, k - 1, j1);

                            if (this.sprite.isWithinRestriction(candidate)) {
                                if (this.placeSapling && this.sprite.getSaplingAmount() > 0
                                        && this.shouldMoveTo(this.sprite.level(), candidate)) {
                                    this.blockPos = candidate;
                                    this.type = 1;
                                    return true;
                                } else if (!this.placeSapling && this.shouldMoveToSapling(this.sprite.level(), candidate)) {
                                    this.blockPos = candidate;
                                    this.type = 0;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

            this.hasChosen = false;
            return false;
        }

        private boolean shouldMoveToSapling(Level level, BlockPos pos) {
            ItemStack heldItem = this.sprite.getMainHandItem();

            if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem) {
                BlockState state = level.getBlockState(pos);

                if (state.getBlock() != this.sprite.getLeaves().getBlock()) {
                    BlockPos above = pos.above();

                    if (level.isEmptyBlock(above)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean shouldMoveTo(Level level, BlockPos pos) {
            ItemStack heldItem = this.sprite.getMainHandItem();

            if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem blockItem) {
                return blockItem.getBlock().defaultBlockState().canSurvive(level, pos);
            }

            return false;
        }

        @Override
        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            return this.placeSapling ? this.shouldMoveTo(this.sprite.level(), pos)
                    : this.shouldMoveToSapling(this.sprite.level(), pos);
        }
    }
}
