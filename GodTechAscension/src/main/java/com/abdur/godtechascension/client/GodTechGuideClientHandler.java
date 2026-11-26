package com.abdur.godtechascension.client;

import com.abdur.godtechascension.GodTechAscension;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Adds Create-style "Hold [W] for Guide" tooltips and opens GodTechGuideScreen
 * when [W] is held while hovering any GodTech: Ascension item in inventory or JEI.
 */
@EventBusSubscriber(modid = GodTechAscension.MODID, value = Dist.CLIENT)
public final class GodTechGuideClientHandler {

    private static final long HOLD_DURATION_MS = 450L;

    private static Item hoveredItem;
    private static long lastTooltipMs;
    private static long holdStartMs;

    private GodTechGuideClientHandler() {
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (GodTechGuideData.get(item) == null) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null || mc.screen instanceof GodTechGuideScreen) {
            return;
        }

        hoveredItem = item;
        lastTooltipMs = Util.getMillis();

        boolean typingInSearch = mc.screen.getFocused() instanceof EditBox;
        boolean wDown = !typingInSearch
                && InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_W);

        if (wDown) {
            if (holdStartMs == 0L) {
                holdStartMs = lastTooltipMs;
            }
            float progress = Math.min(1.0F, (float) (lastTooltipMs - holdStartMs) / HOLD_DURATION_MS);
            event.getToolTip().add(buildProgressLine(progress));
        } else {
            holdStartMs = 0L;
            event.getToolTip().add(
                    Component.literal("Hold [")
                            .withStyle(ChatFormatting.DARK_GRAY)
                            .append(Component.literal("W").withStyle(ChatFormatting.AQUA))
                            .append(Component.literal("] for Guide").withStyle(ChatFormatting.DARK_GRAY))
            );
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (hoveredItem == null) {
            return;
        }

        long now = Util.getMillis();
        if (now - lastTooltipMs > 120L) {
            hoveredItem = null;
            holdStartMs = 0L;
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Screen currentScreen = mc.screen;
        if (currentScreen == null || currentScreen instanceof GodTechGuideScreen) {
            holdStartMs = 0L;
            return;
        }

        boolean typingInSearch = currentScreen.getFocused() instanceof EditBox;
        boolean wDown = !typingInSearch
                && InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_W);

        if (!wDown) {
            holdStartMs = 0L;
            return;
        }

        if (holdStartMs == 0L) {
            holdStartMs = now;
        } else if (now - holdStartMs >= HOLD_DURATION_MS) {
            Item target = hoveredItem;
            hoveredItem = null;
            holdStartMs = 0L;
            mc.setScreen(new GodTechGuideScreen(target, currentScreen));
        }
    }

    private static Component buildProgressLine(float progress) {
        int totalBars = 10;
        int filled = Math.round(progress * totalBars);
        String bar = "█".repeat(filled) + "░".repeat(totalBars - filled);
        return Component.literal("Hold [W] ")
                .withStyle(ChatFormatting.AQUA)
                .append(Component.literal(bar).withStyle(ChatFormatting.GOLD));
    }
}
