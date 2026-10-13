// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMob;
import com.skd.mobweavecore.util.EntityUtil;
import com.skd.primitivemenagerie.core.PMSoundEvents;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

/**
 * Brain Slime, ported from {@code EntityBrainSlime}. It rides on top of its victim and slowly drains
 * the helmet or health until it grows in size.
 *
 * <p>Simplifications: the custom coloured slime particles and the particle network message of the
 * original are dropped (the vanilla slime particles are used); the sand-only spawn check is not
 * ported because spawn placements are registered in P6.</p>
 *
 * @author Daveyx0
 */
public class BrainSlime extends Slime implements IMultiMob {

    public int attackDelay;
    public float suckingb;
    public float suckingc;
    public float suckingd;
    public float suckinge;
    public float suckingh;
    private boolean wasOnGround;
    public int maxStack;

    private static final EntityDataAccessor<Integer> SATURATION =
            SynchedEntityData.defineId(BrainSlime.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_DELAY =
            SynchedEntityData.defineId(BrainSlime.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<UUID>> VICTIM_UNIQUE_ID =
            SynchedEntityData.defineId(BrainSlime.class, EntityDataSerializers.OPTIONAL_UUID);

    public BrainSlime(EntityType<? extends Slime> type, Level level) {
        super(type, level);
        this.moveControl = new BrainSlimeMoveControl(this);
        this.setAttackDelay(0);
        this.suckingb = 0.0F;
        this.suckingc = 0.0F;
        this.suckingh = 1.0F;
        this.maxStack = 10;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BrainSlimeFloatGoal(this));
        this.goalSelector.addGoal(2, new BrainSlimeAttackGoal(this));
        this.goalSelector.addGoal(3, new BrainSlimeFaceRandomGoal(this));
        this.goalSelector.addGoal(4, new BrainSlimeHopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACK_DELAY, 0);
        builder.define(SATURATION, 0);
        builder.define(VICTIM_UNIQUE_ID, Optional.empty());
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnGroupData) {
        int i = this.random.nextInt(3);
        this.setSize(i, true);

        if (i == 0) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(2.0D);
            this.setHealth(this.getMaxHealth());
        }

        return spawnGroupData;
    }

    @Override
    public void tick() {
        if (this.getSaturation() >= 10) {
            this.setSaturation(this.getSaturation() + 1);

            if (this.getSaturation() >= 100) {
                this.setSize(this.getSize() + 1, true);
                this.setSaturation(0);
                this.setAttackDelay(50);
                // TODO(verify): the original sent a particle message to all clients here.
            }
        }

        this.suckinge = this.suckingb;
        this.suckingd = this.suckingc;
        this.setAttackDelay(this.getAttackDelay() - 1);

        if (this.getTarget() != null) {
            this.setVictimId(this.getTarget().getUUID());
        }

        if (!this.isPassenger() && this.getVictim() != null) {
            this.suckingb = 0.0F;
            this.suckingc = 0.0F;
            this.suckingh = 1.0F;

            if (this.getAttackDelay() <= 0 && this.distanceToSqr(this.getVictim()) < 6.0D) {
                this.startRidingTopEntity(this.getVictim());
                this.setAttackDelay(5);
            }
        }

        if (this.isPassenger()) {
            if (this.getAttackDelay() <= 0) {
                this.attackRiddenEntity();
                this.setAttackDelay(20);
            }

            this.suckingc = (float) ((double) this.suckingc + 4 * 0.8D);
            this.suckingc = Math.clamp(this.suckingc, 0.0F, 0.2F);

            if (this.suckingh < 0.2F) {
                this.suckingh = 0.2F;
            }

            this.suckingh = (float) ((double) this.suckingh * 0.9D);
            this.suckingb += this.suckingh * 2.0F;

            if (this.getVictim() == null || !this.isAlive()) {
                this.dismountSelf();
            }
        }

        super.tick();
    }

    public boolean hasBrainToSuck(LivingEntity entity) {
        if (this.getSaturation() >= 10) {
            return false;
        }

        if (entity instanceof net.minecraft.world.entity.TamableAnimal tameable && tameable.isTame()) {
            return false;
        }

        if (entity.getType().is(net.minecraft.tags.EntityTypeTags.UNDEAD)) {
            return false;
        }

        return !(entity instanceof Slime);
    }

