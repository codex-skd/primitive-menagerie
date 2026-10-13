// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.network;

import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.BabySpider;
import com.skd.primitivemenagerie.entity.monster.VoidEye;
import com.skd.primitivemenagerie.entity.passive.GroveSprite;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Network registry of the mod, replacing the original {@code PrimitiveMobsMessageRegistry}. Every
 * message of the original is ported as a {@link net.minecraft.network.protocol.common.custom.CustomPacketPayload}
 * and registered on the mod event bus through {@link RegisterPayloadHandlersEvent}.
 *
 * @author Daveyx0
 */
public final class PMNetwork {

    private static final String VERSION = "1";

    private PMNetwork() {}

    /** Registers the payload listeners on the mod event bus. */
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(PMNetwork::onRegisterPayloadHandlers);
    }

    private static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(VERSION);

        registrar.playToServer(PrimitiveColorPayload.TYPE, PrimitiveColorPayload.STREAM_CODEC,
                (payload, context) -> handlePrimitiveColor(payload, context.player()));
        registrar.playToServer(PrimitiveColorSapPayload.TYPE, PrimitiveColorSapPayload.STREAM_CODEC,
                (payload, context) -> handlePrimitiveColorSap(payload, context.player()));
        registrar.playToServer(PrimitiveJumpingPayload.TYPE, PrimitiveJumpingPayload.STREAM_CODEC,
                (payload, context) -> handlePrimitiveJumping(payload, context.player()));
        registrar.playToServer(TeleportEyePayload.TYPE, TeleportEyePayload.STREAM_CODEC,
                (payload, context) -> handleTeleportEye(payload, context.player()));
    }

    private static void handlePrimitiveColor(PrimitiveColorPayload payload, Player player) {
        Entity entity = getEntity(payload.id(), player);

        if (entity == null) {
            entity = player;
        }

        int slotId = payload.slot();

        if (slotId < 0 || slotId >= net.minecraft.world.entity.EquipmentSlot.values().length) {
            return;
        }

        net.minecraft.world.entity.EquipmentSlot slot = net.minecraft.world.entity.EquipmentSlot.values()[slotId];

        if (entity instanceof net.minecraft.world.entity.LivingEntity living) {
            net.minecraft.world.item.ItemStack stack = living.getItemBySlot(slot);

            if (stack.getItem() instanceof com.skd.primitivemenagerie.item.ItemCamouflageArmor) {
                com.skd.primitivemenagerie.item.ItemCamouflageArmor.setColor(stack, payload.color());
            }
        }
    }

    private static void handlePrimitiveColorSap(PrimitiveColorSapPayload payload, Player player) {
        Entity entity = getEntity(payload.id(), player);

        if (entity instanceof GroveSprite sprite) {
            sprite.setSapColor(payload.color());
        }
    }

    private static void handlePrimitiveJumping(PrimitiveJumpingPayload payload, Player player) {
        Entity entity = getEntity(payload.id(), player);

        if (entity instanceof Player ridingPlayer && ridingPlayer.getVehicle() instanceof BabySpider spider) {
            spider.setIsJumping(true);
        }
    }

    private static void handleTeleportEye(TeleportEyePayload payload, Player player) {
        Entity entity = getEntity(payload.id(), player);

        if (entity instanceof VoidEye eye) {
            eye.setTeleports(payload.teleport());
        }
    }

    /** Resolves an entity by uuid using the level of the sending player. */
    private static Entity getEntity(java.util.UUID id, Player player) {
        if (player instanceof ServerPlayer serverPlayer && serverPlayer.level() instanceof ServerLevel serverLevel) {
            return serverLevel.getEntity(id);
        }

        return null;
    }

    /** @return the network version string of the mod (used for the payload registrar) */
    public static String version() {
        return VERSION;
    }
}
