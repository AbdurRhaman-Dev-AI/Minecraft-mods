package com.abdur.godtechascension.client;

import com.abdur.godtechascension.menu.AlloySynthesizerMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * Clean sci-fi container screen for the Alloy Synthesizer (drawn procedurally, no external texture required).
 */
public class AlloySynthesizerScreen extends AbstractContainerScreen<AlloySynthesizerMenu> {

    public AlloySynthesizerScreen(AlloySynthesizerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        // Main dark sci-fi panel
        g.fill(x, y, x + imageWidth, y + imageHeight, 0xFF1A202C);
        g.renderOutline(x, y, imageWidth, imageHeight, 0xFF39C5E8);

        // Energy bar background (x+14, y+18, 12x52)
        int barX = x + 14;
        int barY = y + 18;
        int barH = 52;
        g.fill(barX, barY, barX + 12, barY + barH, 0xFF0C1017);
        g.renderOutline(barX, barY, 12, barH, 0xFF2C5A73);

        int energy = menu.getEnergyStored();
        int maxEnergy = menu.getMaxEnergy();
        int filled = (int) ((energy / (float) maxEnergy) * (barH - 2));
        if (filled > 0) {
            g.fill(barX + 1, barY + barH - 1 - filled, barX + 11, barY + barH - 1, 0xFF39C5E8);
        }

        // Slots (input 44,35 & 66,35; output 124,35)
        drawSlotBox(g, x + 43, y + 34);
        drawSlotBox(g, x + 65, y + 34);
        drawSlotBox(g, x + 123, y + 34);

        // Progress bar (x+88, y+39, 28x8)
        int progX = x + 88;
        int progY = y + 39;
        g.fill(progX, progY, progX + 28, progY + 8, 0xFF0C1017);
        g.renderOutline(progX, progY, 28, 8, 0xFF2C5A73);
        int progW = (int) ((menu.getProgress() / (float) menu.getMaxProgress()) * 26);
        if (progW > 0) {
            g.fill(progX + 1, progY + 1, progX + 1 + progW, progY + 7, 0xFF55FF55);
        }

        // Player inventory area background
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                drawSlotBox(g, x + 7 + col * 18, y + 83 + row * 18);
            }
        }
        for (int col = 0; col < 9; col++) {
            drawSlotBox(g, x + 7 + col * 18, y + 141);
        }
    }

    private static void drawSlotBox(GuiGraphics g, int x, int y) {
        g.fill(x, y, x + 18, y + 18, 0xFF0E131A);
        g.renderOutline(x, y, 18, 18, 0xFF2D3A4B);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        this.renderTooltip(g, mouseX, mouseY);

        // Energy bar tooltip
        if (isHovering(14, 18, 12, 52, mouseX, mouseY)) {
            g.renderTooltip(
                    this.font,
                    Component.literal(String.format("%,d / %,d FE", menu.getEnergyStored(), menu.getMaxEnergy()))
                            .withStyle(ChatFormatting.AQUA),
                    mouseX,
                    mouseY
            );
        }
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(this.font, this.title, 34, 6, 0x39C5E8, false);
        g.drawString(this.font, this.playerInventoryTitle, 8, 73, 0xA0AEC0, false);
    }
}
