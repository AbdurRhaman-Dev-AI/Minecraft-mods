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
                "Smelt or blast into a Titanium Ingot, or craft with Flint to make Crushed Raw Titanium.",
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
                "Craft 4 in a 2x2 grid for 4x Titanium Sheets, or alloy with Iron for Titanium Alloy.",
                List.of(ModItems.TITANIUM_SHEET.get(), ModItems.TITANIUM_ALLOY.get(), ModItems.ALLOY_SYNTHESIZER.get())
        ));

        map.put(ModItems.TITANIUM_SHEET.get(), new GuideEntry(
                "Tier 2 - Industrial Plate",
                "Craft 4x Titanium Ingots in a 2x2 pattern to produce 4x Titanium Sheets.",
                "Used to build Titanium Casings and assemble Reinforced Mechanisms.",
                List.of(ModItems.TITANIUM_CASING.get(), ModItems.REINFORCED_MECHANISM.get())
        ));

        map.put(ModItems.TITANIUM_ALLOY.get(), new GuideEntry(
                "Tier 2 - High-Strength Alloy",
                "Crafted from Iron, Titanium, and Copper (or 2x faster in the Alloy Synthesizer).",
                "Core alloy for Quantum Circuits, Exosuit Armor, Jetpack, Railgun, and God Tools.",
                List.of(ModItems.ALLOY_SYNTHESIZER.get(), ModItems.QUANTUM_CIRCUIT.get(), ModItems.GOD_PICKAXE.get())
        ));

        map.put(ModItems.TITANIUM_CASING.get(), new GuideEntry(
                "Tier 2 - Industrial Casing",
                "Crafted from 4x Titanium Sheets, 4x Iron Ingots, and 1x Redstone (yields 2x).",
                "Machine housing for the Arc Reactor, Alloy Synthesizer, and Quantum Warp Beacon.",
                List.of(ModItems.ARC_REACTOR.get(), ModItems.ALLOY_SYNTHESIZER.get(), ModItems.QUANTUM_WARP_BEACON.get())
        ));

        map.put(ModItems.REINFORCED_MECHANISM.get(), new GuideEntry(
                "Tier 2 - Precision Component",
                "Crafted from 4x Titanium Sheets, 2x Gold Ingots, 2x Copper Ingots, and 1x Redstone.",
                "Precision assembly for the Alloy Synthesizer, Ascension Jetpack, and God Pickaxe.",
                List.of(ModItems.ALLOY_SYNTHESIZER.get(), ModItems.ASCENSION_JETPACK.get(), ModItems.GOD_PICKAXE.get())
        ));

        map.put(ModItems.GOD_CRYSTAL.get(), new GuideEntry(
                "Tier 3 - Energy Crystal",
                "Crafted from 1x Diamond, 4x Amethyst Shards, 2x Blaze Powder, and 2x Redstone.",
                "Primary energy crystal for Quantum Circuits, Singularity Shards, and God Cores.",
                List.of(ModItems.QUANTUM_CIRCUIT.get(), ModItems.SINGULARITY_SHARD.get(), ModItems.GOD_CORE.get())
        ));

        map.put(ModItems.VOID_SHARD.get(), new GuideEntry(
                "Tier 3 - Void Material",
                "Crafted from 4x Obsidian, 4x Ender Pearls, and 1x Amethyst Shard (yields 2x).",
                "Used in Singularity Shards, Warp Beacons, Exosuit Armor, and God Tools.",
                List.of(ModItems.SINGULARITY_SHARD.get(), ModItems.QUANTUM_WARP_BEACON.get(), ModItems.GOD_SWORD.get())
        ));

        map.put(ModItems.QUANTUM_CIRCUIT.get(), new GuideEntry(
                "Tier 3 - Advanced Electronics",
                "Crafted from Redstone, Gold, Titanium Alloy, and God Crystal (yields 2x in Alloy Synthesizer).",
                "Processor for the Arc Reactor, Alloy Synthesizer, Exosuit, Railgun, and God Core.",
                List.of(ModItems.ARC_REACTOR.get(), ModItems.ALLOY_SYNTHESIZER.get(), ModItems.GOD_CORE.get())
        ));

        map.put(ModItems.SINGULARITY_SHARD.get(), new GuideEntry(
                "Tier 4 - Singularity Material",
                "Synthesized in the Alloy Synthesizer from God Crystal + Void Shard (yields 2x).",
                "Used in the Plasma Railgun, Quantum Magnet Ring, and Ascension Exosuit.",
                List.of(ModItems.ALLOY_SYNTHESIZER.get(), ModItems.PLASMA_RAILGUN.get(), ModItems.ASCENSION_CHESTPLATE.get())
        ));

        map.put(ModItems.GOD_CORE.get(), new GuideEntry(
                "Tier 4 - Ascension Core",
                "Crafted from 4x God Crystals, 4x Quantum Circuits, and 1x Nether Star.",
                "Endgame singularity core powering the Arc Reactor, Jetpack, Railgun, and God Tools.",
                List.of(ModItems.ARC_REACTOR.get(), ModItems.ASCENSION_JETPACK.get(), ModItems.GOD_SWORD.get())
        ));

        map.put(ModItems.ARC_REACTOR.get(), new GuideEntry(
                "Tier 4 - Create Rotational Generator",
                "Crafted from Steam Whistles, Cogwheel, Steam Engines, Reactor Core, and Oxidized Copper.",
                "Connects to Create Shafts/Cogs/Gearboxes. Flip a Lever ON to generate 1,474,560 su (5x Max Steam Engine at 256 RPM) with a 16-block generator sound. Wrenchable!",
                List.of(ModItems.REACTOR_CORE.get(), ModItems.MKV_REACTOR_CONTROLLER.get(), ModItems.GOD_CORE.get())
        ));

        map.put(ModItems.REACTOR_CORE.get(), new GuideEntry(
                "Tier 4 - Reactor Core Block",
                "Crafted from 6x Redstone Blocks, Precision Mechanism, God Core, and Void Shard.",
                "Primary core block used to craft the 1-block Arc Reactor rotational SU generator.",
                List.of(ModItems.ARC_REACTOR.get(), ModItems.GOD_CORE.get(), ModItems.VOID_SHARD.get())
        ));

        map.put(ModItems.MKV_REACTOR_CONTROLLER.get(), new GuideEntry(
                "Tier 5 - 4x2x2 Industrial Reactor",
                "Crafted from Arc Reactor, Titanium Casing, God Core, and Reinforced Mechanisms.",
                "Build a 4x2x2 multiblock with Titanium Casings + 2+ MK-V Reactor Cores. Flip a Lever ON to output 1,474,560 su (5x Max Steam Engines) + 100,000 FE/t!",
                List.of(ModItems.MKV_REACTOR_CORE.get(), ModItems.TITANIUM_CASING.get(), ModItems.ARC_REACTOR.get())
        ));

        map.put(ModItems.MKV_REACTOR_CORE.get(), new GuideEntry(
                "Tier 5 - Multiblock Plasma Core",
                "Crafted from God Core, Singularity Shards, Quantum Circuits, and Titanium Alloy.",
                "Place at least 2 inside a 4x2x2 MK-V Reactor multiblock alongside Titanium Casings and the MK-V Controller.",
                List.of(ModItems.MKV_REACTOR_CONTROLLER.get(), ModItems.TITANIUM_CASING.get(), ModItems.GOD_CORE.get())
        ));

        map.put(ModItems.ALLOY_SYNTHESIZER.get(), new GuideEntry(
                "Tier 3 - Sci-Fi Machine",
                "Crafted from Titanium Casing, Reinforced Mechanisms, Quantum Circuits, and a Blast Furnace.",
                "FE-powered 2-input synthesizer for Titanium Alloy, Quantum Circuits, and Singularity Shards.",
                List.of(ModItems.ARC_REACTOR.get(), ModItems.SINGULARITY_SHARD.get(), ModItems.TITANIUM_ALLOY.get())
        ));

        map.put(ModItems.QUANTUM_WARP_BEACON.get(), new GuideEntry(
                "Tier 3 - Teleport Anchor",
                "Crafted from Titanium Casing, Void Shards, Quantum Circuit, and Ender Pearl.",
                "Place at any base or airship dock, then right-click with a Quantum Warp Linker to bind.",
                List.of(ModItems.QUANTUM_WARP_LINKER.get(), ModItems.VOID_SHARD.get(), ModItems.TITANIUM_CASING.get())
        ));

        map.put(ModItems.QUANTUM_WARP_LINKER.get(), new GuideEntry(
                "Tier 3 - Personal Teleporter",
                "Crafted from Quantum Circuit, Void Shard, and Titanium Alloy.",
                "Right-click a Quantum Warp Beacon to bind, then right-click anywhere to warp back across dimensions.",
                List.of(ModItems.QUANTUM_WARP_BEACON.get(), ModItems.QUANTUM_CIRCUIT.get())
        ));

        map.put(ModItems.GOD_PICKAXE.get(), new GuideEntry(
                "Tier 4 - Ascension Tool",
                "Crafted from Titanium Alloy, God Core, Reinforced Mechanism, and Void Shard.",
                "Unbreakable & lava-proof. Mines 10x faster than Efficiency V in a 3x3 area (hold Sneak for 1x1).",
                List.of(ModItems.GOD_CORE.get(), ModItems.REINFORCED_MECHANISM.get(), ModItems.QUANTUM_MAGNET_RING.get())
        ));

        map.put(ModItems.GOD_SWORD.get(), new GuideEntry(
                "Tier 4 - Ascension Weapon",
                "Crafted from Titanium Alloy, God Core, and Void Shard.",
                "Unbreakable, lava-proof, 25 Attack Damage! Right-click for a 5-block AoE Energy Slash (3s cooldown).",
                List.of(ModItems.GOD_CORE.get(), ModItems.PLASMA_RAILGUN.get(), ModItems.ASCENSION_CHESTPLATE.get())
        ));

        map.put(ModItems.PLASMA_RAILGUN.get(), new GuideEntry(
                "Tier 4 - Sci-Fi Weapon",
                "Crafted from Titanium Alloy, Quantum Circuit, Singularity Shard, and God Core.",
                "Lava-proof railgun. Right-click to fire a piercing 30-block cyan plasma beam (28 damage, 1.5s cooldown).",
                List.of(ModItems.SINGULARITY_SHARD.get(), ModItems.GOD_CORE.get(), ModItems.GOD_SWORD.get())
        ));

        map.put(ModItems.QUANTUM_MAGNET_RING.get(), new GuideEntry(
                "Tier 3 - Utility Gadget",
                "Crafted from Titanium Alloy, Singularity Shard, and Redstone.",
                "Keep in your inventory to pull dropped items within 8 blocks straight to you (hold Sneak to pause).",
                List.of(ModItems.GOD_PICKAXE.get(), ModItems.SINGULARITY_SHARD.get())
        ));

        map.put(ModItems.ASCENSION_HELMET.get(), new GuideEntry(
                "Tier 4 - Ascension Exosuit",
                "Crafted from Titanium Alloy, Quantum Circuit, and God Crystal.",
                "Unbreakable & lava-proof. Grants Night Vision, Water Breathing, and built-in Create Engineer's Goggles!",
                List.of(ModItems.ASCENSION_CHESTPLATE.get(), ModItems.ASCENSION_JETPACK.get(), ModItems.ASCENSION_BOOTS.get())
        ));

        map.put(ModItems.ASCENSION_CHESTPLATE.get(), new GuideEntry(
                "Tier 4 - Ascension Exosuit",
                "Crafted from Titanium Alloy, Singularity Shard, and God Core.",
                "Unbreakable & lava-proof. Grants Fire Resistance and heavy armor protection.",
                List.of(ModItems.ASCENSION_JETPACK.get(), ModItems.ASCENSION_HELMET.get(), ModItems.ASCENSION_LEGGINGS.get())
        ));

        map.put(ModItems.ASCENSION_JETPACK.get(), new GuideEntry(
                "Tier 4 - Sci-Fi Flight",
                "Crafted from Titanium Alloy, Reinforced Mechanism, God Core, and Blaze Rods. Refuel with Blaze Powder.",
                "Unbreakable & lava-proof. Right-click with Blaze Powder to fuel (10x = 100%). Shows live km/h speed, km odometer, and fuel HUD.",
                List.of(ModItems.ASCENSION_CHESTPLATE.get(), ModItems.GOD_CORE.get(), ModItems.REINFORCED_MECHANISM.get())
        ));

        map.put(ModItems.ASCENSION_LEGGINGS.get(), new GuideEntry(
                "Tier 4 - Ascension Exosuit",
                "Crafted from Titanium Alloy, Reinforced Mechanism, and Void Shard.",
                "Unbreakable & lava-proof. Grants Speed II boost while worn.",
                List.of(ModItems.ASCENSION_BOOTS.get(), ModItems.ASCENSION_CHESTPLATE.get())
        ));

        map.put(ModItems.ASCENSION_BOOTS.get(), new GuideEntry(
                "Tier 4 - Ascension Exosuit",
                "Crafted from Titanium Alloy, Void Shard, and God Crystal.",
                "Unbreakable & lava-proof. Grants complete Fall-Damage Immunity.",
                List.of(ModItems.ASCENSION_LEGGINGS.get(), ModItems.ASCENSION_JETPACK.get())
        ));

        return map;
    }
}
