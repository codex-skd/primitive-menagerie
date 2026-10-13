// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.skd.primitivemenagerie.client.models.ModelFlameSpewer;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerFlameSpewerEyes;
import com.skd.primitivemenagerie.client.renderer.layer.LayerLavaSkin;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.FlameSpewer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Flame Spewer renderer, ported from {@code RenderFlameSpewer}. Adds the lava-skin and glowing-eyes
 * layers.
 *
 * @author Daveyx0
 */
public class RenderFlameSpewer extends MobRenderer<FlameSpewer, ModelFlameSpewer> {

    private static final ResourceLocation TEXTURE = PMReference.id("textures/entity/flamespewer/flamespewer.png");

    public RenderFlameSpewer(EntityRendererProvider.Context context) {
        super(context, new ModelFlameSpewer(context.bakeLayer(PMModelLayers.FLAME_SPEWER)), 0.5F);
        this.addLayer(new LayerLavaSkin(this, context.getModelSet()));
        this.addLayer(new LayerFlameSpewerEyes(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(FlameSpewer entity) {
        return TEXTURE;
    }
}
