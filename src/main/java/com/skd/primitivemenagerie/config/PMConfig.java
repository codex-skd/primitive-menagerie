// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.config;

import java.util.List;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Common configuration of the mod, merging the original {@code PrimitiveMobsConfig},
 * {@code PrimitiveMobsConfigMobs} and {@code PrimitiveMobsConfigSpecial} into a single
 * {@link ModConfigSpec}. The Forge configuration GUI classes of the original are not ported;
 * NeoForge generates the config screen from the spec.
 *
 * <p>Values must not be read while registries are being filled; the getters below are only meant
 * for use after the configuration has loaded.</p>
 *
 * @author Daveyx0
 */
public final class PMConfig {

    public static final String CATEGORY_MOBS = "Mob Activation Settings";
    public static final String CATEGORY_SPECIAL = "Mob Specific Settings";

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // ---------------------------------------------------------------------------------------------
    // Mob Activation Settings
    // ---------------------------------------------------------------------------------------------
    public static final ModConfigSpec.BooleanValue ENABLE_CHAMELEON;
    public static final ModConfigSpec.BooleanValue ENABLE_TREASURE_SLIME;
    public static final ModConfigSpec.BooleanValue ENABLE_HAUNTED_TOOL;
    public static final ModConfigSpec.BooleanValue ENABLE_GROVE_SPRITE;
    public static final ModConfigSpec.BooleanValue ENABLE_ENCHANTED_BOOK;
    public static final ModConfigSpec.BooleanValue ENABLE_FILCH_LIZARD;
    public static final ModConfigSpec.BooleanValue ENABLE_BRAIN_SLIME;
    public static final ModConfigSpec.BooleanValue ENABLE_ROCKET_CREEPER;
    public static final ModConfigSpec.BooleanValue ENABLE_FESTIVE_CREEPER;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPPORT_CREEPER;
    public static final ModConfigSpec.BooleanValue ENABLE_SKELETON_WARRIOR;
    public static final ModConfigSpec.BooleanValue ENABLE_BLAZING_JUGGERNAUT;
    public static final ModConfigSpec.BooleanValue ENABLE_LILY_LURKER;
    public static final ModConfigSpec.BooleanValue ENABLE_SPIDER_FAMILY;
    public static final ModConfigSpec.BooleanValue ENABLE_TROLLAGER;
    public static final ModConfigSpec.BooleanValue ENABLE_LOST_MINER;
    public static final ModConfigSpec.BooleanValue ENABLE_MERCHANT;
    public static final ModConfigSpec.BooleanValue ENABLE_DODO;
    public static final ModConfigSpec.BooleanValue ENABLE_MIMIC;
    public static final ModConfigSpec.BooleanValue ENABLE_SHEEPMAN;
    public static final ModConfigSpec.BooleanValue ENABLE_GOBLIN;
    public static final ModConfigSpec.BooleanValue ENABLE_HARPY;
    public static final ModConfigSpec.BooleanValue ENABLE_FLAME_SPEWER;
    public static final ModConfigSpec.BooleanValue ENABLE_VOID_WATCHER;

    // ---------------------------------------------------------------------------------------------
    // Mob Specific Settings
    // ---------------------------------------------------------------------------------------------
    public static final ModConfigSpec.BooleanValue MINER_IN_VILLAGE;
    public static final ModConfigSpec.BooleanValue FESTIVE_CREEPER_DESTRUCTION;
    public static final ModConfigSpec.IntValue MAX_SPIDER_FAMILY_SIZE;
    public static final ModConfigSpec.BooleanValue LOST_MINER_SOUNDS;
    public static final ModConfigSpec.BooleanValue TROLL_DESTRUCTION;
    public static final ModConfigSpec.IntValue TAMEABLE_SLIME_CHANCE;
    public static final ModConfigSpec.IntValue FILCH_LIZARD_LOOT_CHANCE;
    public static final ModConfigSpec.BooleanValue MERCHANT_CAN_SETTLE;
    public static final ModConfigSpec.BooleanValue MIMIC_GENERATES;
    public static final ModConfigSpec.BooleanValue DODO_MYCELIUM;
    public static final ModConfigSpec.BooleanValue TRAVELER_VISIT;
    public static final ModConfigSpec.BooleanValue ROCKET_CREEPER_ALWAYS_JUMP;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> LOST_MINER_LOOT_RANGE;
    public static final ModConfigSpec.BooleanValue HAUNTED_TOOL_FULL_DURABILITY;
    public static final ModConfigSpec.IntValue MIMIC_SPAWN_RATE;
    public static final ModConfigSpec.BooleanValue GROVE_SPRITES_PLANT;

