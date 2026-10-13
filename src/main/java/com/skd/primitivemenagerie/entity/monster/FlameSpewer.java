// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMobLava;
import com.skd.primitivemenagerie.core.PMLootTables;
import com.skd.primitivemenagerie.core.PMSoundEvents;
import com.skd.primitivemenagerie.entity.item.FlameSpit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;

/**
 * Flame Spewer, ported from {@code EntityFlameSpewer}. A lava-dwelling ranged mob that dives into
 * lava pools and spits fireballs at its target.
 *
 * <p>Simplifications: the original implemented {@code IRangedAttackMob} with empty methods (nothing
 * to port in 1.21); the spawn light-level and Y-height checks are P6 spawn-placement concerns.</p>
 *
 * @author Daveyx0
 */
public class FlameSpewer extends Monster implements IMultiMobLava {

    private static final EntityDataAccessor<Byte> ON_FIRE =
            SynchedEntityData.defineId(FlameSpewer.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> IN_DANGER =
            SynchedEntityData.defineId(FlameSpewer.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> ATTACK_TIME =
            SynchedEntityData.defineId(FlameSpewer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> ATTACK_SIGNAL =
            SynchedEntityData.defineId(FlameSpewer.class, EntityDataSerializers.FLOAT);

    public FlameSpewer(EntityType<? extends FlameSpewer> type, Level level) {
        super(type, level);
        this.setOnFire(false);
        this.setInDanger(false);
        this.setAttackTime(10);
        this.setAttackSignal(0);
        this.setPathfindingMalus(PathType.LAVA, 10.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new AIGoToLava(this));
        this.goalSelector.addGoal(4, new AIFlameSpewAttack(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ON_FIRE, (byte) 0);
        builder.define(IN_DANGER, (byte) 0);
        builder.define(ATTACK_TIME, 0);
        builder.define(ATTACK_SIGNAL, 0.0F);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return PMSoundEvents.ENTITY_FLAMESPEWER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SQUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SQUID_DEATH;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return this.getAttackTime() < 10 || this.getAttackSignal() > 0 || super.isInvulnerableTo(source);
    }

    public void setAttackTime(int time) {
        this.entityData.set(ATTACK_TIME, time);
    }

    public void setAttackSignal(float signal) {
        this.entityData.set(ATTACK_SIGNAL, signal);
    }

    public int getAttackTime() {
        return this.entityData.get(ATTACK_TIME);
    }

    public float getAttackSignal() {
        return this.entityData.get(ATTACK_SIGNAL);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isInLava() || this.isInWater()) {
            if (this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() + 0.5D, this.getZ())).isAir()) {
                this.setDeltaMovement(this.getDeltaMovement().x, 0.0D, this.getDeltaMovement().z);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().x, 0.1D, this.getDeltaMovement().z);
            }

            if (this.isInWater() && this.tickCount % 15 == 0) {
                this.hurt(this.damageSources().drown(), 4.0F);
            }
        } else if (this.tickCount % 25 == 0) {
            this.hurt(this.damageSources().drown(), 1.0F);
            this.getJumpControl().jump();
            this.setZza(1.0F);
        }
    }

    @Override
    @Nullable
    public ItemEntity spawnAtLocation(ItemStack stack, float offsetY) {
        if (stack.isEmpty()) {
            return null;
        }

        ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY() + 1.5D, this.getZ(), stack);
        entityitem.setDefaultPickUpDelay();

        for (int i = 0; i < 50; i++) {
            Vec3 vec = LandRandomPos.getPos(this, 10, 7);

            if (vec != null) {
                entityitem.setDeltaMovement(
                        (vec.x - entityitem.getX()) / 18.0D,
                        (vec.y - entityitem.getY()) / 18.0D + 0.5D,
                        (vec.z - entityitem.getZ()) / 18.0D);
                break;
            }
        }

