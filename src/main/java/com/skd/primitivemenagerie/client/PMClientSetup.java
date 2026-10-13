// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client;

import com.skd.primitivemenagerie.client.models.ModelBlazingJuggernaut;
import com.skd.primitivemenagerie.client.models.ModelBrainSlime;
import com.skd.primitivemenagerie.client.models.ModelChameleon;
import com.skd.primitivemenagerie.client.models.ModelDodo;
import com.skd.primitivemenagerie.client.models.ModelEmpty;
import com.skd.primitivemenagerie.client.models.ModelEnchantedBook;
import com.skd.primitivemenagerie.client.models.ModelFilchLizard;
import com.skd.primitivemenagerie.client.models.ModelFlameSpewer;
import com.skd.primitivemenagerie.client.models.ModelGoblin;
import com.skd.primitivemenagerie.client.models.ModelGroveSprite;
import com.skd.primitivemenagerie.client.models.ModelHarpy;
import com.skd.primitivemenagerie.client.models.ModelLilyLurker;
import com.skd.primitivemenagerie.client.models.ModelLostMiner;
import com.skd.primitivemenagerie.client.models.ModelMimic;
import com.skd.primitivemenagerie.client.models.ModelPrimitiveCreeper;
import com.skd.primitivemenagerie.client.models.ModelSheepman;
import com.skd.primitivemenagerie.client.models.ModelTravelingMerchant;
import com.skd.primitivemenagerie.client.models.ModelTreasureSlime;
import com.skd.primitivemenagerie.client.models.ModelTrollager;
import com.skd.primitivemenagerie.client.models.ModelVoidEye;
import com.skd.primitivemenagerie.client.models.ModelYeti;
import com.skd.primitivemenagerie.client.models.PMModelLayers;
import com.skd.primitivemenagerie.client.renderer.RenderBabySpider;
import com.skd.primitivemenagerie.client.renderer.RenderBrainSlime;
import com.skd.primitivemenagerie.client.renderer.RenderEnchantedBook;
import com.skd.primitivemenagerie.client.renderer.RenderFlyingItem;
import com.skd.primitivemenagerie.client.renderer.RenderGoblin;
import com.skd.primitivemenagerie.client.renderer.RenderHarpy;
import com.skd.primitivemenagerie.client.renderer.RenderHauntedTool;
import com.skd.primitivemenagerie.client.renderer.RenderMotherSpider;
import com.skd.primitivemenagerie.client.renderer.RenderPrimitiveCreeper;
import com.skd.primitivemenagerie.client.renderer.RenderPrimitiveTNTPrimed;
import com.skd.primitivemenagerie.client.renderer.RenderSkeletonWarrior;
import com.skd.primitivemenagerie.client.renderer.RenderThrownBlock;
import com.skd.primitivemenagerie.client.renderer.RenderTreasureSlime;
import com.skd.primitivemenagerie.core.PMEntities;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Client-only setup. Registers every {@code createBodyLayer()} of the mod through the
 * {@link EntityRenderersEvent.RegisterLayerDefinitions} event. This class must only be loaded on the
 * client; the mod constructor guards the call with {@code FMLEnvironment.dist == Dist.CLIENT}.
 *
 * @author Daveyx0
 */
public final class PMClientSetup {

    private PMClientSetup() {}

    /** Registers the client listeners on the mod event bus. */
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(PMClientSetup::onRegisterLayerDefinitions);
        modEventBus.addListener(PMClientSetup::onRegisterRenderers);
    }

    private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PMEntities.ROCKET_CREEPER.get(), RenderPrimitiveCreeper::new);
        event.registerEntityRenderer(PMEntities.FESTIVE_CREEPER.get(), RenderPrimitiveCreeper::new);
        event.registerEntityRenderer(PMEntities.SUPPORT_CREEPER.get(), RenderPrimitiveCreeper::new);
        event.registerEntityRenderer(PMEntities.SKELETON_WARRIOR.get(), RenderSkeletonWarrior::new);
        event.registerEntityRenderer(PMEntities.MOTHER_SPIDER.get(), RenderMotherSpider::new);
        event.registerEntityRenderer(PMEntities.BABY_SPIDER.get(), RenderBabySpider::new);
        event.registerEntityRenderer(PMEntities.GOBLIN.get(), RenderGoblin::new);
        event.registerEntityRenderer(PMEntities.HARPY.get(), RenderHarpy::new);
        event.registerEntityRenderer(PMEntities.TREASURE_SLIME.get(), RenderTreasureSlime::new);
        event.registerEntityRenderer(PMEntities.BRAIN_SLIME.get(), RenderBrainSlime::new);
        event.registerEntityRenderer(PMEntities.HAUNTED_TOOL.get(), RenderHauntedTool::new);
        event.registerEntityRenderer(PMEntities.BEWITCHED_TOME.get(), RenderEnchantedBook::new);
        event.registerEntityRenderer(PMEntities.SPIDER_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(PMEntities.PRIMITIVE_EGG.get(), RenderFlyingItem::new);
        event.registerEntityRenderer(PMEntities.THROWN_BLOCK.get(), RenderThrownBlock::new);
        event.registerEntityRenderer(PMEntities.PRIMITIVE_TNT_PRIMED.get(), RenderPrimitiveTNTPrimed::new);
        event.registerEntityRenderer(PMEntities.FLAME_SPIT.get(), context -> new ThrownItemRenderer<>(context, 0.75F, true));
    }

    private static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PMModelLayers.EMPTY, ModelEmpty::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.PRIMITIVE_CREEPER, ModelPrimitiveCreeper::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.BRAIN_SLIME, ModelBrainSlime::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.TREASURE_SLIME, ModelTreasureSlime::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.MIMIC, ModelMimic::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.ENCHANTED_BOOK, ModelEnchantedBook::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.VOID_EYE, ModelVoidEye::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.FLAME_SPEWER, ModelFlameSpewer::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.BLAZING_JUGGERNAUT, ModelBlazingJuggernaut::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.HARPY, ModelHarpy::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.LILY_LURKER, ModelLilyLurker::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.YETI, ModelYeti::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.GOBLIN, ModelGoblin::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.TROLLAGER, ModelTrollager::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.SHEEPMAN, ModelSheepman::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.GROVE_SPRITE, ModelGroveSprite::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.CHAMELEON, ModelChameleon::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.DODO, ModelDodo::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.FILCH_LIZARD, ModelFilchLizard::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.LOST_MINER, ModelLostMiner::createBodyLayer);
        event.registerLayerDefinition(PMModelLayers.TRAVELING_MERCHANT, ModelTravelingMerchant::createBodyLayer);
    }
}
