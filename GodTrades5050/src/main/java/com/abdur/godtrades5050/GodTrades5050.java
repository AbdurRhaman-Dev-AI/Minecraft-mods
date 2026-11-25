package com.abdur.godtrades5050;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Main entrypoint for the GodTrades5050 mod.
 */
@Mod(GodTrades5050.MODID)
public class GodTrades5050 {

    public static final String MODID = "godtrades5050";

    public GodTrades5050() {
        NeoForge.EVENT_BUS.addListener(VillagerTradeHandler::onVillagerTrades);
    }
}