        this.level().addFreshEntity(entityitem);
        return entityitem;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AttackTime", this.getAttackTime());
        compound.putFloat("AttackSignal", this.getAttackSignal());
        compound.putBoolean("onFire", this.isOnFire());
        compound.putBoolean("inDanger", this.isInDanger());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setAttackTime(compound.getInt("AttackTime"));
        this.setAttackSignal(compound.getFloat("AttackSignal"));
        this.setOnFire(compound.getBoolean("onFire"));
        this.setInDanger(compound.getBoolean("inDanger"));
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_FLAMESPEWER);
    }

    public boolean isOnFire() {
        return (this.entityData.get(ON_FIRE) & 1) != 0;
    }

    public void setOnFire(boolean onFire) {
        byte b0 = this.entityData.get(ON_FIRE);
        b0 = onFire ? (byte) (b0 | 1) : (byte) (b0 & -2);
        this.entityData.set(ON_FIRE, b0);
    }

    public boolean isInDanger() {
        return (this.entityData.get(IN_DANGER) & 1) != 0;
    }

    public void setInDanger(boolean inDanger) {
        byte b0 = this.entityData.get(IN_DANGER);
        b0 = inDanger ? (byte) (b0 | 1) : (byte) (b0 & -2);
        this.entityData.set(IN_DANGER, b0);
    }

    /** Ranged fireball attack, ported from {@code EntityFlameSpewer.AIFlameSpewAttack}. */
    static class AIFlameSpewAttack extends Goal {
        private final FlameSpewer spewer;
        private int attackStep;
        private int attackTime;
        private float attackSignal;
        private boolean performingAttack;
        private boolean hasSeenPlayerThisAttack;

        public AIFlameSpewAttack(FlameSpewer spewer) {
            this.spewer = spewer;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.spewer.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void start() {
            this.attackStep = 10;
            this.attackSignal = 0;
            this.attackTime = 100;
            this.performingAttack = false;
            this.hasSeenPlayerThisAttack = false;
        }

        @Override
        public void stop() {
            this.spewer.setOnFire(false);
            this.attackSignal = 0;
            this.attackStep = 10;
            this.attackTime = 100;
            this.spewer.setAttackTime(this.attackStep);
            this.spewer.setAttackSignal(this.attackSignal);
            this.spewer.setOnFire(false);
            this.spewer.setInDanger(false);
            this.performingAttack = false;
        }

        @Override
        public void tick() {
            --this.attackTime;
            LivingEntity target = this.spewer.getTarget();

            if (target == null) {
                return;
            }

            if (!this.performingAttack && this.spewer.isInLava() && this.spewer.hasLineOfSight(target)) {
                if (this.attackTime <= 50) {
                    this.spewer.setOnFire(false);
                    this.hasSeenPlayerThisAttack = false;
                }

                if (!this.spewer.isOnFire()) {
                    if (this.spewer.hasLineOfSight(target)) {
                        this.hasSeenPlayerThisAttack = true;
                    }

                    this.attackStep = (this.attackTime * 2) / 10;

                    if (this.attackTime <= 3) {
                        this.attackSignal += 0.05F;
                    }
                }
            }

            double d0 = this.spewer.distanceToSqr(target);

            if (d0 < 5.0D && !this.spewer.isInLava()) {
                this.spewer.setInDanger(true);

                if (this.attackTime <= 0) {
                    this.attackTime = 20;
                    this.spewer.doHurtTarget(target);
                }
            } else if (d0 < Math.pow(this.getFollowDistance() / 2.0D, 2) && this.spewer.isInLava()) {
                this.spewer.setInDanger(false);
                double d1 = target.getX() - this.spewer.getX();
                double d2 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 2.0F + 0.25F)
                        - (this.spewer.getY() + (double) (this.spewer.getBbHeight() / 2.0F));
                double d3 = target.getZ() - this.spewer.getZ();

                if (this.attackTime <= 0) {
                    ++this.attackStep;
                    this.attackSignal -= 0.05F;

                    if (this.attackStep == 1) {
                        this.attackTime = 30;
                        this.spewer.setOnFire(true);
                        this.performingAttack = true;
                    } else if (this.attackStep <= 10) {
                        this.attackTime = 3;
                        this.spewer.setOnFire(true);
                        this.performingAttack = true;
                    } else {
                        this.attackTime = 100;
                        this.attackStep = 10;
                        this.performingAttack = false;
                    }

                    if (this.attackStep > 1 && this.hasSeenPlayerThisAttack) {
                        float f = Mth.sqrt(Mth.sqrt((float) d0) * 0.1F);
                        this.spewer.level().levelEvent(1018, this.spewer.blockPosition(), 0);

                        Vec3 direction = new Vec3(
                                d1 + this.spewer.getRandom().nextGaussian() * 0.02F * (double) f,
                                d2 - this.spewer.getRandom().nextGaussian() * 0.02F * (double) f,
                                d3 + this.spewer.getRandom().nextGaussian() * 0.02F * (double) f);
                        FlameSpit spit = new FlameSpit(this.spewer.level(), this.spewer, direction);
                        spit.setPos(spit.getX(), this.spewer.getY() + (double) (this.spewer.getBbHeight() / 2.0F) - 0.5D,
                                spit.getZ());
                        this.spewer.level().addFreshEntity(spit);
                    }
                }
            } else if (this.spewer.isInLava() && this.spewer.hasLineOfSight(target)) {
                double d1 = target.getX() - this.spewer.getX();
                double d3 = target.getZ() - this.spewer.getZ();
                Vec3 motion = this.spewer.getDeltaMovement();
                this.spewer.setDeltaMovement(d1 * 0.01D, motion.y, d3 * 0.01D);
                this.spewer.setInDanger(false);
                this.spewer.setOnFire(false);
            } else {
                this.spewer.getNavigation().stop();
                this.spewer.setInDanger(false);
                this.spewer.setOnFire(false);
            }

            this.spewer.getLookControl().setLookAt(target, 10.0F, 10.0F);

            this.attackSignal = Mth.clamp(this.attackSignal, 0.0F, 0.4F);
            this.spewer.setAttackSignal(this.attackSignal);
            this.spewer.setAttackTime(this.attackStep);
        }

        private double getFollowDistance() {
            return this.spewer.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }

    /** Walks to a lava pool, ported from {@code EntityFlameSpewer.AIGoToLava}. */
    static class AIGoToLava extends MoveToBlockGoal {
        private final FlameSpewer spewer;

        public AIGoToLava(FlameSpewer spewer) {
            super(spewer, 0.699999988079071D, 25);
            this.spewer = spewer;
        }

        @Override
        public boolean canUse() {
            return !this.spewer.isInLava() && super.canUse();
        }

        @Override
        public void tick() {
            super.tick();
            this.spewer.getLookControl().setLookAt((double) this.blockPos.getX() + 0.5D, this.blockPos.getY() + 1,
                    (double) this.blockPos.getZ() + 0.5D, 10.0F, (float) this.spewer.getMaxHeadXRot());

            if (this.isReachedTarget()) {
                this.nextStartTick = 10;
            }
        }

        @Override
        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            BlockState state = level.getBlockState(pos);

            if (state.is(Blocks.LAVA) && level.isEmptyBlock(pos.above())) {
                return level.getBlockState(pos.east()).is(Blocks.LAVA)
                        && level.getBlockState(pos.west()).is(Blocks.LAVA)
                        && level.getBlockState(pos.north()).is(Blocks.LAVA)
                        && level.getBlockState(pos.south()).is(Blocks.LAVA);
            }

            return false;
        }
    }
}
