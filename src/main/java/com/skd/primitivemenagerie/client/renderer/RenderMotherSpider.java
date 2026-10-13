// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.skd.primitivemenagerie.client.renderer.layer.LayerPrimitiveSpiderEyes;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.MotherSpider;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Mother spider renderer, ported from {@code RenderMotherSpider}. Uses the vanilla spider model with
 * the mod texture and glowing eyes.
 *
 * @author Daveyx0
 */
public class RenderMotherSpider extends MobRenderer<MotherSpider, SpiderModel<MotherSpider>> {

    private static final ResourceLocation MOTHER_SPIDER_TEXTURE = PMReference.id("textures/entity/spiderfamily/motherspider.png");

    public RenderMotherSpider(EntityRendererProvider.Context context) {
        super(context, new SpiderModel<>(context.bakeLayer(ModelLayers.SPIDER)), 0.3F);
        this.addLayer(new LayerPrimitiveSpiderEyes<>(this));
    }

    @Override
    protected float getFlipDegrees(MotherSpider spider) {
        return 180.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(MotherSpider spider) {
        return MOTHER_SPIDER_TEXTURE;
    }
}
