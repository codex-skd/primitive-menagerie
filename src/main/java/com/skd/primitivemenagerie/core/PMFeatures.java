// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import com.skd.primitivemenagerie.world.gen.MimicChestFeature;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Feature registry of the mod, replacing the original {@code PrimitiveMobsMapGen} world generator.
 *
 * @author Daveyx0
 */
public final class PMFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(BuiltInRegistries.FEATURE, PMReference.MODID);

    /** Places a marker mimic chest on a cave floor. */
    public static final DeferredHolder<Feature<?>, MimicChestFeature> MIMIC_CHEST =
            FEATURES.register("mimic_chest", () -> new MimicChestFeature(NoneFeatureConfiguration.CODEC));

    private PMFeatures() {}

    /** Forces class initialisation so the deferred entries above are created. */
    public static void register() {}
}
