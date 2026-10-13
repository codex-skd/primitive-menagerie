// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMob;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Bewitched Tome (registry name {@code bewitched_tome}), ported from {@code EntityEnchantedBook}. A
 * floating undead book that attacks players and drops a random enchanted book.
 *
 * <p>Simplifications: the original {@code EntityAIMoveTowardsRestriction} goal is omitted and the
 * {@code getCanSpawnHere} below-Y-40 restriction is not ported (spawn placements are registered in
 * P6).</p>
 *
 * @author Daveyx0
 */
public class EnchantedBook extends Monster implements IMultiMob {

    public float floatingb;
    public float floatingc;
    public float floatingd;
    public float floatinge;
    public float floatingh;

    public EnchantedBook(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.floatingb = 0.0F;
        this.floatingc = 0.0F;
        this.floatingh = 1.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000298023224D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.4D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnGroupData) {
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
            this.level().addParticle(ParticleTypes.ENCHANT,
                    this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                    this.getY() + (this.random.nextFloat() - this.random.nextFloat()) + 1.0F,
                    this.getZ() + (this.random.nextFloat() - this.random.nextFloat()), 0.0D, 0.0D, 0.0D);
        }

        super.tick();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean hitByPlayer) {
        if (this.random.nextInt(2) == 0) {
            level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getRandom(this.random).ifPresent(holder -> {
                int power = 1 + this.random.nextInt(Math.max(1, holder.value().getMaxLevel()));
                ItemStack stack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(holder, power));
                this.spawnAtLocation(stack, 1.0F);
            });
        } else {
            this.spawnAtLocation(new ItemStack(Items.BOOK), 1.0F);
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
        return SoundEvents.WOOL_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOOL_BREAK;
    }
}
