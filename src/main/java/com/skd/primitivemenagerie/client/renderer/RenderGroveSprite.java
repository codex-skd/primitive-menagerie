// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.client.models.ModelGroveSprite;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerGroveSpriteLeaves;
import com.skd.primitivemenagerie.client.renderer.layer.LayerGroveSpriteStump;
import com.skd.primitivemenagerie.client.renderer.layer.LayerHeldItemCustom;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.passive.GroveSprite;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Grove Sprite renderer, ported from {@code RenderGroveSprite}. Uses the stump variant of the model
 * plus the held-item, leaves and stump overlay layers. The body is tinted with the log color.
 *
 * @author Daveyx0
 */
public class RenderGroveSprite extends MobRenderer<GroveSprite, ModelGroveSprite> {

    private static final ResourceLocation GROVEBASE_TEXTURES = PMReference.id("textures/entity/grovesprite/grovebase.png");
    private static final ResourceLocation GROVEBASE_CINDER_TEXTURES = PMReference.id("textures/entity/grovesprite/grovecinder.png");

    public RenderGroveSprite(EntityRendererProvider.Context context) {
        super(context, new ModelGroveSprite(context.bakeLayer(PMModelLayers.GROVE_SPRITE)), 0.3F);
        this.model.setVariant(false);
        this.addLayer(new LayerHeldItemCustom<>(this));
        this.addLayer(new LayerGroveSpriteLeaves(this, context.getModelSet()));
        this.addLayer(new LayerGroveSpriteStump(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(GroveSprite entity) {
        return entity.isCinderSprite() ? GROVEBASE_CINDER_TEXTURES : GROVEBASE_TEXTURES;
    }

    @Override
    public void render(GroveSprite entity, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffer,
            int packedLight) {
        float[] rgb = entity.getLogRGB();
        MultiBufferSource tinted = renderType -> PMTint.color(buffer.getBuffer(renderType),
                rgb[0] / 255.0F, rgb[1] / 255.0F, rgb[2] / 255.0F, 1.0F);
        super.render(entity, entityYaw, partialTicks, pose, tinted, packedLight);
    }

    @Override
    protected void scale(GroveSprite entity, PoseStack pose, float partialTick) {
        pose.translate(0.0F, 0.9F, 0.0F);
    }
}
