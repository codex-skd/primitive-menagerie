// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.item;

import java.util.function.Supplier;

import com.skd.primitivemenagerie.entity.item.PrimitiveThrowable;
import com.skd.primitivemenagerie.entity.item.SpiderEgg;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Thrown egg item, ported from {@code ItemPrimitiveEgg}. Right-clicking throws a
 * {@link PrimitiveThrowable} that may spawn the configured creature on impact. A baby spider egg
 * is thrown as a {@link SpiderEgg} instead, matching the original.
 *
 * @author Daveyx0
 */
public class ItemPrimitiveEgg extends ItemPrimitive {

    private final Supplier<EntityType<? extends Mob>> entry;
    private final int spawnChance;
    private final boolean spiderEgg;
    private final boolean glint;

    public ItemPrimitiveEgg(Properties properties, Supplier<EntityType<? extends Mob>> entry, int spawnChance, boolean spiderEgg) {
        this(properties, entry, spawnChance, spiderEgg, false);
    }

    public ItemPrimitiveEgg(Properties properties, Supplier<EntityType<? extends Mob>> entry, int spawnChance, boolean spiderEgg, boolean glint) {
        super(properties);
        this.entry = entry;
        this.spawnChance = spawnChance;
        this.spiderEgg = spiderEgg;
        this.glint = glint;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.glint || super.isFoil(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW,
                SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            if (this.spiderEgg) {
                SpiderEgg egg = new SpiderEgg(level, player);
                egg.setSpawnChance(this.spawnChance);
                egg.setDefaultItem(this);
                egg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(egg);
            } else {
                PrimitiveThrowable egg = new PrimitiveThrowable(level, player, this.entry, this.spawnChance);
                egg.setDefaultItem(this);
                egg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(egg);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
