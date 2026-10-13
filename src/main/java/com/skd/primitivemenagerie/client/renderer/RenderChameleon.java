// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.client.models.ModelChameleon;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.passive.Chameleon;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Chameleon renderer, ported from {@code RenderChameleon}. Applies the entity's shifting skin color
 * as a vertex tint and halves the size of babies (`TODO(verify)` for the exact original scale).
 *
 * @author Daveyx0
 */
public class RenderChameleon extends MobRenderer<Chameleon, ModelChameleon> {

    private static final ResourceLocation CHAMELEON_TEXTURES = PMReference.id("textures/entity/chameleon/chameleon.png");

    public RenderChameleon(EntityRendererProvider.Context context) {
        super(context, new ModelChameleon(context.bakeLayer(PMModelLayers.CHAMELEON)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Chameleon entity) {
        return CHAMELEON_TEXTURES;
    }

    @Override
    public void render(Chameleon entity, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffer,
            int packedLight) {
        float[] rgb = entity.getSkinRGB();
        MultiBufferSource tinted = renderType -> PMTint.color(buffer.getBuffer(renderType),
                rgb[0] / 255.0F, rgb[1] / 255.0F, rgb[2] / 255.0F, 1.0F);
        super.render(entity, entityYaw, partialTicks, pose, tinted, packedLight);
    }

    @Override
    protected void scale(Chameleon entity, PoseStack pose, float partialTick) {
        if (entity.isBaby()) {
            pose.scale(0.5F, 0.5F, 0.5F);
        }
    }
}