    public static final ModConfigSpec SPEC;

    static {
        BUILDER.comment("Enable/Disable mobs").push(CATEGORY_MOBS);

        ENABLE_CHAMELEON = BUILDER.comment("Enable/Disable the Chameleon").define("Enable Chameleon", true);
        ENABLE_TREASURE_SLIME = BUILDER.comment("Enable/Disable the Treasure Slime").define("Enable Treasure Slime", true);
        ENABLE_HAUNTED_TOOL = BUILDER.comment("Enable/Disable the Haunted Tool").define("Enable Haunted Tool", true);
        ENABLE_GROVE_SPRITE = BUILDER.comment("Enable/Disable the Grove Sprite").define("Enable Grove Sprite", true);
        ENABLE_ENCHANTED_BOOK = BUILDER.comment("Enable/Disable the Bewitched Tome").define("Enable Bewitched Tome", true);
        ENABLE_FILCH_LIZARD = BUILDER.comment("Enable/Disable the Filch Lizard").define("Enable Filch Lizard", true);
        ENABLE_BRAIN_SLIME = BUILDER.comment("Enable/Disable the Brain Slime").define("Enable Brain Slime", true);
        ENABLE_ROCKET_CREEPER = BUILDER.comment("Enable/Disable the Rocket Creeper").define("Enable Rocket Creeper", true);
        ENABLE_FESTIVE_CREEPER = BUILDER.comment("Enable/Disable the Festive Creeper").define("Enable Festive Creeper", true);
        ENABLE_SUPPORT_CREEPER = BUILDER.comment("Enable/Disable the Support Creeper").define("Enable Support Creeper", true);
        ENABLE_SKELETON_WARRIOR = BUILDER.comment("Enable/Disable the Skeleton Warrior").define("Enable Skeleton Warrior", true);
        ENABLE_BLAZING_JUGGERNAUT = BUILDER.comment("Enable/Disable the Blazing Juggernaut").define("Enable Blazing Juggernaut", true);
        ENABLE_LILY_LURKER = BUILDER.comment("Enable/Disable the Lily lurker").define("Enable Lily Lurker", true);
        ENABLE_SPIDER_FAMILY = BUILDER.comment("Enable/Disable the Spider Family").define("Enable Spider Family", true);
        ENABLE_TROLLAGER = BUILDER.comment("Enable/Disable the Troll").define("Enable Troll", true);
        ENABLE_LOST_MINER = BUILDER.comment("Enable/Disable the Lost Miner").define("Enable Lost Miner", true);
        ENABLE_MERCHANT = BUILDER.comment("Enable/Disable the Traveling Merchant").define("Enable Traveling Merchant", true);
        ENABLE_DODO = BUILDER.comment("Enable/Disable the Dodo").define("Enable Dodo", true);
        ENABLE_MIMIC = BUILDER.comment("Enable/Disable the Mimic").define("Enable Mimic", true);
        ENABLE_SHEEPMAN = BUILDER.comment("Enable/Disable the Sheepman").define("Enable Sheepman", true);
        ENABLE_GOBLIN = BUILDER.comment("Enable/Disable the Goblin").define("Enable Goblin", true);
        ENABLE_HARPY = BUILDER.comment("Enable/Disable the Harpy").define("Enable Harpy", true);
        ENABLE_FLAME_SPEWER = BUILDER.comment("Enable/Disable the Flame Spewer").define("Enable Flame Spewer", true);
        ENABLE_VOID_WATCHER = BUILDER.comment("Enable/Disable the Void Eye").define("Enable Void Eye", true);

        BUILDER.pop();

        BUILDER.comment("Settings specific to certain mobs.").push(CATEGORY_SPECIAL);

        MINER_IN_VILLAGE = BUILDER
                .comment("Enable/Disable if the Miner Villager profession should also spawn naturally in villages")
                .define("The Miner profession spawns in villages", false);
        FESTIVE_CREEPER_DESTRUCTION = BUILDER
                .comment("Enable/Disable if the Festive Creeper throws tnt that harms the terrain (this way you do not have to disable ALL Creeper explosions with mobGriefing)")
                .define("Festive Creeper tnt harms terrain", true);
        MAX_SPIDER_FAMILY_SIZE = BUILDER
                .comment("Set the maximum amount of Baby Spiders that can potentially spawn with a Mother Spider")
                .defineInRange("Max Spider Family Size", 6, 0, Integer.MAX_VALUE);
        LOST_MINER_SOUNDS = BUILDER
                .comment("Enable/Disable if the Lost Miner should make villager sounds")
                .define("Lost Miner makes villager sounds", true);
        TROLL_DESTRUCTION = BUILDER
                .comment("Enable/Disable if the Trollager can destroy terrain with its attack")
                .define("Trollager can destroy terrain", true);
        TAMEABLE_SLIME_CHANCE = BUILDER
                .comment("Set the chance that a tameable Treasure Slime can spawn in percentage")
                .defineInRange("Chance that a tameable Treasure Slime spawns", 5, 0, 100);
        FILCH_LIZARD_LOOT_CHANCE = BUILDER
                .comment("Set the chance that a Filch Lizard spawns holding loot in percentage")
                .defineInRange("Chance that a Filch Lizard spawns holding loot", 25, 0, 100);
        MERCHANT_CAN_SETTLE = BUILDER
                .comment("Enable/Disable if the Traveling Merchant can settle after being bribed with an Emerald Block")
                .define("Traveling Merchant can settle", true);
        MIMIC_GENERATES = BUILDER
                .comment("Enable/Disable if the Mimic chests should generate in caves, instead of spawn like mobs")
                .define("Mimic chests generate in caves", true);
        DODO_MYCELIUM = BUILDER
                .comment("Enable/Disable if Dodos should convert Grass into Mycelium blocks overtime")
                .define("Dodos convert Grass into Mycelium", true);
        TRAVELER_VISIT = BUILDER
                .comment("Enable/Disable if the Traveling Merchant should enter houses")
                .define("Traveling Merchant enters houses", true);
        ROCKET_CREEPER_ALWAYS_JUMP = BUILDER
                .comment("Enable/Disable if rocket creepers should always jump at the player, even if they would hit the ceiling")
                .define("Rocket Creepers always jump, even without adequate room", false);
        LOST_MINER_LOOT_RANGE = BUILDER
                .comment("Change the amount of emeralds you receive when saving a Lost Miner. First number = minimum. Second number = variable addition")
                .defineList("Lost Miner emerald loot range", List.of(2, 2), o -> o instanceof Integer);
        HAUNTED_TOOL_FULL_DURABILITY = BUILDER
                .comment("Enable/Disable if Haunted Tools should drop full durability items.")
                .define("Haunted Tool drop full durability", false);
        MIMIC_SPAWN_RATE = BUILDER
                .comment("How often a Mimic chest generates. Higher number = rarer. 0 = no mimics chests.")
                .defineInRange("Spawn Rate of Mimics", 25, 0, Integer.MAX_VALUE);
        GROVE_SPRITES_PLANT = BUILDER
                .comment("Enable/Disable if Grove Sprites should plant sapling they hold.")
                .define("Grove Sprites plant saplings", true);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    private PMConfig() {}

    // ---------------------------------------------------------------------------------------------
    // Mob Activation getters
    // ---------------------------------------------------------------------------------------------
    public static boolean getEnableChameleon() { return ENABLE_CHAMELEON.get(); }
    public static boolean getEnableTreasureSlime() { return ENABLE_TREASURE_SLIME.get(); }
    public static boolean getEnableHauntedTool() { return ENABLE_HAUNTED_TOOL.get(); }
    public static boolean getEnableGroveSprite() { return ENABLE_GROVE_SPRITE.get(); }
    public static boolean getEnableEnchantedBook() { return ENABLE_ENCHANTED_BOOK.get(); }
    public static boolean getEnableFilchLizard() { return ENABLE_FILCH_LIZARD.get(); }
    public static boolean getEnableBrainSlime() { return ENABLE_BRAIN_SLIME.get(); }
    public static boolean getEnableRocketCreeper() { return ENABLE_ROCKET_CREEPER.get(); }
    public static boolean getEnableFestiveCreeper() { return ENABLE_FESTIVE_CREEPER.get(); }
    public static boolean getEnableSupportCreeper() { return ENABLE_SUPPORT_CREEPER.get(); }
    public static boolean getEnableSkeletonWarrior() { return ENABLE_SKELETON_WARRIOR.get(); }
    public static boolean getEnableBlazingJuggernaut() { return ENABLE_BLAZING_JUGGERNAUT.get(); }
    public static boolean getEnableLilyLurker() { return ENABLE_LILY_LURKER.get(); }
    public static boolean getEnableSpiderFamily() { return ENABLE_SPIDER_FAMILY.get(); }
    public static boolean getEnableTrollager() { return ENABLE_TROLLAGER.get(); }
    public static boolean getEnableLostMiner() { return ENABLE_LOST_MINER.get(); }
    public static boolean getEnableMerchant() { return ENABLE_MERCHANT.get(); }
    public static boolean getEnableDodo() { return ENABLE_DODO.get(); }
    public static boolean getEnableMimic() { return ENABLE_MIMIC.get(); }
    public static boolean getEnableSheepman() { return ENABLE_SHEEPMAN.get(); }
    public static boolean getEnableGoblin() { return ENABLE_GOBLIN.get(); }
    public static boolean getEnableHarpy() { return ENABLE_HARPY.get(); }
    public static boolean getEnableFlameSpewer() { return ENABLE_FLAME_SPEWER.get(); }
    public static boolean getEnableVoidWatcher() { return ENABLE_VOID_WATCHER.get(); }

    // ---------------------------------------------------------------------------------------------
    // Mob Specific getters (names kept from the original PrimitiveMobsConfigSpecial)
    // ---------------------------------------------------------------------------------------------
    public static boolean getMinerInVillage() { return MINER_IN_VILLAGE.get(); }
    public static boolean getFestiveCreeperDestruction() { return FESTIVE_CREEPER_DESTRUCTION.get(); }
    public static int getMaxSpiderFamilySize() { return MAX_SPIDER_FAMILY_SIZE.get(); }
    public static boolean getLostMinerSounds() { return LOST_MINER_SOUNDS.get(); }
    public static boolean getTrollDestruction() { return TROLL_DESTRUCTION.get(); }
    public static int getTameableSlimeChance() { return TAMEABLE_SLIME_CHANCE.get(); }
    public static int getFilchLizardLootChance() { return FILCH_LIZARD_LOOT_CHANCE.get(); }
    public static boolean getMerchantCanSettle() { return MERCHANT_CAN_SETTLE.get(); }
    public static boolean getMimicGeneratesInCaves() { return MIMIC_GENERATES.get(); }
    public static boolean getDodoMycelium() { return DODO_MYCELIUM.get(); }
    public static boolean getTravelerVisit() { return TRAVELER_VISIT.get(); }
    public static boolean getRocketCreeperAlwaysJump() { return ROCKET_CREEPER_ALWAYS_JUMP.get(); }
    public static int getMimicRarity() { return MIMIC_SPAWN_RATE.get(); }
    public static boolean getHauntedToolDurability() { return HAUNTED_TOOL_FULL_DURABILITY.get(); }
    public static boolean getGroveSpritesPlant() { return GROVE_SPRITES_PLANT.get(); }

    public static int[] getLostMinerLootRange() {
        List<? extends Integer> values = LOST_MINER_LOOT_RANGE.get();
        int[] result = new int[values.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values.get(i);
        }
        return result;
    }
}
