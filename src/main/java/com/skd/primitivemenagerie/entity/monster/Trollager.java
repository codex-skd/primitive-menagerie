// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.List;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMob;
import com.skd.mobweavecore.entity.ai.SenseEntityNearestPlayerGoal;
import com.skd.mobweavecore.util.NBTUtil;
import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.core.PMEntities;
import com.skd.primitivemenagerie.core.PMSoundEvents;
import com.skd.primitivemenagerie.entity.IAnimatedMob;
import com.skd.primitivemenagerie.entity.ai.TrollagerAttacksGoal;
import com.skd.primitivemenagerie.entity.item.ThrownBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;

/**
 * Trollager, ported from {@code EntityTrollager}. A huge troll boss that disguises itself as stone in
 * daylight and throws blocks, smashes the ground and swipes at its target.
 *
 * <p>Simplifications: the original boat-collision and collision-box overrides when petrified, and the
 * {@code isNotColliding} override, are not expressible in 1.21 and are omitted; the explosion
 * particle network message is replaced by the vanilla explosion effect; the getCanSpawnHere check is
 * a P6 spawn-placement concern; the looting modifier of the loot drop is not available in the 1.21
 * {@code dropCustomDeathLoot} hook ({@code TODO(verify)}).</p>
 *
 * @author Daveyx0
 */
public class Trollager extends Monster implements IAnimatedMob, IMultiMob {

    private static final EntityDataAccessor<Integer> ANIMATION_STATE =
            SynchedEntityData.defineId(Trollager.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockPos> CURRENT_THROWN_BLOCK =
            SynchedEntityData.defineId(Trollager.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> IS_STONE =
            SynchedEntityData.defineId(Trollager.class, EntityDataSerializers.BOOLEAN);

    private int previousState;
    private float animVar;
    private float previousYawStone = -2.0F;
    private float previousPitchStone = -2.0F;
    private float previousYawHeadStone = -2.0F;
    public boolean isBeingSupported;

    public Trollager(EntityType<? extends Trollager> type, Level level) {
        super(type, level);
        this.isBeingSupported = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TrollagerAttacksGoal(this, 1.25D, 2.5F, 20.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new SenseEntityNearestPlayerGoal(this, 40));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION_STATE, 0);
        builder.define(CURRENT_THROWN_BLOCK, BlockPos.ZERO);
        builder.define(IS_STONE, false);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);

        if (this.random.nextInt(20) == 0) {
            Goblin goblin = new Goblin(PMEntities.GOBLIN.get(), this.level());
            goblin.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            this.level().addFreshEntity(goblin);
            goblin.startRiding(this);
        }

        return spawnGroupData;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, net.minecraft.world.entity.EntityDimensions dimensions, float partialTick) {
        return new Vec3(0.0D, this.getBbHeight() * 0.9D, 0.0D);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isVehicle() && !this.level().isClientSide) {
            Entity passenger = this.getFirstPassenger();

            if (passenger instanceof Mob mob && mob.getTarget() != null) {
                this.setTarget(mob.getTarget());
            }

            if (this.horizontalCollision) {
                this.getJumpControl().jump();
            }
        }

        if (this.getTarget() != null && !this.getTarget().isAlive()) {
            this.setTarget(null);
        }

        this.animationHandling();

        if (!(this.getAnimationState() == 1 || this.getAnimationState() == 2) && this.tickCount % 5 == 0) {
            this.setThrowingBlockFromFloor();
        }

        if (this.isBeingSupported) {
            this.setStone(false);
        }

