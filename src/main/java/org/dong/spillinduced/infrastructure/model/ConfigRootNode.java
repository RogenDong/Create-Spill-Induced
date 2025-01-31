package org.dong.spillinduced.infrastructure.model;

import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigRootNode {
    public List<DefaultGen> gen = new ArrayList<>();

    public static ConfigRootNode defaultConfig() {
        HashMap<Block, Integer> results = new HashMap<>();
        ConfigRootNode def = new ConfigRootNode();

        results.put(Blocks.MAGMA_BLOCK, 32);
        results.put(Blocks.NETHERRACK, 32);
        results.put(Blocks.BLACKSTONE, 32);
        results.put(Blocks.BASALT, 32);
        // AllFluids.CHOCOLATE.getSource()
        def.gen.add(new DefaultGen(Fluids.WATER, Fluids.LAVA, Blocks.SOUL_SOIL, Blocks.CRYING_OBSIDIAN, results));
        results.clear();

        results.put(AllPaletteStoneTypes.LIMESTONE.baseBlock.get(), 32);
        results.put(Blocks.ANDESITE, 32);
        results.put(Blocks.GRANITE, 32);
        results.put(Blocks.DIORITE, 32);
        def.gen.add(new DefaultGen(Fluids.LAVA, Fluids.WATER, Blocks.BEDROCK, null, results));
        return def;
    }
}
