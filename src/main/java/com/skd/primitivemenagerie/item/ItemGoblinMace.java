// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;

/**
 * Goblin mace, ported from {@code ItemGoblinMace}. The original custom {@code ToolMaterial}
 * ({@code goblin_metal}: 400 uses, speed 6.0, damage 2.5, enchantability 10) is expressed as a
 * {@link Tier}. The extra armor damage is applied by the goblin mace interaction handler.
 *
 * @author Daveyx0
 */
public class ItemGoblinMace extends SwordItem {

    public static final float damagePercentage = 0.1F;

    /** Ported {@code ToolMaterial} of the original. */
    public static final Tier GOBLIN_METAL = new Tier() {
        @Override
        public int getUses() {
            return 400;
        }

        @Override
        public float getSpeed() {
            return 6.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 2.5F;
        }

        @Override
        public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_IRON_TOOL;
        }

        @Override
        public int getEnchantmentValue() {
            return 10;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(Items.IRON_INGOT);
        }
    };

    public ItemGoblinMace(Item.Properties properties) {
        super(GOBLIN_METAL, properties.attributes(SwordItem.createAttributes(GOBLIN_METAL, 3.0F, -3.0F)));
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(Items.IRON_INGOT) || super.isValidRepairItem(toRepair, repair);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Damages armor more quickly; independent of armor strength.")
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
