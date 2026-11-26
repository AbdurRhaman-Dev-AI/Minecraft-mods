package com.abdur.godtechascension.registry;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.recipe.AlloyRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers custom RecipeTypes and RecipeSerializers for GodTech: Ascension.
 */
public final class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, GodTechAscension.MODID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, GodTechAscension.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<AlloyRecipe>> ALLOY_TYPE =
            TYPES.register("alloy_synthesizing", () -> RecipeType.simple(
                    ResourceLocation.fromNamespaceAndPath(GodTechAscension.MODID, "alloy_synthesizing")
            ));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AlloyRecipe>> ALLOY_SERIALIZER =
            SERIALIZERS.register("alloy_synthesizing", AlloyRecipe.Serializer::new);

    private ModRecipes() {
    }
}
