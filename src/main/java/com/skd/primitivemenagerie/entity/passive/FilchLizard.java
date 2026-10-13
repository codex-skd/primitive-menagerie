// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.passive;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMobPassive;
import com.skd.mobweavecore.entity.ai.GrabItemFromFloorGoal;
import com.skd.mobweavecore.entity.ai.StealFromPlayerGoal;
import com.skd.mobweavecore.util.EntityUtil;
import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * Filch Lizard, ported from {@code EntityFilchLizard}. A small passive creature that picks up and
 * steals valuable items and flees when it is holding one.
 *
 * <p>Simplification: the original changed its collision height while holding an item; in 1.21 the
 * entity keeps a fixed size ({@code TODO(verify)}).</p>
 *
 * @author Daveyx0
 */
public class FilchLizard extends PathfinderMob implements IMultiMobPassive {

    private final int itemChance = 4;

    @Nullable
    private ItemStack[] stealItems;

    public FilchLizard(EntityType<? extends FilchLizard> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25000000417232513D);
    }

    @Override
    protected void registerGoals() {
        int prio = 0;
        this.goalSelector.addGoal(++prio, new FloatGoal(this));
        this.goalSelector.addGoal(++prio, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(++prio,
                new GrabItemFromFloorGoal(this, 1.2D, this.getStealItemSet(), true));
        this.goalSelector.addGoal(++prio,
                new StealFromPlayerGoal(this, 0.8D, this.getStealItemSet(), true));
        this.goalSelector.addGoal(++prio, new AIAvoidWhenNasty(this, Player.class, 16.0F, 1.0D, 1.33D));
        this.goalSelector.addGoal(++prio, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(++prio, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(++prio, new RandomLookAroundGoal(this));
    }

    private Set<ItemStack> getStealItemSet() {
        if (this.stealItems == null) {
            this.stealItems = EntityUtil.getCustomLootItems(this, PMLootTables.FILCHLIZARD_STEAL,
                    new ItemStack(Items.IRON_INGOT));
        }

        return new HashSet<>(Arrays.asList(this.stealItems));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
            @Nullable SpawnGroupData spawnData) {
        int chance = PMConfig.getFilchLizardLootChance();

        if (chance > 0 && (chance >= 100 || this.random.nextInt(100 / chance) == 0)) {
            int guard = 0;

            while (this.getMainHandItem().isEmpty() && !this.level().isClientSide && guard++ < 20) {
                this.setItemSlot(EquipmentSlot.MAINHAND,
                        EntityUtil.getCustomLootItem(this, PMLootTables.FILCHLIZARD_SPAWN, new ItemStack(Items.IRON_INGOT)));
            }
        }

        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        ItemStack stack = this.getMainHandItem();

        if (!stack.isEmpty() && !this.level().isClientSide) {
            this.spawnAtLocation(stack.copy());
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }

        return super.hurt(source, amount);
    }

    /** Avoids players only while carrying a stolen item. */
    static class AIAvoidWhenNasty extends AvoidEntityGoal<Player> {
        private final PathfinderMob mob;

        public AIAvoidWhenNasty(PathfinderMob entity, Class<Player> classToAvoid, float avoidDistance,
                double farSpeed, double nearSpeed) {
            super(entity, classToAvoid, avoidDistance, farSpeed, nearSpeed);
            this.mob = entity;
        }

        @Override
        public boolean canUse() {
            return !this.mob.getMainHandItem().isEmpty() && super.canUse();
        }
    }
}
