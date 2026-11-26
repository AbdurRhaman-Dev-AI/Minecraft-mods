package com.abdur.godtechascension.compat;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.registry.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Registers JEI info pages for every GodTech: Ascension item and block so players
 * can inspect usage and obtainability directly in JEI (press U or R).
 */
@JeiPlugin
public class GodTechJeiPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_UID =
            ResourceLocation.fromNamespaceAndPath(GodTechAscension.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        addInfo(registration, ModItems.TITANIUM_ORE.get(), "jei.godtechascension.info.titanium_ore");
        addInfo(registration, ModItems.DEEPSLATE_TITANIUM_ORE.get(), "jei.godtechascension.info.titanium_ore");
        addInfo(registration, ModItems.TITANIUM_CASING.get(), "jei.godtechascension.info.titanium_casing");
        addInfo(registration, ModItems.RAW_TITANIUM.get(), "jei.godtechascension.info.raw_titanium");
        addInfo(registration, ModItems.CRUSHED_RAW_TITANIUM.get(), "jei.godtechascension.info.crushed_raw_titanium");
        addInfo(registration, ModItems.TITANIUM_INGOT.get(), "jei.godtechascension.info.titanium_ingot");
        addInfo(registration, ModItems.TITANIUM_SHEET.get(), "jei.godtechascension.info.titanium_sheet");
        addInfo(registration, ModItems.TITANIUM_ALLOY.get(), "jei.godtechascension.info.titanium_alloy");
        addInfo(registration, ModItems.REINFORCED_MECHANISM.get(), "jei.godtechascension.info.reinforced_mechanism");
        addInfo(registration, ModItems.GOD_CRYSTAL.get(), "jei.godtechascension.info.god_crystal");
        addInfo(registration, ModItems.VOID_SHARD.get(), "jei.godtechascension.info.void_shard");
        addInfo(registration, ModItems.QUANTUM_CIRCUIT.get(), "jei.godtechascension.info.quantum_circuit");
        addInfo(registration, ModItems.GOD_CORE.get(), "jei.godtechascension.info.god_core");
        addInfo(registration, ModItems.GOD_PICKAXE.get(), "jei.godtechascension.info.god_pickaxe");
        addInfo(registration, ModItems.GOD_SWORD.get(), "jei.godtechascension.info.god_sword");
    }

    private static void addInfo(IRecipeRegistration registration, Item item, String translationKey) {
        registration.addIngredientInfo(
                new ItemStack(item),
                VanillaTypes.ITEM_STACK,
                Component.translatable(translationKey)
        );
    }
}
