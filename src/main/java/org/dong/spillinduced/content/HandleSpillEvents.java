package org.dong.spillinduced.content;

import com.simibubi.create.api.event.PipeCollisionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.CreateSpillInduced;
import org.dong.spillinduced.infrastructure.model.BasaltGen;
import org.dong.spillinduced.infrastructure.model.ResultMapping;
import org.dong.spillinduced.infrastructure.model.StoneGen;
import org.dong.spillinduced.infrastructure.model.WeightedWrapper;
import org.dong.spillinduced.utils.ModConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber
public class HandleSpillEvents {
    private static final Logger LOGGER = CreateSpillInduced.LOGGER;

    /**
     * TODO: 事件触发位置周围有接触面的方块
     * 1. 排除管道
     * 2. 排除流体
     * 3. 排除下方方块
     */
    private static final List<Block> aroundBlocks = new ArrayList<>(5);

    private static void getAroundBlocks(PipeCollisionEvent.Spill event) {
        aroundBlocks.clear();
        Level world = event.getLevel();
        BlockPos eventPos = event.getPos();

        // around
        BlockState[] tmpBlocks = new BlockState[]{
                world.getBlockState(eventPos.above()),
                world.getBlockState(eventPos.east()),
                world.getBlockState(eventPos.south()),
                world.getBlockState(eventPos.west()),
                world.getBlockState(eventPos.north())
        };

        for (BlockState ab : tmpBlocks) {
            if (!ab.isAir() && ab.getFluidState().is(Fluids.EMPTY))
                aroundBlocks.add(ab.getBlock());
        }
    }

    private static boolean isAround(Block b) {
        for (Block a : aroundBlocks) {
            if (a == b) return true;
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void handleSpillCollision(PipeCollisionEvent.Spill event) {
        Level world = event.getLevel();
        Fluid pf = event.getPipeFluid();
        Fluid wf = event.getWorldFluid();
        BlockPos eventPos = event.getPos();
        if (world.getFluidState(eventPos).isSource()) return;
        Block bottomBlock = world.getBlockState(eventPos.below()).getBlock();
        getAroundBlocks(event);

        List<WeightedWrapper> ls = null;
        for (ResultMapping m : ModConfig.getInstance().resultMapping) {
            if (m.genType == StoneGen.class) continue;
            if (!m.pipeFluid.isSame(pf) || !m.impactFluid.isSame(wf))
                continue;

            // 如果配置下方方块为空气，表示任意匹配
            if (m.bottomBlock != Blocks.AIR && m.bottomBlock != bottomBlock)
                continue;

            // 不接受配置其他方块为空气
            if (m.genType == BasaltGen.class && m.otherBlock != null && m.otherBlock != Blocks.AIR && !isAround(m.otherBlock))
                continue;

            ls = m.results;
            break;
        }
        if (ls == null || ls.isEmpty()) return;

        Optional<WeightedWrapper> it = WeightedRandom.getRandomItem(world.getRandom(), ls);
        if (it.isPresent()) {
            Block b = it.get().getBlock();
            event.setState(b.defaultBlockState());
        }
    }
}
