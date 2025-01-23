package org.dong.spillinduced.infrastructure.model;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;
import org.dong.spillinduced.utils.Utils;

import java.util.Map;

public class BasaltGen extends CollisionType {

    /**
     * 催化方块
     */
    public String catalystBlock;

    public BasaltGen() {
    }

    public BasaltGen(
            FlowingFluid pipeFluid,
            FlowingFluid impactFluid,
            Block bottomBlock,
            Block catalystBlock,
            Map<Block, Integer> results
    ) {
        super(pipeFluid, impactFluid, bottomBlock, results);
        this.catalystBlock = Utils.getBlockId(catalystBlock);
    }
}