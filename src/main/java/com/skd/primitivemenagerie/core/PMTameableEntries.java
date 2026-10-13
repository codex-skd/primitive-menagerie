// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import com.skd.mobweavecore.core.MMTameableEntries;
import com.skd.primitivemenagerie.entity.monster.BabySpider;
import com.skd.primitivemenagerie.entity.monster.FestiveCreeper;
import com.skd.primitivemenagerie.entity.monster.RocketCreeper;
import com.skd.primitivemenagerie.entity.monster.SupportCreeper;
import com.skd.primitivemenagerie.entity.passive.Chameleon;

import net.minecraft.world.item.Items;

/**
 * Tameable entry registry of the mod, replacing the original {@code PrimitiveTameableEntries}. The
 * entries are fed into the Mobweave Core {@link MMTameableEntries} map (keyed by entity class), which
 * drives the taming and healing interaction.
 *
 * @author Daveyx0
 */
public final class PMTameableEntries {

    private PMTameableEntries() {}

    /** Registers every tameable creature of the mod. */
    public static void registerTameables() {
        MMTameableEntries.addTameable(Chameleon.class,
                new net.minecraft.world.item.Item[] {Items.MELON_SLICE},
                new net.minecraft.world.item.Item[] {Items.SPIDER_EYE, Items.FERMENTED_SPIDER_EYE},
                20.0F, 100, true);
        MMTameableEntries.addTameable(BabySpider.class,
                new net.minecraft.world.item.Item[] {Items.MELON_SLICE},
                new net.minecraft.world.item.Item[] {Items.ROTTEN_FLESH},
                20.0F, 100, false);
        MMTameableEntries.addTameable(FestiveCreeper.class,
                new net.minecraft.world.item.Item[] {Items.MELON_SLICE},
                new net.minecraft.world.item.Item[] {Items.GUNPOWDER},
                20.0F, 100, false);
        MMTameableEntries.addTameable(SupportCreeper.class,
                new net.minecraft.world.item.Item[] {Items.MELON_SLICE},
                new net.minecraft.world.item.Item[] {Items.GUNPOWDER},
                20.0F, 100, false);
        MMTameableEntries.addTameable(RocketCreeper.class,
                new net.minecraft.world.item.Item[] {Items.MELON_SLICE},
                new net.minecraft.world.item.Item[] {Items.GUNPOWDER},
                20.0F, 100, false);
    }
}