        if (this.isStone()) {
            this.getNavigation().stop();
            this.setTarget(null);
            this.setNoAi(true);

            if (this.previousYawStone == -2.0F) {
                this.previousYawStone = this.getYRot();
                this.previousYawHeadStone = this.getYHeadRot();
                this.previousPitchStone = this.getXRot();
            }

            this.setYRot(this.previousYawStone);
            this.setXRot(this.previousPitchStone);
            this.setYHeadRot(this.previousYawHeadStone);

            if (this.isVehicle()) {
                Entity passenger = this.getFirstPassenger();

                if (passenger != null) {
                    passenger.stopRiding();
                }
            }
        } else {
            this.previousPitchStone = -2.0F;
            this.previousYawHeadStone = -2.0F;
            this.previousYawStone = -2.0F;
            this.setNoAi(false);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isStone()) {
            Entity attacker = source.getEntity();

            if (attacker instanceof LivingEntity living) {
                ItemStack stack = living.getMainHandItem();

                if (stack.isEmpty() || !stack.isCorrectToolForDrops(Blocks.STONE.defaultBlockState())) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return super.hurt(source, amount);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean hitByPlayer) {
        if (!this.level().isClientSide) {
            if (!this.isStone()) {
                int i = 1 + this.random.nextInt(2);

                for (int j = 0; j < i; ++j) {
                    ItemStack stack = this.getRandomLoot();

                    if (stack != null) {
                        this.spawnAtLocation(stack, 1.0F);
                    }
                }
            } else {
                this.spawnAtLocation(new ItemStack(Blocks.STONE, 32), 1.0F);
            }
        }
    }

    public ItemStack getRandomLoot() {
        int chance = this.random.nextInt(100);

        if (chance > 50) {
            return new ItemStack(Blocks.OBSIDIAN);
        } else if (chance > 10) {
            return new ItemStack(Items.EMERALD);
        } else {
            return new ItemStack(Items.GOLDEN_APPLE);
        }
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide) {
            if (this.level().isDay()) {
                if (this.level().canSeeSky(BlockPos.containing(this.getX(), this.getY() + (double) this.getEyeHeight(), this.getZ()))) {
                    if (!this.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD).isEmpty()) {
                        this.setStone(false);
                    } else {
                        this.setStone(true);
                    }
                } else {
                    this.setStone(false);
                }
            } else {
                this.setStone(false);
            }
        }

