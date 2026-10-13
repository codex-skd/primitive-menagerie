// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import com.skd.primitivemenagerie.client.models.ModelEnchantedBook;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.core.PMReference;
import com.skd.primitivemenagerie.entity.monster.EnchantedBook;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Bewitched Tome renderer, ported from {@code RenderEchantedBook}. It bobs up and down and is tilted
 * back by 30 degrees.
 *
 * @author Daveyx0
 */
public class RenderEnchantedBook extends MobRenderer<EnchantedBook, ModelEnchantedBook> {

    private static final ResourceLocation BOOK_TEXTURES = PMReference.id("textures/entity/enchantedbook/book.png");

    public RenderEnchantedBook(EntityRendererProvider.Context context) {
        super(context, new ModelEnchantedBook(context.bakeLayer(PMModelLayers.ENCHANTED_BOOK)), 0.4F);
    }

    @Override
    protected void scale(EnchantedBook entity, PoseStack pose, float partialTick) {
        float f1 = entity.floatinge + (entity.floatingb - entity.floatinge) * partialTick;
        float f2 = entity.floatingd + (entity.floatingc - entity.floatingd) * partialTick;
        float f3 = (Mth.sin(f1) + 0.5F) * f2 * 1.5F;
        float offsetY = f3 - 0.8F;
        pose.translate(0.0F, offsetY, 0.0F);
        pose.mulPose(Axis.XP.rotationDegrees(-30.0F));
    }

    @Override
    public ResourceLocation getTextureLocation(EnchantedBook entity) {
        return BOOK_TEXTURES;
    }
}
