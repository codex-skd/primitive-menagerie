// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import java.util.List;

import com.google.common.collect.ImmutableSet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Villager professions of the mod, ported from {@code PrimitiveMobsVillagerProfessions}.
 *
 * <p>The original Forge 1.12 professions had no point of interest: the Lost Miner, Sheepman and
 * Traveling Merchant simply set their own profession on spawn. The 1.21 {@link VillagerProfession}
 * therefore uses {@link PoiType#NONE} for both job-site predicates, so vanilla villagers never claim
 * these professions; the ported entities assign them directly. The trade lists are added through the
 * NeoForge {@link VillagerTradesEvent}.</p>
 *
 * <p>Simplification: the original trade prices were ranges ({@code PriceInfo}) and the Traveling
 * Merchant randomised its stock; here a representative fixed price per listing is used and the
 * merchant's offers are driven by the standard villager trade map ({@code TODO(verify)}).</p>
 *
 * @author Daveyx0
 */
public final class PMVillagerProfessions {

    public static final DeferredRegister<VillagerProfession> PROFESSIONS =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, PMReference.MODID);

    public static final DeferredHolder<VillagerProfession, VillagerProfession> MINER =
            register("miner");
    public static final DeferredHolder<VillagerProfession, VillagerProfession> MERCHANT =
            register("merchant");
    public static final DeferredHolder<VillagerProfession, VillagerProfession> FAKE_MERCHANT =
            register("fakemerchant");
    public static final DeferredHolder<VillagerProfession, VillagerProfession> SHEEPMAN_SCAVENGER =
            register("sheepman_scavenger");
    public static final DeferredHolder<VillagerProfession, VillagerProfession> SHEEPMAN_ALCHEMIST =
            register("sheepman_alchemist");
    public static final DeferredHolder<VillagerProfession, VillagerProfession> SHEEPMAN_THIEF =
            register("sheepman_thief");

    private static final int MAX_USES = 12;
    private static final int XP = 5;

    private PMVillagerProfessions() {}

    private static DeferredHolder<VillagerProfession, VillagerProfession> register(String name) {
        return PROFESSIONS.register(name, () -> new VillagerProfession(name, PoiType.NONE, PoiType.NONE,
                ImmutableSet.of(), ImmutableSet.of(), null));
    }

    /** Registers the deferred registry and the trade-listener on their respective buses. */
    public static void register(IEventBus modEventBus) {
        PROFESSIONS.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(PMVillagerProfessions::onVillagerTrades);
    }

    private static void onVillagerTrades(VillagerTradesEvent event) {
        VillagerProfession profession = event.getType();

        if (profession == MINER.get()) {
            addMinerTrades(event.getTrades());
        } else if (profession == MERCHANT.get()) {
            addMerchantTrades(event.getTrades());
        } else if (profession == FAKE_MERCHANT.get()) {
            addMerchantTrades(event.getTrades());
        } else if (profession == SHEEPMAN_SCAVENGER.get()) {
            addScavengerTrades(event.getTrades());
        } else if (profession == SHEEPMAN_ALCHEMIST.get()) {
            addAlchemistTrades(event.getTrades());
        } else if (profession == SHEEPMAN_THIEF.get()) {
            addThiefTrades(event.getTrades());
        }
    }

    private static void offer(java.util.Map<Integer, List<ItemListing>> trades, int level, ItemStack cost, ItemStack result) {
        trades.get(level).add(new BasicItemListing(cost, result, MAX_USES, XP, 0.05F));
    }

    private static ItemStack emerald(int count) {
        return new ItemStack(Items.EMERALD, count);
    }

    private static ItemStack gold(int count) {
        return new ItemStack(Items.GOLD_INGOT, count);
    }

    // -----------------------------------------------------------------------------------------
    // Miner
    // -----------------------------------------------------------------------------------------
    private static void addMinerTrades(java.util.Map<Integer, List<ItemListing>> t) {
        offer(t, 1, new ItemStack(Items.COAL, 14), emerald(1));
        offer(t, 2, new ItemStack(Items.IRON_ORE, 6), emerald(1));
        offer(t, 2, new ItemStack(Items.GOLD_ORE, 7), emerald(1));
        offer(t, 3, new ItemStack(Items.OBSIDIAN, 1), emerald(1));

        offer(t, 1, new ItemStack(Items.COBBLESTONE, 45), emerald(1));
        offer(t, 2, new ItemStack(Items.STONE, 10), emerald(1));
        offer(t, 2, new ItemStack(Items.GRANITE, 10), emerald(1));
        offer(t, 2, new ItemStack(Items.ANDESITE, 10), emerald(1));
        offer(t, 3, new ItemStack(Items.STONE, 15), emerald(1));
        offer(t, 4, new ItemStack(Items.OBSIDIAN, 1), emerald(1));

        offer(t, 1, new ItemStack(Items.LAPIS_LAZULI, 3), emerald(1));
        offer(t, 2, new ItemStack(Items.REDSTONE, 3), emerald(1));
        offer(t, 2, new ItemStack(Items.QUARTZ, 3), emerald(1));
        offer(t, 3, emerald(2), new ItemStack(Items.DIAMOND, 1));
    }

    // -----------------------------------------------------------------------------------------
    // Traveling / fake merchant
    // -----------------------------------------------------------------------------------------
    private static void addMerchantTrades(java.util.Map<Integer, List<ItemListing>> t) {
        // Level 1: sell food for emeralds.
        offer(t, 1, emerald(1), new ItemStack(Items.CARROT, 10));
        offer(t, 1, emerald(1), new ItemStack(Items.BREAD, 4));
        offer(t, 1, emerald(1), new ItemStack(Items.APPLE, 4));
        offer(t, 1, emerald(1), new ItemStack(Items.COOKED_BEEF, 4));
        offer(t, 1, emerald(1), new ItemStack(Items.COOKED_CHICKEN, 5));
        offer(t, 1, emerald(1), new ItemStack(Items.COOKED_COD, 3));
        offer(t, 1, emerald(1), new ItemStack(Items.COOKED_PORKCHOP, 4));
        offer(t, 1, emerald(1), new ItemStack(Items.COOKED_RABBIT, 3));

        // Level 2: buy items for emeralds.
        offer(t, 2, new ItemStack(Items.PUMPKIN_SEEDS, 8), emerald(1));
        offer(t, 2, new ItemStack(Items.MELON_SEEDS, 8), emerald(1));
        offer(t, 2, new ItemStack(Items.GOLD_INGOT, 8), emerald(1));
        offer(t, 2, new ItemStack(Items.ARROW, 10), emerald(1));
        offer(t, 2, new ItemStack(Items.SLIME_BALL, 4), emerald(1));
        offer(t, 2, new ItemStack(Items.LIGHT_BLUE_DYE, 6), emerald(1));
        offer(t, 2, new ItemStack(Items.BEETROOT, 10), emerald(1));
        offer(t, 2, new ItemStack(Items.PAPER, 26), emerald(1));
        offer(t, 2, new ItemStack(Items.FLINT, 8), emerald(1));
        offer(t, 2, new ItemStack(Items.SNOWBALL, 14), emerald(1));

        // Level 3.
        offer(t, 3, new ItemStack(Items.BONE, 8), emerald(1));
        offer(t, 3, new ItemStack(Items.FIRE_CHARGE, 8), emerald(1));
        offer(t, 3, new ItemStack(Items.IRON_INGOT, 7), emerald(1));
        offer(t, 3, new ItemStack(Items.ARROW, 10), emerald(1));
        offer(t, 3, new ItemStack(Items.STRING, 15), emerald(1));
        offer(t, 3, new ItemStack(Items.CLAY_BALL, 6), emerald(1));
        offer(t, 3, new ItemStack(Items.RABBIT_FOOT, 2), emerald(1));
        offer(t, 3, new ItemStack(Items.REDSTONE, 3), emerald(1));
        offer(t, 3, new ItemStack(Items.BLAZE_POWDER, 4), emerald(1));
        offer(t, 3, new ItemStack(Items.BOOK, 4), emerald(1));

        // Level 4.
        offer(t, 4, new ItemStack(Items.ENDER_PEARL, 1), emerald(1));
        offer(t, 4, emerald(1), new ItemStack(Items.EXPERIENCE_BOTTLE, 1));
        offer(t, 4, emerald(2), new ItemStack(Items.DIAMOND, 1));
        offer(t, 4, emerald(2), new ItemStack(Items.GHAST_TEAR, 1));
        offer(t, 4, new ItemStack(Items.PRISMARINE_CRYSTALS, 4), emerald(1));
        offer(t, 4, new ItemStack(Items.NAME_TAG, 5), emerald(1));
        offer(t, 4, new ItemStack(Items.TIPPED_ARROW, 10), emerald(1));
        offer(t, 4, emerald(4), new ItemStack(Items.SADDLE, 1));
        offer(t, 4, new ItemStack(Items.CHORUS_FRUIT, 2), emerald(1));
        offer(t, 4, emerald(1), new ItemStack(Items.GOLDEN_APPLE, 1));

        // Poisoned tipped arrow, replacing the original "poison" potion arrow.
        t.get(4).add(new BasicItemListing(new ItemStack(Items.TIPPED_ARROW, 10),
                PotionContents.createItemStack(Items.TIPPED_ARROW, Potions.POISON), MAX_USES, XP, 0.05F));
    }

    // -----------------------------------------------------------------------------------------
    // Sheepman variants: trade items against gold ingots.
    // -----------------------------------------------------------------------------------------
    private static void addScavengerTrades(java.util.Map<Integer, List<ItemListing>> t) {
        offer(t, 1, gold(1), new ItemStack(Items.NETHER_BRICK, 4));
        offer(t, 2, gold(1), new ItemStack(Items.FLINT, 2));
        offer(t, 2, gold(1), new ItemStack(Items.STICK, 8));
        offer(t, 3, gold(1), new ItemStack(Items.FLINT_AND_STEEL, 1));
        offer(t, 3, gold(1), new ItemStack(Items.QUARTZ, 3));
        offer(t, 4, gold(12), new ItemStack(Items.OBSIDIAN, 1));
    }

    private static void addAlchemistTrades(java.util.Map<Integer, List<ItemListing>> t) {
        offer(t, 1, gold(2), new ItemStack(Items.NETHER_WART, 1));
        offer(t, 1, gold(1), new ItemStack(Items.GLASS_BOTTLE, 1));
        offer(t, 2, gold(4), PotionContents.createItemStack(Items.POTION, Potions.AWKWARD));
        offer(t, 3, gold(12), PotionContents.createItemStack(Items.POTION, Potions.REGENERATION));
        offer(t, 3, gold(10), PotionContents.createItemStack(Items.POTION, Potions.FIRE_RESISTANCE));
        offer(t, 3, gold(15), PotionContents.createItemStack(Items.POTION, Potions.HEALING));
        offer(t, 4, gold(32), PotionContents.createItemStack(Items.POTION, Potions.LONG_FIRE_RESISTANCE));
    }

    private static void addThiefTrades(java.util.Map<Integer, List<ItemListing>> t) {
        offer(t, 1, gold(1), new ItemStack(Items.GLOWSTONE_DUST, 8));
        offer(t, 2, gold(2), new ItemStack(Items.BLAZE_POWDER, 1));
        offer(t, 3, gold(5), new ItemStack(Items.MAGMA_CREAM, 1));
        offer(t, 3, gold(5), new ItemStack(Items.BLAZE_ROD, 1));
        offer(t, 4, gold(35), new ItemStack(Items.GHAST_TEAR, 1));
    }
}
