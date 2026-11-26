package com.abdur.godtechascension.client;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.registry.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * Registers client-only menu screens for GodTech: Ascension.
 */
@EventBusSubscriber(modid = GodTechAscension.MODID, value = Dist.CLIENT)
public final class GodTechClientSetup {

    private GodTechClientSetup() {
    }

    @SubscribeEvent
    public static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.ALLOY_SYNTHESIZER.get(), AlloySynthesizerScreen::new);
    }
}
