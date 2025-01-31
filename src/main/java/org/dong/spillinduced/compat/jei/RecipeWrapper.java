package org.dong.spillinduced.compat.jei;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.dong.spillinduced.infrastructure.model.ResultMapping;
import org.dong.spillinduced.infrastructure.model.WeightedWrapper;
import org.jetbrains.annotations.NotNull;

public class RecipeWrapper implements Comparable<RecipeWrapper> {
    public Fluid pipeFluid;
    public Fluid impactFluid;
    public Block bottomBlock;
    public Block otherBlock;
    public WeightedWrapper result;

    public RecipeWrapper(ResultMapping mapping, WeightedWrapper ww) {
        pipeFluid = mapping.pipeFluid;
        impactFluid = mapping.impactFluid;
        bottomBlock = mapping.bottomBlock;
        otherBlock = mapping.otherBlock;
        result = ww;
    }

    @Override
    public int compareTo(@NotNull RecipeWrapper o) {
        return Integer.compare(o.result.getWeight().asInt(), result.getWeight().asInt());
    }
}
