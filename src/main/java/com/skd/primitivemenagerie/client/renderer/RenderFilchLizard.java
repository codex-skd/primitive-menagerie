// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.skd.primitivemenagerie.client.models.ModelFilchLizard;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerHeldItemCustom;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.passive.FilchLizard;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Filch Lizard renderer, ported from {@code RenderFilchLizard}.
 *
 * @author Daveyx0
 */
public class RenderFilchLizard extends MobRenderer<FilchLizard, ModelFilchLizard> {

    private static final ResourceLocation FILCHLIZARD_TEXTURES = PMReference.id("textures/entity/filchlizard/filchlizard.png");

    public RenderFilchLizard(EntityRendererProvider.Context context) {
        super(context, new ModelFilchLizard(context.bakeLayer(PMModelLayers.FILCH_LIZARD)), 0.4F);
        this.addLayer(new LayerHeldItemCustom<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(FilchLizard entity) {
        return FILCHLIZARD_TEXTURES;
    }
}
