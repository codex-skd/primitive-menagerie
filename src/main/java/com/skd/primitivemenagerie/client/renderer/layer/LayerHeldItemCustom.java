// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import com.skd.primitivemenagerie.client.models.ModelTrollager;
import com.skd.primitivemenagerie.entity.monster.LilyLurker;
import com.skd.primitivemenagerie.entity.monster.SkeletonWarrior;
import com.skd.primitivemenagerie.entity.monster.Trollager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Bespoke held-item renderer, ported from {@code LayerHeldItemCustom}. Branches are lazily selected
 * by entity type. Ported here: Skeleton Warrior (back item), Lily Lurker (held lily pad) and Trollager
 * (the block it is about to throw). The Grove Sprite, Filch Lizard and Lost Miner branches belong to
 * later phases ({@code TODO}).
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
        if (entity instanceof SkeletonWarrior warrior) {
            ItemStack stack = warrior.getBackItem();

            if (stack.isEmpty() || !(this.getParentModel() instanceof HumanoidModel<?> humanoid)) {
                return;
            }

            pose.pushPose();
            humanoid.body.translateAndRotate(pose);
            pose.mulPose(Axis.YP.rotationDegrees(90.0F));
            pose.translate(0.1F, 0.15F, -0.1F);
            renderItem(stack, pose, buffer, entity, packedLight);
            pose.popPose();
            return;
        }

        if (entity instanceof LilyLurker lily) {
            ItemStack stack = lily.getMainHandItem();

            if (stack.isEmpty()) {
                return;
            }

            pose.pushPose();
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose.translate(-0.025F, -0.2F, -0.9F);
            pose.scale(2.0F, 2.0F, 2.0F);
            renderItem(stack, pose, buffer, entity, packedLight);
            pose.popPose();
            return;
        }

        if (entity instanceof Trollager troll) {
            BlockPos pos = troll.getThrownBlock();

            if (pos.equals(BlockPos.ZERO)) {
                return;
            }

            BlockState state = troll.level().getBlockState(pos);

            if (state.isAir()) {
                return;
            }

            if (this.getParentModel() instanceof ModelTrollager model) {
                pose.pushPose();
                pose.scale(1.5F, 1.5F, 1.5F);
                model.getBlockHolder().translateAndRotate(pose);
                pose.translate(0.5F, 0.5F, 0.0F);
                renderItem(new ItemStack(state.getBlock()), pose, buffer, entity, packedLight);
                pose.popPose();
            }
        }
    }

    private static void renderItem(ItemStack stack, PoseStack pose, MultiBufferSource buffer, LivingEntity entity,
            int packedLight) {
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                packedLight, OverlayTexture.NO_OVERLAY, pose, buffer, entity.level(), entity.getId());
    }
}
