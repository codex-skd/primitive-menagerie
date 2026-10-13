// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.skd.primitivemenagerie.client.renderer.layer.LayerBabySpiderEyes;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.BabySpider;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Baby spider renderer, ported from {@code RenderBabySpider}. The model is the vanilla spider model
 * scaled by the growth level, and the texture switches to the adult spider one at full growth.
 *
 * @author Daveyx0
 */
public class RenderBabySpider extends MobRenderer<BabySpider, SpiderModel<BabySpider>> {

    private static final ResourceLocation BABY_SPIDER_TEXTURE = PMReference.id("textures/entity/spiderfamily/babyspider.png");
    private static final ResourceLocation SPIDER_TEXTURE = PMReference.id("textures/entity/spiderfamily/spider.png");

    public RenderBabySpider(EntityRendererProvider.Context context) {
        super(context, new SpiderModel<>(context.bakeLayer(ModelLayers.SPIDER)), 0.3F);
        this.addLayer(new LayerBabySpiderEyes(this));
    }

    @Override
    protected void scale(BabySpider spider, PoseStack pose, float partialTick) {
        float scale = switch (Mth.clamp(spider.getGrowthLevel(), 0, 5)) {
            case 0 -> 0.5F;
            case 1 -> 0.6F;
            case 2 -> 0.7F;
            case 3 -> 0.8F;
            case 4 -> 0.9F;
            default -> 1.0F;
        };
        pose.scale(scale, scale, scale);
    }

    @Override
    protected float getFlipDegrees(BabySpider spider) {
        return 180.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(BabySpider spider) {
        return spider.getGrowthLevel() >= 5 ? SPIDER_TEXTURE : BABY_SPIDER_TEXTURE;
    }
}
