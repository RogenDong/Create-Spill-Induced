package org.dong.spillinduced.infrastructure.model;

import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
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
    public List<BasaltGen> basaltLike = new ArrayList<>(4);

    /**
     * 圆石模式的配置
     */
    public List<CobbleGen> cobbleLike = new ArrayList<>(4);

    /**
     * 石头模式的配置
     */
    public List<StoneGen> stoneLike = new ArrayList<>(4);

    public static ConfigRootNode defaultConfig() {
        HashMap<Block, Integer> results = new HashMap<>();
        ConfigRootNode def = new ConfigRootNode();

//        results.put(Blocks.STONE, 100);
//        def.stoneLike.add(new StoneGen(Fluids.LAVA, Fluids.WATER, Blocks.BEDROCK, results));
//        results.clear();

        results.put(Blocks.MAGMA_BLOCK, 32);
        results.put(Blocks.NETHERRACK, 32);
        results.put(Blocks.BLACKSTONE, 32);
        results.put(Blocks.BASALT, 32);
        // AllFluids.CHOCOLATE.getSource()
        def.basaltLike.add(new BasaltGen(Fluids.WATER, Fluids.LAVA, Blocks.SOUL_SOIL, Blocks.CRYING_OBSIDIAN, results));
        results.clear();

        results.put(AllPaletteStoneTypes.LIMESTONE.baseBlock.get(), 32);
        results.put(Blocks.ANDESITE, 32);
        results.put(Blocks.GRANITE, 32);
        results.put(Blocks.DIORITE, 32);
        def.cobbleLike.add(new CobbleGen(Fluids.LAVA, Fluids.WATER, Blocks.BEDROCK, results));
        return def;
    }
}
