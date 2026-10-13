// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Loot table ids of the mod, replacing the original {@code PrimitiveMobsLootTables}. In 1.21 loot
 * tables are data driven, so these constants now only point at the JSON files under
 * {@code data/primitive_menagerie/loot_table/}; no runtime registration is needed.
 *
 * @author Daveyx0
 */
public final class PMLootTables {

    // Entity drops
    public static final ResourceLocation ENTITIES_CHAMELEON = PMReference.id("entities/chameleon");
    public static final ResourceLocation ENTITIES_ROCKETCREEPER = PMReference.id("entities/rocket_creeper");
    public static final ResourceLocation ENTITIES_FESTIVECREEPER = PMReference.id("entities/festive_creeper");
    public static final ResourceLocation ENTITIES_SUPPORTCREEPER = PMReference.id("entities/support_creeper");
    public static final ResourceLocation ENTITIES_BLAZINGJUGGERNAUT = PMReference.id("entities/blazing_juggernaut");
    public static final ResourceLocation ENTITIES_LILYLURKER = PMReference.id("entities/lily_lurker");
    public static final ResourceLocation ENTITIES_DODO = PMReference.id("entities/dodo");
    public static final ResourceLocation ENTITIES_MIMIC = PMReference.id("entities/mimic");
    public static final ResourceLocation ENTITIES_FLAMESPEWER = PMReference.id("entities/flame_spewer");
    public static final ResourceLocation ENTITIES_VOIDEYE = PMReference.id("entities/void_eye");
    public static final ResourceLocation ENTITIES_HARPY = PMReference.id("entities/harpy");
    public static final ResourceLocation ENTITIES_GOBLIN = PMReference.id("entities/goblin");
    public static final ResourceLocation ENTITIES_MOTHERSPIDER = PMReference.id("entities/mother_spider");

    // Mimic chest loot
    public static final ResourceLocation MIMIC_TREASURE = PMReference.id("chests/mimic_treasure");
    public static final ResourceLocation MIMIC_TRAP = PMReference.id("chests/mimic_trap");

    // Markers used to recognise a generated (or orb-marked) mimic chest in 1.21, where the 1.12
    // "Mimic" tile-entity flag no longer exists. Both tables are empty; the chest is intercepted
    // before it opens.
    public static final ResourceLocation MIMIC_UNOPENED = PMReference.id("chests/mimic_unopened");
    public static final ResourceLocation MIMIC_GUARANTEED = PMReference.id("chests/mimic_guaranteed");

    // Special entity loot
    public static final ResourceLocation FILCHLIZARD_STEAL = PMReference.id("entities/special/filch_lizard_steal");
    public static final ResourceLocation FILCHLIZARD_SPAWN = PMReference.id("entities/special/filch_lizard_spawn");
    public static final ResourceLocation HAUNTEDTOOL_SPAWN = PMReference.id("entities/special/haunted_tool");
    public static final ResourceLocation TREASURESLIME_SPAWN = PMReference.id("entities/special/treasure_slime");

    private PMLootTables() {}

    /** Wraps a loot table id into the {@link ResourceKey} required by {@code LivingEntity#getLootTable()}. */
    public static ResourceKey<LootTable> key(ResourceLocation id) {
        return ResourceKey.create(Registries.LOOT_TABLE, id);
    }
}
