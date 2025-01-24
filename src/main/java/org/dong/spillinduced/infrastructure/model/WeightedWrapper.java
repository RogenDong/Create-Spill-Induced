package org.dong.spillinduced.infrastructure.model;

import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class WeightedWrapper implements WeightedEntry {

    private final Block block;

    private final WeightedEntry.Wrapper<Block> wrapper;

    public WeightedWrapper(Block block, int weight) {
        wrapper = WeightedEntry.wrap(block, weight);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public @NotNull Weight getWeight() {
        return wrapper.getWeight();
    }
}