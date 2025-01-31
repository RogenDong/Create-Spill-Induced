package org.dong.spillinduced.infrastructure.model;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.Constants;
import org.dong.spillinduced.CreateSpillInduced;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;

public class ResultMapping {
    private static final Logger LOGGER = CreateSpillInduced.LOGGER;
    public Fluid pipeFluid;
    public Fluid impactFluid;
    public Block bottomBlock;
    public Block otherBlock;
    public List<WeightedWrapper> results;

    public ResultMapping(DefaultGen config) throws InvalidPropertiesFormatException {
        pipeFluid = getFluid(config.pipeFluid);
        impactFluid = getFluid(config.impactFluid);
        bottomBlock = getBlock(config.bottomBlock);
        otherBlock = getBlock(config.otherBlock);
        results = new ArrayList<>(config.results.size());
        for (Map.Entry<String, Integer> e : config.results.entrySet()) {
            Block r = getBlock(e.getKey());
            results.add(new WeightedWrapper(r, e.getValue()));
        }
    }

    private static ResourceLocation getResLoc(String id) throws InvalidPropertiesFormatException {
        ResourceLocation rl = ResourceLocation.tryParse(id);
        if (rl == null) throw new InvalidPropertiesFormatException(String.format("\"%s\" is incorrect!", id));
        return rl;
    }

    private static Fluid getFluid(String id) throws InvalidPropertiesFormatException {
        Fluid f = ForgeRegistries.FLUIDS.getValue(getResLoc(id));
        if (f == null) throw new InvalidPropertiesFormatException(String.format("\"%s\" is incorrect!", id));
        return f;
    }

    private static Block getBlock(String id) throws InvalidPropertiesFormatException {
        if (id == null || id.isEmpty() || Constants.ID_AIR.contains(id)) return Blocks.AIR;
        Block f = ForgeRegistries.BLOCKS.getValue(getResLoc(id));
        if (f == null) throw new InvalidPropertiesFormatException(String.format("\"%s\" is incorrect!", id));
        return f;
    }
}
