package com.abdur.godtechascension.client;

import com.abdur.godtechascension.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Renders live 60 FPS animated demonstration scenes inside GodTechGuideScreen
 * so players immediately see how each item, machine, or armor piece works.
 */
public final class GodTechGuideAnimator {

    private GodTechGuideAnimator() {
    }

    public static void renderScene(GuiGraphics g, Font font, Item item, int x, int y, int w, int h) {
        g.fill(x, y, x + w, y + h, 0xFF0C1017);
        g.renderOutline(x, y, w, h, 0xFF2C5A73);

        for (int gx = x + 16; gx < x + w; gx += 16) {
            g.fill(gx, y + 1, gx + 1, y + h - 1, 0x1839C5E8);
        }
        for (int gy = y + 16; gy < y + h; gy += 16) {
            g.fill(x + 1, gy, x + w - 1, gy + 1, 0x1839C5E8);
        }

        double t = (Util.getMillis() % 4000L) / 4000.0;

        if (item == ModItems.GOD_PICKAXE.get()) {
            renderPickaxeScene(g, font, x, y, w, h, t);
        } else if (item == ModItems.GOD_SWORD.get()) {
            renderSwordScene(g, font, x, y, w, h, t);
        } else if (item == ModItems.PLASMA_RAILGUN.get()) {
            renderRailgunScene(g, font, x, y, w, h, t);
        } else if (item == ModItems.ARC_REACTOR.get()) {
            renderArcReactorScene(g, font, x, y, w, h, t);
        } else if (item == ModItems.REACTOR_CORE.get() || item == ModItems.MKV_REACTOR_CONTROLLER.get() || item == ModItems.MKV_REACTOR_CORE.get()) {
            renderMkvReactorScene(g, font, x, y, w, h, t);
        } else if (item == ModItems.ALLOY_SYNTHESIZER.get()) {
            renderSynthesizerScene(g, font, x, y, w, h, t);
        } else if (item == ModItems.QUANTUM_WARP_BEACON.get() || item == ModItems.QUANTUM_WARP_LINKER.get()) {
            renderWarpScene(g, font, x, y, w, h, t);
        } else if (item == ModItems.QUANTUM_MAGNET_RING.get()) {
            renderMagnetScene(g, font, x, y, w, h, t);
        } else if (item == ModItems.ASCENSION_JETPACK.get()
                || item == ModItems.ASCENSION_HELMET.get()
                || item == ModItems.ASCENSION_CHESTPLATE.get()
                || item == ModItems.ASCENSION_LEGGINGS.get()
                || item == ModItems.ASCENSION_BOOTS.get()) {
            renderExosuitScene(g, font, item, x, y, w, h, t);
        } else if (item == ModItems.TITANIUM_ORE.get() || item == ModItems.DEEPSLATE_TITANIUM_ORE.get()) {
            renderOreScene(g, font, item, x, y, w, h, t);
        } else if (item == ModItems.RAW_TITANIUM.get() || item == ModItems.CRUSHED_RAW_TITANIUM.get()
                || item == ModItems.TITANIUM_INGOT.get()) {
            renderSmeltChainScene(g, font, item, x, y, w, h, t);
        } else if (item == ModItems.TITANIUM_SHEET.get() || item == ModItems.TITANIUM_ALLOY.get()
                || item == ModItems.TITANIUM_CASING.get() || item == ModItems.REINFORCED_MECHANISM.get()) {
            renderCraftFlowScene(g, font, item, x, y, w, h, t);
        } else {
            renderEnergyCoreScene(g, font, item, x, y, w, h, t);
        }
    }

