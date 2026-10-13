// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.EnumSet;

import com.skd.mobweavecore.entity.IMultiMob;
import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;

/**
 * Blazing Juggernaut, ported from {@code EntityBlazingJuggernaut}. A fire-immune floating melee mob
 * with a charge attack that launches itself at its target.
 *
 * <p>Simplifications: {@code canTriggerWalking} (crop trampling) and the custom render brightness
 * overrides have no direct 1.21 equivalent and are not ported; the spawn light-level override is a
 * P6 spawn-placement concern.</p>
 *
 * @author Daveyx0
 */
public class BlazingJuggernaut extends Monster implements IMultiMob {

    /** Random offset used in the floating behaviour. */
    private float heightOffset = 0.5F;
    /** Ticks until the height offset is randomized. */
    private int heightOffsetUpdateTime;

    public BlazingJuggernaut(EntityType<? extends BlazingJuggernaut> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new AIChargeAttack(this));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BLAZE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

    @Override
    public void aiStep() {
        if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x, motion.y * 0.6D, motion.z);
        }

        if (this.level().isClientSide) {
            if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                this.level().playLocalSound(this.getX() + 0.5D, this.getY() + 0.5D, this.getZ() + 0.5D,
                        SoundEvents.BLAZE_BURN, this.getSoundSource(), 1.0F + this.random.nextFloat(),
                        this.random.nextFloat() * 0.7F + 0.3F, false);
            }

            for (int i = 0; i < 2; ++i) {
                this.level().addParticle(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE,
                        this.getX() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(),
                        this.getY() + this.random.nextDouble() * (double) this.getBbHeight(),
                        this.getZ() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(),
                        0.0D, 0.0D, 0.0D);
            }
        }

        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {
        if (this.isInWaterRainOrBubble()) {
            this.hurt(this.damageSources().drown(), 1.0F);
        }

        --this.heightOffsetUpdateTime;

        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.random.nextGaussian() * 3.0F;
        }

        LivingEntity target = this.getTarget();

        if (target != null
                && target.getY() + (double) target.getEyeHeight() > this.getY() + (double) this.getEyeHeight() + (double) this.heightOffset) {
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x, motion.y + (0.30000001192092896D - motion.y) * 0.30000001192092896D, motion.z);
        }

        super.customServerAiStep();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_BLAZINGJUGGERNAUT);
    }

    /** Charge-attack goal, ported from {@code EntityBlazingJuggernaut.AIChargeAttack}. */
    static class AIChargeAttack extends Goal {
        private final BlazingJuggernaut blaze;
        private int attackCooldown;

        public AIChargeAttack(BlazingJuggernaut blaze) {
            this.blaze = blaze;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.blaze.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void start() {
            this.attackCooldown = 0;
        }

        @Override
        public void tick() {
            LivingEntity target = this.blaze.getTarget();

            if (target == null) {
                return;
            }

            double distance = this.blaze.distanceToSqr(target);

            ++this.attackCooldown;

            if (distance < 5.0D) {
                if (this.attackCooldown > 10) {
                    this.attackCooldown = 0;
                    this.blaze.doHurtTarget(target);
                }

                this.blaze.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0D);

                if (target.getY() < this.blaze.getY()) {
                    Vec3 motion = this.blaze.getDeltaMovement();
                    this.blaze.setDeltaMovement(motion.x, motion.y - 0.1D, motion.z);
                }
            } else if (distance < 30.0D) {
                double dx = target.getX() - this.blaze.getX();
                double dy = target.getBoundingBox().minY + (double) (target.getBbHeight() / 2.0F)
                        - (this.blaze.getY() + (double) (this.blaze.getBbHeight() / 2.0F));
                double dz = target.getZ() - this.blaze.getZ();

                if (this.attackCooldown > 5) {
                    Vec3 motion = this.blaze.getDeltaMovement();
                    this.blaze.setDeltaMovement(
                            motion.x + (Math.signum(dx) * 0.5D - motion.x) * 0.8D,
                            motion.y + (Math.signum(dy) * 0.699999988079071D - motion.y) * 0.8D,
                            motion.z + (Math.signum(dz) * 0.5D - motion.z) * 0.8D);
                    this.blaze.setZza(1.5F);
                    this.attackCooldown = 0;
                }

                this.blaze.getLookControl().setLookAt(target, 10.0F, 10.0F);
            } else {
                this.blaze.getNavigation().stop();
                this.blaze.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0D);
            }
        }
    }
}
