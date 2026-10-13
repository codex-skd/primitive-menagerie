// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import com.skd.primitivemenagerie.entity.item.SpiderEgg;
import com.skd.primitivemenagerie.entity.monster.BabySpider;
import com.skd.primitivemenagerie.entity.monster.FestiveCreeper;
import com.skd.primitivemenagerie.entity.monster.Goblin;
import com.skd.primitivemenagerie.entity.monster.Harpy;
import com.skd.primitivemenagerie.entity.monster.MotherSpider;
import com.skd.primitivemenagerie.entity.monster.RocketCreeper;
import com.skd.primitivemenagerie.entity.monster.SkeletonWarrior;
import com.skd.primitivemenagerie.entity.monster.SupportCreeper;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Entity type registry of the mod. Names and spawn-egg colours are taken from the original
 * {@code PrimitiveMobsEntityRegistry}; attributes are supplied through the helper in
 * {@link PMRegistries} and applied on {@code EntityAttributeCreationEvent}.
 *
 * <p>Only the phase P3 creatures are registered here; later phases add the rest. A disabled mob is
 * kept registered so existing worlds do not break, but its spawn entries (registered in P6) are gated
 * behind the {@link com.skd.primitivemenagerie.config.PMConfig} enable flags.</p>
 *
 * @author Daveyx0
 */
public final class PMEntities {

    public static final DeferredHolder<EntityType<?>, EntityType<RocketCreeper>> ROCKET_CREEPER =
            PMRegistries.registerMob("rocket_creeper", RocketCreeper::new, MobCategory.MONSTER, 0.6F, 1.7F,
                    RocketCreeper::createAttributes, 0x4CA9D0, 0x000000);

    public static final DeferredHolder<EntityType<?>, EntityType<FestiveCreeper>> FESTIVE_CREEPER =
            PMRegistries.registerMob("festive_creeper", FestiveCreeper::new, MobCategory.MONSTER, 0.6F, 1.7F,
                    FestiveCreeper::createAttributes, 0xBC3608, 0x000000);

    public static final DeferredHolder<EntityType<?>, EntityType<SupportCreeper>> SUPPORT_CREEPER =
            PMRegistries.registerMob("support_creeper", SupportCreeper::new, MobCategory.MONSTER, 0.6F, 1.7F,
                    SupportCreeper::createAttributes, 0xDBBD2F, 0x000000);

    public static final DeferredHolder<EntityType<?>, EntityType<SkeletonWarrior>> SKELETON_WARRIOR =
            PMRegistries.registerMob("skeleton_warrior", SkeletonWarrior::new, MobCategory.MONSTER, 0.6F, 1.99F,
                    SkeletonWarrior::createAttributes, 0xABA188, 0x6C5239);

    public static final DeferredHolder<EntityType<?>, EntityType<MotherSpider>> MOTHER_SPIDER =
            PMRegistries.registerMob("mother_spider", MotherSpider::new, MobCategory.MONSTER, 1.4F, 0.9F,
                    MotherSpider::createAttributes, 0x250522, 0xA80E0E);

    public static final DeferredHolder<EntityType<?>, EntityType<BabySpider>> BABY_SPIDER =
            PMRegistries.registerMob("baby_spider", BabySpider::new, MobCategory.MONSTER, 0.5F, 0.3F,
                    BabySpider::createAttributes, 0xB59468, 0xA80E0E);

    public static final DeferredHolder<EntityType<?>, EntityType<Goblin>> GOBLIN =
            PMRegistries.registerMob("goblin", Goblin::new, MobCategory.MONSTER, 0.6F, 1.95F,
                    Goblin::createAttributes, 0x8B976D, 0x524137);

    public static final DeferredHolder<EntityType<?>, EntityType<Harpy>> HARPY =
            PMRegistries.registerMob("harpy", Harpy::new, MobCategory.MONSTER, 0.8F, 1.0F,
                    Harpy::createAttributes, 0x3D3454, 0xA4936B);

    /** Thrown spider egg projectile; registered with the original custom tracking parameters. */
    public static final DeferredHolder<EntityType<?>, EntityType<SpiderEgg>> SPIDER_EGG =
            PMRegistries.registerSimpleEntity("spider_egg", SpiderEgg::new, MobCategory.MISC, 0.25F, 0.25F, 64, 20);

    private PMEntities() {}

    /** Forces class initialisation so the deferred entries above are created. */
    public static void register() {}
}
