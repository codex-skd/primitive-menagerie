// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * Mimic orb, ported from the anonymous {@code ItemPrimitive} of {@code PrimitiveMobsItems}. The orb
 * turns an empty vanilla chest into a mimic (handled by the block interaction event).
 *
 * @author Daveyx0
 */
public class ItemMimicOrb extends ItemPrimitive {

    public ItemMimicOrb(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Joke Item").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("R-Click empty Chest to change it into a Mimic").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
