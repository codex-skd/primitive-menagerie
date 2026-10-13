// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import net.minecraft.resources.ResourceLocation;

/**
 * Reference constants of the mod, replacing the original {@code PrimitiveMobsReference}.
 *
 * @author Daveyx0
 */
public class PMReference {

    public static final String MODID = "primitive_menagerie";
    public static final String NAME = "Primitive Menagerie";
    public static final String VERSION = "1.2.3";
    public static final String RESOURCE_PREFIX = MODID + ":";

    private PMReference() {}

    /** Builds a {@link ResourceLocation} in this mod's namespace. */
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
