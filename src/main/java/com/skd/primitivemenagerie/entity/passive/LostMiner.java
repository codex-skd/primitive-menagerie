// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.passive;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMobPassive;
import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.core.PMVillagerProfessions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * Lost Miner, ported from {@code EntityLostMiner}. A scared miner villager that follows a nearby
 * player and is "saved" when it reaches open sky, rewarding the player with emeralds.
 *
 * @author Daveyx0
 */
public class LostMiner extends Villager implements IMultiMobPassive {

    private static final EntityDataAccessor<Boolean> IS_SAVED =
            SynchedEntityData.defineId(LostMiner.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private Player currentPlayer;

    public LostMiner(EntityType<? extends LostMiner> type, Level level) {
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
        builder.define(IS_SAVED, false);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
            @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);
        this.setVillagerData(this.getVillagerData().setProfession(PMVillagerProfessions.MINER.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_PICKAXE));
        return data;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.isSaved() && this.tickCount % 5 == 0) {
            Player near = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), 8.0D, false);
            Player veryNear = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), 2.0D, false);
            this.currentPlayer = (near != null && veryNear == null) ? near : null;

            if (!this.level().isClientSide) {
                float f = 0.01745278F;
                double d = this.getX() - Math.sin(this.getYRot() * f) / 3.0D;
                double d1 = this.getY() + this.random.nextDouble() / 3.0D;
                double d2 = this.getZ() + Math.cos(this.getYRot() * f) / 3.0D;
                this.level().addParticle(this.isNotScared() ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.SPLASH,
                        d, d1 + 1.8D, d2, 0.0D, 0.0D, 0.0D);
            }
        }

        if (!this.isSaved() && this.currentPlayer != null) {
            this.getNavigation().moveTo(this.currentPlayer, 0.65F);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.isSaved() && hand == InteractionHand.MAIN_HAND && player.getMainHandItem().isEmpty()) {
            boolean flag = this.isNotScared();

            if (flag) {
                this.setSaved(true);

                if (this.level().isClientSide) {
                    for (int i = 0; i < 8; i++) {
                        this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                                this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                                this.getY() + this.random.nextFloat() + 1.0D,
                                this.getZ() + (this.random.nextFloat() - this.random.nextFloat()), 0.0D, 0.0D, 0.0D);
                    }
                }

                if (!this.level().isClientSide) {
                    int[] range = this.getEmeralds();
                    int amount = (range.length > 0 ? range[0] : 0)
                            + (range.length > 1 ? this.random.nextInt(range[1]) : 0);

                    for (int j = 0; j < amount; ++j) {
                        this.spawnAtLocation(Items.EMERALD);
                    }
                }
            }

            return flag ? InteractionResult.sidedSuccess(this.level().isClientSide) : InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    public boolean isNotScared() {
        BlockPos eye = BlockPos.containing(this.getX(), this.getY() + this.getEyeHeight(), this.getZ());
        return this.level().canSeeSky(eye) && this.getY() > 50.0D;
    }

    public int[] getEmeralds() {
        return PMConfig.getLostMinerLootRange();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        if (PMConfig.getLostMinerSounds()) {
            return this.isTrading() ? SoundEvents.VILLAGER_TRADE : SoundEvents.VILLAGER_AMBIENT;
        }

        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return PMConfig.getLostMinerSounds() ? SoundEvents.VILLAGER_HURT : SoundEvents.GENERIC_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return PMConfig.getLostMinerSounds() ? SoundEvents.VILLAGER_DEATH : SoundEvents.GENERIC_HURT;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return !this.isSaved();
    }

    public void setSaved(boolean saved) {
        this.entityData.set(IS_SAVED, saved);
    }

    public boolean isSaved() {
        return this.entityData.get(IS_SAVED);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Saved", this.isSaved());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSaved(compound.getBoolean("Saved"));
    }
}
