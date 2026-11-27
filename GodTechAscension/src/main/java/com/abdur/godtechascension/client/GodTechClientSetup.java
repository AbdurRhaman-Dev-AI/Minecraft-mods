package com.abdur.godtechascension.client;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.compat.create.CreateClientCompat;
import com.abdur.godtechascension.registry.ModMenus;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/**
 * Registers client-only menu screens, HUD overlays, 3D Jetpack layers, and Create renderers.
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
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(JetpackModel.LAYER_LOCATION, JetpackModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            var renderer = event.getSkin(skin);
            if (renderer instanceof LivingEntityRenderer<?, ?> living) {
                @SuppressWarnings("unchecked")
                var playerRenderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) living;
                playerRenderer.addLayer(new JetpackBackLayer<>(playerRenderer, event.getEntityModels()));
            }
        }

        var stand = event.getRenderer(EntityType.ARMOR_STAND);
        if (stand instanceof LivingEntityRenderer<?, ?> living) {
            @SuppressWarnings("unchecked")
            var standRenderer = (LivingEntityRenderer<ArmorStand, ArmorStandArmorModel>) living;
            standRenderer.addLayer(new JetpackBackLayer<>(standRenderer, event.getEntityModels()));
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
