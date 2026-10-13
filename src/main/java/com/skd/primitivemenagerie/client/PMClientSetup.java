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
