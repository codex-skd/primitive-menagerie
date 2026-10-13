// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import com.skd.mobweavecore.entity.IMultiMob;
import com.skd.mobweavecore.entity.ai.CustomFollowOwnerGoal;
import com.skd.mobweavecore.entity.ai.CustomOwnerHurtByTargetGoal;
import com.skd.mobweavecore.entity.ai.CustomOwnerHurtTargetGoal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Baby Spider, ported from {@code EntityBabySpider}. Grows by being fed fermented spider eyes, can be
 * ridden once fully grown and follows its owner (usually a Mother Spider) using the Mobweave Core
 * owner goals.
 *
 * @author Daveyx0
 */
public class BabySpider extends PrimitiveSpider implements IMultiMob {

    private static final EntityDataAccessor<Integer> DATA_GROWTH_LEVEL =
            SynchedEntityData.defineId(BabySpider.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_DYE_COLOR =
            SynchedEntityData.defineId(BabySpider.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_IS_JUMPING =
            SynchedEntityData.defineId(BabySpider.class, EntityDataSerializers.BOOLEAN);

    private int rideAttemptDelay;
    private boolean initChild;

    public BabySpider(EntityType<? extends BabySpider> type, Level level) {
        super(type, level);
        this.setTamed(false);
        this.setGrowthLevel(0);
        this.rideAttemptDelay = this.random.nextInt(100);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_GROWTH_LEVEL, 0);
        builder.define(DATA_DYE_COLOR, (byte) 0);
        builder.define(DATA_IS_JUMPING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new PrimitiveSpider.AISpiderAttack(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new CustomFollowOwnerGoal(this, 1.0D, 8.0F, 2.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new CustomOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new CustomOwnerHurtTargetGoal(this));
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        EntityDimensions dimensions = switch (Mth.clamp(this.getGrowthLevel(), 0, 5)) {
            case 0 -> EntityDimensions.scalable(0.5F, 0.3F);
            case 1 -> EntityDimensions.scalable(0.7F, 0.4F);
            case 2 -> EntityDimensions.scalable(0.9F, 0.5F);
            case 3 -> EntityDimensions.scalable(1.1F, 0.6F);
            case 4 -> EntityDimensions.scalable(1.3F, 0.7F);
            default -> EntityDimensions.scalable(1.5F, 0.8F);
        };

        return dimensions.withEyeHeight(dimensions.height() / 2.0F);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.initChild && this.getOwner() instanceof MotherSpider mother) {
            mother.addFollower(this);
            this.initChild = true;
        }

        if (this.rideAttemptDelay++ > 100 && this.getOwner() instanceof MotherSpider mother && !mother.isVehicle()) {
            this.startRiding(mother);
        }

        if (this.level().isClientSide && this.getOwner() == null) {
            this.level().addParticle(ParticleTypes.SPLASH,
                    this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                    this.getY() + (this.random.nextFloat() - this.random.nextFloat()) + 1.0F,
                    this.getZ() + (this.random.nextFloat() - this.random.nextFloat()),
                    0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public boolean onClimbable() {
        BlockPos pos = this.blockPosition();
        boolean side1 = this.level().getBlockState(pos.east()).isSolidRender(this.level(), pos.east());
        boolean side2 = this.level().getBlockState(pos.west()).isSolidRender(this.level(), pos.west());
        boolean side3 = this.level().getBlockState(pos.south()).isSolidRender(this.level(), pos.south());
        boolean side4 = this.level().getBlockState(pos.north()).isSolidRender(this.level(), pos.north());
        return this.isClimbing() || side1 || side2 || side3 || side4;
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getOwner() instanceof MotherSpider mother) {
            mother.setIsAngry(true);
        }

        if (source.is(DamageTypes.IN_WALL)) {
            return !this.isPassenger();
        }

        return super.hurt(source, amount);
    }

    public boolean canBeSteered() {
        return this.getGrowthLevel() >= 5;
    }

    public void setGrowthLevel(int level) {
        this.entityData.set(DATA_GROWTH_LEVEL, level);
        this.refreshDimensions();
    }

    public int getGrowthLevel() {
        return this.entityData.get(DATA_GROWTH_LEVEL);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.FERMENTED_SPIDER_EYE)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            this.setGrowthLevel(this.getGrowthLevel() + 1);

            for (int i = 0; i < 8; i++) {
                this.level().addParticle(ParticleTypes.SMOKE,
                        this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                        this.getY() + this.random.nextFloat() + 1.0D,
                        this.getZ() + (this.random.nextFloat() - this.random.nextFloat()),
                        0.0D, 0.0D, 0.0D);
            }

            if (this.getGrowthLevel() == 5) {
                this.setEyeColor(getRandomEyeColor(this.random));
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D + this.random.nextFloat() * 0.1F);
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0D);
                this.setHealth(30.0F);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (stack.getItem() instanceof DyeItem dye) {
            if (this.getEyeColor() != dye.getDyeColor()) {
                this.setEyeColor(dye.getDyeColor());

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
        }

        return super.mobInteract(player, hand);
    }

    public DyeColor getEyeColor() {
        return DyeColor.byId(this.entityData.get(DATA_DYE_COLOR) & 15);
    }

    public void setEyeColor(DyeColor color) {
        byte b0 = this.entityData.get(DATA_DYE_COLOR);
        this.entityData.set(DATA_DYE_COLOR, (byte) (b0 & 240 | color.getId() & 15));
    }

    public void setIsJumping(boolean jumping) {
        this.entityData.set(DATA_IS_JUMPING, jumping);
    }

    public boolean getIsJumping() {
        return this.entityData.get(DATA_IS_JUMPING);
    }

    @Override
    public LivingEntity getControllingPassenger() {
        if (this.canBeSteered() && !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof Player player) {
            return player;
        }

        return null;
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float strafe = player.xxa * 0.5F;
        float forward = player.zza;

        if (forward <= 0.0F) {
            forward *= 0.5F;
        }

        return new Vec3(strafe, 0.0D, forward);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) / 1.5F;
    }

    @Override
    protected float getJumpPower() {
        return 0.62F;
    }

    public static DyeColor getRandomEyeColor(RandomSource random) {
        return DyeColor.byId(random.nextInt(DyeColor.values().length));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Growth", this.getGrowthLevel());
        compound.putBoolean("Jumping", this.getIsJumping());
        compound.putByte("Color", (byte) this.getEyeColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setGrowthLevel(compound.getInt("Growth"));
        this.setIsJumping(compound.getBoolean("Jumping"));
        this.setEyeColor(DyeColor.byId(compound.getByte("Color")));
    }
}
