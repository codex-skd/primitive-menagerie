// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.skd.primitivemenagerie.client.models.ModelBlazingJuggernaut;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.BlazingJuggernaut;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Blazing Juggernaut renderer, ported from {@code RenderBlazingJuggernaut}.
 *
 * @author Daveyx0
 */
public class RenderBlazingJuggernaut extends MobRenderer<BlazingJuggernaut, ModelBlazingJuggernaut> {

    private static final ResourceLocation TEXTURE = PMReference.id("textures/entity/blazingjuggernaut/blazingjuggernaut.png");

    public RenderBlazingJuggernaut(EntityRendererProvider.Context context) {
        super(context, new ModelBlazingJuggernaut(context.bakeLayer(PMModelLayers.BLAZING_JUGGERNAUT)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(BlazingJuggernaut entity) {
        return TEXTURE;
    }
}
