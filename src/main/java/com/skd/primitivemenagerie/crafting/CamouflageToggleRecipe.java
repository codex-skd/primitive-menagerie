// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.crafting;

import com.skd.primitivemenagerie.core.PMRecipes;
import com.skd.primitivemenagerie.item.ItemCamouflageArmor;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Camouflage armor toggle recipe, ported from {@code CamouflageToggleRecipe}. Crafting a single
 * camouflage armor piece with itself toggles its automatic camouflage while keeping its color and
 * sampled block. The 1.21 {@link CustomRecipe} base replaces the 1.12 {@code ShapelessOreRecipe}
 * subclass with {@code getCraftingResult}.
 *
 * @author Daveyx0
 */
public class CamouflageToggleRecipe extends CustomRecipe {

    public CamouflageToggleRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int count = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (!stack.isEmpty()) {
                if (!(stack.getItem() instanceof ItemCamouflageArmor)) {
                    return false;
                }

                count++;
            }
        }

        return count == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (stack.getItem() instanceof ItemCamouflageArmor) {
                ItemStack output = stack.copy();
                ItemCamouflageArmor.setCannotChange(output, !ItemCamouflageArmor.getCannotChange(stack));
                return output;
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
        return PMRecipes.CAMOUFLAGE_TOGGLE.get();
    }
}
