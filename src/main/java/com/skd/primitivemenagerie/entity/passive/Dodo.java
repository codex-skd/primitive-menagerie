// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.passive;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.skd.mobweavecore.entity.IMultiMobPassive;
import com.skd.mobweavecore.entity.ai.TemptItemStackGoal;
import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.core.PMItems;
import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Dodo, ported from {@code EntityDodo}. A chicken-like passive animal that carries a random breeding
 * item, lays dodo eggs and (optionally) turns grass into mycelium.
 *
 * @author Daveyx0
 */
public class Dodo extends Chicken implements IMultiMobPassive {

    private static final List<ItemStack> BREEDING_ITEMS = List.of(
            new ItemStack(Items.LIGHT_BLUE_DYE),
            new ItemStack(Items.BEETROOT_SEEDS),
            new ItemStack(Items.BROWN_MUSHROOM),
            new ItemStack(Items.RED_MUSHROOM),
            new ItemStack(Items.PUMPKIN_SEEDS),
            new ItemStack(Items.MELON_SEEDS),
            new ItemStack(Items.WHEAT_SEEDS),
            new ItemStack(Items.APPLE),
            new ItemStack(Items.BEETROOT),
            new ItemStack(Items.CARROT),
            new ItemStack(Items.MELON_SLICE),
            new ItemStack(Items.WHEAT),
            new ItemStack(Items.SUGAR_CANE),
            new ItemStack(Items.POTATO),
            new ItemStack(Items.PUMPKIN),
            new ItemStack(Items.MELON));

    public int timeUntilNextShed;
    public int timeUntilNextDodoEgg;

    public Dodo(EntityType<? extends Dodo> type, Level level) {
        super(type, level);
        this.timeUntilNextShed = this.random.nextInt(6000) + 6000;
        this.timeUntilNextDodoEgg = this.random.nextInt(10000) + 10000;
    }

    public ItemStack getRandomBreedingItem() {
        return BREEDING_ITEMS.get(this.random.nextInt(BREEDING_ITEMS.size())).copy();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        Set<ItemStack> temptItems = BREEDING_ITEMS.stream().map(ItemStack::copy).collect(Collectors.toSet());
        this.goalSelector.addGoal(3, new TemptItemStackGoal(this, 1.0D, false, temptItems));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return ItemStack.isSameItem(stack, this.getMainHandItem());
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isInLove() && !this.getMainHandItem().isEmpty()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }

        if (!this.isInLove() && !this.level().isClientSide && this.getMainHandItem().isEmpty()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, this.getRandomBreedingItem());
        }

        if (!this.level().isClientSide && !this.isBaby() && !this.isChickenJockey() && --this.timeUntilNextShed <= 0
                && PMConfig.getDodoMycelium()) {
            this.playSound(SoundEvents.GRASS_BREAK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.setGrassToMycelium();
            this.timeUntilNextShed = this.random.nextInt(6000) + 6000;
        }

        if (!this.level().isClientSide && !this.isBaby() && !this.isChickenJockey() && --this.timeUntilNextDodoEgg <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(PMItems.DODO_EGG.get());
            this.timeUntilNextDodoEgg = this.random.nextInt(10000) + 10000;
        }
    }

    public void setGrassToMycelium() {
        BlockPos pos = new BlockPos(net.minecraft.util.Mth.floor(this.getX()),
                net.minecraft.util.Mth.floor(this.getBoundingBox().minY - 0.1D),
                net.minecraft.util.Mth.floor(this.getZ()));
        BlockState state = this.level().getBlockState(pos);

        if (state.is(Blocks.GRASS_BLOCK) && !this.level().isClientSide) {
            this.level().setBlock(pos, Blocks.MYCELIUM.defaultBlockState(), 3);
        }
    }

    @Override
    public Chicken getBreedOffspring(net.minecraft.server.level.ServerLevel level, AgeableMob otherParent) {
        return new Dodo(com.skd.primitivemenagerie.core.PMEntities.DODO.get(), level);
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.1F
                : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ShedTime", this.timeUntilNextShed);
        compound.putInt("DodoEggTime", this.timeUntilNextDodoEgg);
    }


    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("DodoEggTime")) {
            this.timeUntilNextDodoEgg = compound.getInt("DodoEggTime");
        }

        if (compound.contains("ShedTime")) {
            this.timeUntilNextShed = compound.getInt("ShedTime");
        }
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return PMLootTables.key(PMLootTables.ENTITIES_DODO);
    }
}