    public void startRidingTopEntity(Entity entity) {
        Entity top = getTopPassenger(entity);

        if (top != null && !top.isRemoved()) {
            this.startRiding(top, false);
        }
    }

    public static Entity getTopPassenger(Entity entity) {
        Entity top = entity;

        while (entity.isVehicle()) {
            List<Entity> passengers = entity.getPassengers();

            if (!passengers.isEmpty()) {
                entity = passengers.get(0);
                top = entity;
            }
        }

        return top;
    }

    public static Entity getBottomPassenger(Entity entity) {
        Entity bottom = entity;

        while (entity.isPassenger()) {
            Entity vehicle = entity.getVehicle();

            if (vehicle != null && !vehicle.isRemoved()) {
                entity = vehicle;
                bottom = entity;
            }
        }

        return bottom;
    }

    public void attackRiddenEntity() {
        Entity entity = getBottomPassenger(this);

        if (entity instanceof LivingEntity living) {
            if (this.hasBrainToSuck(living)) {
                this.damageHelmetOrEntity(living);
                this.setSaturation(this.getSaturation() + 1);
            } else {
                this.dismountSelf();
                this.setAttackDelay(20);
            }
        }
    }

    public void dismountSelf() {
        this.setVictimId(null);
        this.setAttackDelay(20);
        this.stopRiding();
    }

