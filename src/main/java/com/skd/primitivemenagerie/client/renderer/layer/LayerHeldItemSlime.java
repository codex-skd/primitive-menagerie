// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import com.skd.primitivemenagerie.client.models.ModelTreasureSlime;
import com.skd.primitivemenagerie.entity.monster.TreasureSlime;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Renders the item held by the Treasure Slime, ported from {@code LayerHeldItemSlime}. The original
 * had a separate branch for "full 3D" items; in 1.21 that concept is gone, so tools and blocks are
 * distinguished only by the block branch.
 *
 * @author Daveyx0
 */
public class LayerHeldItemSlime extends RenderLayer<TreasureSlime, ModelTreasureSlime> {

    public LayerHeldItemSlime(RenderLayerParent<TreasureSlime, ModelTreasureSlime> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, TreasureSlime entity,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = entity.getMainHandItem();

        if (stack.isEmpty()) {
            return;
        }

        pose.pushPose();
        pose.mulPose(Axis.XP.rotationDegrees(180.0F));

        if (stack.getItem() instanceof BlockItem) {
            pose.translate(0.0F, -1.15F, 0.0F);
            pose.scale(0.525F, -0.525F, 0.525F);
        } else {
            pose.translate(0.0F, -1.35F, 0.0F);
            pose.scale(0.7F, 0.7F, 0.7F);
        }

        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                packedLight, OverlayTexture.NO_OVERLAY, pose, buffer, entity.level(), entity.getId());
        pose.popPose();
    }
}
