// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.skd.primitivemenagerie.client.renderer.layer.LayerHeldItemCustom;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.SkeletonWarrior;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Skeleton Warrior renderer, ported from {@code RenderSkeletonWarrior}. Uses the vanilla skeleton
 * model and armour layers with the mod texture.
 *
 * @author Daveyx0
 */
public class RenderSkeletonWarrior extends HumanoidMobRenderer<SkeletonWarrior, SkeletonModel<SkeletonWarrior>> {

    private static final ResourceLocation SKELETON_WARRIOR_TEXTURE =
            PMReference.id("textures/entity/skeletonwarrior/skeletonwarrior.png");

    public RenderSkeletonWarrior(EntityRendererProvider.Context context) {
        super(context, new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this,
                new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON_INNER_ARMOR)),
                new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON_OUTER_ARMOR)),
                context.getModelManager()));
        this.addLayer(new LayerHeldItemCustom<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SkeletonWarrior entity) {
        return SKELETON_WARRIOR_TEXTURE;
    }

    @Override
    protected boolean isShaking(SkeletonWarrior entity) {
        return entity.isShaking();
    }
}
