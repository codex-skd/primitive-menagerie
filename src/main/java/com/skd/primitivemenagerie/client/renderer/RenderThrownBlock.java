// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.entity.item.ThrownBlock;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

/**
 * Thrown block renderer, ported from {@code RenderThownBlock}. The block state is read from the world
 * at the entity origin, matching the original.
 *
 * @author Daveyx0
 */
public class RenderThrownBlock extends EntityRenderer<ThrownBlock> {

    private final BlockRenderDispatcher dispatcher;

    public RenderThrownBlock(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(ThrownBlock entity, float entityYaw, float partialTick, PoseStack pose, MultiBufferSource buffer,
            int packedLight) {
        BlockState state = entity.level().getBlockState(entity.getOrigin());

        if (state.getRenderShape() == RenderShape.MODEL) {
            Level level = entity.level();

            if (state != level.getBlockState(entity.blockPosition())) {
                pose.pushPose();
                BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
                pose.translate(-0.5D, 0.0D, -0.5D);
                var model = this.dispatcher.getBlockModel(state);

                for (var renderType : model.getRenderTypes(state, RandomSource.create(state.getSeed(entity.getOrigin())),
                        ModelData.EMPTY)) {
                    this.dispatcher.getModelRenderer().tesselateBlock(level, model, state, blockpos, pose,
                            buffer.getBuffer(net.neoforged.neoforge.client.RenderTypeHelper.getMovingBlockRenderType(renderType)),
                            false, RandomSource.create(), state.getSeed(entity.getOrigin()), OverlayTexture.NO_OVERLAY,
                            ModelData.EMPTY, renderType);
                }

                pose.popPose();
                super.render(entity, entityYaw, partialTick, pose, buffer, packedLight);
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownBlock entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
