// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.client.models.ModelLostMiner;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerHeldItemCustom;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.passive.LostMiner;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Lost Miner renderer, ported from {@code RenderLostMiner}. The model already contains the mining
 * helmet, matching the original.
 *
 * @author Daveyx0
 */
public class RenderLostMiner extends MobRenderer<LostMiner, ModelLostMiner> {

    private static final ResourceLocation LOST_MINER_TEXTURES = PMReference.id("textures/entity/villager/lostminer.png");

    public RenderLostMiner(EntityRendererProvider.Context context) {
        super(context, new ModelLostMiner(context.bakeLayer(PMModelLayers.LOST_MINER)), 0.5F);
        this.addLayer(new LayerHeldItemCustom<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(LostMiner entity) {
        return LOST_MINER_TEXTURES;
    }

    @Override
    protected void scale(LostMiner entity, PoseStack pose, float partialTick) {
        float f = 0.9375F;

        if (entity.isBaby()) {
            f *= 0.5F;
        }

        pose.scale(f, f, f);
    }
}
