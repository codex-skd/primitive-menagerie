// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.skd.primitivemenagerie.client.models.ModelTreasureSlime;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.TreasureSlime;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Coloured gel shell layer for the Treasure Slime, ported from {@code LayerTreasureSlimeGel}. The
 * shell is tinted with the slime's item-derived RGB colour (the original also had a rainbow dye and
 * an enchantment glint, which are not ported — {@code TODO(verify)}).
 *
 * @author Daveyx0
 */
public class LayerTreasureSlimeGel extends RenderLayer<TreasureSlime, ModelTreasureSlime> {

    private static final ResourceLocation SLIME_TEXTURES = PMReference.id("textures/entity/treasureslime/slime_treasure.png");

    private final ModelTreasureSlime model;

    public LayerTreasureSlimeGel(RenderLayerParent<TreasureSlime, ModelTreasureSlime> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new ModelTreasureSlime(modelSet.bakeLayer(PMModelLayers.TREASURE_SLIME));
        this.model.root().getChild("cube").visible = true;
        this.model.root().getChild("right_eye").visible = false;
        this.model.root().getChild("left_eye").visible = false;
        this.model.root().getChild("mouth").visible = false;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, TreasureSlime entity,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }

        float[] rgb = entity.getSkinRGB();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(SLIME_TEXTURES));
        vertexConsumer.setColor((int) rgb[0], (int) rgb[1], (int) rgb[2], 255);
        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.model.renderToBuffer(pose, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F));
    }
}
