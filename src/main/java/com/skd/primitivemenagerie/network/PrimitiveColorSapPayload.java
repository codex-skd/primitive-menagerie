// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.network;

import java.util.UUID;

import com.skd.primitivemenagerie.core.PMReference;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Port of {@code MessagePrimitiveColorSap}. Sent by the Grove Sprite client logic to synchronise the
 * color of the wonder sap it holds with the server.
 *
 * @author Daveyx0
 */
public record PrimitiveColorSapPayload(int color, UUID id) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PrimitiveColorSapPayload> TYPE =
            new CustomPacketPayload.Type<>(PMReference.id("primitive_color_sap"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PrimitiveColorSapPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, PrimitiveColorSapPayload::color,
                    ByteBufCodecs.STRING_UTF8, payload -> payload.id().toString(),
                    (color, id) -> new PrimitiveColorSapPayload(color, UUID.fromString(id)));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
