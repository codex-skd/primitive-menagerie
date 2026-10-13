// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.UUID;

import javax.annotation.Nullable;

import com.skd.mobweavecore.common.capabilities.CapabilityTameableEntity;
import com.skd.mobweavecore.common.capabilities.ITameableEntity;
import com.skd.mobweavecore.common.capabilities.TameableEntityHandler;

import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;

/**
 * Base primitive spider, ported from {@code EntityPrimitiveSpider} (which extended the original
 * {@code EntityPrimitiveTameableMob}). Taming is delegated to the Mobweave Core tameable attachment so
 * the ported owner-follow goals can drive the baby spiders.
 *
 * <p>TODO(verify): the original kept its own tamed/sitting state through data parameters; here the
 * Mobweave Core {@code followState} replaces the vanilla sit flag (follow state 0 = sitting).</p>
 *
 * @author Daveyx0
 */
public class PrimitiveSpider extends Monster implements ITameableEntity {

    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID =
            SynchedEntityData.defineId(PrimitiveSpider.class, EntityDataSerializers.BYTE);

    public PrimitiveSpider(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D);
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

    // ---------------------------------------------------------------------------------------------
    // Spider behaviour
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte) 0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return effect.is(MobEffects.POISON) ? false : super.canBeAffected(effect);
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean climbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return super.getDefaultDimensions(pose).withEyeHeight(0.65F);
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("entities/spider"));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        RandomSource random = level.getRandom();

        if (random.nextInt(100) == 0) {
            Skeleton skeleton = EntityType.SKELETON.create(this.level());

            if (skeleton != null) {
                skeleton.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                skeleton.finalizeSpawn(level, difficulty, spawnType, null);
                skeleton.startRiding(this);
            }
        }

        if (spawnGroupData == null) {
            spawnGroupData = new Spider.SpiderEffectsGroupData();

            if (level.getDifficulty() == Difficulty.HARD && random.nextFloat() < 0.1F * difficulty.getSpecialMultiplier()) {
                ((Spider.SpiderEffectsGroupData) spawnGroupData).setRandomEffect(random);
            }
        }

        if (spawnGroupData instanceof Spider.SpiderEffectsGroupData groupData) {
            Holder<MobEffect> effect = groupData.effect;

            if (effect != null) {
                this.addEffect(new MobEffectInstance(effect, -1));
            }
        }

        return spawnGroupData;
    }

    /** Melee goal that gives up in bright light; the attack reach differs for baby spiders. */
    public static class AISpiderAttack extends MeleeAttackGoal {
        private final PrimitiveSpider spider;

        public AISpiderAttack(PrimitiveSpider spider) {
            super(spider, 1.0D, true);
            this.spider = spider;
        }

        @Override
        public boolean canContinueToUse() {
            float f = this.mob.getLightLevelDependentMagicValue();

            if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            }

            return super.canContinueToUse();
        }

        @Override
        protected boolean canPerformAttack(LivingEntity target) {
            double reach = (this.spider instanceof BabySpider ? 2.0D : 4.0D) + target.getBbWidth();
            return this.isTimeToAttack() && this.mob.distanceToSqr(target) <= reach * reach
                    && this.mob.getSensing().hasLineOfSight(target);
        }
    }

    /** Target goal that only picks targets in the dark. */
    public static class AISpiderTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public AISpiderTarget(PrimitiveSpider spider, Class<T> targetType) {
            super(spider, targetType, true);
        }

        @Override
        public boolean canUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            return f >= 0.5F ? false : super.canUse();
        }
    }
}
