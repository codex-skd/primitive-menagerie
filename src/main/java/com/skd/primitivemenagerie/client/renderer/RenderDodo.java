// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.client.models.ModelDodo;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.passive.Dodo;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Dodo renderer, ported from {@code RenderDodo}. Babies are scaled down in the renderer.
 *
 * @author Daveyx0
 */
public class RenderDodo extends MobRenderer<Dodo, ModelDodo> {

    private static final ResourceLocation DODO_TEXTURES = PMReference.id("textures/entity/rareanimals/dodo.png");

    public RenderDodo(EntityRendererProvider.Context context) {
        super(context, new ModelDodo(context.bakeLayer(PMModelLayers.DODO)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Dodo entity) {
        return DODO_TEXTURES;
    }

    @Override
    protected void scale(Dodo entity, PoseStack pose, float partialTick) {
        if (entity.isBaby()) {
            pose.scale(0.5F, 0.5F, 0.5F);
        }
    }
}
