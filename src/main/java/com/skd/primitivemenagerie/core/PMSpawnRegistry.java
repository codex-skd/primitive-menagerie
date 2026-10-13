// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import com.skd.mobweavecore.api.MWSpawnAPI;
import com.skd.mobweavecore.spawn.MWConfigSpawnEntry;
import com.skd.primitivemenagerie.config.PMConfig;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

/**
 * Spawn entry registry of the mod, replacing the original {@code PrimitiveMobsSpawnRegistry}.
 *
 * <p>The 1.12 {@code BiomeDictionary} types map to the 1.21 biome tags accepted by
 * {@link MWConfigSpawnEntry}: {@code FOREST}/{@code JUNGLE}/{@code BEACH} use the vanilla
 * {@code minecraft:is_*} tags, while {@code SANDY}/{@code SWAMP} use the NeoForge common
 * {@code c:is_sandy}/{@code c:is_swamp} tags (vanilla has no matching tags). Structure names use
 * full resource locations ({@code minecraft:stronghold}, {@code minecraft:mineshaft}).</p>
 *
 * <p>Entries are registered on {@link ServerAboutToStartEvent} (before Mobweave Core builds its
 * spawn list on {@code ServerStartedEvent}) so the per-mob {@link PMConfig} enable flags can be
 * read without touching the config while registries are being filled.</p>
 *
 * @author Daveyx0
 */
public final class PMSpawnRegistry {

    private static boolean registered = false;

    private PMSpawnRegistry() {}

    /** Wires the registration listener on the game event bus. */
    public static void register() {
        NeoForge.EVENT_BUS.addListener(PMSpawnRegistry::onServerAboutToStart);
    }

    private static void onServerAboutToStart(ServerAboutToStartEvent event) {
        if (registered) {
            return;
        }

        registered = true;
        registerSpawns();
    }

