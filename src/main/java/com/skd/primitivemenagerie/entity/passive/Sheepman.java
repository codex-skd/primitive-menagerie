// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.passive;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMobPassive;
import com.skd.mobweavecore.entity.ai.TemptItemStackGoal;
import com.skd.primitivemenagerie.core.PMVillagerProfessions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.IShearable;

/**
 * Sheepman, ported from {@code EntitySheepman}. A villager-like sheep that can be shorn and bribed
 * with a gold block, and that turns into a vanilla sheep when struck by lightning.
 *
 * <p>Simplification: the 1.21 wool colors use {@link DyeColor}; the original meta-based dye mixing
 * for breeding is not ported. The death loot table relies on the vanilla sheep table (per-color wool
 * on death is not reproduced here; wool is still given when sheared).</p>
 *
 * @author Daveyx0
 */
public class Sheepman extends Villager implements IShearable, IMultiMobPassive {

    private static final EntityDataAccessor<Byte> DYE_COLOR =
            SynchedEntityData.defineId(Sheepman.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> CAN_DESPAWN =
            SynchedEntityData.defineId(Sheepman.class, EntityDataSerializers.BOOLEAN);

    private int sheepTimer;

    public Sheepman(EntityType<? extends Sheepman> type, Level level) {
        super(type, level);
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return Villager.createAttributes();
    }

    @Override
    protected net.minecraft.network.chat.Component getTypeName() {
        return net.minecraft.network.chat.Component.translatable(this.getType().getDescriptionId());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DYE_COLOR, (byte) 0);
        builder.define(CAN_DESPAWN, true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new TemptItemStackGoal(this, 1.1D, false, Set.of(new ItemStack(Items.GOLD_INGOT))));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
            @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);

        switch (this.random.nextInt(3)) {
            case 0 -> {
                this.setVillagerData(this.getVillagerData().setProfession(PMVillagerProfessions.SHEEPMAN_SCAVENGER.get()));
                this.setFleeceColor(DyeColor.RED);
            }
            case 1 -> {
                this.setVillagerData(this.getVillagerData().setProfession(PMVillagerProfessions.SHEEPMAN_ALCHEMIST.get()));
                this.setFleeceColor(DyeColor.ORANGE);
            }
            default -> {
                this.setVillagerData(this.getVillagerData().setProfession(PMVillagerProfessions.SHEEPMAN_THIEF.get()));
                this.setFleeceColor(DyeColor.BLACK);
            }
        }

        return data;
    }

    @Override
    public void tick() {
        if (this.level().isClientSide) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }

        super.tick();
    }

    @Override
    protected float getSoundVolume() {
        return 1.1111F;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                net.minecraft.resources.ResourceLocation.withDefaultNamespace("entities/sheep"));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.is(Items.GOLD_BLOCK) && this.canDespawn()) {
            this.consumeItemFromStack(player, itemstack);
            this.setCanDespawn(false);

            for (int i = 0; i < 8; i++) {
                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                        this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                        this.getY() + this.random.nextFloat() + 1.0D,
                        this.getZ() + (this.random.nextFloat() - this.random.nextFloat()), 0.0D, 0.0D, 0.0D);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    private void consumeItemFromStack(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    @Override
    public boolean isShearable(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        return !this.getSheared() && !this.isBaby();
    }

    @Override
    public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        this.setSheared(true);
        int i = 1 + this.random.nextInt(3);
        List<ItemStack> drops = new ArrayList<>();

        for (int j = 0; j < i; ++j) {
            var wool = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
                    net.minecraft.resources.ResourceLocation.withDefaultNamespace(
                            this.getFleeceColor().getName() + "_wool"));
            drops.add(new ItemStack(wool));
        }

        this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
        return drops;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt bolt) {
        if (!this.level().isClientSide && this.isAlive()) {
            Sheep sheep = EntityType.SHEEP.create(this.level());
            sheep.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
            sheep.finalizeSpawn(level, level.getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null);
            sheep.setNoAi(this.isNoAi());
            sheep.setColor(this.getFleeceColor());

            if (this.hasCustomName()) {
                sheep.setCustomName(this.getCustomName());
                sheep.setCustomNameVisible(this.isCustomNameVisible());
            }

            this.level().addFreshEntity(sheep);
            this.discard();
        }
    }

    public DyeColor getFleeceColor() {
        return DyeColor.byId(this.entityData.get(DYE_COLOR) & 15);
    }

    public void setFleeceColor(DyeColor color) {
        byte b0 = this.entityData.get(DYE_COLOR);
        this.entityData.set(DYE_COLOR, (byte) (b0 & 240 | color.getId() & 15));
    }

    public boolean getSheared() {
        return (this.entityData.get(DYE_COLOR) & 16) != 0;
    }

    public void setSheared(boolean sheared) {
        byte b0 = this.entityData.get(DYE_COLOR);

        if (sheared) {
            this.entityData.set(DYE_COLOR, (byte) (b0 | 16));
        } else {
            this.entityData.set(DYE_COLOR, (byte) (b0 & -17));
        }
    }

    public void setCanDespawn(boolean canDespawn) {
        this.entityData.set(CAN_DESPAWN, canDespawn);
    }

    public boolean canDespawn() {
        return this.entityData.get(CAN_DESPAWN);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return this.canDespawn();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Sheared", this.getSheared());
        compound.putByte("Color", (byte) this.getFleeceColor().getId());
        compound.putBoolean("canDespawn", this.canDespawn());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSheared(compound.getBoolean("Sheared"));
        this.setFleeceColor(DyeColor.byId(compound.getByte("Color")));
        this.setCanDespawn(compound.getBoolean("canDespawn"));
    }
}
