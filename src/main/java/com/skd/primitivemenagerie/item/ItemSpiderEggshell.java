// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.item;

import com.skd.primitivemenagerie.core.PMItems;
import com.skd.primitivemenagerie.entity.monster.BabySpider;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Spider eggshell, ported from the anonymous {@code ItemPrimitive} of {@code PrimitiveMobsItems}.
 * Using it on a spiderling (growth level 0) kills the spiderling and drops a spider egg.
 *
 * @author Daveyx0
 */
public class ItemSpiderEggshell extends ItemPrimitive {

    public ItemSpiderEggshell(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof BabySpider spider && hand == InteractionHand.MAIN_HAND) {
            target.hurt(target.damageSources().cramming(), 0.1F);

            if (spider.isAlive() && spider.getGrowthLevel() == 0) {
                if (!player.level().isClientSide) {
                    target.spawnAtLocation(new ItemStack(PMItems.SPIDER_EGG.get()));
                }

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                target.discard();
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
