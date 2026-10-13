// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.client.models.ModelSheepman;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerSheepmanWool;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.passive.Sheepman;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Sheepman renderer, ported from {@code RenderSheepman}. Uses the skin model as its base and a wool
 * overlay layer.
 *
 * @author Daveyx0
 */
public class RenderSheepman extends MobRenderer<Sheepman, ModelSheepman> {

    private static final ResourceLocation SHEEPMAN_TEXTURE = PMReference.id("textures/entity/villager/sheepman.png");

    public RenderSheepman(EntityRendererProvider.Context context) {
        super(context, new ModelSheepman(context.bakeLayer(PMModelLayers.SHEEPMAN)), 0.5F);
        this.model.setWoolVariant(false);
        this.addLayer(new LayerSheepmanWool(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Sheepman entity) {
        return SHEEPMAN_TEXTURE;
    }

    @Override
    protected void scale(Sheepman entity, PoseStack pose, float partialTick) {
        float f = 0.9375F;

        if (entity.isBaby()) {
            f *= 0.5F;
        }

        pose.scale(f, f, f);
    }
}
