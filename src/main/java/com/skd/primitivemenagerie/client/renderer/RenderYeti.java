// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.skd.primitivemenagerie.client.models.ModelYeti;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.Yeti;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Yeti renderer, ported from {@code RenderYeti}.
 *
 * @author Daveyx0
 */
public class RenderYeti extends MobRenderer<Yeti, ModelYeti> {

    private static final ResourceLocation TEXTURE = PMReference.id("textures/entity/yeti/yeti.png");

    public RenderYeti(EntityRendererProvider.Context context) {
        super(context, new ModelYeti(context.bakeLayer(PMModelLayers.YETI)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Yeti entity) {
        return TEXTURE;
    }
}
