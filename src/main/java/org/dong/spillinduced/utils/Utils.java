package org.dong.spillinduced.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

public interface Utils {

    static String getFluidId(FlowingFluid fluid) {
        ResourceLocation r = ForgeRegistries.FLUIDS.getKey(fluid);
        return r == null ? null : r.toString();
    }

    static String getBlockId(Block block) {
        ResourceLocation r = ForgeRegistries.BLOCKS.getKey(block);
        return r == null ? null : r.toString();
    }

    static boolean serverIsNotReady() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return true;
        return !FMLEnvironment.dist.isDedicatedServer();
    }
}
