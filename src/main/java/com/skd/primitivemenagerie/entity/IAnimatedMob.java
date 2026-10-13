// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity;

import net.minecraft.world.entity.LivingEntity;

/**
 * Animation-state contract, ported from the original {@code IAnimatedMob}. Implemented by the
 * Trollager for its throwing/crushing/melee animation state machine.
 *
 * @author Daveyx0
 */
public interface IAnimatedMob {

    void performAction(LivingEntity target, int id);

    void setAnimationState(int state);

    int getAnimationState();

    void setPreviousAnimationState(int state);

    int getPreviousAnimationState();

    void setAnimVar(float var);

    float getAnimVar();
}
