// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.network;

import java.util.UUID;

import com.skd.primitivemenagerie.core.PMReference;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Port of {@code MessageTeleportEye}. Sent by the Void Eye client logic to request a teleport from
 * the server.
 *
 * @author Daveyx0
 */
public record TeleportEyePayload(boolean teleport, UUID id) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TeleportEyePayload> TYPE =
            new CustomPacketPayload.Type<>(PMReference.id("teleport_eye"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TeleportEyePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, TeleportEyePayload::teleport,
                    ByteBufCodecs.STRING_UTF8, payload -> payload.id().toString(),
                    (teleport, id) -> new TeleportEyePayload(teleport, UUID.fromString(id)));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
