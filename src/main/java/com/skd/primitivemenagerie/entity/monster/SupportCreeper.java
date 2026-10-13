// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.List;

import com.skd.mobweavecore.common.capabilities.CapabilityTameableEntity;
import com.skd.mobweavecore.common.capabilities.TameableEntityHandler;

import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;

/**
 * Support Creeper, ported from {@code EntitySupportCreeper}. It buffs nearby hostile mobs (or its
 * owner) with fire resistance, strength and speed, powers vanilla creepers and supports the
 * Trollager, and becomes aggressive once badly hurt.
 *
 * @author Daveyx0
 */
public class SupportCreeper extends PrimitiveCreeper {

    private AvoidEntityGoal<Player> avoidPlayer;
    private AvoidEntityGoal<Ocelot> avoidOcelot;
    private boolean aggressive;

    public SupportCreeper(EntityType<? extends SupportCreeper> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void registerGoals() {
        this.avoidPlayer = new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.0D, 1.2D);
        this.avoidOcelot = new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIBuffMob(this));
        this.goalSelector.addGoal(3, this.avoidPlayer);
        this.goalSelector.addGoal(3, this.avoidOcelot);
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        if (!this.aggressive && this.getHealth() < this.getMaxHealth() / 2.0F) {
            this.aggressive = true;
            this.goalSelector.removeGoal(this.avoidPlayer);
            this.goalSelector.removeGoal(this.avoidOcelot);
            this.goalSelector.addGoal(2, new SwellGoal(this));
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        }

        super.tick();
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_SUPPORTCREEPER);
    }

    /** Buffs the nearest suitable hostile mob (or the owner if tamed). */
    public static class EntityAIBuffMob extends Goal {
        private final SupportCreeper creeper;
        private LivingEntity mobIdol;
        private int strength = 1;

        public EntityAIBuffMob(SupportCreeper creeper) {
            this.creeper = creeper;
        }

        private boolean isSittingTamed() {
            TameableEntityHandler tameable = CapabilityTameableEntity.getCapability(this.creeper);
            return tameable.isTamed() && tameable.getFollowState() == 0;
        }

        @Override
        public boolean canUse() {
            return !this.isSittingTamed();
        }

        @Override
        public boolean canContinueToUse() {
            if (this.isSittingTamed()) {
                return false;
            }

            return this.mobIdol != null && this.mobIdol.isAlive()
                    && this.creeper.distanceToSqr(this.mobIdol) <= 25D * 25D;
        }

        @Override
        public void start() {
            this.mobIdol = this.findMobToSupport();
        }

        @Override
        public void stop() {
            this.mobIdol = null;
        }

        public LivingEntity findMobToSupport() {
            TameableEntityHandler tameable = CapabilityTameableEntity.getCapability(this.creeper);

            if (tameable.isTamed() && tameable.getFollowState() != 0) {
                LivingEntity owner = tameable.getOwner(this.creeper);

                if (owner != null && owner.distanceToSqr(this.creeper) < 30) {
                    return owner;
                }
            } else if (!tameable.isTamed()) {
                AABB area = this.creeper.getBoundingBox().inflate(10.0D, 4.0D, 10.0D);
                List<Monster> list = this.creeper.level().getEntitiesOfClass(Monster.class, area);
                Monster mob = null;
                double closest = Double.MAX_VALUE;

                for (Monster candidate : list) {
                    if (candidate != this.creeper && candidate.getActiveEffects().isEmpty()) {
                        double distance = this.creeper.distanceToSqr(candidate);

                        if (distance <= closest) {
                            closest = distance;
                            mob = candidate;
                        }
                    }
                }

                return mob;
            }

            return null;
        }

        @Override
        public void tick() {
            if (this.mobIdol == null) {
                this.mobIdol = this.findMobToSupport();
                return;
            }

            this.strength = this.creeper.isPowered() ? 2 : 1;

            if (this.creeper.distanceTo(this.mobIdol) > 2D) {
                this.creeper.getNavigation().moveTo(this.mobIdol, 1.0D);
            }

            if (this.mobIdol instanceof Creeper vanillaCreeper) {
                if (!vanillaCreeper.isPowered() && !this.creeper.level().isClientSide) {
                    powerCreeper(vanillaCreeper);
                }

                if (vanillaCreeper.getEffect(MobEffects.FIRE_RESISTANCE) == null) {
                    vanillaCreeper.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, this.strength));
                }

                if (this.creeper.getEffect(MobEffects.FIRE_RESISTANCE) == null) {
                    this.creeper.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, this.strength));
                }
            } else {
                if (this.mobIdol instanceof Trollager trollager) {
                    trollager.isBeingSupported = true;
                }

                if (this.mobIdol.getEffect(MobEffects.DAMAGE_BOOST) == null) {
                    this.mobIdol.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, this.strength));
                }

                if (this.mobIdol.getEffect(MobEffects.MOVEMENT_SPEED) == null) {
                    this.mobIdol.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, this.strength));
                }

                if (this.mobIdol.getEffect(MobEffects.FIRE_RESISTANCE) == null) {
                    this.mobIdol.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, this.strength));
                }
            }

            if (this.creeper.getEffect(MobEffects.MOVEMENT_SPEED) == null) {
                this.creeper.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, this.strength));
            }
        }

        /**
         * Powers a vanilla creeper. The original called {@code Creeper.onStruckByLightning(null)}; in
         * 1.21 there is no public API for this, so the private {@code DATA_IS_POWERED} synched data is
         * set reflectively. TODO(verify): replace with a public API if one is added.
         */
        @SuppressWarnings("unchecked")
        private static void powerCreeper(Creeper creeper) {
            try {
                java.lang.reflect.Field field = Creeper.class.getDeclaredField("DATA_IS_POWERED");
                field.setAccessible(true);
                net.minecraft.network.syncher.EntityDataAccessor<Boolean> accessor =
                        (net.minecraft.network.syncher.EntityDataAccessor<Boolean>) field.get(null);
                creeper.getEntityData().set(accessor, true);
            } catch (ReflectiveOperationException ignored) {
            }
        }
    }
}
