// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import java.util.Calendar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import com.skd.primitivemenagerie.client.models.ModelMimic;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerMimicMouth;
import com.skd.primitivemenagerie.entity.monster.Mimic;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Mimic renderer, ported from {@code RenderMimic}. It uses the vanilla chest textures (with the
 * christmas variant in late December) and tilts while airborne.
 *
 * @author Daveyx0
 */
public class RenderMimic extends MobRenderer<Mimic, ModelMimic> {

    private static final ResourceLocation CHEST_TEXTURES =
            ResourceLocation.withDefaultNamespace("textures/entity/chest/normal.png");
    private static final ResourceLocation CHEST_CHRISTMAS_TEXTURES =
            ResourceLocation.withDefaultNamespace("textures/entity/chest/christmas.png");

    private final boolean isChristmas;

    public RenderMimic(EntityRendererProvider.Context context) {
        super(context, new ModelMimic(context.bakeLayer(PMModelLayers.MIMIC)), 0.45F);
        this.addLayer(new LayerMimicMouth(this, context.getModelSet()));

        Calendar calendar = Calendar.getInstance();
        this.isChristmas = calendar.get(Calendar.MONTH) + 1 == 12
                && calendar.get(Calendar.DAY_OF_MONTH) >= 24
                && calendar.get(Calendar.DAY_OF_MONTH) <= 26;
    }

    @Override
    protected float getBob(Mimic mimic, float partialTick) {
        float f1 = mimic.nomminge + (mimic.nommingb - mimic.nomminge) * partialTick;
        float f2 = mimic.nommingd + (mimic.nommingc - mimic.nommingd) * partialTick;
        return (Mth.sin(f1) + 0.2F) * f2 + mimic.rotation;
    }

    @Override
    protected void setupRotations(Mimic entity, PoseStack pose, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, pose, bob, yBodyRot, partialTick, scale);

        if (!entity.onGround()) {
            if (entity.getDeltaMovement().y > 0.1D && entity.getDeltaMovement().y < 0.5D) {
                pose.mulPose(Axis.XP.rotationDegrees(-25.0F));
            } else {
                pose.mulPose(Axis.XP.rotationDegrees((float) (entity.getDeltaMovement().y * 70.0D)));
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Mimic entity) {
        return this.isChristmas ? CHEST_CHRISTMAS_TEXTURES : CHEST_TEXTURES;
    }
}
