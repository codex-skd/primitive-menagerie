// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import com.skd.primitivemenagerie.entity.monster.SkeletonWarrior;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Bespoke held-item renderer, ported from {@code LayerHeldItemCustom}. The original handled many mobs
 * lazily; only the Skeleton Warrior back-item branch is needed for this phase. The remaining branches
 * (Grove Sprite, Filch Lizard, Lily Lurker, Trollager, Lost Miner) are added with those mobs in later
 * phases ({@code TODO}).
 *
 * @author Daveyx0
 */
public class LayerHeldItemCustom<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public LayerHeldItemCustom(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, T entity,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(entity instanceof SkeletonWarrior warrior)) {
            return;
        }

        ItemStack stack = warrior.getBackItem();

        if (stack.isEmpty() || !(this.getParentModel() instanceof HumanoidModel<?> humanoid)) {
            return;
        }

        pose.pushPose();
        humanoid.body.translateAndRotate(pose);
        pose.mulPose(Axis.YP.rotationDegrees(90.0F));
        pose.translate(0.1F, 0.15F, -0.1F);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                packedLight, OverlayTexture.NO_OVERLAY, pose, buffer, entity.level(), entity.getId());
        pose.popPose();
    }
}
