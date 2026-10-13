// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.item;

import java.util.List;

import com.skd.primitivemenagerie.core.PMDataComponents;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Wonder sap, ported from {@code ItemGroveSpriteSap}. The 1.12 NBT block-state and {@code display.color}
 * tags are replaced by the {@code sap_log} and {@code sap_color} data components.
 *
 * @author Daveyx0
 */
public class ItemGroveSpriteSap extends ItemPrimitive {

    public ItemGroveSpriteSap(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        String logId = stack.get(PMDataComponents.SAP_LOG.get());

        if (logId != null) {
            ResourceLocation id = ResourceLocation.tryParse(logId);
            Block block = id == null ? Blocks.AIR : BuiltInRegistries.BLOCK.get(id);
            tooltip.add(Component.literal("Type: " + block.getName().getString()).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.literal("Type: Unknown").withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }

    /** Stores the log block state the sap was taken from, mirroring {@code setSapLogState}. */
    public static void setSapLogState(ItemStack stack, BlockState log) {
        if (log != null && !log.isAir()) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(log.getBlock());
            stack.set(PMDataComponents.SAP_LOG.get(), id.toString());
        }
    }

    /** @return the log stack this sap converts back into, or an empty stack */
    public static ItemStack getLogFromSap(ItemStack sap, int logAmount) {
        String logId = sap.get(PMDataComponents.SAP_LOG.get());

        if (logId != null) {
            ResourceLocation id = ResourceLocation.tryParse(logId);

            if (id != null && BuiltInRegistries.BLOCK.containsKey(id)) {
                return new ItemStack(BuiltInRegistries.BLOCK.get(id), logAmount);
            }
        }

        return ItemStack.EMPTY;
    }

    /** Sets the tint color of the sap crystal. */
    public static void setColor(ItemStack stack, int color) {
        stack.set(PMDataComponents.SAP_COLOR.get(), color);
    }

    /** @return the tint color of the sap crystal, or white */
    public static int getColor(ItemStack stack) {
        return stack.getOrDefault(PMDataComponents.SAP_COLOR.get(), 0xFFFFFF);
    }
}
