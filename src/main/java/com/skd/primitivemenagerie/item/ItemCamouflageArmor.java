// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.item;

import java.util.List;

import com.skd.primitivemenagerie.core.PMDataComponents;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Camouflage armor, ported from {@code ItemCamouflageArmor}. The 1.12 raw NBT ({@code display.color},
 * {@code change} flag and a serialized block state) is replaced by the {@code camouflage_color},
 * {@code camouflage_disabled} and {@code camouflage_block} data components. The item tint is applied
 * by the client color handler; the per-tick color sampling moves to the client event handler.
 *
 * <p>Simplification: the original smoothly interpolated the color over several ticks before sending
 * it; the port applies the sampled color directly.</p>
 *
 * @author Daveyx0
 */
public class ItemCamouflageArmor extends ArmorItem {

    public ItemCamouflageArmor(Holder<ArmorMaterial> material, ArmorItem.Type type, Item.Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (getCannotChange(stack)) {
            tooltip.add(Component.literal("Camouflage: disabled").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.literal("Camouflage: enabled").withStyle(ChatFormatting.GRAY));
        }

        BlockState state = getColorBlockState(stack);
        if (state != null && !state.isAir()) {
            tooltip.add(Component.literal("Block: " + state.getBlock().getName().getString()).withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }

    /** @return the packed RGB camouflage color, or white */
    public static int getColor(ItemStack stack) {
        return stack.getOrDefault(PMDataComponents.CAMOUFLAGE_COLOR.get(), 0xFFFFFF);
    }

    /** Sets the packed RGB camouflage color. */
    public static void setColor(ItemStack stack, int color) {
        stack.set(PMDataComponents.CAMOUFLAGE_COLOR.get(), color);
        // Mirror into the vanilla dye component so the worn armor layer is tinted by the vanilla
        // humanoid armor layer (the material uses the dyeable leather layer).
        stack.set(net.minecraft.core.component.DataComponents.DYED_COLOR,
                new net.minecraft.world.item.component.DyedItemColor(color & 0xFFFFFF, false));
    }

    /** @return whether the item carries a camouflage color */
    public static boolean hasColor(ItemStack stack) {
        return stack.has(PMDataComponents.CAMOUFLAGE_COLOR.get());
    }

    /** Removes the camouflage color. */
    public static void removeColor(ItemStack stack) {
        stack.remove(PMDataComponents.CAMOUFLAGE_COLOR.get());
    }

    /** @return whether the automatic camouflage has been toggled off */
    public static boolean getCannotChange(ItemStack stack) {
        return stack.getOrDefault(PMDataComponents.CAMOUFLAGE_DISABLED.get(), false);
    }

    /** Toggles the automatic camouflage off or on. */
    public static void setCannotChange(ItemStack stack, boolean set) {
        stack.set(PMDataComponents.CAMOUFLAGE_DISABLED.get(), set);
    }

    /** @return the block state the camouflage color was sampled from, or air */
    public static BlockState getColorBlockState(ItemStack stack) {
        String blockId = stack.get(PMDataComponents.CAMOUFLAGE_BLOCK.get());

        if (blockId != null) {
            ResourceLocation id = ResourceLocation.tryParse(blockId);

            if (id != null && BuiltInRegistries.BLOCK.containsKey(id)) {
                Block block = BuiltInRegistries.BLOCK.get(id);
                return block.defaultBlockState();
            }
        }

        return Blocks.AIR.defaultBlockState();
    }

    /** Stores the block state the camouflage color was sampled from. */
    public static void setColorBlockState(ItemStack stack, BlockState state) {
        if (state != null && !state.isAir()) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
            stack.set(PMDataComponents.CAMOUFLAGE_BLOCK.get(), id.toString());
        } else {
            stack.remove(PMDataComponents.CAMOUFLAGE_BLOCK.get());
        }
    }
}
