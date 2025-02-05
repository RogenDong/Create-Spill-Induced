package org.dong.spillinduced.infrastructure.model;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;
import org.dong.spillinduced.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class DefaultGen {
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
     * 其他方块
     */
    public String otherBlock;

    /**
     * 刷石权重
     */
    public Map<String, Integer> results;

    public DefaultGen() {
    }

    public DefaultGen(
            FlowingFluid pipeFluid,
            FlowingFluid impactFluid,
            Block bottomBlock,
            Block otherBlock,
            Map<Block, Integer> results
    ) {
        this.bottomBlock = bottomBlock == null ? null : Utils.getBlockId(bottomBlock);
        this.otherBlock = otherBlock == null ? null : Utils.getBlockId(otherBlock);
        this.pipeFluid = Utils.getFluidId(pipeFluid);
        this.impactFluid = Utils.getFluidId(impactFluid);
        this.results = new HashMap<>(results.size());
        for (Map.Entry<Block, Integer> e : results.entrySet()) {
            String bid = Utils.getBlockId(e.getKey());
            if (bid == null) continue;
            this.results.put(bid, e.getValue());
        }
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder(256);
        if (results != null && !results.isEmpty()) {
            results.forEach((k, v) -> tmp.append(k).append('@').append(v).append(','));
        }
        return String.format("(%s + %s on %s,%s)=[%s]", pipeFluid, impactFluid, bottomBlock, otherBlock, tmp);
    }
}