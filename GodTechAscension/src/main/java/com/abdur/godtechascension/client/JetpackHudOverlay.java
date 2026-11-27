package com.abdur.godtechascension.client;

import com.abdur.godtechascension.item.AscensionJetpackItem;
import com.abdur.godtechascension.registry.ModDataComponents;
import com.abdur.godtechascension.registry.ModItems;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Car/bike-style digital dashboard HUD showing live speed (km/h),
 * odometer distance (km), and Blaze Powder fuel gauge while wearing the Ascension Jetpack.
 */
public final class JetpackHudOverlay {

    private JetpackHudOverlay() {
    }

    public static void render(GuiGraphics g, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui) {
            return;
        }

        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!chest.is(ModItems.ASCENSION_JETPACK.get())) {
            return;
        }

        ModDataComponents.JetpackData data = AscensionJetpackItem.getData(chest);
        int fuel = data.fuel();
        int pct = Math.round((fuel / (float) AscensionJetpackItem.MAX_FUEL) * 100.0F);
        double km = data.distanceMeters() / 1000.0D;

        // Live speed in km/h (1 block/tick = 20 m/s = 72 km/h)
        double dx = player.getX() - player.xo;
        double dy = player.getY() - player.yo;
        double dz = player.getZ() - player.zo;
        double speedKmh = Math.sqrt(dx * dx + dy * dy + dz * dz) * 20.0D * 3.6D;

        int w = 132;
        int h = 50;
        int x = g.guiWidth() - w - 10;
        int y = g.guiHeight() - h - 10;

        int borderColor = fuel <= 0 ? 0xFFFF4444 : (pct <= 20 ? 0xFFFFAA00 : 0xFF39C5E8);
        g.fill(x, y, x + w, y + h, 0xCC10151E);
        g.renderOutline(x, y, w, h, borderColor);

        Font font = mc.font;
        // Speedometer
        String speedText = String.format("%.1f km/h", speedKmh);
        g.drawString(font, speedText, x + 8, y + 6, 0xFFFFFF, false);

        // Odometer
        String odoText = String.format("ODO %.2f km", km);
        int odoW = font.width(odoText);
        g.drawString(font, odoText, x + w - odoW - 8, y + 6, 0x55FFFF, false);

        // Fuel label + Blaze Cake icon
        Item blazeCakeItem = AscensionJetpackItem.getBlazeCakeItem();
        g.renderItem(new ItemStack(blazeCakeItem != Items.AIR ? blazeCakeItem : Items.BLAZE_POWDER), x + 6, y + 19);
        int mins = fuel / 1200;
        int secs = (fuel % 1200) / 20;
        String fuelText = fuel <= 0 ? "EMPTY (Blaze Cake)" : String.format("%dm %02ds (%d%%)", mins, secs, pct);
        g.drawString(font, fuelText, x + 26, y + 23, borderColor, false);

        // Fuel bar
        int barX = x + 8;
        int barY = y + 38;
        int barW = w - 16;
        g.fill(barX, barY, barX + barW, barY + 6, 0xFF0A0D12);
        g.renderOutline(barX, barY, barW, 6, 0xFF2C4D63);
        int fillW = (int) ((fuel / (float) AscensionJetpackItem.MAX_FUEL) * (barW - 2));
        if (fillW > 0) {
            g.fill(barX + 1, barY + 1, barX + 1 + fillW, barY + 5, borderColor);
        }
    }
}