        super.aiStep();
    }

    public void setStone(boolean stone) {
        this.entityData.set(IS_STONE, stone);
    }

    public boolean isStone() {
        return this.entityData.get(IS_STONE);
    }

    public void animationHandling() {
        if (!this.isStone()) {
            if (this.getPreviousAnimationState() != this.getAnimationState()) {
                this.setPreviousAnimationState(this.getAnimationState());
                this.animVar = 0;
            }

            if (this.animVar < 1.0F) {
                this.animVar += 0.01F;
            } else {
                this.animVar = 1.0F;
            }
        }
    }

    public void setThrowingBlockFromFloor() {
        for (int i = 1; i < 64; i++) {
            BlockPos blockPos = BlockPos.containing(this.getX(), this.getBoundingBox().minY - i, this.getZ());

            if (this.getThrownBlock().equals(blockPos)) {
                break;
            }

            BlockState state = this.level().getBlockState(blockPos);

            if (state.isSolidRender(this.level(), blockPos)) {
                this.setThrownBlock(blockPos);
                break;
            }
        }
    }

    public boolean canBlockAreaSeeEntity(Entity entityIn) {
        boolean flag = true;

        for (int i = 0; i < 4; i++) {
            Vec3 from = new Vec3(this.getX(), this.getY() + (double) this.getEyeHeight() + i, this.getZ());
            Vec3 to = new Vec3(entityIn.getX(), entityIn.getY() + (double) entityIn.getEyeHeight(), entityIn.getZ());
            flag = this.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)) == null;
        }

        return flag;
    }

    @Override
    public void performAction(LivingEntity target, int id) {
        switch (id) {
            case 0: {
                ThrownBlock thrownBlock = new ThrownBlock(PMEntities.THROWN_BLOCK.get(), this.level(), this.getX(), this.getY(),
                        this.getZ(), this, this.getThrownBlock());
                thrownBlock.moveTo(this.getX(), this.getY() + 4.0D, this.getZ(), this.getYRot(), 0.0F);
                thrownBlock.setDeltaMovement((target.getX() - thrownBlock.getX()) / 18.0D,
                        (target.getY() - thrownBlock.getY()) / 18.0D + 0.5D,
                        (target.getZ() - this.getZ()) / 18.0D);
                this.level().addFreshEntity(thrownBlock);
                this.playSound(PMSoundEvents.ENTITY_TROLLAGER_ATTACK.get(), this.getSoundVolume(),
                        ((this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                break;
            }
            case 1: {
                double distanceX = (this.getLookControl().getWantedX() - this.getX());
                double distanceZ = (this.getLookControl().getWantedZ() - this.getZ());
                double length = Math.sqrt((distanceX * distanceX) + (distanceZ * distanceZ));

                if (length != 0) {
                    distanceX = distanceX / length;
                    distanceZ = distanceZ / length;
                }

                double addedHeight = 0.0D;

                if (target.getY() > this.getY()) {
                    addedHeight = 0.5D;
                } else if (target.getY() < this.getY()) {
                    addedHeight = -0.5D;
                }

                double explosionX = this.getX() + (distanceX * 2.0D);
                double explosionZ = this.getZ() + (distanceZ * 2.0D);
                boolean flag = this.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING)
                        && PMConfig.getTrollDestruction();
                this.level().explode(this, explosionX, this.getY() + this.getEyeHeight(), explosionZ, 3.0F, false,
                        flag ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
                this.playSound(PMSoundEvents.ENTITY_TROLLAGER_ATTACK.get(), this.getSoundVolume(),
                        ((this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                break;
            }
            case 2: {
                if (this.getTarget() != null && !this.isStone()) {
                    double d0 = this.getAttackReachSqr(this.getTarget()) + 4.0D;
                    double d1 = this.distanceToSqr(this.getTarget().getX(), this.getTarget().getBoundingBox().minY,
                            this.getTarget().getZ());
                    boolean flag = this.hasLineOfSight(this.getTarget());

                    if (d1 <= d0 && flag) {
                        this.doHurtTarget(this.getTarget());
                    }

                    this.playSound(PMSoundEvents.ENTITY_TROLLAGER_ATTACK.get(), this.getSoundVolume(),
                            ((this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                }
                break;
            }
            default:
                break;
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (double) (this.getBbWidth() * 2.0F * this.getBbWidth() * 2.0F + attackTarget.getBbWidth());
    }

    public void setThrownBlock(BlockPos pos) {
        this.entityData.set(CURRENT_THROWN_BLOCK, pos);
    }

    public BlockPos getThrownBlock() {
        BlockPos pos = this.entityData.get(CURRENT_THROWN_BLOCK);
        return pos != null ? pos : BlockPos.ZERO;
    }

    @Override
    public void setAnimationState(int state) {
        this.entityData.set(ANIMATION_STATE, state);
    }

    @Override
    public int getAnimationState() {
        return this.entityData.get(ANIMATION_STATE);
    }

    @Override
    public SoundEvent getAmbientSound() {
        if (this.isStone()) {
            return null;
        }

        return PMSoundEvents.ENTITY_TROLLAGER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        if (this.isStone()) {
            return SoundEvents.STONE_BREAK;
        }

        return PMSoundEvents.ENTITY_TROLLAGER_HIT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return PMSoundEvents.ENTITY_TROLLAGER_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.5F;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        NBTUtil.setBlockPosToNBT(this.getThrownBlock(), "ThrownBlock", compound);
        compound.putBoolean("Stone", this.isStone());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setThrownBlock(NBTUtil.getBlockPosFromNBT("ThrownBlock", compound));
        this.setStone(compound.getBoolean("Stone"));
    }

    @Override
    public int getPreviousAnimationState() {
        return this.previousState;
    }

    @Override
    public void setPreviousAnimationState(int state) {
        this.previousState = state;
    }

    @Override
    public float getAnimVar() {
        return this.animVar;
    }

    @Override
    public void setAnimVar(float var) {
        this.animVar = var;
    }
}
