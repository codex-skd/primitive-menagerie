// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import java.util.List;
import java.util.Map;

import com.skd.primitivemenagerie.entity.passive.Dodo;
import com.skd.primitivemenagerie.item.ItemCamouflageArmor;
import com.skd.primitivemenagerie.item.ItemCamouflageDye;
import com.skd.primitivemenagerie.item.ItemGoblinMace;
import com.skd.primitivemenagerie.item.ItemGroveSpriteSap;
import com.skd.primitivemenagerie.item.ItemMimicOrb;
import com.skd.primitivemenagerie.item.ItemPrimitiveEgg;
import com.skd.primitivemenagerie.item.ItemPrimitiveFood;
import com.skd.primitivemenagerie.item.ItemSpiderEggshell;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Item registry of the mod, replacing the original {@code PrimitiveMobsItems}. Every item keeps the
 * registry id of the original so the ported loot tables and recipes keep working.
 *
 * @author Daveyx0
 */
public final class PMItems {

    /** Registry of the armor materials of the mod. */
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, PMReference.MODID);

    /**
     * Camouflage armor material. The original used {@code ArmorMaterial.LEATHER}; the port keeps the
     * leather stats, sound and layer texture (the item is dyed through its color component).
     */
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CAMOUFLAGE_MATERIAL =
            ARMOR_MATERIALS.register("camouflage", () -> new ArmorMaterial(
                    Map.of(
                            ArmorItem.Type.BOOTS, 1,
                            ArmorItem.Type.LEGGINGS, 2,
                            ArmorItem.Type.CHESTPLATE, 3,
                            ArmorItem.Type.HELMET, 1,
                            ArmorItem.Type.BODY, 5),
                    15,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(Items.LEATHER),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("leather"), "", true)),
                    0.0F,
                    0.0F));

    /** {@code camouflage_dye} — dropped by the Chameleon. */
    public static final DeferredItem<Item> CAMOUFLAGE_DYE = PMRegistries.ITEMS.register(
            "camouflage_dye", () -> new ItemCamouflageDye(new Item.Properties()));

    /** {@code camouflage_helmet}. */
    public static final DeferredItem<Item> CAMOUFLAGE_HELMET = PMRegistries.ITEMS.register(
            "camouflage_helmet", () -> new ItemCamouflageArmor(CAMOUFLAGE_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties()));

    /** {@code camouflage_chestplate}. */
    public static final DeferredItem<Item> CAMOUFLAGE_CHEST = PMRegistries.ITEMS.register(
            "camouflage_chestplate", () -> new ItemCamouflageArmor(CAMOUFLAGE_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    /** {@code camouflage_boots}. */
    public static final DeferredItem<Item> CAMOUFLAGE_BOOTS = PMRegistries.ITEMS.register(
            "camouflage_boots", () -> new ItemCamouflageArmor(CAMOUFLAGE_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties()));

    /** {@code camouflage_leggings}. */
    public static final DeferredItem<Item> CAMOUFLAGE_LEGS = PMRegistries.ITEMS.register(
            "camouflage_leggings", () -> new ItemCamouflageArmor(CAMOUFLAGE_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    /** {@code dodo} — raw meat, dropped by the Dodo. */
    public static final DeferredItem<Item> RAW_DODO = PMRegistries.ITEMS.register(
            "dodo", () -> new ItemPrimitiveFood(new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationModifier(1.2F)
                            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)
                            .build())));

    /** {@code cooked_dodo}. */
    public static final DeferredItem<Item> COOKED_DODO = PMRegistries.ITEMS.register(
            "cooked_dodo", () -> new ItemPrimitiveFood(new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationModifier(2.4F)
                            .build())));

    /** {@code dodo_egg} — thrown to spawn a Dodo. */
    public static final DeferredItem<Item> DODO_EGG = PMRegistries.ITEMS.register(
            "dodo_egg", () -> new ItemPrimitiveEgg(new Item.Properties().stacksTo(16), () -> PMEntities.DODO.get(), 8, false));

    /** {@code spider_egg} — thrown to spawn a Baby Spider. */
    public static final DeferredItem<Item> SPIDER_EGG = PMRegistries.ITEMS.register(
            "spider_egg", () -> new ItemPrimitiveEgg(new Item.Properties().stacksTo(16), null, 1, true));

    /** {@code mysteryegg1} — spawns a Festive Creeper, glinting. */
    public static final DeferredItem<Item> MYSTERYEGG1 = PMRegistries.ITEMS.register(
            "mysteryegg1", () -> new ItemPrimitiveEgg(new Item.Properties().stacksTo(16), () -> PMEntities.FESTIVE_CREEPER.get(), 1, false, true));

    /** {@code mysteryegg2} — spawns a Support Creeper, glinting. */
    public static final DeferredItem<Item> MYSTERYEGG2 = PMRegistries.ITEMS.register(
            "mysteryegg2", () -> new ItemPrimitiveEgg(new Item.Properties().stacksTo(16), () -> PMEntities.SUPPORT_CREEPER.get(), 1, false, true));

    /** {@code mysteryegg3} — spawns a Rocket Creeper, glinting. */
    public static final DeferredItem<Item> MYSTERYEGG3 = PMRegistries.ITEMS.register(
            "mysteryegg3", () -> new ItemPrimitiveEgg(new Item.Properties().stacksTo(16), () -> PMEntities.ROCKET_CREEPER.get(), 1, false, true));

    /** {@code mimic_orb} — turns an empty chest into a Mimic. */
    public static final DeferredItem<Item> MIMIC_ORB = PMRegistries.ITEMS.register(
            "mimic_orb", () -> new ItemMimicOrb(new Item.Properties()));

    /** {@code goblin_mace}. */
    public static final DeferredItem<Item> GOBLIN_MACE = PMRegistries.ITEMS.register(
            "goblin_mace", () -> new ItemGoblinMace(new Item.Properties()));

    /** {@code wonder_sap} — held by the Grove Sprite. */
    public static final DeferredItem<Item> WONDER_SAP = PMRegistries.ITEMS.register(
            "wonder_sap", () -> new ItemGroveSpriteSap(new Item.Properties()));

    /** {@code spider_eggshell}. */
    public static final DeferredItem<Item> SPIDER_EGGSHELL = PMRegistries.ITEMS.register(
            "spider_eggshell", () -> new ItemSpiderEggshell(new Item.Properties()));

    private PMItems() {}

    /** Forces class initialisation so the deferred entries above are created. */
    public static void register() {}
}
