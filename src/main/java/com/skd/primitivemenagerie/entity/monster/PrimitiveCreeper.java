// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import com.skd.mobweavecore.common.capabilities.CapabilityTameableEntity;
import com.skd.mobweavecore.common.capabilities.TameableEntityHandler;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/**
 * Base primitive creeper, ported from {@code EntityPrimitiveCreeper}. It keeps the vanilla creeper
 * mechanics and adds the growing-age bookkeeping the original copied from {@code EntityAgeable}
 * (used by thrown-egg spawns) plus the tamed-creeper handling of the original.
 *
 * <p>Simplification: the original shadowed the vanilla fuse with its own {@code timeSinceIgnited};
 * in 1.21 the vanilla swell state is reused, so {@code setIgnitedTime} becomes {@code setSwellDir}.</p>
 *
 * @author Daveyx0
 */
public class PrimitiveCreeper extends Creeper {

    public static final int BABY_AGE = -24000;

    private static final EntityDataAccessor<Boolean> DATA_BABY =
            SynchedEntityData.defineId(PrimitiveCreeper.class, EntityDataSerializers.BOOLEAN);

    protected int growingAge;
    protected int forcedAge;
    protected int forcedAgeTimer;

    public PrimitiveCreeper(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BABY, false);
    }

    @Override
    public void tick() {
        if (this.getTarget() != null) {
            TameableEntityHandler tameable = CapabilityTameableEntity.getCapability(this);

            if (tameable.isTamed() && tameable.getFollowState() == 0) {
                this.setSwellDir(-1);
            }
        }

        super.tick();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        TameableEntityHandler tameable = CapabilityTameableEntity.getCapability(this);

        if (tameable.isTamed() && (source.is(DamageTypeTags.IS_FALL)
                || source.is(DamageTypeTags.IS_DROWNING)
                || source.is(DamageTypes.IN_WALL)
                || source.is(DamageTypes.FIREWORKS))) {
            return false;
        }

        return super.hurt(source, amount);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.level().isClientSide) {
            if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                            this.getX() + (this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
                            this.getY() + 0.5D + this.random.nextFloat() * this.getBbHeight(),
                            this.getZ() + (this.random.nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
                            0.0D, 0.0D, 0.0D);
                }

                --this.forcedAgeTimer;
            }
        } else {
            int i = this.getGrowingAge();

            if (i < 0) {
                ++i;
                this.setGrowingAge(i);

                if (i == 0) {
                    this.onGrowingAdult();
                }
            } else if (i > 0) {
                --i;
                this.setGrowingAge(i);
            }
        }
    }

    public int getGrowingAge() {
        if (this.level().isClientSide) {
            return this.entityData.get(DATA_BABY) ? -1 : 1;
        }

        return this.growingAge;
    }

    public void ageUp(int growthSeconds, boolean updateForcedAge) {
        int i = this.getGrowingAge();
        int j = i;
        i = i + growthSeconds * 20;

        if (i > 0) {
            i = 0;

            if (j < 0) {
                this.onGrowingAdult();
            }
        }

        int k = i - j;
        this.setGrowingAge(i);

        if (updateForcedAge) {
            this.forcedAge += k;

            if (this.forcedAgeTimer == 0) {
                this.forcedAgeTimer = 40;
            }
        }

        if (this.getGrowingAge() == 0) {
            this.setGrowingAge(this.forcedAge);
        }
    }

    public void addGrowth(int growth) {
        this.ageUp(growth, false);
    }

    public void setGrowingAge(int age) {
        this.entityData.set(DATA_BABY, age < 0);
        this.growingAge = age;
        this.refreshDimensions();
    }

    protected void onGrowingAdult() {
    }

    public boolean isBaby() {
        return this.getGrowingAge() < 0;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_BABY.equals(key)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Age", this.getGrowingAge());
        compound.putInt("ForcedAge", this.forcedAge);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setGrowingAge(compound.getInt("Age"));
        this.forcedAge = compound.getInt("ForcedAge");
    }
}
