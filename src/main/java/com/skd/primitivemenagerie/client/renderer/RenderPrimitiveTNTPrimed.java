// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import com.skd.primitivemenagerie.entity.item.PrimitiveTNTPrimed;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

/**
 * Primed primitive TNT renderer, ported from {@code RenderPrimitiveTNTPrimed}. It scales up as the
 * fuse runs out and shrinks the smaller (strength &lt; 3) charge.
 *
 * <p>Simplification: the flickering white overlay of the original is not ported ({@code TODO(verify)}).</p>
 *
 * @author Daveyx0
 */
public class RenderPrimitiveTNTPrimed extends EntityRenderer<PrimitiveTNTPrimed> {

    private final BlockRenderDispatcher dispatcher;

    public RenderPrimitiveTNTPrimed(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(PrimitiveTNTPrimed entity, float entityYaw, float partialTick, PoseStack pose,
            MultiBufferSource buffer, int packedLight) {
        pose.pushPose();
        pose.translate(0.0F, 0.5F, 0.0F);

        float remaining = (float) entity.getFuse() - partialTick + 1.0F;

        if (remaining < 10.0F) {
            float f = 1.0F - remaining / 10.0F;
            f = Mth.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            pose.scale(f1, f1, f1);
        }

        pose.mulPose(Axis.YP.rotationDegrees(-90.0F));
        pose.translate(-0.5F, -0.5F, 0.5F);

        if (entity.getStrength() < 3.0F) {
            pose.scale(0.5F, 0.5F, 0.5F);
        }

        BlockState state = Blocks.TNT.defaultBlockState();
        Level level = entity.level();
        BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
        var model = this.dispatcher.getBlockModel(state);

        for (var renderType : model.getRenderTypes(state, RandomSource.create(state.getSeed(blockpos)), ModelData.EMPTY)) {
            this.dispatcher.getModelRenderer().tesselateBlock(level, model, state, blockpos, pose,
                    buffer.getBuffer(net.neoforged.neoforge.client.RenderTypeHelper.getMovingBlockRenderType(renderType)),
                    false, RandomSource.create(), state.getSeed(blockpos), OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY, renderType);
        }

        pose.popPose();
        super.render(entity, entityYaw, partialTick, pose, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PrimitiveTNTPrimed entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
