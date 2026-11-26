package com.abdur.godtechascension.client;

import com.abdur.godtechascension.registry.ModItems;
import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores Hold [W] Guide entries for every GodTech: Ascension item and block.
 */
public final class GodTechGuideData {

    public record GuideEntry(
            String tier,
            String obtain,
            String usage,
            List<Item> related
    ) {
    }

    private static Map<Item, GuideEntry> entries;

    private GodTechGuideData() {
    }

    public static GuideEntry get(Item item) {
        if (entries == null) {
            entries = buildEntries();
        }
        return entries.get(item);
    }

    private static Map<Item, GuideEntry> buildEntries() {
        Map<Item, GuideEntry> map = new LinkedHashMap<>();

        map.put(ModItems.TITANIUM_ORE.get(), new GuideEntry(
                "Tier 1 - Worldgen Ore",
                "Generates underground in Overworld stone between Y = -64 and Y = 32. Mine with an Iron Pickaxe or better.",
                "Drops Raw Titanium (affected by Fortune). Use Silk Touch to collect the ore block itself.",
                List.of(ModItems.RAW_TITANIUM.get(), ModItems.DEEPSLATE_TITANIUM_ORE.get())
        ));

        map.put(ModItems.DEEPSLATE_TITANIUM_ORE.get(), new GuideEntry(
                "Tier 1 - Worldgen Ore",
                "Generates in Overworld deepslate between Y = -64 and Y = 0. Mine with an Iron Pickaxe or better.",
                "Drops Raw Titanium (affected by Fortune). Use Silk Touch to collect the ore block itself.",
                List.of(ModItems.RAW_TITANIUM.get(), ModItems.TITANIUM_ORE.get())
        ));

        map.put(ModItems.RAW_TITANIUM.get(), new GuideEntry(
                "Tier 1 - Raw Material",
                "Dropped when mining Titanium Ore or Deepslate Titanium Ore.",
                "Smelt or blast directly into a Titanium Ingot, or craft with Flint to make Crushed Raw Titanium.",
                List.of(ModItems.CRUSHED_RAW_TITANIUM.get(), ModItems.TITANIUM_INGOT.get())
        ));

        map.put(ModItems.CRUSHED_RAW_TITANIUM.get(), new GuideEntry(
                "Tier 1 - Crushed Intermediate",
                "Crafted shapelessly from Raw Titanium + Flint (or via Create Crushing).",
                "Smelt or blast into a Titanium Ingot for industrial processing lines.",
                List.of(ModItems.RAW_TITANIUM.get(), ModItems.TITANIUM_INGOT.get())
        ));

        map.put(ModItems.TITANIUM_INGOT.get(), new GuideEntry(
                "Tier 1 - Refined Metal",
                "Smelt or blast Raw Titanium or Crushed Raw Titanium.",
                "Craft 4 in a 2x2 grid for 4x Titanium Sheets, or alloy with Iron and Copper to make Titanium Alloy.",
                List.of(ModItems.TITANIUM_SHEET.get(), ModItems.TITANIUM_ALLOY.get())
        ));

        map.put(ModItems.TITANIUM_SHEET.get(), new GuideEntry(
                "Tier 2 - Industrial Plate",
                "Craft 4x Titanium Ingots in a 2x2 pattern to produce 4x Titanium Sheets.",
                "Used to build Titanium Casings and assemble Reinforced Mechanisms.",
                List.of(ModItems.TITANIUM_CASING.get(), ModItems.REINFORCED_MECHANISM.get())
        ));

        map.put(ModItems.TITANIUM_ALLOY.get(), new GuideEntry(
                "Tier 2 - High-Strength Alloy",
                "Crafted from 4x Iron Ingots, 2x Titanium Ingots, and 2x Copper Ingots (yields 2x).",
                "Core structural alloy used in Quantum Circuits, the God Pickaxe, and the God Sword.",
                List.of(ModItems.QUANTUM_CIRCUIT.get(), ModItems.GOD_PICKAXE.get(), ModItems.GOD_SWORD.get())
        ));

        map.put(ModItems.TITANIUM_CASING.get(), new GuideEntry(
                "Tier 2 - Industrial Casing",
                "Crafted from 4x Titanium Sheets, 4x Iron Ingots, and 1x Redstone (yields 2x).",
                "Sturdy building block and future machine housing for GodTech industrial machines.",
                List.of(ModItems.TITANIUM_SHEET.get(), ModItems.REINFORCED_MECHANISM.get())
        ));

        map.put(ModItems.REINFORCED_MECHANISM.get(), new GuideEntry(
                "Tier 2 - Precision Component",
                "Crafted from 4x Titanium Sheets, 2x Gold Ingots, 2x Copper Ingots, and 1x Redstone.",
                "Required mechanical assembly for crafting the God Pickaxe and upcoming Tier 2+ machines.",
                List.of(ModItems.TITANIUM_SHEET.get(), ModItems.GOD_PICKAXE.get())
        ));

        map.put(ModItems.GOD_CRYSTAL.get(), new GuideEntry(
                "Tier 3 - Energy Crystal",
                "Crafted from 1x Diamond, 4x Amethyst Shards, 2x Blaze Powder, and 2x Redstone.",
                "Primary energy crystal used to craft Quantum Circuits and the endgame God Core.",
                List.of(ModItems.QUANTUM_CIRCUIT.get(), ModItems.GOD_CORE.get())
        ));

        map.put(ModItems.VOID_SHARD.get(), new GuideEntry(
                "Tier 3 - Void Material",
                "Crafted from 4x Obsidian, 4x Ender Pearls, and 1x Amethyst Shard (yields 2x).",
                "Used as the handle/conduit material for the God Pickaxe and God Sword.",
                List.of(ModItems.GOD_PICKAXE.get(), ModItems.GOD_SWORD.get())
        ));

        map.put(ModItems.QUANTUM_CIRCUIT.get(), new GuideEntry(
                "Tier 3 - Advanced Electronics",
                "Crafted from 4x Redstone, 2x Gold Ingots, 2x Titanium Alloy, and 1x God Crystal.",
                "High-tier processor required (4x) to craft the God Core.",
                List.of(ModItems.GOD_CRYSTAL.get(), ModItems.TITANIUM_ALLOY.get(), ModItems.GOD_CORE.get())
        ));

        map.put(ModItems.GOD_CORE.get(), new GuideEntry(
                "Tier 4 - Ascension Core",
                "Crafted from 4x God Crystals, 4x Quantum Circuits, and 1x Nether Star.",
                "Endgame singularity core used to forge the God Pickaxe and God Sword.",
                List.of(ModItems.GOD_PICKAXE.get(), ModItems.GOD_SWORD.get(), ModItems.QUANTUM_CIRCUIT.get())
        ));

        map.put(ModItems.GOD_PICKAXE.get(), new GuideEntry(
                "Tier 4 - Ascension Tool",
                "Crafted from 3x Titanium Alloy, 1x God Core, 1x Reinforced Mechanism, and 1x Void Shard.",
                "Mines a 3x3 area at once on any pickaxe-mineable block. Hold Sneak (Shift) to mine 1x1 precisely.",
                List.of(ModItems.GOD_CORE.get(), ModItems.REINFORCED_MECHANISM.get(), ModItems.TITANIUM_ALLOY.get())
        ));

        map.put(ModItems.GOD_SWORD.get(), new GuideEntry(
                "Tier 4 - Ascension Weapon",
                "Crafted from 3x Titanium Alloy, 1x God Core, and 1x Void Shard.",
                "High-damage fire-resistant blade. Right-click to unleash a 5-block AoE Energy Slash (3s cooldown).",
                List.of(ModItems.GOD_CORE.get(), ModItems.VOID_SHARD.get(), ModItems.TITANIUM_ALLOY.get())
        ));

        return map;
    }
}
