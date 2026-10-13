// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import com.skd.primitivemenagerie.client.models.ModelPrimitiveCreeper;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerPrimitiveCreeperCharge;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.FestiveCreeper;
import com.skd.primitivemenagerie.entity.monster.PrimitiveCreeper;
import com.skd.primitivemenagerie.entity.monster.RocketCreeper;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Renderer for every primitive creeper variant, ported from {@code RenderPrimitiveCreeper}. The
 * texture is picked by the concrete entity type and the swell/rocket transforms match the original.
 *
 * <p>Simplification: the original scaled the head/body of children separately; here baby creepers are
 * uniformly scaled, which is only relevant for the thrown-egg spawns added in P6.</p>
 *
 * @author Daveyx0
 */
public class RenderPrimitiveCreeper extends MobRenderer<PrimitiveCreeper, ModelPrimitiveCreeper> {

    private static final ResourceLocation ROCKET_TEXTURE = PMReference.id("textures/entity/rocketcreeper/rocketcreeper.png");
    private static final ResourceLocation FESTIVE_TEXTURE = PMReference.id("textures/entity/festivecreeper/festivecreeper.png");
    private static final ResourceLocation SUPPORT_TEXTURE = PMReference.id("textures/entity/supportcreeper/supportcreeper.png");

    public RenderPrimitiveCreeper(EntityRendererProvider.Context context) {
        super(context, new ModelPrimitiveCreeper(context.bakeLayer(PMModelLayers.PRIMITIVE_CREEPER)), 0.5F);
        this.addLayer(new LayerPrimitiveCreeperCharge(this, context.getModelSet()));
    }

    @Override
    protected void scale(PrimitiveCreeper creeper, PoseStack pose, float partialTick) {
        float f = creeper.getSwelling(partialTick);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        pose.scale(f2, f3, f2);

        if (creeper.isBaby()) {
            pose.scale(0.5F, 0.5F, 0.5F);
        }

        if (creeper instanceof RocketCreeper rocket && !rocket.onGround() && rocket.isRocket()) {
            double motionY = rocket.getDeltaMovement().y;

            if (motionY > 0.1D && motionY < 0.5D) {
                pose.mulPose(Axis.XN.rotationDegrees(25.0F));
            } else {
                pose.mulPose(Axis.XN.rotationDegrees((float) (motionY * 175D)));
            }
        }
    }

    @Override
    protected float getWhiteOverlayProgress(PrimitiveCreeper creeper, float partialTick) {
        float f = creeper.getSwelling(partialTick);
        return (int) (f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(PrimitiveCreeper creeper) {
        if (creeper instanceof FestiveCreeper) {
            return FESTIVE_TEXTURE;
        } else if (creeper instanceof RocketCreeper) {
            return ROCKET_TEXTURE;
        }

        return SUPPORT_TEXTURE;
    }
}
