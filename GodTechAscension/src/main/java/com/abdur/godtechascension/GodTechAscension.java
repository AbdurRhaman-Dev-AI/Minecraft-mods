package com.abdur.godtechascension;

import com.abdur.godtechascension.registry.ModBlocks;
import com.abdur.godtechascension.registry.ModCreativeTabs;
import com.abdur.godtechascension.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * Main entrypoint for GodTech: Ascension (NeoForge 1.21.1).
 */
@Mod(GodTechAscension.MODID)
public class GodTechAscension {

    public static final String MODID = "godtechascension";

    public GodTechAscension(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }
}
