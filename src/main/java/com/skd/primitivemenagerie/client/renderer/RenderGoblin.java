// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.skd.primitivemenagerie.client.models.ModelGoblin;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.Goblin;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Goblin renderer, ported from {@code RenderGoblin}. Uses the mod's goblin model.
 *
 * @author Daveyx0
 */
public class RenderGoblin extends MobRenderer<Goblin, ModelGoblin> {

    private static final ResourceLocation GOBLIN_TEXTURE = PMReference.id("textures/entity/goblin/goblin.png");

    public RenderGoblin(EntityRendererProvider.Context context) {
        super(context, new ModelGoblin(context.bakeLayer(PMModelLayers.GOBLIN)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Goblin entity) {
        return GOBLIN_TEXTURE;
    }
}
