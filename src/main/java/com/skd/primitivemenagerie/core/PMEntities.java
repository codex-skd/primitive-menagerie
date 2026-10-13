// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import com.skd.primitivemenagerie.entity.item.FlameSpit;
import com.skd.primitivemenagerie.entity.item.PrimitiveTNTPrimed;
import com.skd.primitivemenagerie.entity.item.PrimitiveThrowable;
import com.skd.primitivemenagerie.entity.item.SpiderEgg;
import com.skd.primitivemenagerie.entity.item.ThrownBlock;
import com.skd.primitivemenagerie.entity.monster.BabySpider;
import com.skd.primitivemenagerie.entity.monster.BlazingJuggernaut;
import com.skd.primitivemenagerie.entity.monster.BrainSlime;
import com.skd.primitivemenagerie.entity.monster.EnchantedBook;
import com.skd.primitivemenagerie.entity.monster.FestiveCreeper;
import com.skd.primitivemenagerie.entity.monster.FlameSpewer;
import com.skd.primitivemenagerie.entity.monster.Goblin;
import com.skd.primitivemenagerie.entity.monster.Harpy;
import com.skd.primitivemenagerie.entity.monster.HauntedTool;
import com.skd.primitivemenagerie.entity.monster.LilyLurker;
import com.skd.primitivemenagerie.entity.monster.Mimic;
import com.skd.primitivemenagerie.entity.monster.MotherSpider;
import com.skd.primitivemenagerie.entity.monster.RocketCreeper;
import com.skd.primitivemenagerie.entity.monster.SkeletonWarrior;
import com.skd.primitivemenagerie.entity.monster.SupportCreeper;
import com.skd.primitivemenagerie.entity.monster.TreasureSlime;
import com.skd.primitivemenagerie.entity.monster.Trollager;
import com.skd.primitivemenagerie.entity.monster.VoidEye;
import com.skd.primitivemenagerie.entity.monster.Yeti;

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

    public static final DeferredHolder<EntityType<?>, EntityType<TreasureSlime>> TREASURE_SLIME =
            PMRegistries.registerMob("treasure_slime", TreasureSlime::new, MobCategory.MONSTER, 0.52F, 0.52F,
                    TreasureSlime::createAttributes, 0xFFF399, 0xFFE332);

    public static final DeferredHolder<EntityType<?>, EntityType<BrainSlime>> BRAIN_SLIME =
            PMRegistries.registerMob("brain_slime", BrainSlime::new, MobCategory.MONSTER, 0.52F, 0.52F,
                    BrainSlime::createAttributes, 0xC696B0, 0xD1A5BD);

    public static final DeferredHolder<EntityType<?>, EntityType<HauntedTool>> HAUNTED_TOOL =
            PMRegistries.registerMob("haunted_tool", HauntedTool::new, MobCategory.MONSTER, 0.6F, 1.0F,
                    HauntedTool::createAttributes, 0x493615, 0x684E1E);

    public static final DeferredHolder<EntityType<?>, EntityType<EnchantedBook>> BEWITCHED_TOME =
            PMRegistries.registerMob("bewitched_tome", EnchantedBook::new, MobCategory.MONSTER, 0.5F, 1.0F,
                    EnchantedBook::createAttributes, 0xB77A35, 0xD0D0D0);

    public static final DeferredHolder<EntityType<?>, EntityType<BlazingJuggernaut>> BLAZING_JUGGERNAUT =
            PMRegistries.registerMob("blazing_juggernaut", BlazingJuggernaut::new, MobCategory.MONSTER, 0.6F, 1.8F,
                    BlazingJuggernaut::createAttributes, 0x30181C, 0xB0A938);

    public static final DeferredHolder<EntityType<?>, EntityType<LilyLurker>> LILY_LURKER =
            PMRegistries.registerMob("lily_lurker", LilyLurker::new, MobCategory.MONSTER, 0.5F, 0.5F,
                    LilyLurker::createAttributes, 0x593D29, 0x3D3C1C);

    public static final DeferredHolder<EntityType<?>, EntityType<Trollager>> TROLLAGER =
            PMRegistries.registerMob("trollager", Trollager::new, MobCategory.MONSTER, 2.25F, 3.0F,
                    Trollager::createAttributes, 0x56845D, 0x35251F);

    public static final DeferredHolder<EntityType<?>, EntityType<Mimic>> MIMIC =
            PMRegistries.registerMob("mimic", Mimic::new, MobCategory.MONSTER, 0.9F, 0.9F,
                    Mimic::createAttributes, 0xAB792D, 0x2A251D);

    public static final DeferredHolder<EntityType<?>, EntityType<FlameSpewer>> FLAME_SPEWER =
            PMRegistries.registerMob("flame_spewer", FlameSpewer::new, MobCategory.MONSTER, 1.0F, 1.25F,
                    FlameSpewer::createAttributes, 0x86462E, 0x8D6E66);

    public static final DeferredHolder<EntityType<?>, EntityType<VoidEye>> VOID_EYE =
            PMRegistries.registerMob("void_eye", VoidEye::new, MobCategory.MONSTER, 0.6F, 0.6F,
                    VoidEye::createAttributes, 0x0C0D12, 0x8ECD79);

    /**
     * Yeti. Note: the original mod never registered this mob (it only ships the class, model and
     * renderer); the spawn-egg colours below are new, chosen to fit the creature.
     */
    public static final DeferredHolder<EntityType<?>, EntityType<Yeti>> YETI =
            PMRegistries.registerMob("yeti", Yeti::new, MobCategory.MONSTER, 1.7F, 3.0F,
                    Yeti::createAttributes, 0xE8E8E8, 0x9AA7B8);

    /** Thrown spider egg projectile; registered with the original custom tracking parameters. */
    public static final DeferredHolder<EntityType<?>, EntityType<SpiderEgg>> SPIDER_EGG =
            PMRegistries.registerSimpleEntity("spider_egg", SpiderEgg::new, MobCategory.MISC, 0.25F, 0.25F, 64, 20);

    /** Base thrown primitive egg projectile, used by the mystery eggs in P6. */
    public static final DeferredHolder<EntityType<?>, EntityType<PrimitiveThrowable>> PRIMITIVE_EGG =
            PMRegistries.registerSimpleEntity("primitive_egg", PrimitiveThrowable::new, MobCategory.MISC, 0.25F, 0.25F, 64, 20);

    /** Thrown block projectile. */
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownBlock>> THROWN_BLOCK =
            PMRegistries.registerSimpleEntity("thrown_block", ThrownBlock::new, MobCategory.MISC, 1.0F, 1.0F, 64, 20);

    /** Flame spit projectile fired by the Flame Spewer. */
    public static final DeferredHolder<EntityType<?>, EntityType<FlameSpit>> FLAME_SPIT =
            PMRegistries.registerSimpleEntity("flame_spit", FlameSpit::new, MobCategory.MISC, 0.3125F, 0.3125F, 64, 1);

    /** Primed primitive TNT thrown by the Festive Creeper. */
    public static final DeferredHolder<EntityType<?>, EntityType<PrimitiveTNTPrimed>> PRIMITIVE_TNT_PRIMED =
            PMRegistries.registerSimpleEntity("primitive_tnt_primed", PrimitiveTNTPrimed::new, MobCategory.MISC, 0.98F, 0.98F, 64, 20);

    private PMEntities() {}

    /** Forces class initialisation so the deferred entries above are created. */
    public static void register() {}
}
