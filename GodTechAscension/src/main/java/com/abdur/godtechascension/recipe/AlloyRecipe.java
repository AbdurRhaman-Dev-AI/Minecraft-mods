package com.abdur.godtechascension.recipe;

import com.abdur.godtechascension.registry.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * Custom FE-powered 2-input recipe for the Alloy Synthesizer.
 */
public record AlloyRecipe(
        Ingredient inputA,
        Ingredient inputB,
        ItemStack output,
        int energyCost,
        int processTicks
) implements Recipe<AlloyRecipeInput> {

    @Override
    public boolean matches(AlloyRecipeInput input, Level level) {
        return (inputA.test(input.first()) && inputB.test(input.second()))
                || (inputA.test(input.second()) && inputB.test(input.first()));
    }

    @Override
    public ItemStack assemble(AlloyRecipeInput input, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ALLOY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ALLOY_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<AlloyRecipe> {

        public static final MapCodec<AlloyRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("input_a").forGetter(AlloyRecipe::inputA),
                Ingredient.CODEC_NONEMPTY.fieldOf("input_b").forGetter(AlloyRecipe::inputB),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(AlloyRecipe::output),
                Codec.INT.optionalFieldOf("energy", 4000).forGetter(AlloyRecipe::energyCost),
                Codec.INT.optionalFieldOf("ticks", 40).forGetter(AlloyRecipe::processTicks)
        ).apply(inst, AlloyRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, AlloyRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, AlloyRecipe::inputA,
                Ingredient.CONTENTS_STREAM_CODEC, AlloyRecipe::inputB,
                ItemStack.STREAM_CODEC, AlloyRecipe::output,
                ByteBufCodecs.VAR_INT, AlloyRecipe::energyCost,
                ByteBufCodecs.VAR_INT, AlloyRecipe::processTicks,
                AlloyRecipe::new
        );

        @Override
        public MapCodec<AlloyRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AlloyRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
