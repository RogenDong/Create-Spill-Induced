package org.dong.spillinduced.compat.jei;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecipeCategory implements IRecipeCategory<RecipeWrapper> {

    private static final int OFFSET = 18;
    private final ResourceLocation resId;
    private final IDrawable background;
    private final IDrawable icon;

    public RecipeCategory(ResourceLocation id, IGuiHelper guiHelper) {
        icon = new DoubleItemIcon(AllBlocks.FLUID_PIPE::asStack, Items.COBBLESTONE::getDefaultInstance);
        background = guiHelper.createBlankDrawable(72, 54);
        resId = id;
    }

    @Override
    public @NotNull RecipeType<RecipeWrapper> getRecipeType() {
        return new RecipeType<>(resId, RecipeWrapper.class);
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable(I18n.get("createspillinduced.jei.title"));
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, RecipeWrapper recipe, @NotNull IFocusGroup g) {
        // other block
        if (recipe.otherBlock != null && recipe.otherBlock != Blocks.AIR) {
            builder.addSlot(RecipeIngredientRole.INPUT, OFFSET * 2, 0).addItemStack(new ItemStack(recipe.otherBlock));
        }
        // pipe fluid
        builder.addSlot(RecipeIngredientRole.INPUT, 0, OFFSET).addFluidStack(recipe.pipeFluid, 1000);
        builder.addSlot(RecipeIngredientRole.INPUT, OFFSET, OFFSET).addItemStack(AllBlocks.FLUID_PIPE.asStack());
        // result
        builder.addSlot(RecipeIngredientRole.OUTPUT, OFFSET * 2, OFFSET).addItemStack(new ItemStack(recipe.result.getBlock()));
        // impact fluid
        builder.addSlot(RecipeIngredientRole.INPUT, OFFSET * 3, OFFSET).addFluidStack(recipe.impactFluid, 500);
        // bottom
        if (recipe.bottomBlock != Blocks.AIR) {
            builder.addSlot(RecipeIngredientRole.INPUT, OFFSET * 2, OFFSET * 2).addItemStack(new ItemStack(recipe.bottomBlock));
        }
        // show weight
    }

    @Override
    public void draw(RecipeWrapper recipe, @NotNull IRecipeSlotsView v, GuiGraphics guiGraphics, double x, double y) {
        Minecraft minecraft = Minecraft.getInstance();
        Weight w = recipe.result.getWeight();
        minecraft.font.drawInBatch(I18n.get("createspillinduced.jei.weight"),
                0, 0, 0xFF808080, false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880, false);
        minecraft.font.drawInBatch(w.toString(),
                4, 8, 0xFF808080, false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880, false);
    }
}
