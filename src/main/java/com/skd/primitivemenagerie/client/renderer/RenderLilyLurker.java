// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.client.models.ModelLilyLurker;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerHeldItemCustom;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.LilyLurker;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Lily Lurker renderer, ported from {@code RenderLilyLurker}. A camouflaged lurker is shifted down so
 * its lily pad sits at the water surface.
 *
 * @author Daveyx0
 */
public class RenderLilyLurker extends MobRenderer<LilyLurker, ModelLilyLurker> {

    private static final ResourceLocation TEXTURE = PMReference.id("textures/entity/lilylurker/lilylurker.png");

    public RenderLilyLurker(EntityRendererProvider.Context context) {
        super(context, new ModelLilyLurker(context.bakeLayer(PMModelLayers.LILY_LURKER)), 0.5F);
        this.addLayer(new LayerHeldItemCustom<>(this));
    }

    @Override
    protected void scale(LilyLurker entity, PoseStack pose, float partialTick) {
        if (entity.isCamouflaged()) {
            pose.translate(0.0F, -0.24F, 0.0F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(LilyLurker entity) {
        return TEXTURE;
    }
}
