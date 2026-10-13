// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.client.models.ModelTreasureSlime;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerHeldItemSlime;
import com.skd.primitivemenagerie.client.renderer.layer.LayerTreasureSlimeGel;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.TreasureSlime;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Treasure Slime renderer, ported from {@code RenderTreasureSlime}. The main model draws the eyes and
 * mouth; the coloured gel shell is a render layer and the held item is drawn by another layer.
 *
 * <p>Simplification: the per-item enchanted glint of the original layer is not ported
 * ({@code TODO(verify)}).</p>
 *
 * @author Daveyx0
 */
public class RenderTreasureSlime extends MobRenderer<TreasureSlime, ModelTreasureSlime> {

    private static final ResourceLocation SLIME_TEXTURES = PMReference.id("textures/entity/treasureslime/slime_treasure.png");

    public RenderTreasureSlime(EntityRendererProvider.Context context) {
        super(context, new ModelTreasureSlime(context.bakeLayer(PMModelLayers.TREASURE_SLIME)), 0.25F);
        this.getModel().root().getChild("cube").visible = false;
        this.addLayer(new LayerHeldItemSlime(this));
        this.addLayer(new LayerTreasureSlimeGel(this, context.getModelSet()));
    }

    @Override
    protected float getShadowRadius(TreasureSlime entity) {
        return 0.25F * (float) entity.getSize();
    }

    @Override
    protected void scale(TreasureSlime entity, PoseStack pose, float partialTick) {
        pose.scale(0.999F, 0.999F, 0.999F);

        float size = (float) entity.getSize();
        float squish = Mth.lerp(partialTick, entity.oSquish, entity.squish) / (size * 0.5F + 1.0F);
        float inverse = 1.0F / (squish + 1.0F);
        pose.scale(inverse * size, 1.0F / inverse * size, inverse * size);
    }

    @Override
    public ResourceLocation getTextureLocation(TreasureSlime entity) {
        return SLIME_TEXTURES;
    }
}
