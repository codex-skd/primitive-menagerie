// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import com.skd.mobweavecore.entity.IMultiMob;
import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;

/**
 * Mimic, ported from {@code EntityMimic}. A chest-shaped hostile mob that can be primed to explode
 * and stores an arbitrary chest block state.
 *
 * <p>Simplifications: the original smoke particle network message is replaced by the vanilla smoke
 * particle; the getCanSpawnHere path is a P6 spawn-placement concern.</p>
 *
 * @author Daveyx0
 */
public class Mimic extends Monster implements IMultiMob {

    private static final EntityDataAccessor<BlockState> CHEST =
            SynchedEntityData.defineId(Mimic.class, EntityDataSerializers.BLOCK_STATE);

    public float nommingb;
    public float nommingc;
    public float nommingd;
    public float nomminge;
    public float nommingh;
    public float rotation;

    private boolean explode;
    private int explosionTimer;

    public Mimic(EntityType<? extends Mimic> type, Level level) {
        super(type, level);
        this.explode = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4000000417232513D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.15D, false));
        this.goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHEST, Blocks.CHEST.defaultBlockState());
    }

    public void setToExplode() {
        this.explode = true;
    }

    @Override
    public void tick() {
        Vec3 motion = this.getDeltaMovement();

        if (this.onGround() && (motion.x > 0.05D || motion.z > 0.05D || motion.x < -0.05D || motion.z < -0.05D)) {
            this.setDeltaMovement(motion.x, 0.4D, motion.z);
        }

        if (this.horizontalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.5D, this.getDeltaMovement().z);
        }

        this.nomminge = this.nommingb;
        this.nommingd = this.nommingc;

        this.nommingc = (float) ((double) this.nommingc + 4 * 0.8D);
        this.nommingc = Math.clamp(this.nommingc, 0.0F, 0.2F);

        if (this.nommingh < 0.2F) {
            this.nommingh = 0.2F;
        }

        this.nommingh = (float) ((double) this.nommingh * 0.9D);
        this.nommingb += this.nommingh * 2.0F;
        this.rotation = -0.7853982F;

        if (this.random.nextInt(80) == 0) {
            float f = 0.01745278F;
            double d = this.getX() - Math.sin(this.getYRot() * f) / 3.0D;
            double d1 = this.getY() + this.random.nextDouble() / 3.0D;
            double d2 = this.getZ() + Math.cos(this.getYRot() * f) / 3.0D;

            for (int j = 0; j < 12; j++) {
                this.level().addParticle(ParticleTypes.SPLASH, d, d1 + 0.3D, d2, 0.0D, 0.0D, 0.0D);
            }
        }

        if (this.explode) {
            this.setSpeed(0.0F);

            if (this.level().isClientSide) {
                for (int i = 0; i < 5; i++) {
                    this.level().addParticle(ParticleTypes.LARGE_SMOKE,
                            this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                            this.getY() + 1.0F + (this.random.nextFloat() - this.random.nextFloat()),
                            this.getZ() + (this.random.nextFloat() - this.random.nextFloat()),
                            0.0D, 0.0D, 0.0D);
                }
            }

            if (this.explosionTimer == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 1.0F);
            } else if (this.explosionTimer >= 40 && !this.level().isClientSide) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, true,
                        Level.ExplosionInteraction.MOB);
                this.discard();
            }

            this.explosionTimer++;
        }

        super.tick();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CHEST_OPEN;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHEST_CLOSE;
    }

    public void setChest(BlockState chest) {
        this.entityData.set(CHEST, chest);
    }

    public BlockState getChest() {
        return this.entityData.get(CHEST);
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_MIMIC);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("chestState", NbtUtils.writeBlockState(this.getChest()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("chestState")) {
            this.setChest(NbtUtils.readBlockState(this.registryAccess().lookupOrThrow(Registries.BLOCK),
                    compound.getCompound("chestState")));
        }
    }
}
