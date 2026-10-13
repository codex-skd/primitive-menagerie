package com.skd.primitivemenagerie;

import com.mojang.logging.LogUtils;
import com.skd.primitivemenagerie.config.PMConfig;
import com.skd.primitivemenagerie.core.PMRegistries;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(PrimitiveMenagerie.MODID)
public class PrimitiveMenagerie {
    public static final String MODID = "primitive_menagerie";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PrimitiveMenagerie(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, PMConfig.SPEC);

        PMRegistries.register(modEventBus);
        com.skd.primitivemenagerie.network.PMNetwork.register(modEventBus);

        com.skd.primitivemenagerie.core.PMSpawnRegistry.register();
        com.skd.primitivemenagerie.core.PMTameableEntries.registerTameables();
        com.skd.primitivemenagerie.core.PMEvents.register();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.skd.primitivemenagerie.client.PMClientSetup.register(modEventBus);
        }

        LOGGER.info("Primitive Menagerie loading");
    }
}
