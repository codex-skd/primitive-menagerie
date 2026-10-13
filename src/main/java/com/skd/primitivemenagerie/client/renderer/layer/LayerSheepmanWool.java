// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.skd.primitivemenagerie.client.models.ModelSheepman;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.PMTint;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.passive.Sheepman;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

/**
 * Sheepman wool layer, ported from {@code LayerSheepmanWool}. Draws the wool overlay tinted with the
 * sheepman's fleece color (or a rainbow cycle for a sheepman named {@code jeb_}).
 *
 * @author Daveyx0
 */
public class LayerSheepmanWool extends RenderLayer<Sheepman, ModelSheepman> {

    private static final ResourceLocation TEXTURE = PMReference.id("textures/entity/villager/sheepman.png");

    private final ModelSheepman model;

    public LayerSheepmanWool(RenderLayerParent<Sheepman, ModelSheepman> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new ModelSheepman(modelSet.bakeLayer(PMModelLayers.SHEEPMAN));
        this.model.setWoolVariant(true);
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, Sheepman entity, float limbSwing,
            float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.getSheared() || entity.isInvisible()) {
            return;
        }

        float r;
        float g;
        float b;

        if (entity.hasCustomName() && "jeb_".equals(entity.getCustomName().getString())) {
            int i1 = 25;
            int i = entity.tickCount / 25 + entity.getId();
            int j = DyeColor.values().length;
            int k = i % j;
            int l = (i + 1) % j;
            float f = ((float) (entity.tickCount % 25) + partialTick) / 25.0F;
            int c1 = DyeColor.byId(k).getTextureDiffuseColor();
            int c2 = DyeColor.byId(l).getTextureDiffuseColor();
            r = (((c1 >> 16) & 255) * (1.0F - f) + ((c2 >> 16) & 255) * f) / 255.0F;
            g = (((c1 >> 8) & 255) * (1.0F - f) + ((c2 >> 8) & 255) * f) / 255.0F;
            b = ((c1 & 255) * (1.0F - f) + (c2 & 255) * f) / 255.0F;
        } else {
            int color = entity.getFleeceColor().getTextureDiffuseColor();
            r = ((color >> 16) & 255) / 255.0F;
            g = ((color >> 8) & 255) / 255.0F;
            b = (color & 255) / 255.0F;
        }

        VertexConsumer vertexConsumer = PMTint.color(buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), r, g, b, 1.0F);
        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.model.renderToBuffer(pose, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F));
    }
}
