// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.client.models.ModelTrollager;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerHeldItemCustom;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.Trollager;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Trollager renderer, ported from {@code RenderTrollager}. The petrified (stone) form uses a separate
 * texture and the model is drawn at double scale.
 *
 * @author Daveyx0
 */
public class RenderTrollager extends MobRenderer<Trollager, ModelTrollager> {

    private static final ResourceLocation TEXTURE = PMReference.id("textures/entity/troll/troll.png");
    private static final ResourceLocation STONED_TEXTURE = PMReference.id("textures/entity/troll/troll_stoned.png");

    public RenderTrollager(EntityRendererProvider.Context context) {
        super(context, new ModelTrollager(context.bakeLayer(PMModelLayers.TROLLAGER)), 2.35F);
        this.addLayer(new LayerHeldItemCustom<>(this));
    }

    @Override
    protected void scale(Trollager entity, PoseStack pose, float partialTick) {
        pose.scale(2.0F, 2.0F, 2.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Trollager entity) {
        return entity.isStone() ? STONED_TEXTURE : TEXTURE;
    }
}
