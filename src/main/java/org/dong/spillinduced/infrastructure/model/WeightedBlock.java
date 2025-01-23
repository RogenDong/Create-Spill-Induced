package org.dong.spillinduced.infrastructure.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class WeightedBlock {
    public String id;
    public int weight;

    public WeightedBlock(String id, int weight) {
        this.id = id;
        this.weight = weight;
    }

    public static WeightedBlock fromBlock(Block block, int weight) {
        final String id = ForgeRegistries.BLOCKS.getKey(block).toString();
        return new WeightedBlock(id, weight);
    }

    public Block tryGetBlock() {
        ResourceLocation r = ResourceLocation.tryParse(id);
        if (r == null) return null;
        return ForgeRegistries.BLOCKS.getValue(r);
    }

}