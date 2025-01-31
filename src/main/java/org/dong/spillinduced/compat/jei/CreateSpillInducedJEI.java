package org.dong.spillinduced.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.CreateSpillInduced;
import org.dong.spillinduced.utils.ModConfig;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class CreateSpillInducedJEI implements IModPlugin {
    private static final Logger LOGGER = CreateSpillInduced.LOGGER;
    private static final ResourceLocation ID = new ResourceLocation(CreateSpillInduced.MOD_ID, "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RecipeCategory(ID, registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        ModConfig config = ModConfig.getInstance();
        List<RecipeWrapper> all = config.resultMapping.stream()
                .flatMap(m -> m.results.stream().map(r -> new RecipeWrapper(m, r)))
                .sorted()
                .toList();
        registration.addRecipes(new RecipeType<>(ID, RecipeWrapper.class), all);
    }
}
