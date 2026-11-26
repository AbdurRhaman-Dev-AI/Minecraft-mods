package com.abdur.godtechascension.registry;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.item.GodPickaxeItem;
import com.abdur.godtechascension.item.GodSwordItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all items, block items, and God-tier tools for GodTech: Ascension.
 */
public final class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GodTechAscension.MODID);

    public static final Tier GOD_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            4096,
            14.0F,
            5.0F,
            22,
            () -> Ingredient.of(ModItems.TITANIUM_ALLOY.get())
    );

    // Block items
    public static final DeferredItem<BlockItem> TITANIUM_ORE = ITEMS.registerSimpleBlockItem(ModBlocks.TITANIUM_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_TITANIUM_ORE = ITEMS.registerSimpleBlockItem(ModBlocks.DEEPSLATE_TITANIUM_ORE);
    public static final DeferredItem<BlockItem> TITANIUM_CASING = ITEMS.registerSimpleBlockItem(ModBlocks.TITANIUM_CASING);

    // Create-style materials & components
    public static final DeferredItem<Item> RAW_TITANIUM = ITEMS.registerSimpleItem("raw_titanium");
    public static final DeferredItem<Item> CRUSHED_RAW_TITANIUM = ITEMS.registerSimpleItem("crushed_raw_titanium");
    public static final DeferredItem<Item> TITANIUM_INGOT = ITEMS.registerSimpleItem("titanium_ingot");
    public static final DeferredItem<Item> TITANIUM_SHEET = ITEMS.registerSimpleItem("titanium_sheet");
    public static final DeferredItem<Item> TITANIUM_ALLOY = ITEMS.registerSimpleItem(
            "titanium_alloy", new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> GOD_CRYSTAL = ITEMS.registerSimpleItem(
            "god_crystal", new Item.Properties().rarity(Rarity.RARE));
    public static final DeferredItem<Item> VOID_SHARD = ITEMS.registerSimpleItem(
            "void_shard", new Item.Properties().rarity(Rarity.RARE));
    public static final DeferredItem<Item> REINFORCED_MECHANISM = ITEMS.registerSimpleItem(
            "reinforced_mechanism", new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> QUANTUM_CIRCUIT = ITEMS.registerSimpleItem(
            "quantum_circuit", new Item.Properties().rarity(Rarity.RARE));
    public static final DeferredItem<Item> GOD_CORE = ITEMS.registerSimpleItem(
            "god_core", new Item.Properties().rarity(Rarity.EPIC).stacksTo(16));

    // Endgame tools
    public static final DeferredItem<PickaxeItem> GOD_PICKAXE = ITEMS.register(
            "god_pickaxe",
            () -> new GodPickaxeItem(
                    GOD_TIER,
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .fireResistant()
                            .attributes(PickaxeItem.createAttributes(GOD_TIER, 1.0F, -2.8F))
            )
    );

    public static final DeferredItem<SwordItem> GOD_SWORD = ITEMS.register(
            "god_sword",
            () -> new GodSwordItem(
                    GOD_TIER,
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .fireResistant()
                            .attributes(SwordItem.createAttributes(GOD_TIER, 6, -2.2F))
            )
    );

    private ModItems() {
    }
}
