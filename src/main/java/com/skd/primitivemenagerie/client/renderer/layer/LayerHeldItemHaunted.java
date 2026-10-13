// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import com.skd.primitivemenagerie.client.models.ModelEmpty;
import com.skd.primitivemenagerie.entity.monster.HauntedTool;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Renders the tool floating in front of the Haunted Tool, ported from {@code LayerHeldItemHaunted}.
 *
 * @author Daveyx0
 */
public class LayerHeldItemHaunted extends RenderLayer<HauntedTool, ModelEmpty> {

    public LayerHeldItemHaunted(RenderLayerParent<HauntedTool, ModelEmpty> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, HauntedTool entity,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = entity.getMainHandItem();

        if (stack.isEmpty()) {
            return;
        }

        float f1 = entity.floatinge + (entity.floatingb - entity.floatinge) * partialTick;
        float f2 = entity.floatingd + (entity.floatingc - entity.floatingd) * partialTick;
        float f3 = (Mth.sin(f1) + 0.5F) * f2 * 1.5F;
        float offsetY = f3 + 0.5F;

        pose.pushPose();
        pose.translate(0.0F, 0.0F, -0.75F);
        pose.translate(0.0F, offsetY - 1.0F, 0.0F);
        pose.mulPose(Axis.XP.rotationDegrees(180.0F));
        pose.mulPose(Axis.XP.rotationDegrees(45.0F));

        if (stack.getItem() instanceof BlockItem) {
            pose.translate(0.0F, 0.0625F, -0.25F);
            pose.scale(0.375F, -0.375F, 0.375F);
        } else {
            pose.translate(0.0F, -1.35F, 0.0F);
            pose.scale(0.7F, 0.7F, 0.7F);
        }

        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                packedLight, OverlayTexture.NO_OVERLAY, pose, buffer, entity.level(), entity.getId());
        pose.popPose();
    }
}
