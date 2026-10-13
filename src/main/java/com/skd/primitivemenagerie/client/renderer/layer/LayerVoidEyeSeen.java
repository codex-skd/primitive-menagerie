// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.skd.primitivemenagerie.client.models.ModelVoidEye;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.VoidEye;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * "Seen" overlay layer for the Void Eye, ported from {@code LayerVoidEyeSeen}. Only the eye shell is
 * drawn, with an additive glow, matching the original {@code ModelVoidEye(false)}.
 *
 * @author Daveyx0
 */
public class LayerVoidEyeSeen extends RenderLayer<VoidEye, ModelVoidEye> {

    private static final ResourceLocation SEEN_TEXTURE = PMReference.id("textures/entity/voideye/voideye_seen.png");

    private final ModelVoidEye model;

    public LayerVoidEyeSeen(RenderLayerParent<VoidEye, ModelVoidEye> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new ModelVoidEye(modelSet.bakeLayer(PMModelLayers.VOID_EYE));
        this.model.root().getChild("body1").visible = false;
        this.model.root().getChild("body2").visible = false;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, VoidEye entity, float limbSwing,
            float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.eyes(SEEN_TEXTURE));
        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.model.renderToBuffer(pose, vertexConsumer, packedLight, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
    }
}
