// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client;

import org.lwjgl.glfw.GLFW;

import com.skd.primitivemenagerie.entity.monster.BabySpider;
import com.skd.primitivemenagerie.network.PrimitiveJumpingPayload;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Client-only input handler, replacing the original {@code KeyInputEvent} handler. Pressing the jump
 * key while riding a Baby Spider sends the {@link PrimitiveJumpingPayload} to the server.
 *
 * @author Daveyx0
 */
public final class PMClientEvents {

    private PMClientEvents() {}

    /** Registers the handler on the game event bus (client only). */
    public static void register() {
        NeoForge.EVENT_BUS.addListener(PMClientEvents::onKeyInput);
    }

    private static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }

        if (event.getAction() == GLFW.GLFW_PRESS && minecraft.options.keyJump.isDown()
                && minecraft.player.getVehicle() instanceof BabySpider) {
            PacketDistributor.sendToServer(new PrimitiveJumpingPayload(minecraft.player.getUUID()));
        }
    }
}
