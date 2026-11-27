package com.abdur.godtechascension.client;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.compat.create.CreateClientCompat;
import com.abdur.godtechascension.registry.ModMenus;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/**
 * Registers client-only menu screens, HUD overlays, and Create shaft renderers for GodTech: Ascension.
 */
@EventBusSubscriber(modid = GodTechAscension.MODID, value = Dist.CLIENT)
public final class GodTechClientSetup {

    private GodTechClientSetup() {
    }

    @SubscribeEvent
    public static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.ALLOY_SYNTHESIZER.get(), AlloySynthesizerScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (ModList.get().isLoaded("create")) {
            CreateClientCompat.registerRenderers(event);
        }
    }

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
                VanillaGuiLayers.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(GodTechAscension.MODID, "jetpack_hud"),
                JetpackHudOverlay::render
        );
    }
}
