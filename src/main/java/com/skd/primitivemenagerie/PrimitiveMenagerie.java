package com.skd.primitivemenagerie;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(PrimitiveMenagerie.MODID)
public class PrimitiveMenagerie {
    public static final String MODID = "primitive_menagerie";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PrimitiveMenagerie(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Primitive Menagerie loading");
    }
}
