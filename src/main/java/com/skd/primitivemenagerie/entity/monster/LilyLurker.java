// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.List;

import com.skd.mobweavecore.entity.IMultiMobWater;
import com.skd.mobweavecore.entity.MMSwimmingCreature;
import com.skd.mobweavecore.entity.ai.SwimmingUnderwaterGoal;
import com.skd.mobweavecore.util.EntityUtil;
import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;

/**
 * Lily Lurker, ported from {@code EntityLilyLurker}. An aquatic ambusher that disguises itself as a
 * lily pad, blending in until a living entity gets close.
 *
 * <p>Simplifications: the original collision-box overrides that let a camouflaged lurker be pushed
 * differently are not expressible in 1.21 and are omitted; the getCanSpawnHere surface check is a P6
 * spawn-placement concern.</p>
 *
 * @author Daveyx0
 */
public class LilyLurker extends MMSwimmingCreature implements IMultiMobWater {

    private int aggroTimer;
    private int timeOnLand;

    private static final EntityDataAccessor<Boolean> IS_CAMOUFLAGED =
            SynchedEntityData.defineId(LilyLurker.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TIME_REGROW =
            SynchedEntityData.defineId(LilyLurker.class, EntityDataSerializers.INT);

    public LilyLurker(EntityType<? extends LilyLurker> type, Level level) {
        super(type, level);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.LILY_PAD));
        this.setCamouflaged(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MMSwimmingCreature.createAttributes()
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 30.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new SwimmingUnderwaterGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Squid.class, false, false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_CAMOUFLAGED, false);
        builder.define(TIME_REGROW, 0);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        float height = this.isCamouflaged() ? 0.98F : 0.5F;
        return EntityDimensions.scalable(0.5F, height).withEyeHeight(height * 0.25F);
    }

    @Override
    protected void playStepSound(BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
    }

    @Override
    public void tick() {
        if (this.isCamouflaged()) {
            if (!this.isInWater()) {
                this.setCamouflaged(false);
            }

            this.setYRot(0.25F);
            this.setYHeadRot(0.25F);
            this.setNoGravity(true);

            if (!this.level().isClientSide && EntityUtil.distanceToSurface(this) > 1.5F) {
                this.move(MoverType.SELF, new Vec3(0.0D, 0.05D, 0.0D));
            }

            if (this.getTarget() == null || !this.getTarget().isAlive()) {
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(1.0D, 1.0D, 1.0D));

                for (LivingEntity entity : list) {
                    if (entity instanceof Player player && player.isCreative()) {
                        continue;
                    }

                    this.setTarget(entity);
                    break;
                }
            } else {
                this.setCamouflaged(false);
            }

            this.aggroTimer = 0;
        } else {
            this.setNoGravity(false);

            if (this.isInWater() && (this.getTarget() == null || !this.getTarget().isAlive()) && ++this.aggroTimer > 250) {
                this.aggroTimer = 0;
                this.setCamouflaged(true);
            }

            if (!this.isInWater()) {
                if (++this.timeOnLand > 100) {
                    this.hurt(this.damageSources().drown(), 3.0F);
                    this.getJumpControl().jump();
                    Vec3 motion = this.getDeltaMovement();
                    this.setDeltaMovement(
                            motion.x + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) / 2.0F,
                            motion.y,
                            motion.z + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) / 2.0F);
                    this.timeOnLand = 80;
                }
            } else {
                this.timeOnLand = 0;
            }
        }

        super.tick();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.setCamouflaged(false);

        if (source.is(DamageTypes.IN_WALL) && this.isInWater()) {
            return false;
        }

        return super.hurt(source, amount);
    }

    public void setCamouflaged(boolean camouflaged) {
        if (this.entityData.get(IS_CAMOUFLAGED) != camouflaged) {
            this.entityData.set(IS_CAMOUFLAGED, camouflaged);
            this.refreshDimensions();
        }
    }

    public boolean isCamouflaged() {
        return this.entityData.get(IS_CAMOUFLAGED);
    }

    public void setTimeToRegrow(int time) {
        this.entityData.set(TIME_REGROW, time);
    }

    public int getTimeToRegrow() {
        return this.entityData.get(TIME_REGROW);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Camouflaged", this.isCamouflaged());
        compound.putInt("RegrowTime", this.getTimeToRegrow());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setCamouflaged(compound.getBoolean("Camouflaged"));
        this.setTimeToRegrow(compound.getInt("RegrowTime"));
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_LILYLURKER);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
            SpawnGroupData spawnGroupData) {
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
}