    public void damageHelmetOrEntity(LivingEntity base) {
        ItemStack stack = base.getItemBySlot(EquipmentSlot.HEAD);
        int damage = this.getAttackStrength();

        if (!stack.isEmpty() && stack.isDamageableItem()) {
            stack.hurtAndBreak(damage, base, EquipmentSlot.HEAD);
        } else if (base.hurt(this.damageSources().mobAttack(this), damage >= 6 ? damage : 6)) {
            this.playSound(SoundEvents.SLIME_ATTACK, 1.0F,
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    @Override
    protected boolean spawnCustomParticles() {
        return false;
    }

    protected int getAttackStrength() {
        return (this.getSize() + 1) / 3;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isPassenger()) {
            this.dismountSelf();
            this.setAttackDelay(10);
        }

        return super.hurt(source, amount);
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        if (this.getVehicle() instanceof Player) {
            return new Vec3(0.0D, 0.25D, 0.0D);
        }

        return super.getPassengerAttachmentPoint(entity, dimensions, partialTick);
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("CurrentsAttackDelay", this.getAttackDelay());
        compound.putInt("Saturation", this.getSaturation());

        if (this.getVictimId() == null) {
            compound.putString("VictimUUID", "");
        } else {
            compound.putString("VictimUUID", this.getVictimId().toString());
        }
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setAttackDelay(compound.getInt("CurrentAttackDelay"));
        this.setSaturation(compound.getInt("Saturation"));

        String s = compound.getString("VictimUUID");

        if (!s.isEmpty()) {
            try {
                this.setVictimId(UUID.fromString(s));
            } catch (Throwable ignored) {
            }
        }
    }

    public void setAttackDelay(int delay) {
        this.entityData.set(ATTACK_DELAY, delay);
    }

    public int getAttackDelay() {
        return this.entityData.get(ATTACK_DELAY);
    }

    public void setSaturation(int saturation) {
        this.entityData.set(SATURATION, saturation);
    }

    public int getSaturation() {
        return this.entityData.get(SATURATION);
    }

    @Nullable
    public UUID getVictimId() {
        return this.entityData.get(VICTIM_UNIQUE_ID).orElse(null);
    }

    public void setVictimId(@Nullable UUID id) {
        this.entityData.set(VICTIM_UNIQUE_ID, Optional.ofNullable(id));
    }

    @Nullable
    public LivingEntity getVictim() {
        UUID uuid = this.getVictimId();

        if (uuid == null) {
            return null;
        }

        Player player = this.level().getPlayerByUUID(uuid);

        if (player != null) {
            return player;
        }

        return EntityUtil.getLoadedEntityByUUID(uuid, this.level());
    }

    // ---------------------------------------------------------------------------------------------
    // Goals and move control, ported from the original nested classes
    // ---------------------------------------------------------------------------------------------

    static class BrainSlimeAttackGoal extends Goal {
        private final BrainSlime slime;

        public BrainSlimeAttackGoal(BrainSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.slime.getTarget();

            if (this.slime.getSaturation() >= 10) {
                return false;
            }

            if (target != null && this.slime.isPassengerOfSameVehicle(target)) {
                return false;
            }

            if (target != null && this.slime.distanceTo(target) > 12.0F) {
                return false;
            }

            return target != null && target.isAlive() && !(target instanceof Player player && player.isCreative());
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.slime.getTarget();

            if (target != null) {
                this.slime.lookAt(target, 10.0F, 10.0F);
            }

            if (this.slime.getMoveControl() instanceof BrainSlimeMoveControl control) {
                control.setDirection(this.slime.getYRot(), this.slime.isDealsDamage());
            }
        }
    }

    static class BrainSlimeFaceRandomGoal extends Goal {
        private final BrainSlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public BrainSlimeFaceRandomGoal(BrainSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.slime.getTarget() == null
                    && (this.slime.onGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION));
        }

        @Override
        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = this.adjustedTickDelay(10 + this.slime.getRandom().nextInt(60));
                this.chosenDegrees = (float) this.slime.getRandom().nextInt(360);
            }

            if (this.slime.getMoveControl() instanceof BrainSlimeMoveControl control) {
                control.setDirection(this.chosenDegrees, false);
            }
        }
    }

    static class BrainSlimeHopGoal extends Goal {
        private final BrainSlime slime;

        public BrainSlimeHopGoal(BrainSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            if (this.slime.getMoveControl() instanceof BrainSlimeMoveControl control) {
                control.setWantedMovement(1.0D);
            }
        }
    }

    static class BrainSlimeFloatGoal extends Goal {
        private final BrainSlime slime;

        public BrainSlimeFloatGoal(BrainSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
            slime.getNavigation().setCanFloat(true);
        }

        @Override
        public boolean canUse() {
            return this.slime.isInWater() || this.slime.isInLava();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.slime.getRandom().nextFloat() < 0.8F) {
                this.slime.getJumpControl().jump();
            }

            if (this.slime.getMoveControl() instanceof BrainSlimeMoveControl control) {
                control.setWantedMovement(1.2D);
            }
        }
    }

    /** Move control ported from {@code SlimeMoveHelper}, including the charge attack. */
    static class BrainSlimeMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final BrainSlime slime;
        private boolean isAggressive;

        public BrainSlimeMoveControl(BrainSlime slime) {
            super(slime);
            this.slime = slime;
            this.yRot = 180.0F * slime.getYRot() / (float) Math.PI;
        }

        public void setDirection(float yRot, boolean aggressive) {
            this.yRot = yRot;
            this.isAggressive = aggressive;
        }

        public void setWantedMovement(double speed) {
            this.speedModifier = speed;
            this.operation = MoveControl.Operation.MOVE_TO;
        }

        @Override
        public void tick() {
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();

            if (this.operation != MoveControl.Operation.MOVE_TO) {
                this.mob.setZza(0.0F);
            } else {
                this.operation = MoveControl.Operation.WAIT;

                if (this.mob.onGround()) {
                    this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));

                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();

                        if (this.isAggressive && this.slime.getSaturation() < 10) {
                            if (this.slime.doPlayJumpSound()) {
                                this.slime.playSound(PMSoundEvents.ENTITY_BRAINSLIME_CHARGE.get(), this.slime.getSoundVolume(),
                                        ((this.slime.getRandom().nextFloat() - this.slime.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                            }

                            this.performChargeAttack();
                        } else {
                            this.slime.getJumpControl().jump();

                            if (this.slime.doPlayJumpSound()) {
                                this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(),
                                        ((this.slime.getRandom().nextFloat() - this.slime.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                            }
                        }
                    } else {
                        this.slime.xxa = 0.0F;
                        this.slime.zza = 0.0F;
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }
            }
        }

        public void performChargeAttack() {
            LivingEntity target = this.slime.getTarget();

            if (target != null) {
                Vec3 motion = this.slime.getDeltaMovement();
                double mx = motion.x + (target.getX() - this.slime.getX()) / 8.0D;
                double my = motion.y + (target.getY() - this.slime.getY()) / 8.0D;

                if (my <= 0.0D) {
                    my = 0.0D;
                }

                my += 0.5D;
                double mz = motion.z + (target.getZ() - this.slime.getZ()) / 8.0D;
                this.slime.setDeltaMovement(mx, my, mz);
            }
        }
    }
}
