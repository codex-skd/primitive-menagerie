// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import com.skd.mobweavecore.entity.IMultiMob;

import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Festive Creeper, ported from {@code EntityFestiveCreeper}. It never explodes on its own; instead it
 * back off from its target and throws primed TNT at it.
 *
 * <p>TODO(P4): the original threw the ported {@code EntityPrimitiveTNTPrimed} projectile. That entity
 * belongs to a later phase, so the throw goal currently only runs its cooldown.</p>
 *
 * @author Daveyx0
 */
public class FestiveCreeper extends PrimitiveCreeper implements IMultiMob {

    public FestiveCreeper(EntityType<? extends FestiveCreeper> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIThrowTNT(this));
        this.goalSelector.addGoal(3, new com.skd.mobweavecore.entity.ai.BackOffFromEntityGoal(this, 7.5D, true));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    /** The festive creeper is not allowed to explode; its fuse is permanently disabled. */
    @Override
    public void setSwellDir(int direction) {
        super.setSwellDir(-1);
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_FESTIVECREEPER);
    }

    /** Throws primed TNT at the current target from a distance. */
    public static class EntityAIThrowTNT extends Goal {
        private final FestiveCreeper creeper;
        private LivingEntity target;
        private float power = 1.5F;
        private int attackCooldown;

        public EntityAIThrowTNT(FestiveCreeper creeper) {
            this.creeper = creeper;
        }

        @Override
        public boolean canUse() {
            this.target = this.creeper.getTarget();

            if (this.target == null || !this.target.isAlive()) {
                return false;
            }

            return this.creeper.distanceTo(this.target) > 2.0D
                    && this.creeper.distanceToSqr(this.target) < 144D
                    && this.creeper.hasLineOfSight(this.target);
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void stop() {
            this.target = null;
            this.attackCooldown = 0;
        }

        @Override
        public void tick() {
            // TODO(P4): spawn the ported EntityPrimitiveTNTPrimed here (power, fuse 30) aimed at the
            // target once that projectile entity exists. Until then the goal only fires its cooldown.
            if (this.creeper.isPowered()) {
                this.power = 3.0F;
            }

            if (this.target != null && --this.attackCooldown <= 0) {
                this.creeper.playSound(SoundEvents.TNT_PRIMED, 1.0F, 1.0F);
                this.attackCooldown = 60;
            }
        }
    }
}
