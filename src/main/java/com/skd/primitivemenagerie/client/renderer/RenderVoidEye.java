// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.skd.primitivemenagerie.client.models.ModelVoidEye;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerVoidEyeSeen;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.VoidEye;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * Void Eye renderer, ported from {@code RenderVoidEye}. Besides the model it draws the gaze beam
 * towards the targeted entity. The original used a rotating multi-segment beam with additive
 * blending; here it is drawn as a simpler two-plane translucent beam ({@code TODO(verify)}).
 *
 * @author Daveyx0
 */
public class RenderVoidEye extends MobRenderer<VoidEye, ModelVoidEye> {

    private static final ResourceLocation TEXTURE = PMReference.id("textures/entity/voideye/voideye.png");
    private static final ResourceLocation BEAM_TEXTURE = PMReference.id("textures/entity/voideye/voideye_beam.png");
    private static final ResourceLocation BEAM_WEAK_TEXTURE = PMReference.id("textures/entity/voideye/voideye_beam_weak.png");

    public RenderVoidEye(EntityRendererProvider.Context context) {
        super(context, new ModelVoidEye(context.bakeLayer(PMModelLayers.VOID_EYE)), 0.5F);
        this.addLayer(new LayerVoidEyeSeen(this, context.getModelSet()));
    }

    @Override
    public void render(VoidEye entity, float entityYaw, float partialTick, PoseStack pose, MultiBufferSource buffer,
            int packedLight) {
        super.render(entity, entityYaw, partialTick, pose, buffer, packedLight);

        LivingEntity target = entity.getTargetedEntity();

        if (target == null) {
            return;
        }

        float scale = entity.getAttackAnimationScale(partialTick);
        Vec3 entityPos = entity.getPosition(partialTick);
        Vec3 targetPos = target.getPosition(partialTick).add(0.0D, target.getBbHeight() * 0.5D, 0.0D);
        Vec3 relative = targetPos.subtract(entityPos);
        double length = relative.length();

        if (length < 1.0E-4D) {
            return;
        }

        Vec3 direction = relative.normalize();
        Vec3 side1 = direction.cross(new Vec3(0.0D, 1.0D, 0.0D));

        if (side1.lengthSqr() < 1.0E-6D) {
            side1 = new Vec3(1.0D, 0.0D, 0.0D);
        }

        side1 = side1.normalize();
        Vec3 side2 = direction.cross(side1).normalize();

        float f7 = scale * scale;
        int red = 64 + (int) (f7 * 191.0F);
        int green = 32 + (int) (f7 * 191.0F);
        int blue = 128 - (int) (f7 * 128.0F);

        Vec3 start = new Vec3(0.0D, entity.getEyeHeight(), 0.0D);
        Vec3 end = start.add(relative);

        ResourceLocation texture = entity.canSeeTarget() ? BEAM_TEXTURE : BEAM_WEAK_TEXTURE;
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(texture));

        pose.pushPose();
        Matrix4f matrix = pose.last().pose();
        drawBeamQuad(vertexConsumer, matrix, start, end, side1, 0.282F, red, green, blue);
        drawBeamQuad(vertexConsumer, matrix, start, end, side2, 0.282F, red, green, blue);
        pose.popPose();
    }

    private static void drawBeamQuad(VertexConsumer consumer, Matrix4f matrix, Vec3 start, Vec3 end, Vec3 side,
            float width, int red, int green, int blue) {
        Vec3 offset = side.scale(width);
        Vec3 a1 = start.add(offset);
        Vec3 a2 = start.subtract(offset);
        Vec3 b1 = end.add(offset);
        Vec3 b2 = end.subtract(offset);

        vertex(consumer, matrix, a1, 0.0F, 0.0F, red, green, blue);
        vertex(consumer, matrix, b1, 0.0F, 1.0F, red, green, blue);
        vertex(consumer, matrix, b2, 1.0F, 1.0F, red, green, blue);
        vertex(consumer, matrix, a2, 1.0F, 0.0F, red, green, blue);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix, Vec3 pos, float u, float v, int red, int green,
            int blue) {
        consumer.addVertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z)
                .setColor(red, green, blue, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(VoidEye entity) {
        return TEXTURE;
    }
}
