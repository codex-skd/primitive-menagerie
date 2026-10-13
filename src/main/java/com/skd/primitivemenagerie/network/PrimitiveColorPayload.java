// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.network;

import java.util.UUID;

import com.skd.primitivemenagerie.core.PMReference;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Port of {@code MessagePrimitiveColor}. Sent by the camouflage armor (P6) to tell the server which
 * color the armor piece should carry.
 *
 * @author Daveyx0
 */
public record PrimitiveColorPayload(int color, int slot, UUID id) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PrimitiveColorPayload> TYPE =
            new CustomPacketPayload.Type<>(PMReference.id("primitive_color"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PrimitiveColorPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, PrimitiveColorPayload::color,
                    ByteBufCodecs.VAR_INT, PrimitiveColorPayload::slot,
                    ByteBufCodecs.STRING_UTF8, payload -> payload.id().toString(),
                    (color, slot, id) -> new PrimitiveColorPayload(color, slot, UUID.fromString(id)));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
