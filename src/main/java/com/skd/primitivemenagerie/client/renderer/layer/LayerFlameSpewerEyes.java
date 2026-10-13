// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.skd.primitivemenagerie.client.models.ModelFlameSpewer;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.FlameSpewer;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Glowing lava-eyes layer for the Flame Spewer, ported from {@code LayerFlameSpewerEyes}.
 *
 * @author Daveyx0
 */
public class LayerFlameSpewerEyes extends RenderLayer<FlameSpewer, ModelFlameSpewer> {

    private static final ResourceLocation EYES_TEXTURE = PMReference.id("textures/entity/flamespewer/flamespewer_lava.png");

    private final ModelFlameSpewer eyesModel;

    public LayerFlameSpewerEyes(RenderLayerParent<FlameSpewer, ModelFlameSpewer> parent, EntityModelSet modelSet) {
        super(parent);
        this.eyesModel = new ModelFlameSpewer(modelSet.bakeLayer(PMModelLayers.FLAME_SPEWER));

        for (int i = 0; i < 8; i++) {
            this.eyesModel.root().getChild("tentacle" + i).visible = false;
        }
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, FlameSpewer entity,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        float alpha = Math.clamp((10.0F - entity.getAttackTime()) / 10.0F, 0.0F, 1.0F);

        if (alpha <= 0.0F) {
            return;
        }

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(EYES_TEXTURE));
        this.eyesModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.eyesModel.renderToBuffer(pose, vertexConsumer, packedLight,
                LivingEntityRenderer.getOverlayCoords(entity, 0.0F),
                ((int) (alpha * 255.0F) << 24) | 0xFFFFFF);
    }
}
