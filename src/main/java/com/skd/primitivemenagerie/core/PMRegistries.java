// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import java.util.LinkedHashMap;
import java.util.Map;

import com.skd.primitivemenagerie.PrimitiveMenagerie;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * All deferred registries of the mod, replacing the original {@code PrimitiveMobsEntityRegistry}
 * plus the item/sound registration of the original core package. Everything is registered on the
 * mod event bus.
 *
 * <p>Entity types may be registered with an {@link AttributeSupplier.Builder}; the recorded
 * supplier is applied when {@link EntityAttributeCreationEvent} fires, so the individual entity
 * classes only have to expose their {@code createAttributes()} method in later phases.</p>
 *
 * @author Daveyx0
 */
public final class PMRegistries {

    /** Registry of every entity type of the mod. */
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PMReference.MODID);

    /** Registry of the base items of the mod. */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PMReference.MODID);

    /** Registry of the sound events of the mod. */
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, PMReference.MODID);

    private static final int DEFAULT_TRACKING_RANGE = 80;
    private static final int DEFAULT_UPDATE_INTERVAL = 3;

    /** Attribute suppliers keyed by the entity type holder they belong to. */
    private static final Map<DeferredHolder<EntityType<?>, ?>, AttributeSupplier.Builder> ATTRIBUTES = new LinkedHashMap<>();

    private PMRegistries() {}

    /** Registers every deferred registry and the attribute-creation listener on the mod event bus. */
    public static void register(IEventBus modEventBus) {
        ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUNDS.register(modEventBus);
        modEventBus.addListener(PMRegistries::onEntityAttributeCreation);

        // Force the item registry class to initialise so its deferred entries are created.
        PMItems.register();
    }

    /**
     * Registers an entity type without attributes.
     *
     * @param name     registry name
     * @param factory  entity factory
     * @param category creature category
     * @param width    entity width
     * @param height   entity height
     */
    public static <T extends Mob> DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(
            String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height) {
        return registerEntity(name, factory, category, width, height, null);
    }

    /**
     * Registers an entity type and records the supplied attribute builder so it is applied on
     * {@link EntityAttributeCreationEvent}. Pass {@code null} for an entity without attributes.
     */
    public static <T extends Mob> DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(
            String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height,
            AttributeSupplier.Builder attributes) {
        DeferredHolder<EntityType<?>, EntityType<T>> holder = ENTITIES.register(name,
                () -> EntityType.Builder.of(factory, category)
                        .sized(width, height)
                        .clientTrackingRange(DEFAULT_TRACKING_RANGE)
                        .updateInterval(DEFAULT_UPDATE_INTERVAL)
                        .build(PMReference.MODID + ":" + name));

        if (attributes != null) {
            ATTRIBUTES.put(holder, attributes);
        }

        return holder;
    }

    /**
     * Registers a spawn egg item for an entity type using the two egg colours declared by the
     * original {@code PrimitiveMobsEntityRegistry}. The item name is {@code <entityName>_spawn_egg}.
     */
    public static <T extends Mob> DeferredItem<SpawnEggItem> registerSpawnEgg(
            String entityName, DeferredHolder<EntityType<?>, EntityType<T>> type,
            int backgroundEggColor, int foregroundEggColor) {
        return ITEMS.register(entityName + "_spawn_egg", () -> new DeferredSpawnEggItem(
                () -> type.get(), backgroundEggColor, foregroundEggColor, new Item.Properties()));
    }

    /**
     * Convenience helper registering an entity type together with its attribute builder and spawn
     * egg, mirroring the original {@code addEntities(...)} signature.
     */
    public static <T extends Mob> DeferredHolder<EntityType<?>, EntityType<T>> registerMob(
            String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height,
            AttributeSupplier.Builder attributes, int backgroundEggColor, int foregroundEggColor) {
        DeferredHolder<EntityType<?>, EntityType<T>> holder = registerEntity(name, factory, category, width, height, attributes);
        registerSpawnEgg(name, holder, backgroundEggColor, foregroundEggColor);
        return holder;
    }

    /** @return the recorded attribute builders, keyed by their entity type holder */
    public static Map<DeferredHolder<EntityType<?>, ?>, AttributeSupplier.Builder> getAttributes() {
        return ATTRIBUTES;
    }

    private static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        ATTRIBUTES.forEach((holder, builder) -> {
            @SuppressWarnings("unchecked")
            EntityType<? extends LivingEntity> type = (EntityType<? extends LivingEntity>) holder.get();
            event.put(type, builder.build());
        });

        PrimitiveMenagerie.LOGGER.debug("Registered {} entity attribute suppliers", ATTRIBUTES.size());
    }
}
