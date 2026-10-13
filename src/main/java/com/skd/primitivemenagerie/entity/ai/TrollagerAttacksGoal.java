// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.ai;

import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.entity.IAnimatedMob;
import com.skd.primitivemenagerie.entity.monster.Trollager;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * Trollager attack state machine, ported from {@code EntityAITrollagerAttacks}. Chooses between a
 * melee swipe, a block throw and a ground smash, driving the {@link IAnimatedMob} animation states.
 *
 * @author Daveyx0
 */
public class TrollagerAttacksGoal extends Goal {

    private final Trollager entity;
    private final double moveSpeedAmp;
    private final float maxAttackDistance;
    private final float meleeAttackDistance;
    private int attackTime = -1;
    private int animTime = -1;
    private boolean isAttacking;

    public TrollagerAttacksGoal(Trollager entity, double moveSpeed, float meleeAttackDistance, float maxAttackDistance) {
        this.entity = entity;
        this.moveSpeedAmp = moveSpeed;
        this.maxAttackDistance = maxAttackDistance * maxAttackDistance;
        this.meleeAttackDistance = meleeAttackDistance * meleeAttackDistance;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null && !this.entity.isStone();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.attackTime = -1;
        this.animTime = -1;
        this.isAttacking = false;
        this.determineAttackAndPerform();
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAnimationState(0);
        this.attackTime = -1;
        this.animTime = -1;
        this.isAttacking = false;
    }

    public boolean checkCanThrow() {
        LivingEntity target = this.entity.getTarget();
        return target != null && this.entity.canBlockAreaSeeEntity(target);
    }

    @Override
    public void tick() {
        this.determineAttackAndPerform();
    }

    private void determineAttackAndPerform() {
        LivingEntity target = this.entity.getTarget();

        if (target == null) {
            return;
        }

        double distanceToEnemy = this.entity.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());

        boolean canSeeEnemy = this.entity.hasLineOfSight(target);
        this.entity.getNavigation().moveTo(target, this.moveSpeedAmp);
        boolean isWithinMeleeRange = distanceToEnemy <= (double) this.meleeAttackDistance;
        boolean isWithinAttackRange = distanceToEnemy <= (double) this.maxAttackDistance;
        boolean canThrowBlock = this.checkCanThrow();

        boolean canPerformMeleeAttack = isWithinMeleeRange && canSeeEnemy;
        boolean canPerformSmashAttack = PMConfig.getTrollDestruction()
                && ((isWithinAttackRange && (!canSeeEnemy || !canThrowBlock)) || this.entity.getRandom().nextInt(4) == 0)
                && !this.entity.isVehicle();
        boolean canPerformThrowAttack = canSeeEnemy && canThrowBlock && !this.entity.isVehicle();

        if (--this.attackTime <= 0 && this.isAttacking) {
            if (this.entity.getAnimationState() == 3) {
                this.entity.performAction(target, 1);
                this.entity.setAnimationState(4);
                this.animTime = 20;
            } else if (this.entity.getAnimationState() == 0) {
                this.entity.performAction(target, 2);
                this.entity.setAnimationState(6);
                this.animTime = 7;
            } else {
                this.entity.performAction(target, 0);
                this.entity.setAnimationState(2);
                this.animTime = 20;
            }

            this.attackTime = Integer.MAX_VALUE;
        }

        if (this.animTime >= 0 && --this.animTime == 0) {
            if (this.entity.getAnimationState() == 4) {
                this.entity.setAnimationState(5);
                this.animTime = 10;
            } else if (this.entity.getAnimationState() == 6) {
                this.entity.setAnimationState(7);
                this.animTime = 7;
            } else {
                this.animTime = -1;
                this.isAttacking = false;
            }
        }

        if (!this.isAttacking) {
            if (canPerformMeleeAttack) {
                this.entity.setAnimationState(0);
                this.attackTime = 10;
                this.isAttacking = true;
            } else if (canPerformSmashAttack) {
                this.entity.setAnimationState(3);
                this.attackTime = 40;
                this.isAttacking = true;
            } else if (canPerformThrowAttack) {
                this.entity.setAnimationState(1);
                this.attackTime = 30;
                this.isAttacking = true;
            } else {
                this.entity.setAnimationState(0);
                this.attackTime = Integer.MAX_VALUE;
            }
        }

        this.entity.getNavigation().moveTo(target, this.moveSpeedAmp);
        this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }
}
