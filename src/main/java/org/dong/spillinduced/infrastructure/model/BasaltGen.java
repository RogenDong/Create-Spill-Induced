package org.dong.spillinduced.infrastructure.model;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;
import org.dong.spillinduced.utils.Utils;

import java.util.Map;

public class BasaltGen extends CollisionType {

    public String otherBlock;

    public BasaltGen() {
    }

    public BasaltGen(
            FlowingFluid pipeFluid,
            FlowingFluid impactFluid,
            Block bottomBlock,
            Block otherBlock,
            Map<Block, Integer> results
    ) {
        super(pipeFluid, impactFluid, bottomBlock, results);
        this.otherBlock = Utils.getBlockId(otherBlock);
    }
}