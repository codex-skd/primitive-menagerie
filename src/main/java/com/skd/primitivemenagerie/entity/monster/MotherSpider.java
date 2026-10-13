// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.Arrays;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMob;

import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.core.PMEntities;
import com.skd.primitivemenagerie.core.PMLootTables;
import com.skd.primitivemenagerie.core.PMSoundEvents;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Mother Spider, ported from {@code EntityMotherSpider}. Spawns a family of tamed baby spiders that
 * ride on her back and attacks whatever hurts one of her children.
 *
 * @author Daveyx0
 */
public class MotherSpider extends PrimitiveSpider implements IMultiMob {

    private static final EntityDataAccessor<Boolean> DATA_IS_ANGRY =
            SynchedEntityData.defineId(MotherSpider.class, EntityDataSerializers.BOOLEAN);

    private final int minFollowers = 3;
    private final int maxFollowers;
    private final LivingEntity[] followers;

    public MotherSpider(EntityType<? extends MotherSpider> type, Level level) {
        super(type, level);
        this.maxFollowers = PMConfig.getMaxSpiderFamilySize();
        this.followers = new LivingEntity[this.maxFollowers];
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PrimitiveSpider.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_ANGRY, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new PrimitiveSpider.AISpiderAttack(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new PrimitiveSpider.AISpiderTarget<>(this, Player.class));
        this.targetSelector.addGoal(4, new PrimitiveSpider.AISpiderTarget<>(this, IronGolem.class));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof BabySpider) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.18D);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        }

        if (this.isAngry()) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D);
            this.level().addParticle(ParticleTypes.SMOKE,
                    this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                    this.getY() + this.random.nextFloat(),
                    this.getZ() + (this.random.nextFloat() - this.random.nextFloat()),
                    0.0D, 0.0D, 0.0D);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return PMSoundEvents.ENTITY_MOTHERSPIDER_SCREECH.get();
    }

    public void addFollower(LivingEntity follower) {
        for (int i = 0; i < this.followers.length; i++) {
            if (this.followers[i] == null) {
                this.followers[i] = follower;
                return;
            }
        }
    }

    public LivingEntity[] getFollowers() {
        return this.followers;
    }

    public boolean hasFollowers() {
        return this.followers != null && Arrays.stream(this.followers).anyMatch(follower -> follower != null);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnGroupData) {
        this.getAttribute(Attributes.FOLLOW_RANGE).addTransientModifier(new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath("primitive_menagerie", "random_spawn_bonus"),
                this.random.nextGaussian() * 0.05D, AttributeModifier.Operation.ADD_VALUE));

        spawnGroupData = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);

        for (int i = 0; i < this.maxFollowers; i++) {
            if (!this.level().isClientSide && (this.random.nextInt(2) == 0 || i <= this.minFollowers)) {
                BabySpider baby = PMEntities.BABY_SPIDER.get().create(this.level());

                if (baby != null) {
                    baby.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    baby.setTamed(true);
                    baby.setOwner(this.getUUID());
                    this.level().addFreshEntity(baby);

                    if (!this.isVehicle()) {
                        baby.startRiding(this);
                    }
                }
            }
        }

        return spawnGroupData;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            float sin = Mth.sin(this.yBodyRot * 0.017453292F);
            float cos = Mth.cos(this.yBodyRot * 0.017453292F);
            float yOffset = passenger instanceof BabySpider ? 0.25F : 0.0F;
            moveFunction.accept(passenger, this.getX() + 0.1F * sin,
                    this.getY() + this.getBbHeight() * 0.5F + yOffset, this.getZ() - 0.1F * cos);

            if (passenger instanceof LivingEntity living) {
                living.yBodyRot = this.yBodyRot;
            }
        }
    }

    public void setIsAngry(boolean angry) {
        this.entityData.set(DATA_IS_ANGRY, angry);
    }

    public boolean isAngry() {
        return this.entityData.get(DATA_IS_ANGRY);
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_MOTHERSPIDER);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Angry", this.isAngry());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setIsAngry(compound.getBoolean("Angry"));
    }
}
