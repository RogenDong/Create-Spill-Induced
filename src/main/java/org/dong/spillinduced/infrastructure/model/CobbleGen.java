package org.dong.spillinduced.infrastructure.model;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.Map;

public class CobbleGen extends CollisionType {
    public CobbleGen() {
    }

    public CobbleGen(FlowingFluid a, FlowingFluid b, Block c, Map<Block, Integer> d) {
        super(a, b, c, d);
    }
}