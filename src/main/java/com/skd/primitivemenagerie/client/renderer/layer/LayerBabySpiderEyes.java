// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.BabySpider;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;

/**
 * Glowing eyes layer for the baby spider, ported from {@code LayerBabySpiderEyes}.
 *
 * <p>Simplification: the original tinted the eyes with the entity dye colour; the 1.21 eye layer
 * renders the emissive texture untinted. TODO(verify): restore the dye tinting if it is desired.</p>
 *
 * @author Daveyx0
 */
public class LayerBabySpiderEyes extends EyesLayer<BabySpider, SpiderModel<BabySpider>> {

    private static final RenderType SPIDER_EYES = RenderType.eyes(PMReference.id("textures/entity/spider_eyes.png"));

    public LayerBabySpiderEyes(RenderLayerParent<BabySpider, SpiderModel<BabySpider>> parent) {
        super(parent);
    }

    @Override
    public RenderType renderType() {
        return SPIDER_EYES;
    }
}
