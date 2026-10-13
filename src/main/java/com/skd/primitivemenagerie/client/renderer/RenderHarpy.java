// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.skd.primitivemenagerie.client.models.ModelHarpy;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.Harpy;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Harpy renderer, ported from {@code RenderHarpy}. The bob factor is the custom wing flap used by the
 * original model.
 *
 * @author Daveyx0
 */
public class RenderHarpy extends MobRenderer<Harpy, ModelHarpy> {

    private static final ResourceLocation HARPY_TEXTURE = PMReference.id("textures/entity/harpy/harpy.png");

    public RenderHarpy(EntityRendererProvider.Context context) {
        super(context, new ModelHarpy(context.bakeLayer(PMModelLayers.HARPY)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Harpy entity) {
        return HARPY_TEXTURE;
    }

    @Override
    protected float getBob(Harpy harpy, float partialTick) {
        float flap = harpy.oFlap + (harpy.flap - harpy.oFlap) * partialTick;
        float flapSpeed = harpy.oFlapSpeed + (harpy.flapSpeed - harpy.oFlapSpeed) * partialTick;
        return (Mth.sin(flap) + 1.0F) * flapSpeed;
    }
}
