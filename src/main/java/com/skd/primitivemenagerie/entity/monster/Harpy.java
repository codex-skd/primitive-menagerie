// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMob;
import com.skd.mobweavecore.entity.MMFlyingMob;

import com.skd.primitivemenagerie.core.PMLootTables;
import com.skd.primitivemenagerie.core.PMSoundEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Harpy, ported from {@code EntityHarpy}. A flying hostile mob that picks up its target and carries
 * it into the air before dropping it.
 *
 * <p>Simplification: the original used a bespoke {@code EntityHarpyFlyHelper}; in 1.21 the flying move
 * control inherited from {@code MMFlyingMob} is used instead. TODO(verify): the original also skipped
 * entity collision entirely.</p>
 *
 * @author Daveyx0
 */
public class Harpy extends MMFlyingMob implements IMultiMob {

    public Harpy(EntityType<? extends Harpy> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MMFlyingMob.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FLYING_SPEED, 0.5000000059604645D)
                .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AIHarpyLift(this, 1.5D, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        if (this.isVehicle()) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.25D, this.getDeltaMovement().z);

            if (!this.level().isClientSide
                    && (this.getDistanceToGround(this.blockPosition()) >= 20 || !this.level().isEmptyBlock(this.blockPosition().above()))) {
                this.ejectPassengers();
            }
        }

        super.tick();
    }

    public double getDistanceToGround(BlockPos pos) {
        for (int i = 0; i < 64; i++) {
            BlockPos current = pos.below(i);

            if (this.level().isEmptyBlock(current)) {
                continue;
            }

            return this.distanceToSqr(current.getX(), current.getY(), current.getZ());
        }

        return 20;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide && this.isVehicle()) {
            this.ejectPassengers();
        }

        return super.hurt(source, amount);
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            moveFunction.accept(passenger, this.getX(), this.getY() - passenger.getBbHeight(), this.getZ());

            if (passenger instanceof LivingEntity living) {
                living.setShiftKeyDown(false);
            }
        }
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return PMSoundEvents.ENTITY_HARPY_IDLE.get();
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.8F;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return PMSoundEvents.ENTITY_HARPY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return PMSoundEvents.ENTITY_HARPY_HURT.get();
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_HARPY);
    }

    /** Melee goal that lifts the target onto the harpy's back when it can see the sky. */
    public static class AIHarpyLift extends MeleeAttackGoal {
        public AIHarpyLift(Harpy harpy, double speed, boolean useLongMemory) {
            super(harpy, speed, useLongMemory);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();

                if (target.isVehicle() || !this.mob.level().canSeeSky(this.mob.blockPosition())) {
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(target);
                } else {
                    target.startRiding(this.mob);
                }
            }
        }
    }
}
