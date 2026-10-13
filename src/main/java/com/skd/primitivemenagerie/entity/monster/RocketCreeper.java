// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.Collection;

import com.skd.mobweavecore.common.capabilities.CapabilityTameableEntity;
import com.skd.mobweavecore.common.capabilities.TameableEntityHandler;
import com.skd.mobweavecore.entity.IMultiMob;

import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;

/**
 * Rocket Creeper, ported from {@code EntityRocketCreeper}. When its fuse would normally blow up close
 * to its target it instead launches itself into the air and explodes on landing.
 *
 * <p>Simplification: the original's {@code isCreatureType(MONSTER) == false} cannot be expressed per
 * instance in 1.21; the entity keeps the {@code MONSTER} spawn category. The actual spawn entries are
 * registered in P6 and already gate this mob behind its config flag.</p>
 *
 * @author Daveyx0
 */
public class RocketCreeper extends PrimitiveCreeper implements IMultiMob {

    private static final EntityDataAccessor<Boolean> IS_ROCKET =
            SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.BOOLEAN);

    private final int explosionRadius = 3;
    private int timeBeforeJumping;

    public RocketCreeper(EntityType<? extends RocketCreeper> type, Level level) {
        super(type, level);
        this.setRocket(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_ROCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public void tick() {
        if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) > 25D) {
            this.setSwellDir(-1);
        }

        if (this.getSwellDir() > 0) {
            this.timeBeforeJumping++;
        } else {
            this.timeBeforeJumping = 0;
        }

        int state = this.getSwellDir();

        if (this.timeBeforeJumping > 15 && this.isAlive() && this.getTarget() != null
                && this.hasEnoughSpaceToJump(this.getTarget()) && state > 0 && this.onGround()) {
            this.setSwellDir(-1);

            if (this.level().isClientSide) {
                this.level().addParticle(ParticleTypes.SMOKE,
                        this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                        this.getY() - (this.random.nextFloat() - this.random.nextFloat()) - 1.0F,
                        this.getZ() + (this.random.nextFloat() - this.random.nextFloat()),
                        0.0D, 0.0D, 0.0D);
            }

            this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 0.5F);
            this.setDeltaMovement((this.getTarget().getX() - this.getX()) / 6D, 1.2000000476837158D,
                    (this.getTarget().getZ() - this.getZ()) / 6D);
            this.setRocket(true);
        }

        super.tick();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.isRocket()) {
            super.playStepSound(pos, state);
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (this.isRocket()) {
            this.explode();
        }

        return false;
    }

    public boolean hasEnoughSpaceToJump(Entity entity) {
        boolean flag = true;

        if (!PMConfig.getRocketCreeperAlwaysJump()) {
            for (int i = 0; i < 5; i++) {
                Vec3 start = new Vec3(this.getX(), this.getY() + this.getEyeHeight() + i, this.getZ());
                Vec3 end = new Vec3(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
                flag = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE, this)) == null;
            }
        }

        return flag;
    }

    private void explode() {
        if (!this.level().isClientSide) {
            float f = this.isPowered() ? 2.0F : 1.0F;
            TameableEntityHandler tameable = CapabilityTameableEntity.getCapability(this);

            if (tameable.isTamed()) {
                this.hurt(this.damageSources().generic(), 1.0F);
                this.setRocket(false);
            } else {
                this.discard();
            }

            this.level().explode(this, this.getX(), this.getY(), this.getZ(),
                    this.explosionRadius * f, Level.ExplosionInteraction.MOB);
            this.spawnLingeringCloud();
        }
    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();

        if (!collection.isEmpty()) {
            AreaEffectCloud cloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            cloud.setRadius(2.5F);
            cloud.setRadiusOnUse(-0.5F);
            cloud.setWaitTime(10);
            cloud.setDuration(cloud.getDuration() / 2);
            cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());

            for (MobEffectInstance effect : collection) {
                cloud.addEffect(new MobEffectInstance(effect));
            }

            this.level().addFreshEntity(cloud);
        }
    }

    public void setRocket(boolean rocket) {
        this.entityData.set(IS_ROCKET, rocket);
    }

    public boolean isRocket() {
        return this.entityData.get(IS_ROCKET);
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_ROCKETCREEPER);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Rocket", this.isRocket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setRocket(compound.getBoolean("Rocket"));
    }
}
