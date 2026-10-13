// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.skd.primitivemenagerie.client.models.ModelBrainSlime;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.BrainSlime;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Translucent gel shell layer for the Brain Slime, ported from {@code LayerBrainSlimeGel}.
 *
 * @author Daveyx0
 */
public class LayerBrainSlimeGel extends RenderLayer<BrainSlime, ModelBrainSlime> {

    private static final ResourceLocation SLIME_TEXTURES = PMReference.id("textures/entity/brainslime/slime_brain.png");

    private final ModelBrainSlime model;

    public LayerBrainSlimeGel(RenderLayerParent<BrainSlime, ModelBrainSlime> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new ModelBrainSlime(modelSet.bakeLayer(PMModelLayers.BRAIN_SLIME));
        this.model.root().getChild("cube").visible = true;
        this.model.root().getChild("inner_cube").visible = false;
        this.model.root().getChild("right_eye").visible = false;
        this.model.root().getChild("left_eye").visible = false;
        this.model.root().getChild("mouth").visible = false;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, BrainSlime entity,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(SLIME_TEXTURES));
        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.model.renderToBuffer(pose, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F));
    }
}
