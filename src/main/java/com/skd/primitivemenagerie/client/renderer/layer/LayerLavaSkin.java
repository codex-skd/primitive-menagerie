// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.skd.primitivemenagerie.client.models.ModelFlameSpewer;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.entity.monster.FlameSpewer;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Lava skin layer for the Flame Spewer, ported from {@code LayerLavaSkin}. The original bound a
 * dynamic texture built from the lava sprite; here the vanilla {@code lava_flow} texture is used
 * directly, with the texture scroll animation omitted ({@code TODO(verify)}).
 *
 * @author Daveyx0
 */
public class LayerLavaSkin extends RenderLayer<FlameSpewer, ModelFlameSpewer> {

    private static final ResourceLocation LAVA_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/block/lava_flow.png");

    private final ModelFlameSpewer lavaModel;

    public LayerLavaSkin(RenderLayerParent<FlameSpewer, ModelFlameSpewer> parent, EntityModelSet modelSet) {
        super(parent);
        this.lavaModel = new ModelFlameSpewer(modelSet.bakeLayer(PMModelLayers.FLAME_SPEWER_LAVA));
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, FlameSpewer entity,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        float alpha = Math.clamp((10.0F - entity.getAttackTime()) / 10.0F, 0.0F, 1.0F);

        if (alpha <= 0.0F) {
            return;
        }

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(LAVA_TEXTURE));
        this.lavaModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.lavaModel.renderToBuffer(pose, vertexConsumer, packedLight,
                LivingEntityRenderer.getOverlayCoords(entity, 0.0F),
                ((int) (alpha * 255.0F) << 24) | 0xFFFFFF);
    }
}
