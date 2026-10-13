// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.item;

import net.minecraft.world.item.Item;

/**
 * Base class of the simple items of the mod, ported from {@code ItemPrimitive}. The 1.12
 * {@code setRegistryName}/{@code setUnlocalizedName} calls are replaced by the deferred registry in
 * {@link com.skd.primitivemenagerie.core.PMItems}.
 *
 * @author Daveyx0
 */
public class ItemPrimitive extends Item {

    public ItemPrimitive(Item.Properties properties) {
        super(properties);
    }
}
