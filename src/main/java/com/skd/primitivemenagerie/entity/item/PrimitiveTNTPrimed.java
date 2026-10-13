// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.item;

import javax.annotation.Nullable;

import com.skd.primitivemenagerie.config.PMConfig;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

/**
 * Primed primitive TNT, ported from {@code EntityPrimitiveTNTPrimed}. Like vanilla primed TNT but
 * with a configurable explosion strength and a terrain-destruction config flag.
 *
 * @author Daveyx0
 */
public class PrimitiveTNTPrimed extends Entity {

    private static final EntityDataAccessor<Integer> FUSE =
            SynchedEntityData.defineId(PrimitiveTNTPrimed.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> STRENGTH =
            SynchedEntityData.defineId(PrimitiveTNTPrimed.class, EntityDataSerializers.FLOAT);

    @Nullable
    private LivingEntity tntPlacedBy;
    private int fuse;
    private float strength;

    public PrimitiveTNTPrimed(EntityType<? extends PrimitiveTNTPrimed> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
        this.fuse = 80;
        this.strength = 1.5F;
    }

    public PrimitiveTNTPrimed(EntityType<? extends PrimitiveTNTPrimed> type, Level level, double x, double y, double z,
            @Nullable LivingEntity igniter, float power, int fuse) {
        this(type, level);
        this.setPos(x, y, z);
        float f = (float) (Math.random() * (Math.PI * 2D));
        this.setDeltaMovement(-Math.sin(f) * 0.02D, 0.2D, -Math.cos(f) * 0.02D);
        this.setFuse(fuse);
        this.setStrength(power);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.tntPlacedBy = igniter;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(FUSE, 80);
        builder.define(STRENGTH, 1.5F);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04D;
    }

    @Override
    public void tick() {
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }

        --this.fuse;

        if (this.fuse <= 0) {
            this.discard();

            if (!this.level().isClientSide) {
                this.explode();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode() {
        boolean flag = PMConfig.getFestiveCreeperDestruction() && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        this.level().explode(this, this.getX(), this.getY() + (double) (this.getBbHeight() / 16.0F), this.getZ(),
                this.strength, flag, Level.ExplosionInteraction.TNT);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putShort("Fuse", (short) this.getFuse());
        compound.putFloat("Strength", this.getStrength());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setFuse(compound.getShort("Fuse"));
        this.setStrength(compound.getFloat("Strength"));
    }

    @Nullable
    public LivingEntity getTntPlacedBy() {
        return this.tntPlacedBy;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    public void setFuse(int fuse) {
        this.entityData.set(FUSE, fuse);
        this.fuse = fuse;
    }

    public void setStrength(float strength) {
        this.entityData.set(STRENGTH, strength);
        this.strength = strength;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (FUSE.equals(key)) {
            this.fuse = this.entityData.get(FUSE);
        }

        if (STRENGTH.equals(key)) {
            this.strength = this.entityData.get(STRENGTH);
        }

        super.onSyncedDataUpdated(key);
    }

    public int getFuse() {
        return this.fuse;
    }

    public float getStrength() {
        return this.strength;
    }
}
