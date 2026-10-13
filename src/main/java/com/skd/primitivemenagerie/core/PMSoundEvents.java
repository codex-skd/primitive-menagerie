// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.core;

import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Sound events of the mod, replacing the original {@code PrimitiveMobsSoundEvents}. Every event is
 * registered through the {@link PMRegistries#SOUNDS} deferred register and described in
 * {@code assets/primitive_menagerie/sounds.json}.
 *
 * @author Daveyx0
 */
public final class PMSoundEvents {

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BRAINSLIME_CHARGE = register("entity.brainslime.slimecharge");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_MOTHERSPIDER_SCREECH = register("entity.motherspider.spiderscreech");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GROVESPRITE_ANGRY = register("entity.grovesprite.angry");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GROVESPRITE_DEATH = register("entity.grovesprite.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GROVESPRITE_HURT = register("entity.grovesprite.hurt");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GROVESPRITE_IDLE = register("entity.grovesprite.idle");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GROVESPRITE_THANKS = register("entity.grovesprite.thanks");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TROLLAGER_IDLE = register("entity.trollager.idle");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TROLLAGER_HIT = register("entity.trollager.hit");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TROLLAGER_DEATH = register("entity.trollager.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TROLLAGER_ATTACK = register("entity.trollager.attack");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HARPY_IDLE = register("entity.harpy.idle");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HARPY_HURT = register("entity.harpy.hurt");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_FLAMESPEWER_IDLE = register("entity.flamespewer.idle");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_VOIDEYE_IDLE = register("entity.voideye.idle");

    private PMSoundEvents() {}

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return PMRegistries.SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(PMReference.id(name)));
    }
}
