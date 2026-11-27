package com.abdur.godtechascension.registry;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.item.AscensionArmorItem;
import com.abdur.godtechascension.item.AscensionJetpackItem;
import com.abdur.godtechascension.item.GodPickaxeItem;
import com.abdur.godtechascension.item.GodSwordItem;
import com.abdur.godtechascension.item.PlasmaRailgunItem;
import com.abdur.godtechascension.item.QuantumMagnetRingItem;
import com.abdur.godtechascension.item.QuantumWarpLinkerItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all items, machines, sci-fi armor, jetpack, and God-tier tools for GodTech: Ascension.
 */
public final class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GodTechAscension.MODID);

    public static final Tier GOD_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            4096,
            180.0F,
            5.0F,
            22,
            () -> Ingredient.of(ModItems.TITANIUM_ALLOY.get())
    );

    // Block items
    public static final DeferredItem<BlockItem> TITANIUM_ORE = ITEMS.registerSimpleBlockItem(ModBlocks.TITANIUM_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_TITANIUM_ORE = ITEMS.registerSimpleBlockItem(ModBlocks.DEEPSLATE_TITANIUM_ORE);
    public static final DeferredItem<BlockItem> TITANIUM_CASING = ITEMS.registerSimpleBlockItem(ModBlocks.TITANIUM_CASING);
    public static final DeferredItem<BlockItem> ARC_REACTOR = ITEMS.registerSimpleBlockItem(
            ModBlocks.ARC_REACTOR, new Item.Properties().rarity(Rarity.EPIC).fireResistant());
    public static final DeferredItem<BlockItem> REACTOR_CORE = ITEMS.registerSimpleBlockItem(
            ModBlocks.REACTOR_CORE, new Item.Properties().rarity(Rarity.RARE).fireResistant());
    public static final DeferredItem<BlockItem> ALLOY_SYNTHESIZER = ITEMS.registerSimpleBlockItem(
            ModBlocks.ALLOY_SYNTHESIZER, new Item.Properties().rarity(Rarity.RARE).fireResistant());
    public static final DeferredItem<BlockItem> QUANTUM_WARP_BEACON = ITEMS.registerSimpleBlockItem(
            ModBlocks.QUANTUM_WARP_BEACON, new Item.Properties().rarity(Rarity.RARE).fireResistant());
    public static final DeferredItem<BlockItem> MKV_REACTOR_CORE = ITEMS.registerSimpleBlockItem(
            ModBlocks.MKV_REACTOR_CORE, new Item.Properties().rarity(Rarity.EPIC).fireResistant());
    public static final DeferredItem<BlockItem> MKV_REACTOR_CONTROLLER = ITEMS.registerSimpleBlockItem(
            ModBlocks.MKV_REACTOR_CONTROLLER, new Item.Properties().rarity(Rarity.EPIC).fireResistant());

    // Create-style materials & sci-fi components
    public static final DeferredItem<Item> RAW_TITANIUM = ITEMS.registerSimpleItem("raw_titanium");
    public static final DeferredItem<Item> CRUSHED_RAW_TITANIUM = ITEMS.registerSimpleItem("crushed_raw_titanium");
    public static final DeferredItem<Item> TITANIUM_INGOT = ITEMS.registerSimpleItem("titanium_ingot");
    public static final DeferredItem<Item> TITANIUM_SHEET = ITEMS.registerSimpleItem("titanium_sheet");
    public static final DeferredItem<Item> TITANIUM_ALLOY = ITEMS.registerSimpleItem(
            "titanium_alloy", new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant());
    public static final DeferredItem<Item> REINFORCED_MECHANISM = ITEMS.registerSimpleItem(
            "reinforced_mechanism", new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant());
    public static final DeferredItem<Item> GOD_CRYSTAL = ITEMS.registerSimpleItem(
            "god_crystal", new Item.Properties().rarity(Rarity.RARE).fireResistant());
    public static final DeferredItem<Item> VOID_SHARD = ITEMS.registerSimpleItem(
            "void_shard", new Item.Properties().rarity(Rarity.RARE).fireResistant());
    public static final DeferredItem<Item> QUANTUM_CIRCUIT = ITEMS.registerSimpleItem(
            "quantum_circuit", new Item.Properties().rarity(Rarity.RARE).fireResistant());
    public static final DeferredItem<Item> SINGULARITY_SHARD = ITEMS.registerSimpleItem(
            "singularity_shard", new Item.Properties().rarity(Rarity.EPIC).fireResistant());
    public static final DeferredItem<Item> GOD_CORE = ITEMS.registerSimpleItem(
            "god_core", new Item.Properties().rarity(Rarity.EPIC).stacksTo(16).fireResistant());

    // Endgame Unbreakable & Fire/Lava-Proof Tools & Gadgets
    public static final DeferredItem<PickaxeItem> GOD_PICKAXE = ITEMS.register(
            "god_pickaxe",
            () -> new GodPickaxeItem(
                    GOD_TIER,
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .fireResistant()
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
                            .attributes(PickaxeItem.createAttributes(GOD_TIER, 1.0F, -2.6F))
            )
    );

    public static final DeferredItem<SwordItem> GOD_SWORD = ITEMS.register(
            "god_sword",
            () -> new GodSwordItem(
                    GOD_TIER,
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .fireResistant()
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
                            .attributes(SwordItem.createAttributes(GOD_TIER, 19, -2.0F))
            )
    );

    public static final DeferredItem<Item> PLASMA_RAILGUN = ITEMS.register(
            "plasma_railgun",
            () -> new PlasmaRailgunItem(
                    new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant()
            )
    );

    public static final DeferredItem<Item> QUANTUM_MAGNET_RING = ITEMS.register(
            "quantum_magnet_ring",
            () -> new QuantumMagnetRingItem(
                    new Item.Properties().rarity(Rarity.RARE).stacksTo(1).fireResistant()
            )
    );

    public static final DeferredItem<Item> QUANTUM_WARP_LINKER = ITEMS.register(
            "quantum_warp_linker",
            () -> new QuantumWarpLinkerItem(
                    new Item.Properties().rarity(Rarity.RARE).stacksTo(1).fireResistant()
            )
    );

    // Ascension Exosuit Armor + Jetpack (Unbreakable & Fire/Lava-Proof)
    public static final DeferredItem<ArmorItem> ASCENSION_HELMET = ITEMS.register(
            "ascension_helmet",
            () -> new AscensionArmorItem(
                    ModArmorMaterials.ASCENSION,
                    ArmorItem.Type.HELMET,
                    new Item.Properties().rarity(Rarity.EPIC).fireResistant()
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );

    public static final DeferredItem<ArmorItem> ASCENSION_CHESTPLATE = ITEMS.register(
            "ascension_chestplate",
            () -> new AscensionArmorItem(
                    ModArmorMaterials.ASCENSION,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.EPIC).fireResistant()
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );

    public static final DeferredItem<ArmorItem> ASCENSION_LEGGINGS = ITEMS.register(
            "ascension_leggings",
            () -> new AscensionArmorItem(
                    ModArmorMaterials.ASCENSION,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.EPIC).fireResistant()
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );

    public static final DeferredItem<ArmorItem> ASCENSION_BOOTS = ITEMS.register(
            "ascension_boots",
            () -> new AscensionArmorItem(
                    ModArmorMaterials.ASCENSION,
                    ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.EPIC).fireResistant()
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );

    public static final DeferredItem<ArmorItem> ASCENSION_JETPACK = ITEMS.register(
            "ascension_jetpack",
            () -> new AscensionJetpackItem(
                    ModArmorMaterials.ASCENSION_JETPACK,
                    new Item.Properties().rarity(Rarity.EPIC).fireResistant()
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );

    private ModItems() {
    }
}
