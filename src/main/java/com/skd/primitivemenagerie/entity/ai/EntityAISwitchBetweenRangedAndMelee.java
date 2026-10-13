// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.ai;

import java.util.EnumSet;

import com.skd.primitivemenagerie.entity.monster.SkeletonWarrior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;

/**
 * Skeleton Warrior combat goal, ported from {@code EntityAISwitchBetweenRangedAndMelee}. While the
 * skeleton holds a bow it keeps its distance and strafes like the vanilla bow goal; with a melee
 * weapon it charges and attacks.
 *
 * @author Daveyx0
 */
public class EntityAISwitchBetweenRangedAndMelee extends MeleeAttackGoal {

    private final SkeletonWarrior skeleton;
    private final double speedModifier;
    private final int attackIntervalMin;
    private final float attackRadiusSqr;
    private final float maxAttackDistance;

    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public EntityAISwitchBetweenRangedAndMelee(SkeletonWarrior skeleton, double speedAmplifier, int delay, float maxDistance) {
        super(skeleton, speedAmplifier, false);
        this.skeleton = skeleton;
        this.speedModifier = speedAmplifier;
        this.attackIntervalMin = delay;
        this.maxAttackDistance = maxDistance;
        this.attackRadiusSqr = maxDistance * maxDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    protected boolean isBowInMainhand() {
        return this.skeleton.isHolding(stack -> stack.getItem() instanceof BowItem);
    }

    @Override
    public boolean canUse() {
        return this.skeleton.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.skeleton.getTarget() != null;
    }

    @Override
    public void start() {
        super.start();
        this.skeleton.setAggressive(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.skeleton.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.skeleton.stopUsingItem();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.isBowInMainhand()) {
            this.tickRanged();
        } else {
            super.tick();
        }
    }

    private void tickRanged() {
        LivingEntity target = this.skeleton.getTarget();

        if (target == null) {
            return;
        }

        double distanceSqr = this.skeleton.distanceToSqr(target.getX(), target.getY(), target.getZ());
        boolean seen = this.skeleton.getSensing().hasLineOfSight(target);
        boolean wasSeen = this.seeTime > 0;

        if (seen != wasSeen) {
            this.seeTime = 0;
        }

        if (seen) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }

        if (distanceSqr <= this.attackRadiusSqr && this.seeTime >= 20) {
            this.skeleton.getNavigation().stop();
            ++this.strafingTime;
        } else {
            this.skeleton.getNavigation().moveTo(target, this.speedModifier);
            this.strafingTime = -1;
        }

        if (this.strafingTime >= 20) {
            if (this.skeleton.getRandom().nextFloat() < 0.3F) {
                this.strafingClockwise = !this.strafingClockwise;
            }

            if (this.skeleton.getRandom().nextFloat() < 0.3F) {
                this.strafingBackwards = !this.strafingBackwards;
            }

            this.strafingTime = 0;
        }

        if (this.strafingTime > -1) {
            if (distanceSqr > this.attackRadiusSqr * 0.75F) {
                this.strafingBackwards = false;
            } else if (distanceSqr < this.attackRadiusSqr * 0.25F) {
                this.strafingBackwards = true;
            }

            this.skeleton.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
            this.skeleton.lookAt(target, 30.0F, 30.0F);
        } else {
            this.skeleton.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }

        if (this.skeleton.isUsingItem()) {
            if (!seen && this.seeTime < -60) {
                this.skeleton.stopUsingItem();
            } else if (seen) {
                int i = this.skeleton.getTicksUsingItem();

                if (i >= 20) {
                    this.skeleton.stopUsingItem();
                    this.skeleton.performRangedAttack(target, BowItem.getPowerForTime(i));
                    this.attackTime = this.attackIntervalMin;
                }
            }
        } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
            this.skeleton.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.skeleton, stack -> stack instanceof BowItem));
        }
    }
}
