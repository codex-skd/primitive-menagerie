// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.skd.primitivemenagerie.client.models.ModelMimic;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.Mimic;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Mimic mouth layer, ported from {@code LayerMimicMouth}. Draws the mod chest texture over the model.
 *
 * @author Daveyx0
 */
public class LayerMimicMouth extends RenderLayer<Mimic, ModelMimic> {

    private static final ResourceLocation MOUTH_TEXTURE = PMReference.id("textures/entity/mimic/chest.png");

    private final ModelMimic model;

    public LayerMimicMouth(RenderLayerParent<Mimic, ModelMimic> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new ModelMimic(modelSet.bakeLayer(PMModelLayers.MIMIC));
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, Mimic entity, float limbSwing,
            float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(MOUTH_TEXTURE));
        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.model.renderToBuffer(pose, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F));
    }
}
