// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * Item registry of the mod, replacing the original {@code PrimitiveMobsItems}.
 *
 * <p>Phase P1 only needs the item ids that the ported loot tables reference, so the loot data
 * parses cleanly. The real item implementations (camouflage dye/armour, dodo meat and eggs,
 * mystery eggs, mimic orb, goblin mace, wonder sap, spider eggshell) are ported in a later phase
 * and will replace or extend these plain placeholders.</p>
 *
 * TODO(P6): replace the placeholder items below with the ported item classes.
 *
 * @author Daveyx0
 */
public final class PMItems {

    /** {@code camouflage_dye} — dropped by the Chameleon. */
    public static final DeferredItem<Item> CAMOUFLAGE_DYE = register("camouflage_dye");

    /** {@code dodo} — raw meat, dropped by the Dodo. */
    public static final DeferredItem<Item> DODO = register("dodo");

    /** {@code spider_egg} — dropped by the Mother Spider. */
    public static final DeferredItem<Item> SPIDER_EGG = register("spider_egg");

    /** {@code mysteryegg1} — rare drop of the Festive Creeper. */
    public static final DeferredItem<Item> MYSTERY_EGG_1 = register("mysteryegg1");

    /** {@code mysteryegg2} — rare drop of the Support Creeper. */
    public static final DeferredItem<Item> MYSTERY_EGG_2 = register("mysteryegg2");

    /** {@code mysteryegg3} — rare drop of the Rocket Creeper. */
    public static final DeferredItem<Item> MYSTERY_EGG_3 = register("mysteryegg3");

    /** {@code mimic_orb} — dropped by the Mimic and found in mimic chests. */
    public static final DeferredItem<Item> MIMIC_ORB = register("mimic_orb");

    private PMItems() {}

    /** Forces class initialisation so the deferred entries above are created. */
    public static void register() {}

    private static DeferredItem<Item> register(String name) {
        return PMRegistries.ITEMS.registerSimpleItem(name);
    }
}
