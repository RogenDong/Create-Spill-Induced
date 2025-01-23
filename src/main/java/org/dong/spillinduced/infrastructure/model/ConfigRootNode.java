package org.dong.spillinduced.infrastructure.model;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigRootNode {
    /**
     * 玄武岩模式的配置
     */
    public List<BasaltGen> basaltLike;

    /**
     * 圆石模式的配置
     */
    public List<CobbleGen> cobbleLike;

    /**
     * 石头模式的配置
     */
    public List<StoneGen> stoneLike;

    public static ConfigRootNode defaultConfig() {
        HashMap<Block, Integer> results = new HashMap<>();
        ConfigRootNode def = new ConfigRootNode();
        def.basaltLike = new ArrayList<>(1);
        def.cobbleLike = new ArrayList<>(4);
        def.stoneLike = new ArrayList<>(1);

        results.put(Blocks.BASALT, 100);
        def.basaltLike.add(new BasaltGen(Fluids.LAVA, Fluids.WATER, Blocks.SOUL_SOIL, Blocks.BLUE_ICE, results));
        results.clear();

        results.put(Blocks.NETHERRACK, 25);
        results.put(Blocks.ANDESITE, 25);
        results.put(Blocks.GRANITE, 25);
        results.put(Blocks.DIORITE, 25);
        def.cobbleLike.add(new CobbleGen(Fluids.LAVA, Fluids.WATER, Blocks.BEDROCK, results));
        results.clear();

        results.put(Blocks.STONE, 100);
        def.stoneLike.add(new StoneGen(Fluids.LAVA, Fluids.WATER, Blocks.BEDROCK, results));
        return def;
    }
}
