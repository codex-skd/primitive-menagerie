// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.client.models.ModelTravelingMerchant;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.passive.TravelingMerchant;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Traveling Merchant renderer, ported from {@code RenderTravelingMerchant}.
 *
 * @author Daveyx0
 */
public class RenderTravelingMerchant extends MobRenderer<TravelingMerchant, ModelTravelingMerchant> {

    private static final ResourceLocation TRAVELINGMERCHANT_TEXTURE = PMReference.id("textures/entity/villager/travelingmerchant.png");

    public RenderTravelingMerchant(EntityRendererProvider.Context context) {
        super(context, new ModelTravelingMerchant(context.bakeLayer(PMModelLayers.TRAVELING_MERCHANT)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(TravelingMerchant entity) {
        return TRAVELINGMERCHANT_TEXTURE;
    }

    @Override
    protected void scale(TravelingMerchant entity, PoseStack pose, float partialTick) {
        float f = 0.9375F;

        if (entity.isBaby()) {
            f *= 0.5F;
        }

        pose.scale(f, f, f);
    }
}
