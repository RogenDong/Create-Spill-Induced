package org.dong.spillinduced.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;

public interface Utils {

    static String getFluidId(FlowingFluid fluid) {
        ResourceLocation r = ForgeRegistries.FLUIDS.getKey(fluid);
        return r == null ? null : r.toString();
    }

    static String getBlockId(Block block) {
        ResourceLocation r = ForgeRegistries.BLOCKS.getKey(block);
        return r == null ? null : r.toString();
    }
}
