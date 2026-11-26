package com.abdur.godtechascension.compat;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.recipe.AlloyRecipe;
import com.abdur.godtechascension.registry.ModItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * JEI Recipe Category for the Alloy Synthesizer.
 */
public class AlloyRecipeCategory implements IRecipeCategory<AlloyRecipe> {

    public static final RecipeType<AlloyRecipe> TYPE = RecipeType.create(
            GodTechAscension.MODID, "alloy_synthesizing", AlloyRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public AlloyRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(140, 48);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.ALLOY_SYNTHESIZER.get()));
    }

    @Override
    public RecipeType<AlloyRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.godtechascension.alloy_synthesizer");
    }

    @Override
    public int getWidth() {
        return 140;
    }

    @Override
    public int getHeight() {
        return 48;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlloyRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 12).addIngredients(recipe.inputA());
        builder.addSlot(RecipeIngredientRole.INPUT, 34, 12).addIngredients(recipe.inputB());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 106, 12).addItemStack(recipe.output());
    }

    @Override
    public void draw(AlloyRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics g, double mouseX, double mouseY) {
        background.draw(g, 0, 0);
        g.renderOutline(9, 11, 18, 18, 0xFF39C5E8);
        g.renderOutline(33, 11, 18, 18, 0xFF39C5E8);
        g.renderOutline(105, 11, 18, 18, 0xFF55FF55);
        g.drawString(Minecraft.getInstance().font, "→", 68, 16, 0x39C5E8, false);
        g.drawString(
                Minecraft.getInstance().font,
                String.format("%,d FE", recipe.energyCost()),
                10,
                36,
                0x55FFFF,
                false
        );
    }
}