    private static void registerSpawns() {
        // -----------------------------------------------------------------------------------------
        // Hostile mobs
        // -----------------------------------------------------------------------------------------
        if (PMConfig.getEnableBrainSlime()) {
            entry("_BrainSlime_Beach", "brain_slime", 80).setBiomeTypes(new String[] {"minecraft:is_beach"});
        }
        if (PMConfig.getEnableHauntedTool()) {
            entry("_HauntedTool", "haunted_tool", 70).setupBaseMobSpawnEntry(false);
        }
        if (PMConfig.getEnableRocketCreeper()) {
            entry("_RocketCreeper", "rocket_creeper", 50).setupBaseMobSpawnEntry(false).setHeightLevel(50, -1);
        }
        if (PMConfig.getEnableFestiveCreeper()) {
            entry("_FestiveCreeper", "festive_creeper", 10).setupBaseMobSpawnEntry(false);
            entry("_FestiveCreeper_Nether", "festive_creeper", 10).setDimensions(new int[] {-1});
        }
        if (PMConfig.getEnableSupportCreeper()) {
            entry("_SupportCreeper", "support_creeper", 30).setupBaseMobSpawnEntry(false);
        }
        if (PMConfig.getEnableTreasureSlime()) {
            entry("_TreasureSlime", "treasure_slime", 20).setupBaseMobSpawnEntry(false).setAdditionalRarity(2);
        }
        if (PMConfig.getEnableEnchantedBook()) {
            entry("_BewitchedTome", "bewitched_tome", 20).setupBaseMobSpawnEntry(false).setAdditionalRarity(2);
            entry("_BewitchedTome_Stronghold", "bewitched_tome", 100).setupBaseMobSpawnEntry(false)
                    .setHeightLevel(-1, 45).setStructures(new String[] {"minecraft:stronghold"});
        }
        if (PMConfig.getEnableSkeletonWarrior()) {
            entry("_SkeletonWarrior", "skeleton_warrior", 100).setupBaseMobSpawnEntry(false).setGroupSize(3, 3);
        }
        if (PMConfig.getEnableSpiderFamily()) {
            entry("_SpiderFamily", "mother_spider", 40).setupBaseMobSpawnEntry(false);
        }
        if (PMConfig.getEnableBlazingJuggernaut()) {
            entry("_BlazingJuggernaut", "blazing_juggernaut", 10).setDimensions(new int[] {-1});
        }
        if (PMConfig.getEnableTrollager()) {
            entry("_Trollager", "trollager", 5).setupBaseMobSpawnEntry(false).setNeedsMoreSpace(true);
            entry("_Trollager_Underground", "trollager", 25).setupBaseMobSpawnEntry(false)
                    .setHeightLevel(-1, 45).setNeedsMoreSpace(true);
        }
        if (PMConfig.getEnableVoidWatcher()) {
            entry("_VoidEye_Overworld", "void_eye", 10).setupBaseMobSpawnEntry(false)
                    .setDimensions(new int[] {0}).setHeightLevel(-1, 20);
        }
        if (PMConfig.getEnableHarpy()) {
            entry("_Harpy", "harpy", 100).setHeightLevel(100, -1).setGroupSize(1, 3).setOverrideCanSpawnHere(true)
                    .setSpawnBlocks(new String[] {"minecraft:grass_block", "minecraft:stone", "minecraft:dirt", "minecraft:snow"});
        }
        if (PMConfig.getEnableGoblin()) {
            entry("_Goblin", "goblin", 50).setupBaseMobSpawnEntry(false).setHeightLevel(-1, 30).setGroupSize(3, 3);
            entry("_Goblin_Mineshaft", "goblin", 100).setupBaseMobSpawnEntry(false)
                    .setStructures(new String[] {"minecraft:mineshaft"});
        }

        // -----------------------------------------------------------------------------------------
        // Passive mobs
        // -----------------------------------------------------------------------------------------
        if (PMConfig.getEnableChameleon()) {
            entry("_Chameleon_Forest", "chameleon", 100).setupBaseAnimalSpawnEntry(false)
                    .setBiomeTypes(new String[] {"minecraft:is_forest"}).setGroupSize(1, 3).setCreatureType("MULTIMOBPASSIVE");
            entry("_Chameleon_Jungle", "chameleon", 100).setupBaseAnimalSpawnEntry(false)
                    .setBiomeTypes(new String[] {"minecraft:is_jungle"}).setGroupSize(1, 3).setCreatureType("MULTIMOBPASSIVE");
        }
        if (PMConfig.getEnableLostMiner()) {
            entry("_LostMiner", "lost_miner", 4).setupBaseMobSpawnEntry(false).setHeightLevel(-1, 45)
                    .setAdditionalRarity(5).setCreatureType("MULTIMOBPASSIVE");
        }
        if (PMConfig.getEnableMerchant()) {
            entry("_TravelingMerchant", "traveling_merchant", 25).setupBaseAnimalSpawnEntry(false)
                    .setAdditionalRarity(30).setCreatureType("MULTIMOBPASSIVE");
        }
        if (PMConfig.getEnableDodo()) {
            entry("_Dodo", "dodo", 100).setupBaseAnimalSpawnEntry(false)
                    .setSpawnBlocks(new String[] {"minecraft:mycelium"}).setGroupSize(1, 2).setCreatureType("MULTIMOBPASSIVE");
        }
        if (PMConfig.getEnableFilchLizard()) {
            entry("_FilchLizard", "filch_lizard", 25).setBiomeTypes(new String[] {"c:is_sandy"})
                    .setCreatureType("MULTIMOBPASSIVE").setGroupSize(1, 2);
        }
        if (PMConfig.getEnableGroveSprite()) {
            entry("_GroveSprite", "grovesprite", 100).setupBaseAnimalSpawnEntry(false)
                    .setBiomeTypes(new String[] {"minecraft:is_forest"}).setCreatureType("MULTIMOBPASSIVE").setGroupSize(1, 2);
        }
        if (PMConfig.getEnableSheepman()) {
            entry("_Sheepman", "sheepman", 40).setDimensions(new int[] {-1})
                    .setCreatureType("MULTIMOBPASSIVE").setGroupSize(1, 2);
        }

        // -----------------------------------------------------------------------------------------
        // Lava mobs
        // -----------------------------------------------------------------------------------------
        if (PMConfig.getEnableFlameSpewer()) {
            entry("_FlameSpewer_Overworld", "flame_spewer", 100).setDimensions(new int[] {0})
                    .setNeedsMoreSpace(true).setSpawnType("LAVA").setCreatureType("MULTIMOBLAVA");
            entry("_FlameSpewer_Nether", "flame_spewer", 100).setDimensions(new int[] {-1})
                    .setNeedsMoreSpace(true).setSpawnType("LAVA").setCreatureType("MULTIMOBLAVA");
        }

        // -----------------------------------------------------------------------------------------
        // Water mobs
        // -----------------------------------------------------------------------------------------
        if (PMConfig.getEnableLilyLurker()) {
            entry("_LilyLurker", "lily_lurker", 100).setHeightLevel(50, -1).setSpawnType("WATER")
                    .setBiomeTypes(new String[] {"c:is_swamp"}).setCreatureType("MULTIMOBWATER");
        }
    }

    private static MWConfigSpawnEntry entry(String entryName, String entityName, int weight) {
        MWConfigSpawnEntry entry = new MWConfigSpawnEntry(entryName, PMReference.MODID + ":" + entityName, weight, true);
        MWSpawnAPI.registerSpawnEntry(entry);
        return entry;
    }
}
