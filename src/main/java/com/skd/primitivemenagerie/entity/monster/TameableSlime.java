// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.EnumSet;
import java.util.UUID;

import javax.annotation.Nullable;

import com.skd.mobweavecore.common.capabilities.CapabilityTameableEntity;
import com.skd.mobweavecore.common.capabilities.ITameableEntity;
import com.skd.mobweavecore.common.capabilities.TameableEntityHandler;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Tameable slime base, ported from {@code EntityTameableSlime}. The original kept its own tameable
 * data parameters and a bespoke move helper; here the tameable state is delegated to the Mobweave
 * Core attachment and the movement is a custom {@link MoveControl} that also handles sitting.
 *
 * <p>The original sit flag is represented by follow state {@code 0 = sitting}.</p>
 *
 * @author Daveyx0
 */
public abstract class TameableSlime extends Slime implements ITameableEntity {

    public TameableSlime(EntityType<? extends Slime> type, Level level) {
        super(type, level);
        this.moveControl = new TameableSlimeMoveControl(this);
    }

    // ---------------------------------------------------------------------------------------------
    // Tameable API (backed by the Mobweave Core attachment)
    // ---------------------------------------------------------------------------------------------

    protected TameableEntityHandler tameable() {
        return CapabilityTameableEntity.getCapability(this);
    }

    @Override
    public boolean isTamed() {
        return this.tameable().isTamed();
    }

    @Override
    public void setTamed(boolean set) {
        this.tameable().setTamed(set);
    }

    @Nullable
    @Override
    public LivingEntity getOwner(LivingEntity entity) {
        return this.tameable().getOwner(entity);
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.tameable().getOwner(this);
    }

    @Nullable
    @Override
    public UUID getOwnerId() {
        return this.tameable().getOwnerId();
    }

    @Override
    public void setOwner(UUID id) {
        this.tameable().setOwner(id);
    }

    @Override
    public int getFollowState() {
        return this.tameable().getFollowState();
    }

    @Override
    public void setFollowState(int set) {
        this.tameable().setFollowState(set);
    }

    public boolean isOwner(LivingEntity entity) {
        return entity == this.getOwner();
    }

    public boolean isSitting() {
        return this.getFollowState() == 0;
    }

    public void setSitting(boolean sitting) {
        this.setFollowState(sitting ? 0 : 1);
    }

    @Override
    public boolean canBeLeashed() {
        return this.isTamed();
    }

    public boolean isHealingItem(ItemStack stack) {
        return stack.is(Items.SLIME_BALL);
    }

    public boolean isTamingItem(ItemStack stack) {
        return stack.is(Items.SLIME_BALL);
    }

