// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.monster;

import javax.annotation.Nullable;

import com.skd.mobweavecore.util.EntityUtil;
import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.core.PMLootTables;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Treasure Slime, ported from {@code EntityTreasureSlime}. It is a tameable slime that can hold (and
 * colour itself after) an arbitrary item, and can be tamed, healed and sat with slime balls.
 *
 * <p>Simplifications: the coloured slime particles of the original are replaced by the vanilla slime
 * particles; the per-item/block colour is computed on the client through the Mobweave Core
 * {@code ColorUtil} (the original did the same, only on the client side).</p>
 *
 * @author Daveyx0
 */
public class TreasureSlime extends TameableSlime {

    private float r;
    private float g;
    private float b;
    private float newR;
    private float newG;
    private float newB;
    private final int colorSpeed = 10;

    private ItemStack currentItem;
    private boolean wasOnGround;

    public TreasureSlime(EntityType<? extends Slime> type, Level level) {
        super(type, level);

        if (this.r == 0.0F && this.g == 0.0F && this.b == 0.0F) {
            this.setSkinRGB(new int[] {255, 255, 255});
        }
    }

    public TreasureSlime(EntityType<? extends Slime> type, Level level, ItemStack item, float[] rgb) {
        super(type, level);
        this.setSkinRGB(rgb);
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, item);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isTamed();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnGroupData) {
        int chance = PMConfig.getTameableSlimeChance();

        if (!this.isTamed() && chance < 100 && (chance <= 0 || this.random.nextInt(100 / chance) != 0)) {
            while (this.getMainHandItem().isEmpty() && !level.isClientSide()) {
                this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                        EntityUtil.getCustomLootItem(this, PMLootTables.TREASURESLIME_SPAWN, new ItemStack(Items.SLIME_BALL)));
            }
        }

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void tick() {
        if (this.isInWater() && !this.horizontalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.02D, 0.0D));
        }

        if (this.level().isClientSide) {
            this.changeColor();
        }

        if (this.r != this.newR || this.g != this.newG || this.b != this.newB) {
            for (int i = 0; i < this.colorSpeed; i++) {
                this.r = approach(this.r, this.newR);
                this.g = approach(this.g, this.newG);
                this.b = approach(this.b, this.newB);
            }
        }

        if (this.isTamed()) {
            if (this.random.nextInt(200) == 0) {
                this.level().addParticle(ParticleTypes.HEART,
                        this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                        this.getY() + this.random.nextFloat() + 1.0D,
                        this.getZ() + (this.random.nextFloat() - this.random.nextFloat()), 1.0D, 1.0D, 1.0D);
            }

            if (this.isSitting()) {
                this.setTarget(null);
            }
        }

        super.tick();
    }

    private static float approach(float value, float target) {
        if (value > target) {
            return value - 1.0F;
        } else if (value < target) {
            return value + 1.0F;
        }

        return value;
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        int size = this.getSize();

        if (!this.level().isClientSide && size > 1 && this.isDeadOrDying()) {
            int children = 2 + this.random.nextInt(3);

            for (int k = 0; k < children; ++k) {
                float f = ((float) (k % 2) - 0.5F) * (float) size / 4.0F;
                float f1 = ((float) (k / 2) - 0.5F) * (float) size / 4.0F;
                TreasureSlime child = this.createInstance();

                if (this.hasCustomName()) {
                    child.setCustomName(this.getCustomName());
                }

                if (this.isPersistenceRequired()) {
                    child.setPersistenceRequired();
                }

                child.setSize(size / 2, true);
                child.moveTo(this.getX() + (double) f, this.getY() + 0.5D, this.getZ() + (double) f1,
                        this.random.nextFloat() * 360.0F, 0.0F);
                this.level().addFreshEntity(child);
            }

            if (this.isTamed()) {
                ItemStack stack = this.getMainHandItem();

                if (!stack.isEmpty()) {
                    this.spawnAtLocation(stack.copy(), 1.0F);
                }
            }

            // Prevent the inherited Slime split from running a second time.
            this.setSize(1, false);
        }

        super.remove(reason);
    }

    protected TreasureSlime createInstance() {
        if (this.isTamed()) {
            TreasureSlime slime = new TreasureSlime(this.getType(), this.level());

            if (this.getOwner() != null) {
                slime.setTamed(true);
                slime.setOwner(this.getOwnerId());
            }

            return slime;
        }

        return new TreasureSlime(this.getType(), this.level(), this.getMainHandItem(), this.getSkinRGB());
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);

        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0D);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isTamed()
                && (source.is(DamageTypes.IN_WALL) || source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypeTags.IS_DROWNING))) {
            return false;
        }

        return super.hurt(source, amount);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean hitByPlayer) {
        ItemStack stack = this.getMainHandItem();

        if (!stack.isEmpty() && this.getSize() == 1) {
            this.spawnAtLocation(stack.copy(), 1.0F);
        }
    }

    @Nullable
    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return null;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (this.isTamed() && hand == InteractionHand.MAIN_HAND) {
            if (!stack.isEmpty()) {
                if (this.isHealingItem(stack)) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }

                    this.playHealEffect();
                    this.heal(20.0F);
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }

                if (this.isOwner(player)) {
                    if (!this.getMainHandItem().isEmpty()) {
                        this.spawnAtLocation(this.getMainHandItem(), 0.0F);
                    }

                    if (!stack.isEmpty()) {
                        ItemStack newStack = stack.copy();
                        newStack.setCount(1);
                        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, newStack);

                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                    }

                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            } else if (this.isOwner(player)) {
                this.setSitting(!this.isSitting());
                this.playSitEffect();
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        } else if (!stack.isEmpty() && this.isTamingItem(stack) && hand == InteractionHand.MAIN_HAND
                && this.getMainHandItem().isEmpty()) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }

            if (!this.level().isClientSide) {
                this.setTamed(true);
                this.getNavigation().stop();
                this.setHealth(20.0F);
                this.setOwner(player.getUUID());
                this.level().broadcastEntityEvent(this, (byte) 7);
            }

            this.playTameEffect();

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    // ---------------------------------------------------------------------------------------------
    // Client-side colouring
    // ---------------------------------------------------------------------------------------------

    /** Recomputes the target colour from the held item. Client only (uses the client-side ColorUtil). */
    private void changeColor() {
        ItemStack heldItem = this.getMainHandItem();

        if (heldItem.isEmpty()) {
            this.setNewSkinRGB(new int[] {255, 255, 255});
            this.currentItem = null;
            return;
        }

        if (this.currentItem != null && ItemStack.isSameItemSameComponents(heldItem, this.currentItem)) {
            return;
        }

        this.currentItem = heldItem.copy();
        int[] newColor = null;

        if (heldItem.getItem() instanceof BlockItem blockItem) {
            BlockState state = blockItem.getBlock().defaultBlockState();
            newColor = com.skd.mobweavecore.util.ColorUtil.getBlockStateColor(state, null, null, true);
        } else {
            newColor = com.skd.mobweavecore.util.ColorUtil.getItemStackColor(heldItem, this.level());
        }

        if (newColor != null) {
            newColor = com.skd.mobweavecore.util.ColorUtil.setBrightness(newColor, 25.0F);
            this.setNewSkinRGB(newColor);
        }
    }

    public float[] getSkinRGB() {
        return new float[] {this.r, this.g, this.b};
    }

    public void setSkinRGB(int[] rgb) {
        this.r = (float) rgb[0];
        this.g = (float) rgb[1];
        this.b = (float) rgb[2];
    }

    public void setSkinRGB(float[] rgb) {
        this.r = rgb[0];
        this.g = rgb[1];
        this.b = rgb[2];
    }

    public float[] getNewSkinRGB() {
        return new float[] {this.newR, this.newG, this.newB};
    }

    public void setNewSkinRGB(int[] rgb) {
        this.newR = (float) rgb[0];
        this.newG = (float) rgb[1];
        this.newB = (float) rgb[2];
    }
}
