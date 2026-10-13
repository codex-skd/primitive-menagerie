// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.passive;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMobPassive;
import com.skd.primitivemenagerie.core.PMVillagerProfessions;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * Traveling Merchant, ported from {@code EntityTravelingMerchant}. A merchant villager that can be
 * bribed with an emerald block to settle down instead of despawning.
 *
 * <p>Simplification: the original {@code TRAVELER_VISIT} config removed the "move indoors"/door AI
 * tasks; the 1.21 villager brain is not task based, so that toggle is not applied here
 * ({@code TODO(verify)}).</p>
 *
 * @author Daveyx0
 */
public class TravelingMerchant extends Villager implements IMultiMobPassive {

    private static final EntityDataAccessor<Boolean> CAN_DESPAWN =
            SynchedEntityData.defineId(TravelingMerchant.class, EntityDataSerializers.BOOLEAN);

    public TravelingMerchant(EntityType<? extends TravelingMerchant> type, Level level) {
        super(type, level);
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return Villager.createAttributes();
    }

    @Override
    protected net.minecraft.network.chat.Component getTypeName() {
        return net.minecraft.network.chat.Component.translatable(this.getType().getDescriptionId());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CAN_DESPAWN, true);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
            @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);
        this.setVillagerData(this.getVillagerData().setProfession(PMVillagerProfessions.MERCHANT.get()));
        return data;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.is(Items.EMERALD_BLOCK) && this.canDespawn()) {
            this.consumeItemFromStack(player, itemstack);
            this.setCanDespawn(false);

            for (int i = 0; i < 8; i++) {
                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                        this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                        this.getY() + this.random.nextFloat() + 1.0D,
                        this.getZ() + (this.random.nextFloat() - this.random.nextFloat()), 0.0D, 0.0D, 0.0D);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    private void consumeItemFromStack(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    public void setCanDespawn(boolean canDespawn) {
        this.entityData.set(CAN_DESPAWN, canDespawn);
    }

    public boolean canDespawn() {
        return this.entityData.get(CAN_DESPAWN);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return this.canDespawn();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("canDespawn", this.canDespawn());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setCanDespawn(compound.getBoolean("canDespawn"));
    }
}
