// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import com.skd.primitivemenagerie.crafting.CamouflageToggleRecipe;
import com.skd.primitivemenagerie.crafting.WonderSapRecipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Recipe serializer registry of the mod, replacing the original
 * {@code PrimitiveMobsRecipes}/{@code _factories.json} pair with the 1.21 deferred registry.
 *
 * @author Daveyx0
 */
public final class PMRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, PMReference.MODID);

    /** Toggles the automatic camouflage of a camouflage armor piece. */
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> CAMOUFLAGE_TOGGLE =
            SERIALIZERS.register("camouflage_toggle", () -> new SimpleCraftingRecipeSerializer<>(CamouflageToggleRecipe::new));

    /** Converts a wonder sap back into the log it was taken from. */
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> WONDERSAP_CONVERT =
            SERIALIZERS.register("wondersap_convert", () -> new SimpleCraftingRecipeSerializer<>(WonderSapRecipe::new));

    private PMRecipes() {}

    /** Forces class initialisation so the deferred entries above are created. */
    public static void register() {}
}
