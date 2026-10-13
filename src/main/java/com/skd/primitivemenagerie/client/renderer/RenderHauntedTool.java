// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.skd.primitivemenagerie.client.models.ModelEmpty;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.layer.LayerHeldItemHaunted;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.HauntedTool;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Haunted Tool renderer, ported from {@code RenderHauntedTool}. The tool itself is drawn by
 * {@link LayerHeldItemHaunted}; the model is empty and the texture is a fake icon texture used by
 * map mods.
 *
 * @author Daveyx0
 */
public class RenderHauntedTool extends MobRenderer<HauntedTool, ModelEmpty> {

    private static final ResourceLocation FAKE_TEXTURE = PMReference.id("textures/entity/mimic/haunted_tool.png");

    public RenderHauntedTool(EntityRendererProvider.Context context) {
        super(context, new ModelEmpty(context.bakeLayer(PMModelLayers.EMPTY)), 0.2F);
        this.addLayer(new LayerHeldItemHaunted(this));
    }

    @Override
    public ResourceLocation getTextureLocation(HauntedTool entity) {
        return FAKE_TEXTURE;
    }
}
