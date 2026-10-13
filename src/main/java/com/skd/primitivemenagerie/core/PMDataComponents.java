// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Data components of the mod. In 1.12 the camouflage armor and the wonder sap stored their state in
 * the item's NBT tag; 1.21 replaces raw NBT with typed data components.
 *
 * @author Daveyx0
 */
public final class PMDataComponents {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, PMReference.MODID);

    /** The camouflage color of a camouflage armor piece (packed RGB). */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CAMOUFLAGE_COLOR =
            register("camouflage_color", Codec.INT, ByteBufCodecs.VAR_INT);

    /** Whether the camouflage of a camouflage armor piece has been toggled off. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CAMOUFLAGE_DISABLED =
            register("camouflage_disabled", Codec.BOOL, ByteBufCodecs.BOOL);

    /** The id of the block the camouflage color of an armor piece was sampled from. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> CAMOUFLAGE_BLOCK =
            register("camouflage_block", Codec.STRING, ByteBufCodecs.STRING_UTF8);

    /** The id of the log block a wonder sap came from. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> SAP_LOG =
            register("sap_log", Codec.STRING, ByteBufCodecs.STRING_UTF8);

    /** The tint color of a wonder sap crystal. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SAP_COLOR =
            register("sap_color", Codec.INT, ByteBufCodecs.VAR_INT);

    private PMDataComponents() {}

    /** Forces class initialisation so the deferred entries above are created. */
    public static void register() {}

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(
            String name, Codec<T> codec, net.minecraft.network.codec.StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, T> streamCodec) {
        return COMPONENTS.register(name, () -> DataComponentType.<T>builder()
                .persistent(codec)
                .networkSynchronized(streamCodec)
                .build());
    }
}
