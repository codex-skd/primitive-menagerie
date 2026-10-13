// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMob;
import com.skd.mobweavecore.util.EntityUtil;
import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Haunted Tool, ported from {@code EntityHauntedTool}. It is an invisible undead monster that floats
 * around holding a (randomly chosen) tool; its health, damage and speed are derived from the held
 * tool.
 *
 * <p>Simplification: the original {@code EntityAIMoveTowardsRestriction} goal has no direct 1.21
 * equivalent for a mob without a home restriction, so it is omitted.</p>
 *
 * @author Daveyx0
 */
public class HauntedTool extends Monster implements IMultiMob {

    public float floatingb;
    public float floatingc;
    public float floatingd;
    public float floatinge;
    public float floatingh;

    public HauntedTool(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.floatingb = 0.0F;
        this.floatingc = 0.0F;
        this.floatingh = 1.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnGroupData) {
        while (this.getMainHandItem().isEmpty() && !level.isClientSide()) {
            ItemStack tool = EntityUtil.getCustomLootItem(this, PMLootTables.HAUNTEDTOOL_SPAWN,
                    new ItemStack(Items.WOODEN_SWORD));
            this.setItemSlot(EquipmentSlot.MAINHAND, tool);

            if (tool.isDamageableItem()) {
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double) getHealthFromTool(tool));
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(getDamageFromHeldItem(this));
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(getSpeedFromHeldItem(this));
                this.setHealth((float) this.getAttribute(Attributes.MAX_HEALTH).getBaseValue());
            }
        }

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void tick() {
        this.floatinge = this.floatingb;
        this.floatingd = this.floatingc;
        this.floatingc = (float) ((double) this.floatingc + 2 * 0.8D);
        this.floatingc = Math.clamp(this.floatingc, 0.0F, 0.2F);

        if (this.floatingh < 0.2F) {
            this.floatingh = 0.2F;
        }

        this.floatingh = (float) ((double) this.floatingh * 0.9D);
        this.floatingb += this.floatingh * 1.0F;

        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SMOKE,
                    this.getX() + (this.random.nextFloat() / 2 - this.random.nextFloat() / 2),
                    this.getY() + (this.random.nextFloat() / 2 - this.random.nextFloat() / 2) + 1.0F,
                    this.getZ() + (this.random.nextFloat() / 2 - this.random.nextFloat() / 2), 0.0D, 0.0D, 0.0D);
        }

        if (this.getMainHandItem().isEmpty()) {
            this.discard();
        }

        super.tick();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean hitByPlayer) {
        ItemStack stack = this.getMainHandItem();

        if (!stack.isEmpty()) {
            if (!PMConfig.getHauntedToolDurability()) {
                int lootingModifier = Mth.clamp(hitByPlayer ? 1 : 0, 0, 3);
                int itemDurability = stack.getMaxDamage();
                int minDurability = itemDurability / 10;
                int maxDurability = itemDurability / 2;
                minDurability *= lootingModifier + 1;
                maxDurability *= lootingModifier + 2;
                stack.setDamageValue(itemDurability - Mth.nextInt(this.random, minDurability, maxDurability));
            }

            this.spawnAtLocation(stack, 1.0F);
        }
    }

    @Nullable
    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return null;
    }

    @Override
    public boolean onClimbable() {
        return this.horizontalCollision;
    }

    @Override
    public void jumpFromGround() {
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ITEM_BREAK;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ITEM_BREAK;
    }

    public static float getHealthFromTool(ItemStack tool) {
        if (!tool.isEmpty() && tool.isDamageableItem()) {
            float health = tool.getMaxDamage() / 10.0F;

            if (health > 100.0F) {
                health = 100.0F;
            }

            return health;
        }

        return 10.0F;
    }

    public static double getDamageFromHeldItem(LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();

        if (!stack.isEmpty() && stack.isDamageableItem()) {
            double[] amount = {Double.NaN};
            stack.forEachModifier(EquipmentSlot.MAINHAND, (attribute, modifier) -> {
                if (Double.isNaN(amount[0]) && attribute.is(Attributes.ATTACK_DAMAGE)) {
                    amount[0] = modifier.amount();
                }
            });

            if (!Double.isNaN(amount[0])) {
                double attackDamage = amount[0] / 2.0D;
                attackDamage = Mth.clamp(attackDamage, 1.0D, 8.0D);
                return attackDamage;
            }
        }

        return 1.0D;
    }

    public static double getSpeedFromHeldItem(LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();

        if (!stack.isEmpty() && stack.isDamageableItem()) {
            double[] amount = {Double.NaN};
            stack.forEachModifier(EquipmentSlot.MAINHAND, (attribute, modifier) -> {
                if (Double.isNaN(amount[0]) && attribute.is(Attributes.ATTACK_SPEED)) {
                    amount[0] = modifier.amount();
                }
            });

            if (!Double.isNaN(amount[0])) {
                double attackSpeed = 0.5D - (amount[0] * -1 * 0.1D);
                attackSpeed = Mth.clamp(attackSpeed, 0.1D, 0.3D);
                return attackSpeed;
            }
        }

        return 0.2D;
    }
}
