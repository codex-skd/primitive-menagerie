// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.item;

import net.minecraft.world.item.Item;

/**
 * Food item of the mod, ported from {@code ItemPrimitiveFood}. In 1.21 the food values are an
 * immutable {@code FoodProperties} carried by the item's {@link Item.Properties} instead of the
 * 1.12 constructor arguments.
 *
 * @author Daveyx0
 */
public class ItemPrimitiveFood extends Item {

    public ItemPrimitiveFood(Item.Properties properties) {
        super(properties);
    }
}
