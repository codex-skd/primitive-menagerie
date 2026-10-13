// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client;

import com.skd.primitivemenagerie.core.PMItems;
import com.skd.primitivemenagerie.item.ItemCamouflageArmor;
import com.skd.primitivemenagerie.item.ItemGroveSpriteSap;

import net.minecraft.util.FastColor;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

/**
 * Client-only item color handlers. The camouflage armor and the wonder sap are tinted from their
 * data components, replacing the 1.12 built-in {@code ItemColor} behaviour.
 *
 * @author Daveyx0
 */
public final class PMItemColors {

    private PMItemColors() {}

    /** Registers the item color handlers on the mod event bus (client only). */
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> tintIndex > 0 ? -1 : FastColor.ARGB32.opaque(ItemCamouflageArmor.getColor(stack)),
                PMItems.CAMOUFLAGE_HELMET.get(),
                PMItems.CAMOUFLAGE_CHEST.get(),
                PMItems.CAMOUFLAGE_LEGS.get(),
                PMItems.CAMOUFLAGE_BOOTS.get());

        event.register(
                (stack, tintIndex) -> FastColor.ARGB32.opaque(ItemGroveSpriteSap.getColor(stack)),
                PMItems.WONDER_SAP.get());
    }
}
