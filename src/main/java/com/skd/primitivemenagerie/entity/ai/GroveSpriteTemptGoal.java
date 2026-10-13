// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.entity.ai;

import java.util.Set;

import com.skd.mobweavecore.entity.ai.TemptItemStackGoal;
import com.skd.primitivemenagerie.entity.passive.GroveSprite;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;

/**
 * Port of {@code EntityAIGroveSpriteTempt}. Tempts the Grove Sprite with the item it holds and flags
 * the begging state while following the player.
 *
 * @author Daveyx0
 */
public class GroveSpriteTemptGoal extends TemptItemStackGoal {

    public GroveSpriteTemptGoal(PathfinderMob temptedEntity, double speed, boolean scaredByPlayerMovement,
            Set<ItemStack> temptItems) {
        super(temptedEntity, speed, scaredByPlayerMovement, temptItems);
    }

    @Override
    public void start() {
        super.start();

        if (this.temptedEntity instanceof GroveSprite sprite) {
            sprite.setIsBegging(true);
        }
    }

    @Override
    public void stop() {
        super.stop();

        if (this.temptedEntity instanceof GroveSprite sprite) {
            sprite.setIsBegging(false);
        }
    }
}
