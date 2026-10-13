// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMob;
import com.skd.mobweavecore.entity.MMFlyingCreature;
import com.skd.mobweavecore.entity.ai.FlyingAroundGoal;
import com.skd.mobweavecore.entity.ai.SenseEntityNearestPlayerGoal;
import com.skd.primitivemenagerie.core.PMLootTables;
import com.skd.primitivemenagerie.core.PMSoundEvents;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Void Eye, ported from {@code EntityVoidEye}. A flying watcher that locks on to nearby players with
 * its gaze, weakening and damaging them, and teleports when struck.
 *
 * <p>Simplifications: the original synchronised the teleport request through a network message from
 * the client; here the teleport roll is done on the server when the eye is hurt. The spawn-light and
 * dimension (dragon) checks of the original are P6 spawn-placement concerns.</p>
 *
 * @author Daveyx0
 */
public class VoidEye extends MMFlyingCreature implements IMultiMob {

    private static final EntityDataAccessor<Integer> TARGET_ENTITY =
            SynchedEntityData.defineId(VoidEye.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CAN_SEE_TARGET =
            SynchedEntityData.defineId(VoidEye.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DOES_TELEPORT =
            SynchedEntityData.defineId(VoidEye.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private LivingEntity targetedEntity;
    private int clientSideAttackTime;

    public VoidEye(EntityType<? extends VoidEye> type, Level level) {
        super(type, level);
        this.setTeleports(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MMFlyingCreature.createAttributes()
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.MAX_HEALTH, 15.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new AIVoidEyeAttack(this));
        this.goalSelector.addGoal(2, new FlyingAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new SenseEntityNearestPlayerGoal(this, 18));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TARGET_ENTITY, 0);
        builder.define(CAN_SEE_TARGET, false);
        builder.define(DOES_TELEPORT, false);
    }

    public void setCanSeeTarget(boolean sees) {
        this.entityData.set(CAN_SEE_TARGET, sees);
    }

    public boolean canSeeTarget() {
        return this.entityData.get(CAN_SEE_TARGET);
    }

    public void setTeleports(boolean teleports) {
        this.entityData.set(DOES_TELEPORT, teleports);
    }

    public boolean doesTeleport() {
        return this.entityData.get(DOES_TELEPORT);
    }

    public float getAttackAnimationScale(float partialTick) {
        return ((float) this.clientSideAttackTime + partialTick) / (float) this.getAttackDuration();
    }

    @Override
    protected net.minecraft.world.entity.EntityDimensions getDefaultDimensions(Pose pose) {
        return net.minecraft.world.entity.EntityDimensions.scalable(0.6F, 0.6F).withEyeHeight(0.3F);
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide) {
            if (this.hasTargetedEntity() && this.clientSideAttackTime < this.getAttackDuration()) {
                ++this.clientSideAttackTime;
            }
        }

        if (!this.level().isClientSide) {
            LivingEntity target = this.getTarget();
            this.setCanSeeTarget(target != null && this.hasLineOfSight(target));
        }

        if (this.doesTeleport()) {
            this.teleportRandomly();
        }

        if (this.hasTargetedEntity()) {
            this.setYRot(this.getYHeadRot());
        }

        this.level().addParticle(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE,
                this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);

        super.aiStep();
    }

    @Nullable
    public LivingEntity getTargetedEntity() {
        if (!this.hasTargetedEntity()) {
            return null;
        } else if (this.level().isClientSide) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            }

            Entity entity = this.level().getEntity(this.getTargetedEntityID());

            if (entity instanceof LivingEntity living) {
                this.targetedEntity = living;
                return this.targetedEntity;
            }

            return null;
        } else {
            return this.getTarget();
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        if (TARGET_ENTITY.equals(key)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }
    }

    public int getAttackDuration() {
        return 75;
    }

    private void setTargetedEntity(int entityId) {
        this.entityData.set(TARGET_ENTITY, entityId);
    }

    public boolean hasTargetedEntity() {
        return this.entityData.get(TARGET_ENTITY) != 0;
    }

    public int getTargetedEntityID() {
        return this.entityData.get(TARGET_ENTITY);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }

        boolean flag = super.hurt(source, amount);

        if (flag && this.random.nextInt(5) != 0) {
            this.teleportRandomly();
        }

        return flag;
    }

    protected boolean teleportRandomly() {
        double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 16.0D;
        double d1 = this.getY() + (double) (this.random.nextInt(32) - 16);
        double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 16.0D;

        if (this.randomTeleport(d0, d1, d2, false)) {
            this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT,
                    this.getSoundSource(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            this.setTeleports(false);
            return true;
        }

        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return PMSoundEvents.ENTITY_VOIDEYE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENDER_EYE_LAUNCH;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDER_EYE_DEATH;
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_VOIDEYE);
    }

    /** Gaze-attack goal, ported from {@code EntityVoidEye.AIVoidEyeAttack}. */
    static class AIVoidEyeAttack extends Goal {
        private final VoidEye eye;
        private int tickCounter;

        public AIVoidEyeAttack(VoidEye eye) {
            this.eye = eye;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.eye.getTarget();

            List<VoidEye> eyes = this.eye.level().getEntitiesOfClass(VoidEye.class,
                    this.eye.getBoundingBox().inflate(20.0D, 20.0D, 20.0D));

            if (!eyes.isEmpty() && !this.eye.hasTargetedEntity()) {
                for (VoidEye other : eyes) {
                    if (other != this.eye && target != null && other.hasTargetedEntity()
                            && other.getTargetedEntityID() == target.getId()) {
                        return false;
                    }
                }
            }

            return target != null && target.isAlive();
        }

        @Override
        public void start() {
            this.tickCounter = -10;
            this.eye.getNavigation().stop();
            this.eye.getLookControl().setLookAt(this.eye.getTarget(), 90.0F, 90.0F);
        }

        @Override
        public void stop() {
            this.eye.setTargetedEntity(0);
            this.eye.setTarget(null);
        }

        @Override
        public void tick() {
            LivingEntity target = this.eye.getTarget();

            if (target == null) {
                return;
            }

            this.eye.getNavigation().stop();
            this.eye.getLookControl().setLookAt(target, 90.0F, 90.0F);

            ++this.tickCounter;

            if (this.tickCounter == 0) {
                this.eye.playSound(SoundEvents.SHULKER_SHOOT, 1.0F, 1.0F);
                this.eye.setTargetedEntity(target.getId());
            } else if (this.tickCounter >= this.eye.getAttackDuration()) {
                if (this.eye.canSeeTarget()) {
                    target.hurt(this.eye.damageSources().indirectMagic(this.eye, this.eye),
                            (float) this.eye.getAttributeValue(Attributes.ATTACK_DAMAGE));
                }

                target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 240, 0));
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 240, 0));
                target.addEffect(new MobEffectInstance(MobEffects.HUNGER, 240, 0));

                this.eye.setTarget(null);
            }
        }
    }
}
