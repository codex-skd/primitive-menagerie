// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.client.models.ModelBrainSlime;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerBrainSlimeGel;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.BrainSlime;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Brain Slime renderer, ported from {@code RenderBrainSlime}. The inner body/eyes come from the main
 * model; the translucent gel shell is a render layer.
 *
 * @author Daveyx0
 */
public class RenderBrainSlime extends MobRenderer<BrainSlime, ModelBrainSlime> {

    private static final ResourceLocation SLIME_TEXTURES = PMReference.id("textures/entity/brainslime/slime_brain.png");

    public RenderBrainSlime(EntityRendererProvider.Context context) {
        super(context, new ModelBrainSlime(context.bakeLayer(PMModelLayers.BRAIN_SLIME)), 0.25F);
        this.getModel().root().getChild("cube").visible = false;
        this.addLayer(new LayerBrainSlimeGel(this, context.getModelSet()));
    }

    @Override
    protected float getShadowRadius(BrainSlime entity) {
        return 0.25F * (float) entity.getSize();
    }

    @Override
    protected void scale(BrainSlime entity, PoseStack pose, float partialTick) {
        pose.scale(0.999F, 0.999F, 0.999F);
        pose.translate(0.0F, 0.001F, 0.0F);

        float size = (float) entity.getSize();

        if (entity.getSaturation() >= 10) {
            size += (entity.getSaturation() - 10.0F) / 90.0F;
        }

        float squish = Mth.lerp(partialTick, entity.oSquish, entity.squish) / (size * 0.5F + 1.0F);
        float inverse = 1.0F / (squish + 1.0F);
        float f4 = entity.suckinge + (entity.suckingb - entity.suckinge) * partialTick;
        float f5 = entity.suckingd + (entity.suckingc - entity.suckingd) * partialTick;
        float f6 = (Mth.sin(f4) + 0.5F) * f5 * 1.5F;
        pose.scale(inverse * size, 1.0F / inverse * size + f6, inverse * size);
    }

    @Override
    public ResourceLocation getTextureLocation(BrainSlime entity) {
        return SLIME_TEXTURES;
    }
}
