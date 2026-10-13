// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import javax.annotation.Nullable;

import com.skd.primitivemenagerie.entity.ai.EntityAISwitchBetweenRangedAndMelee;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * Skeleton Warrior, ported from {@code EntitySkeletonWarrior}. A tougher skeleton that switches
 * between a bow and an iron sword depending on the distance to its target.
 *
 * @author Daveyx0
 */
public class SkeletonWarrior extends Skeleton {

    public SkeletonWarrior(EntityType<? extends Skeleton> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, 25.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new EntityAISwitchBetweenRangedAndMelee(this, 1.35D, 20, 15.0F));
        this.goalSelector.addGoal(5, new EntityAISwitchWeapons(this, 5D, 6D, new ItemStack(Items.IRON_SWORD), new ItemStack(Items.BOW)));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    /** The custom ranged/melee goal replaces the vanilla weapon reassessment. */
    @Override
    public void reassessWeaponGoal() {
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        this.setLeftHanded(this.random.nextFloat() < 0.05F);

        if (this.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD).isEmpty()) {
            this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
        }

        if (this.getMainHandItem().isEmpty()) {
            this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        }

        return data;
    }

    public ItemStack getBackItem() {
        return this.getMainHandItem().is(Items.IRON_SWORD) ? new ItemStack(Items.BOW) : new ItemStack(Items.IRON_SWORD);
    }

    @Override
    protected AbstractArrow getArrow(ItemStack projectile, float velocity, @Nullable ItemStack weapon) {
        AbstractArrow arrow = super.getArrow(projectile, velocity, weapon);

        if (arrow instanceof Arrow tipped) {
            tipped.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
        }

        return arrow;
    }

    /** Swaps between the sword and the bow based on the distance to the current target. */
    public static class EntityAISwitchWeapons extends Goal {
        private final SkeletonWarrior mob;
        private final double minDistance;
        private final double maxDistance;
        private final ItemStack weaponOne;
        private final ItemStack weaponTwo;
        private LivingEntity target;

        public EntityAISwitchWeapons(SkeletonWarrior mob, double minDistance, double maxDistance,
                ItemStack weaponOne, ItemStack weaponTwo) {
            this.mob = mob;
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.weaponOne = weaponOne;
            this.weaponTwo = weaponTwo;
        }

        @Override
        public boolean canUse() {
            this.target = this.mob.getTarget();

            if (this.target == null || !this.target.isAlive()) {
                return false;
            }

            boolean tooClose = this.mob.distanceTo(this.target) < this.minDistance
                    && !this.mob.getMainHandItem().is(this.weaponOne.getItem());
            boolean tooFar = this.mob.distanceTo(this.target) > this.maxDistance
                    && !this.mob.getMainHandItem().is(this.weaponTwo.getItem());

            return (tooClose || tooFar) && this.mob.hasLineOfSight(this.target);
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public void tick() {
            if (this.mob.distanceTo(this.target) < this.minDistance) {
                this.mob.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, this.weaponOne);
            } else if (this.mob.distanceTo(this.target) > this.maxDistance) {
                this.mob.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, this.weaponTwo);
            }
        }
    }
}
