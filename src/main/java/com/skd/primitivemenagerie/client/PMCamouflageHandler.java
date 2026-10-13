// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client;

import com.skd.mobweavecore.util.ColorUtil;
import com.skd.primitivemenagerie.item.ItemCamouflageArmor;
import com.skd.primitivemenagerie.network.PrimitiveColorPayload;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Client-only handler for the camouflage armor, replacing the 1.12 {@code onArmorTick} and
 * {@code LivingUpdateEvent} color sampling. Every five ticks it samples the block the entity stands
 * on, tints the armor pieces and synchronises the color with the server.
 *
 * <p>Simplification: the original smoothly interpolated the color between samples; the port applies
 * the sampled color directly.</p>
 *
 * @author Daveyx0
 */
public final class PMCamouflageHandler {

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private PMCamouflageHandler() {}

    /** Registers the handler on the game event bus (client only). */
    public static void register() {
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(PMCamouflageHandler::onEntityTick);
    }

    private static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity) || !entity.level().isClientSide) {
            return;
        }

        if (entity.tickCount % 5 != 0) {
            return;
        }

        if (entity instanceof net.minecraft.world.entity.player.Player player
                && com.skd.primitivemenagerie.core.PMEvents.hasFullCamouflageArmor(player)
                && entity.getRandom().nextInt(50) == 0) {
            entity.level().addParticle(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                    entity.getX() + (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()),
                    entity.getY() + entity.getRandom().nextFloat() + 1.0D,
                    entity.getZ() + (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()),
                    0.0D, 0.0D, 0.0D);
        }

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = entity.getItemBySlot(slot);

            if (stack.getItem() instanceof ItemCamouflageArmor && !ItemCamouflageArmor.getCannotChange(stack)) {
                int color = ColorUtil.getBlockColor(entity);

                if (color >= 0 && color != ItemCamouflageArmor.getColor(stack)) {
                    ItemCamouflageArmor.setColor(stack, color);
                    ItemCamouflageArmor.setColorBlockState(stack, ColorUtil.getBlockState(entity));
                    PacketDistributor.sendToServer(
                            new PrimitiveColorPayload(color, slot.ordinal(), entity.getUUID()));
                }
            }
        }
    }
}
