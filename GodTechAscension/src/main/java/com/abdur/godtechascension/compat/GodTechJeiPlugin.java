package com.abdur.godtechascension.compat;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.recipe.AlloyRecipe;
import com.abdur.godtechascension.registry.ModItems;
import com.abdur.godtechascension.registry.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

/**
 * Registers JEI Alloy Synthesizer recipes and Info pages for every GodTech: Ascension item and block.
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
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AlloyRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.ALLOY_SYNTHESIZER.get()), AlloyRecipeCategory.TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level != null) {
            List<AlloyRecipe> alloyRecipes = Minecraft.getInstance().level.getRecipeManager()
                    .getAllRecipesFor(ModRecipes.ALLOY_TYPE.get())
                    .stream()
                    .map(RecipeHolder::value)
                    .toList();
            registration.addRecipes(AlloyRecipeCategory.TYPE, alloyRecipes);
        }

        addInfo(registration, ModItems.TITANIUM_ORE.get(), "jei.godtechascension.info.titanium_ore");
        addInfo(registration, ModItems.DEEPSLATE_TITANIUM_ORE.get(), "jei.godtechascension.info.titanium_ore");
        addInfo(registration, ModItems.TITANIUM_CASING.get(), "jei.godtechascension.info.titanium_casing");
        addInfo(registration, ModItems.ARC_REACTOR.get(), "jei.godtechascension.info.arc_reactor");
        addInfo(registration, ModItems.ALLOY_SYNTHESIZER.get(), "jei.godtechascension.info.alloy_synthesizer");
        addInfo(registration, ModItems.QUANTUM_WARP_BEACON.get(), "jei.godtechascension.info.quantum_warp_beacon");
        addInfo(registration, ModItems.RAW_TITANIUM.get(), "jei.godtechascension.info.raw_titanium");
        addInfo(registration, ModItems.CRUSHED_RAW_TITANIUM.get(), "jei.godtechascension.info.crushed_raw_titanium");
        addInfo(registration, ModItems.TITANIUM_INGOT.get(), "jei.godtechascension.info.titanium_ingot");
        addInfo(registration, ModItems.TITANIUM_SHEET.get(), "jei.godtechascension.info.titanium_sheet");
        addInfo(registration, ModItems.TITANIUM_ALLOY.get(), "jei.godtechascension.info.titanium_alloy");
        addInfo(registration, ModItems.REINFORCED_MECHANISM.get(), "jei.godtechascension.info.reinforced_mechanism");
        addInfo(registration, ModItems.GOD_CRYSTAL.get(), "jei.godtechascension.info.god_crystal");
        addInfo(registration, ModItems.VOID_SHARD.get(), "jei.godtechascension.info.void_shard");
        addInfo(registration, ModItems.QUANTUM_CIRCUIT.get(), "jei.godtechascension.info.quantum_circuit");
        addInfo(registration, ModItems.SINGULARITY_SHARD.get(), "jei.godtechascension.info.singularity_shard");
        addInfo(registration, ModItems.GOD_CORE.get(), "jei.godtechascension.info.god_core");
        addInfo(registration, ModItems.GOD_PICKAXE.get(), "jei.godtechascension.info.god_pickaxe");
        addInfo(registration, ModItems.GOD_SWORD.get(), "jei.godtechascension.info.god_sword");
        addInfo(registration, ModItems.PLASMA_RAILGUN.get(), "jei.godtechascension.info.plasma_railgun");
        addInfo(registration, ModItems.QUANTUM_MAGNET_RING.get(), "jei.godtechascension.info.quantum_magnet_ring");
        addInfo(registration, ModItems.QUANTUM_WARP_LINKER.get(), "jei.godtechascension.info.quantum_warp_linker");
        addInfo(registration, ModItems.ASCENSION_HELMET.get(), "jei.godtechascension.info.ascension_helmet");
        addInfo(registration, ModItems.ASCENSION_CHESTPLATE.get(), "jei.godtechascension.info.ascension_chestplate");
        addInfo(registration, ModItems.ASCENSION_JETPACK.get(), "jei.godtechascension.info.ascension_jetpack");
        addInfo(registration, ModItems.ASCENSION_LEGGINGS.get(), "jei.godtechascension.info.ascension_leggings");
        addInfo(registration, ModItems.ASCENSION_BOOTS.get(), "jei.godtechascension.info.ascension_boots");
    }

    private static void addInfo(IRecipeRegistration registration, Item item, String translationKey) {
        registration.addIngredientInfo(
                new ItemStack(item),
                VanillaTypes.ITEM_STACK,
                Component.translatable(translationKey)
        );
    }
}
