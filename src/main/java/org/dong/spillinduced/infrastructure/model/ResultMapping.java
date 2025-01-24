package org.dong.spillinduced.infrastructure.model;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.CreateSpillInduced;

import java.util.*;

public class ResultMapping {
    private static final Logger LOGGER = CreateSpillInduced.LOGGER;
    public Class<? extends CollisionType> genType;
    public Fluid pipeFluid;
    public Fluid impactFluid;
    public Block bottomBlock;
    public Block otherBlock;
    public List<WeightedWrapper> results;

    public ResultMapping(CollisionType config) throws InvalidPropertiesFormatException {
        pipeFluid = getFluid(config.pipeFluid);
        impactFluid = getFluid(config.impactFluid);
        bottomBlock = getBlock(config.bottomBlock);
        results = new ArrayList<>(config.results.size());
        for (Map.Entry<String, Integer> e : config.results.entrySet()) {
            Block r = getBlock(e.getKey());
            results.add(new WeightedWrapper(r, e.getValue()));
        }
        if (config instanceof BasaltGen bg) {
            otherBlock = getBlock(bg.otherBlock);
            genType = BasaltGen.class;
        } else if (config instanceof CobbleGen)
            genType = CobbleGen.class;
        else if (config instanceof StoneGen)
            genType = StoneGen.class;
        else {
            genType = config.getClass();
            LOGGER.info("custom collision type: {}", genType);
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
        Block f = ForgeRegistries.BLOCKS.getValue(getResLoc(id));
        if (f == null) throw new InvalidPropertiesFormatException(String.format("\"%s\" is incorrect!", id));
        return f;
    }
}
