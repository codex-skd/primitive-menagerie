// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Thrown block, ported from {@code EntityThrownBlock}. It falls under gravity and damages nearby
 * entities when it lands, then disappears. The block state is read from the world at the stored
 * origin position by the renderer.
 *
 * @author Daveyx0
 */
public class ThrownBlock extends Entity {

    private static final EntityDataAccessor<BlockPos> ORIGIN =
            SynchedEntityData.defineId(ThrownBlock.class, EntityDataSerializers.BLOCK_POS);

    @Nullable
    private LivingEntity owner;

    public ThrownBlock(EntityType<? extends ThrownBlock> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
    }

    public ThrownBlock(EntityType<? extends ThrownBlock> type, Level level, double x, double y, double z,
            @Nullable LivingEntity igniter, BlockPos blockPos) {
        this(type, level);
        this.setPos(x, y, z);
        float f = (float) (Math.random() * (Math.PI * 2D));
        this.setDeltaMovement(-Math.sin(f) * 0.02D, 0.2D, -Math.cos(f) * 0.02D);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = igniter;
        this.setOrigin(blockPos);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ORIGIN, BlockPos.ZERO);
    }

    public void setOrigin(BlockPos pos) {
        this.entityData.set(ORIGIN, pos);
    }

    public BlockPos getOrigin() {
        return this.entityData.get(ORIGIN);
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04D;
    }

    @Override
    public void tick() {
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));

            if (!this.level().isClientSide) {
                this.damageNearbyEntities();
            } else {
                BlockState state = this.level().getBlockState(this.getOrigin());

                for (int i = 0; i < 36; i++) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state),
                            this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                            this.getY() + 0.5D,
                            this.getZ() + (this.random.nextFloat() - this.random.nextFloat()),
                            this.random.nextFloat() - this.random.nextFloat(), 1.0D,
                            this.random.nextFloat() - this.random.nextFloat());
                }
            }

            this.discard();
        }
    }

    private void damageNearbyEntities() {
        AABB box = this.getBoundingBox().inflate(1.2D, 1.2D, 1.2D);
        List<Entity> list = this.level().getEntitiesOfClass(Entity.class, box, entity -> entity != this);

        for (Entity entity : list) {
            if (this.owner == null) {
                entity.hurt(this.damageSources().thrown(this, this), 10.0F);
            } else {
                entity.hurt(this.damageSources().thrown(this, this.owner), 10.0F);
            }
        }
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put("BlockPos", NbtUtils.writeBlockPos(this.getOrigin()));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("BlockPos")) {
            NbtUtils.readBlockPos(compound, "BlockPos").ifPresent(this::setOrigin);
        }
    }
}
