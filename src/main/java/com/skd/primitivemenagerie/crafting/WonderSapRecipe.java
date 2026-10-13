// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.crafting;

import com.skd.primitivemenagerie.core.PMRecipes;
import com.skd.primitivemenagerie.item.ItemGroveSpriteSap;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Wonder sap conversion recipe, ported from {@code WonderSapRecipe}. Combining a wonder sap with
 * another item yields the log the sap was taken from. The DynamicTrees branch of the original is not
 * ported.
 *
 * @author Daveyx0
 */
public class WonderSapRecipe extends CustomRecipe {

    public WonderSapRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasSap = false;
        boolean hasOther = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemGroveSpriteSap) {
                    hasSap = true;
                } else {
                    hasOther = true;
                }
            }
        }

        return hasSap && hasOther;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (stack.getItem() instanceof ItemGroveSpriteSap) {
                ItemStack log = ItemGroveSpriteSap.getLogFromSap(stack, 1);

                if (!log.isEmpty()) {
                    return log;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PMRecipes.WONDERSAP_CONVERT.get();
    }
}