    // ---------------------------------------------------------------------------------------------
    // Behaviour
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TameableSlimeFloatGoal(this));
        this.goalSelector.addGoal(2, new TameableSlimeAttackGoal(this));
        this.goalSelector.addGoal(3, new TameableSlimeFaceRandomGoal(this));
        this.goalSelector.addGoal(5, new TameableSlimeHopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        if (this.isTamed() && !this.isSitting() && this.getTarget() == null) {
            this.setTarget(this.getOwner());
        }

        super.tick();
    }

    /** Hearts effect used when the slime is healed or tamed. */
    protected void playHealEffect() {
        this.playTameEffect(ParticleTypes.HEART);
    }

    /** Note effect used when the slime sits or stands up. */
    protected void playSitEffect() {
        this.playTameEffect(ParticleTypes.NOTE);
    }

    /** Hearts effect used when the slime is tamed. */
    protected void playTameEffect() {
        this.playTameEffect(ParticleTypes.HEART);
    }

    private void playTameEffect(net.minecraft.core.particles.SimpleParticleType type) {
        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(type,
                    this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(),
                    this.getY() + 0.5D + (double) (this.random.nextFloat() * this.getBbHeight()),
                    this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(),
                    d0, d1, d2);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Goals and move control, ported from the original nested classes
    // ---------------------------------------------------------------------------------------------

    static class TameableSlimeAttackGoal extends Goal {
        private final TameableSlime slime;

        public TameableSlimeAttackGoal(TameableSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.slime.getTarget();

            if (this.slime.isSitting()) {
                return false;
            }

            if (target == null || !target.isAlive()) {
                return false;
            }

            if (this.slime.isTamed() && this.slime.isOwner(target) && this.slime.getOwner() != null
                    && this.slime.distanceTo(this.slime.getOwner()) > 2.0F) {
                return true;
            }

            return !this.slime.isTamed() && !(target instanceof Player player && player.isCreative());
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.slime.getTarget();

            if (target != null) {
                this.slime.lookAt(target, 10.0F, 10.0F);
            }

            if (this.slime.getMoveControl() instanceof TameableSlimeMoveControl control) {
                control.setDirection(this.slime.getYRot(), true);
            }
        }
    }

    static class TameableSlimeFaceRandomGoal extends Goal {
        private final TameableSlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public TameableSlimeFaceRandomGoal(TameableSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.slime.getTarget() == null
                    && (this.slime.onGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION));
        }

        @Override
        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = this.adjustedTickDelay(40 + this.slime.getRandom().nextInt(60));
                this.chosenDegrees = (float) this.slime.getRandom().nextInt(360);
            }

            if (this.slime.getMoveControl() instanceof TameableSlimeMoveControl control) {
                control.setDirection(this.chosenDegrees, false);
            }
        }
    }

    static class TameableSlimeFloatGoal extends Goal {
        private final TameableSlime slime;

        public TameableSlimeFloatGoal(TameableSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
            slime.getNavigation().setCanFloat(true);
        }

        @Override
        public boolean canUse() {
            return this.slime.isInWater() || this.slime.isInLava();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.slime.getRandom().nextFloat() < 0.8F) {
                this.slime.getJumpControl().jump();
            }

            if (this.slime.getMoveControl() instanceof TameableSlimeMoveControl control) {
                control.setWantedMovement(1.2D);
            }
        }
    }

    static class TameableSlimeHopGoal extends Goal {
        private final TameableSlime slime;

        public TameableSlimeHopGoal(TameableSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            if (this.slime.getMoveControl() instanceof TameableSlimeMoveControl control) {
                control.setWantedMovement(1.0D);
            }
        }
    }

    /** Move control ported from {@code TameableSlimeMoveHelper}; also stops the slime while sitting. */
    static class TameableSlimeMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final TameableSlime slime;
        private boolean isAggressive;

        public TameableSlimeMoveControl(TameableSlime slime) {
            super(slime);
            this.slime = slime;
            this.yRot = 180.0F * slime.getYRot() / (float) Math.PI;
        }

        public void setDirection(float yRot, boolean aggressive) {
            this.yRot = yRot;
            this.isAggressive = aggressive;
        }

        public void setWantedMovement(double speed) {
            this.speedModifier = speed;
            this.operation = MoveControl.Operation.MOVE_TO;
        }

        @Override
        public void tick() {
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();

            if (this.slime.isSitting()) {
                if (this.slime.getTarget() != null) {
                    this.slime.getLookControl().setLookAt(this.slime.getTarget(), this.slime.getYRot(), this.slime.getXRot());
                }

                this.mob.setSpeed(0.0F);
            } else if (this.operation != MoveControl.Operation.MOVE_TO) {
                this.mob.setZza(0.0F);
            } else {
                this.operation = MoveControl.Operation.WAIT;

                if (this.mob.onGround()) {
                    this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));

                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();

                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }

                        this.slime.getJumpControl().jump();

                        if (this.slime.doPlayJumpSound()) {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(),
                                    ((this.slime.getRandom().nextFloat() - this.slime.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                        }
                    } else {
                        this.slime.xxa = 0.0F;
                        this.slime.zza = 0.0F;
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }
            }
        }
    }
}
