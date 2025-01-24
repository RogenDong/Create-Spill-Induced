package org.dong.spillinduced.infrastructure.model;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;
import org.dong.spillinduced.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public abstract class CollisionType {

    /**
     * 管道内流体
     */
    public String pipeFluid;

    /**
     * 管道外流体
     */
    public String impactFluid;

    /**
     * 垫底方块
     */
    public String bottomBlock;

    /**
     * 刷石权重
     */
    public Map<String, Integer> results;

    public CollisionType() {
    }

    public CollisionType(
            FlowingFluid pipeFluid,
            FlowingFluid impactFluid,
            Block bottomBlock,
            Map<Block, Integer> results
    ) {
        this.pipeFluid = Utils.getFluidId(pipeFluid);
        this.impactFluid = Utils.getFluidId(impactFluid);
        this.bottomBlock = Utils.getBlockId(bottomBlock);
        this.results = new HashMap<>(results.size());
        for (Map.Entry<Block, Integer> e : results.entrySet()) {
            String bid = Utils.getBlockId(e.getKey());
            if (bid == null) continue;
            this.results.put(bid, e.getValue());
        }
    }
}
