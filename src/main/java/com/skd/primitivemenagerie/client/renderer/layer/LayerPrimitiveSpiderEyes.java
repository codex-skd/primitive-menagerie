// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.PrimitiveSpider;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;

/**
 * Glowing eyes layer for the primitive spiders, ported from {@code LayerPrimitiveSpiderEyes}.
 *
 * @author Daveyx0
 */
public class LayerPrimitiveSpiderEyes<T extends PrimitiveSpider, M extends SpiderModel<T>> extends EyesLayer<T, M> {

    private static final RenderType SPIDER_EYES = RenderType.eyes(PMReference.id("textures/entity/spider_eyes.png"));

    public LayerPrimitiveSpiderEyes(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public RenderType renderType() {
        return SPIDER_EYES;
    }
}
