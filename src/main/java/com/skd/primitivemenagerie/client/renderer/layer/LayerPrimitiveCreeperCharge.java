// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer.layer;

import com.skd.primitivemenagerie.client.models.ModelPrimitiveCreeper;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.entity.monster.PrimitiveCreeper;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Charged creeper aura layer, ported from {@code LayerPrimitiveCreeperCharge}. It reuses the mod's
 * creeper geometry with the vanilla creeper armour texture.
 *
 * @author Daveyx0
 */
public class LayerPrimitiveCreeperCharge extends EnergySwirlLayer<PrimitiveCreeper, ModelPrimitiveCreeper> {

    private static final ResourceLocation POWER_LOCATION =
            ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");

    private final ModelPrimitiveCreeper model;

    public LayerPrimitiveCreeperCharge(RenderLayerParent<PrimitiveCreeper, ModelPrimitiveCreeper> parent,
            EntityModelSet modelSet) {
        super(parent);
        this.model = new ModelPrimitiveCreeper(modelSet.bakeLayer(PMModelLayers.PRIMITIVE_CREEPER));
    }

    @Override
    protected float xOffset(float tick) {
        return tick * 0.01F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    @Override
    protected EntityModel<PrimitiveCreeper> model() {
        return this.model;
    }
}
