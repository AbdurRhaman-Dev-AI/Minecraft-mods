package com.abdur.godtechascension.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Create-styled popup guide screen opened by holding [W] over any GodTech item.
 * Includes a live 60 FPS animated use-case preview stage alongside obtain/usage text.
 */
public class GodTechGuideScreen extends Screen {

    private static final int PANEL_WIDTH = 388;
    private static final int PANEL_HEIGHT = 236;

    private final Item targetItem;
    private final @Nullable Screen previousScreen;

    private int leftPos;
    private int topPos;

    public GodTechGuideScreen(Item targetItem, @Nullable Screen previousScreen) {
        super(targetItem.getDescription());
        this.targetItem = targetItem;
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - PANEL_WIDTH) / 2;
        this.topPos = (this.height - PANEL_HEIGHT) / 2;

        // Close button
        this.addRenderableWidget(
                Button.builder(Component.literal("X"), btn -> onClose())
                        .bounds(leftPos + PANEL_WIDTH - 24, topPos + 8, 16, 16)
                        .build()
        );

        // Related item navigation buttons
        GodTechGuideData.GuideEntry entry = GodTechGuideData.get(targetItem);
        if (entry != null && !entry.related().isEmpty()) {
            int startX = leftPos + 14;
            int btnY = topPos + PANEL_HEIGHT - 32;
            int btnW = 114;
            int gap = 8;
            int max = Math.min(3, entry.related().size());

            for (int i = 0; i < max; i++) {
                Item relatedItem = entry.related().get(i);
                String label = relatedItem.getDescription().getString();
                if (label.length() > 17) {
                    label = label.substring(0, 16) + "…";
                }
                this.addRenderableWidget(
                        Button.builder(
                                        Component.literal(label),
                                        btn -> {
                                            if (this.minecraft != null) {
                                                this.minecraft.setScreen(new GodTechGuideScreen(relatedItem, previousScreen));
                                            }
                                        }
                                )
                                .bounds(startX + i * (btnW + gap), btnY, btnW, 20)
                                .build()
                );
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render background blur/dim ONCE before drawing our sharp panel and text
        this.renderBlurredBackground(partialTick);
        this.renderTransparentBackground(graphics);

        // Dark Create-inspired panel background & crisp cyan/slate border
        graphics.fill(leftPos, topPos, leftPos + PANEL_WIDTH, topPos + PANEL_HEIGHT, 0xF0141820);
        graphics.renderOutline(leftPos, topPos, PANEL_WIDTH, PANEL_HEIGHT, 0xFF39C5E8);
        graphics.renderOutline(leftPos + 2, topPos + 2, PANEL_WIDTH - 4, PANEL_HEIGHT - 4, 0xFF28485C);

        // Header divider
        graphics.fill(leftPos + 10, topPos + 46, leftPos + PANEL_WIDTH - 10, topPos + 47, 0xFF2C4D63);

        // 2x scaled item icon
        graphics.pose().pushPose();
        graphics.pose().translate(leftPos + 14, topPos + 10, 0);
        graphics.pose().scale(2.0F, 2.0F, 1.0F);
        graphics.renderItem(new ItemStack(targetItem), 0, 0);
        graphics.pose().popPose();

        // Title & tier
        graphics.drawString(
                this.font,
                targetItem.getDescription().copy().withStyle(ChatFormatting.BOLD, ChatFormatting.WHITE),
                leftPos + 54,
                topPos + 12,
                0xFFFFFF
        );

        GodTechGuideData.GuideEntry entry = GodTechGuideData.get(targetItem);
        if (entry != null) {
            graphics.drawString(
                    this.font,
                    Component.literal(entry.tier()).withStyle(ChatFormatting.AQUA),
                    leftPos + 54,
                    topPos + 25,
                    0x55FFFF
            );

            // Left column: Obtain & Usage text
            int textX = leftPos + 14;
            int wrapWidth = 212;
            int y = topPos + 56;

            graphics.drawString(
                    this.font,
                    Component.literal("How to Obtain").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD),
                    textX,
                    y,
                    0xFFAA00
            );
            y += 12;
            for (FormattedCharSequence line : this.font.split(Component.literal(entry.obtain()), wrapWidth)) {
                graphics.drawString(this.font, line, textX, y, 0xE6EEF5);
                y += 11;
            }

            y += 10;

            graphics.drawString(
                    this.font,
                    Component.literal("How to Use").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD),
                    textX,
                    y,
                    0x55FF55
            );
            y += 12;
            for (FormattedCharSequence line : this.font.split(Component.literal(entry.usage()), wrapWidth)) {
                graphics.drawString(this.font, line, textX, y, 0xE6EEF5);
                y += 11;
            }

            // Right column: Live Animated Demonstration Stage
            int stageX = leftPos + 236;
            int stageY = topPos + 56;
            int stageW = 138;
            int stageH = 122;
            GodTechGuideAnimator.renderScene(graphics, this.font, targetItem, stageX, stageY, stageW, stageH);

            // Related section label
            if (!entry.related().isEmpty()) {
                graphics.drawString(
                        this.font,
                        Component.literal("Related Items (Click to View)").withStyle(ChatFormatting.GRAY),
                        textX,
                        topPos + PANEL_HEIGHT - 44,
                        0xAAAAAA
                );
            }
        }

        // Render buttons without calling super.render() (which would re-blur the panel)
        for (Renderable renderable : this.renderables) {
            renderable.render(graphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(previousScreen);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
