// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.models;

import com.skd.primitivemenagerie.PrimitiveMenagerie;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * {@link ModelLayerLocation} identifiers for every entity model of the mod. The actual layer
 * definitions are registered by {@code PMClientSetup} on the client only.
 *
 * @author Daveyx0
 */
public final class PMModelLayers {

    public static final ModelLayerLocation EMPTY = layer("empty");
    public static final ModelLayerLocation PRIMITIVE_CREEPER = layer("primitive_creeper");
    public static final ModelLayerLocation BRAIN_SLIME = layer("brain_slime");
    public static final ModelLayerLocation TREASURE_SLIME = layer("treasure_slime");
    public static final ModelLayerLocation MIMIC = layer("mimic");
    public static final ModelLayerLocation ENCHANTED_BOOK = layer("enchanted_book");
    public static final ModelLayerLocation VOID_EYE = layer("void_eye");
    public static final ModelLayerLocation FLAME_SPEWER = layer("flame_spewer");
    public static final ModelLayerLocation BLAZING_JUGGERNAUT = layer("blazing_juggernaut");
    public static final ModelLayerLocation HARPY = layer("harpy");
    public static final ModelLayerLocation LILY_LURKER = layer("lily_lurker");
    public static final ModelLayerLocation YETI = layer("yeti");
    public static final ModelLayerLocation GOBLIN = layer("goblin");
    public static final ModelLayerLocation TROLLAGER = layer("trollager");
    public static final ModelLayerLocation SHEEPMAN = layer("sheepman");
    public static final ModelLayerLocation GROVE_SPRITE = layer("grove_sprite");
    public static final ModelLayerLocation CHAMELEON = layer("chameleon");
    public static final ModelLayerLocation DODO = layer("dodo");
    public static final ModelLayerLocation FILCH_LIZARD = layer("filch_lizard");
    public static final ModelLayerLocation LOST_MINER = layer("lost_miner");
    public static final ModelLayerLocation TRAVELING_MERCHANT = layer("traveling_merchant");

    private PMModelLayers() {}

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(
                ResourceLocation.fromNamespaceAndPath(PrimitiveMenagerie.MODID, name), "main");
    }
}
