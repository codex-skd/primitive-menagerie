// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.skd.primitivemenagerie.client.models.ModelGroveSprite;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.PMTint;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.passive.GroveSprite;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Grove Sprite leaves layer, ported from {@code LayerGroveSpriteLeaves}. Draws the leaves variant of
 * the model tinted with the entity's leaf color.
 *
 * @author Daveyx0
 */
public class LayerGroveSpriteLeaves extends RenderLayer<GroveSprite, ModelGroveSprite> {

    private static final ResourceLocation GROVELEAF_TEXTURES = PMReference.id("textures/entity/grovesprite/groveleaf.png");
    private static final ResourceLocation GROVELEAF_CINDER_TEXTURES = PMReference.id("textures/entity/grovesprite/cinderleaf.png");

    private final ModelGroveSprite model;

    public LayerGroveSpriteLeaves(RenderLayerParent<GroveSprite, ModelGroveSprite> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new ModelGroveSprite(modelSet.bakeLayer(PMModelLayers.GROVE_SPRITE));
        this.model.setVariant(true);
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, GroveSprite entity, float limbSwing,
            float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        float[] rgb = entity.getLeavesRGB();
        ResourceLocation texture = entity.isCinderSprite() ? GROVELEAF_CINDER_TEXTURES : GROVELEAF_TEXTURES;
        VertexConsumer vertexConsumer = PMTint.color(buffer.getBuffer(RenderType.entityCutoutNoCull(texture)),
                rgb[0] / 255.0F, rgb[1] / 255.0F, rgb[2] / 255.0F, 1.0F);

        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.model.renderToBuffer(pose, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F));
    }
}