    private static void renderPickaxeScene(GuiGraphics g, Font font, int x, int y, int w, int h, double t) {
        boolean sneakMode = t >= 0.5;
        double phase = (t % 0.5) * 2.0;
        boolean broken = phase > 0.55;

        int gridX = x + (w - 54) / 2;
        int gridY = y + 20;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                int bx = gridX + c * 18;
                int by = gridY + r * 18;
                boolean isCenter = (r == 1 && c == 1);
                boolean target = !sneakMode || isCenter;

                if (!(broken && target)) {
                    ItemStack block = (r == 1 && c == 2)
                            ? new ItemStack(ModItems.TITANIUM_ORE.get())
                            : new ItemStack(Items.STONE);
                    g.renderItem(block, bx, by);
                    if (target && phase > 0.2) {
                        g.renderOutline(bx - 1, by - 1, 18, 18, 0xFF39C5E8);
                    }
                } else if (phase < 0.85) {
                    int dropY = by + (int) ((phase - 0.55) * 18);
                    g.renderItem(new ItemStack(Items.COBBLESTONE), bx, dropY);
                }
            }
        }

        int swing = (int) (Math.sin(phase * Math.PI * 3.0) * 6);
        g.renderItem(new ItemStack(ModItems.GOD_PICKAXE.get()), gridX - 18 + Math.max(0, swing), gridY + 18 + swing / 2);

        String caption = sneakMode ? "Sneak: 1x1 | 10x Eff.V" : "Normal: 3x3 Unbreakable";
        drawCenteredCaption(g, font, caption, x + w / 2, y + h - 14, sneakMode ? 0xFFFFAA00 : 0xFF39C5E8);
    }

    private static void renderSwordScene(GuiGraphics g, Font font, int x, int y, int w, int h, double t) {
        int cx = x + w / 2;
        int cy = y + 52;

        double wave = (t * 2.0) % 1.0;
        int radius = 12 + (int) (wave * 34);
        int alpha = (int) ((1.0 - wave) * 220);
        g.renderOutline(cx - radius, cy - radius / 2, radius * 2, radius, (alpha << 24) | 0x39C5E8);

        int push = wave > 0.35 ? (int) ((wave - 0.35) * 14) : 0;
        g.renderItem(new ItemStack(Items.ZOMBIE_HEAD), cx - 42 - push, cy - 10);
        g.renderItem(new ItemStack(Items.SKELETON_SKULL), cx + 26 + push, cy - 10);
        g.renderItem(new ItemStack(Items.CREEPER_HEAD), cx - 8, cy - 34 - push / 2);

        int bob = (int) (Math.sin(t * Math.PI * 4.0) * 3);
        g.renderItem(new ItemStack(ModItems.GOD_SWORD.get()), cx - 8, cy - 8 + bob);

        drawCenteredCaption(g, font, "25 Dmg + 5-Block AoE", cx, y + h - 14, 0xFFFFD15C);
    }

    private static void renderRailgunScene(GuiGraphics g, Font font, int x, int y, int w, int h, double t) {
        int cy = y + 52;
        g.renderItem(new ItemStack(ModItems.PLASMA_RAILGUN.get()), x + 12, cy - 8);

        double beamPhase = (t * 2.0) % 1.0;
        if (beamPhase < 0.65) {
            int beamW = (int) ((beamPhase / 0.65) * 80);
            g.fill(x + 32, cy - 2, x + 32 + beamW, cy + 2, 0xFF39C5E8);
            g.fill(x + 32, cy - 1, x + 32 + beamW, cy + 1, 0xFFFFFFFF);
        }

        g.renderItem(new ItemStack(Items.ZOMBIE_HEAD), x + w - 34, cy - 8);
        g.renderItem(new ItemStack(Items.SKELETON_SKULL), x + w - 20, cy - 14);
        drawCenteredCaption(g, font, "30m Piercing Beam (28 Dmg)", x + w / 2, y + h - 14, 0xFF55FFFF);
    }

    private static void renderArcReactorScene(GuiGraphics g, Font font, int x, int y, int w, int h, double t) {
        int cx = x + w / 2;
        int cy = y + 48;

        int pulse = (int) (Math.abs(Math.sin(t * Math.PI * 4.0)) * 22);
        g.fill(cx - 28, cy - 2, cx + 28, cy + 2, 0xFF1F384D);
        g.fill(cx - 2 - pulse, cy - 1, cx + 2 + pulse, cy + 1, 0xFF39C5E8);
        g.fill(cx - 2, cy - 26, cx + 2, cy + 26, 0xFF1F384D);
        g.fill(cx - 1, cy - 2 - pulse / 2, cx + 1, cy + 2 + pulse / 2, 0xFF39C5E8);

        g.renderItem(new ItemStack(ModItems.ARC_REACTOR.get()), cx - 8, cy - 8);
        g.renderItem(new ItemStack(Items.LEVER), cx + 28, cy - 8);
        g.renderItem(new ItemStack(ModItems.REACTOR_CORE.get()), cx - 44, cy - 8);

        drawCenteredCaption(g, font, "256 RPM | 1,474,560 su", cx, y + h - 14, 0xFF55FFFF);
    }

    private static void renderMkvReactorScene(GuiGraphics g, Font font, int x, int y, int w, int h, double t) {
        int startX = x + (w - 72) / 2;
        int startY = y + 26;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                int bx = startX + col * 18;
                int by = startY + row * 18;
                Item block = (row == 1 && col == 0)
                        ? ModItems.MKV_REACTOR_CONTROLLER.get()
                        : ((col == 1 || col == 2) ? ModItems.MKV_REACTOR_CORE.get() : ModItems.TITANIUM_CASING.get());
                g.renderItem(new ItemStack(block), bx, by);
            }
        }
        g.renderItem(new ItemStack(Items.LEVER), startX + 27, startY - 16);
        drawCenteredCaption(g, font, "4x2x2: 1,474,560 su + FE", x + w / 2, y + h - 14, 0xFFFFAA00);
    }

    private static void renderSynthesizerScene(GuiGraphics g, Font font, int x, int y, int w, int h, double t) {
        int cy = y + 46;
        g.renderItem(new ItemStack(ModItems.GOD_CRYSTAL.get()), x + 12, cy - 14);
        g.renderItem(new ItemStack(ModItems.VOID_SHARD.get()), x + 12, cy + 6);
        g.drawString(font, "→", x + 36, cy - 2, 0x39C5E8, false);

        g.renderOutline(x + 52, cy - 10, 24, 24, 0xFF39C5E8);
        g.renderItem(new ItemStack(ModItems.ALLOY_SYNTHESIZER.get()), x + 56, cy - 6);

        int prog = (int) (t * 20);
        g.fill(x + 80, cy, x + 80 + prog, cy + 4, 0xFF55FF55);
        g.renderItem(new ItemStack(ModItems.SINGULARITY_SHARD.get()), x + 106, cy - 6);

        drawCenteredCaption(g, font, "FE-Powered Synthesis", x + w / 2, y + h - 14, 0xFF55FF55);
    }

    private static void renderWarpScene(GuiGraphics g, Font font, int x, int y, int w, int h, double t) {
        int cy = y + 50;
        g.renderItem(new ItemStack(ModItems.QUANTUM_WARP_LINKER.get()), x + 18, cy - 8);
        g.renderItem(new ItemStack(ModItems.QUANTUM_WARP_BEACON.get()), x + w - 34, cy - 8);

        int dotX = x + 38 + (int) (t * (w - 76));
        g.fill(dotX - 2, cy - 2, dotX + 2, cy + 2, 0xFFCC66FF);
        drawCenteredCaption(g, font, "Bind -> Instant Warp", x + w / 2, y + h - 14, 0xFFCC66FF);
    }

    private static void renderMagnetScene(GuiGraphics g, Font font, int x, int y, int w, int h, double t) {
        int cx = x + w / 2;
        int cy = y + 50;
        double pull = 1.0 - t;
        int offset = (int) (pull * 36);

        g.renderItem(new ItemStack(Items.DIAMOND), cx - 8 - offset, cy - 14);
        g.renderItem(new ItemStack(ModItems.RAW_TITANIUM.get()), cx - 8 + offset, cy + 4);
        g.renderItem(new ItemStack(ModItems.QUANTUM_MAGNET_RING.get()), cx - 8, cy - 8);

        drawCenteredCaption(g, font, "8-Block Item Magnet", cx, y + h - 14, 0xFF55FFFF);
    }

    private static void renderExosuitScene(GuiGraphics g, Font font, Item item, int x, int y, int w, int h, double t) {
        int cx = x + w / 2;
        int cy = y + 48;
        int bob = (int) (Math.sin(t * Math.PI * 4.0) * 5);
        g.renderOutline(cx - 14, cy - 14 + bob, 28, 28, 0xFF39C5E8);
        g.renderItem(new ItemStack(item), cx - 8, cy - 8 + bob);

        String cap = item == ModItems.ASCENSION_JETPACK.get()
                ? "Smooth Flight + No Fall Dmg"
                : "Unbreakable & Lava-Proof";
        drawCenteredCaption(g, font, cap, cx, y + h - 14, 0xFF55FFFF);
    }

    private static void renderOreScene(GuiGraphics g, Font font, Item oreItem, int x, int y, int w, int h, double t) {
        int cx = x + w / 2;
        int cy = y + 48;

        if (t < 0.65) {
            g.renderItem(new ItemStack(oreItem), cx - 8, cy - 8);
            int swing = (int) (Math.sin(t * Math.PI * 8.0) * 5);
            g.renderItem(new ItemStack(Items.IRON_PICKAXE), cx + 12 - Math.abs(swing), cy - 12 + swing);
        } else {
            int pop = (int) ((t - 0.65) * 12);
            g.renderItem(new ItemStack(ModItems.RAW_TITANIUM.get()), cx - 14, cy - 8 - pop);
            g.renderItem(new ItemStack(ModItems.RAW_TITANIUM.get()), cx + 2, cy - 6 - pop / 2);
        }

        drawCenteredCaption(g, font, "Y = -64 to 32 (Fortune)", cx, y + h - 14, 0xFF55FF55);
    }

    private static void renderSmeltChainScene(GuiGraphics g, Font font, Item active, int x, int y, int w, int h, double t) {
        int cy = y + 42;
        Item[] chain = {
                ModItems.RAW_TITANIUM.get(),
                ModItems.CRUSHED_RAW_TITANIUM.get(),
                ModItems.TITANIUM_INGOT.get()
        };

        int activeIdx = (int) (t * 3.0) % 3;
        for (int i = 0; i < 3; i++) {
            int ix = x + 14 + i * 42;
            if (chain[i] == active || i == activeIdx) {
                g.renderOutline(ix - 3, cy - 3, 22, 22, 0xFF39C5E8);
            }
            g.renderItem(new ItemStack(chain[i]), ix, cy);
            if (i < 2) {
                g.drawString(font, Component.literal("→").withStyle(ChatFormatting.GOLD), ix + 24, cy + 4, 0xFFAA00);
            }
        }

        g.renderItem(new ItemStack(Items.BLAST_FURNACE), x + w / 2 - 8, cy + 26);
        drawCenteredCaption(g, font, "Smelt / Blast -> Ingot", x + w / 2, y + h - 14, 0xFF39C5E8);
    }

    private static void renderCraftFlowScene(GuiGraphics g, Font font, Item item, int x, int y, int w, int h, double t) {
        int cy = y + 46;
        Item leftA = Items.IRON_INGOT;
        Item leftB = ModItems.TITANIUM_INGOT.get();

        if (item == ModItems.TITANIUM_SHEET.get()) {
            leftA = ModItems.TITANIUM_INGOT.get();
            leftB = ModItems.TITANIUM_INGOT.get();
        } else if (item == ModItems.TITANIUM_CASING.get()) {
            leftA = ModItems.TITANIUM_SHEET.get();
            leftB = Items.REDSTONE;
        } else if (item == ModItems.REINFORCED_MECHANISM.get()) {
            leftA = ModItems.TITANIUM_SHEET.get();
            leftB = Items.GOLD_INGOT;
        }

        int slide = (int) (Math.sin(t * Math.PI * 2.0) * 3);
        g.renderItem(new ItemStack(leftA), x + 14 + slide, cy - 10);
        g.renderItem(new ItemStack(leftB), x + 14 + slide, cy + 10);
        g.drawString(font, Component.literal("→").withStyle(ChatFormatting.AQUA), x + 46, cy + 2, 0x39C5E8);

        g.renderOutline(x + 70, cy - 4, 24, 24, 0xFF39C5E8);
        g.renderItem(new ItemStack(item), x + 74, cy);

        drawCenteredCaption(g, font, "Crafting Progression", x + w / 2, y + h - 14, 0xFFE6EEF5);
    }

    private static void renderEnergyCoreScene(GuiGraphics g, Font font, Item item, int x, int y, int w, int h, double t) {
        int cx = x + w / 2;
        int cy = y + 48;

        Item[] orbiters;
        if (item == ModItems.GOD_CORE.get()) {
            orbiters = new Item[]{ModItems.GOD_CRYSTAL.get(), ModItems.QUANTUM_CIRCUIT.get(), Items.NETHER_STAR};
        } else if (item == ModItems.QUANTUM_CIRCUIT.get()) {
            orbiters = new Item[]{Items.REDSTONE, Items.GOLD_INGOT, ModItems.GOD_CRYSTAL.get()};
        } else if (item == ModItems.SINGULARITY_SHARD.get()) {
            orbiters = new Item[]{ModItems.GOD_CRYSTAL.get(), ModItems.VOID_SHARD.get(), ModItems.ALLOY_SYNTHESIZER.get()};
        } else if (item == ModItems.VOID_SHARD.get()) {
            orbiters = new Item[]{Items.OBSIDIAN, Items.ENDER_PEARL, Items.AMETHYST_SHARD};
        } else {
            orbiters = new Item[]{Items.DIAMOND, Items.AMETHYST_SHARD, Items.BLAZE_POWDER};
        }

        double angleBase = t * Math.PI * 2.0;
        for (int i = 0; i < orbiters.length; i++) {
            double ang = angleBase + i * (Math.PI * 2.0 / orbiters.length);
            int ox = cx + (int) (Math.cos(ang) * 34) - 8;
            int oy = cy + (int) (Math.sin(ang) * 20) - 8;
            g.renderItem(new ItemStack(orbiters[i]), ox, oy);
        }

        int pulse = (int) (Math.abs(Math.sin(t * Math.PI * 4.0)) * 2);
        g.renderOutline(cx - 11 - pulse, cy - 11 - pulse, 22 + pulse * 2, 22 + pulse * 2, 0xFF39C5E8);
        g.renderItem(new ItemStack(item), cx - 8, cy - 8);

        drawCenteredCaption(g, font, "Energy Component", cx, y + h - 14, 0xFF55FFFF);
    }

    private static void drawCenteredCaption(GuiGraphics g, Font font, String text, int centerX, int y, int color) {
        int tw = font.width(text);
        g.drawString(font, text, centerX - tw / 2, y, color);
    }
}
