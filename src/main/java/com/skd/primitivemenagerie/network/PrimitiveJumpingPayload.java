// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.network;

import java.util.UUID;

import com.skd.primitivemenagerie.core.PMReference;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Port of {@code MessagePrimitiveJumping}. Sent by the client when the player presses the jump key
 * so a ridden Baby Spider can jump.
 *
 * @author Daveyx0
 */
public record PrimitiveJumpingPayload(UUID id) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PrimitiveJumpingPayload> TYPE =
            new CustomPacketPayload.Type<>(PMReference.id("primitive_jumping"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PrimitiveJumpingPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, payload -> payload.id().toString(),
                    id -> new PrimitiveJumpingPayload(UUID.fromString(id)));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
