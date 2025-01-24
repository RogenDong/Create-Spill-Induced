package org.dong.spillinduced.content;

import com.simibubi.create.api.event.PipeCollisionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.CreateSpillInduced;
import org.dong.spillinduced.infrastructure.model.CobbleGen;
import org.dong.spillinduced.infrastructure.model.ResultMapping;
import org.dong.spillinduced.infrastructure.model.WeightedWrapper;
import org.dong.spillinduced.utils.ModConfig;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber
public class HandleSpillEvents {
    private static final Logger LOGGER = CreateSpillInduced.LOGGER;

//    /**
//     * TODO: 事件触发位置周围有接触面的方块
//     * 1. 排除管道
//     * 2. 排除流体
//     * 3. 排除下方方块
//     */
//    private static final List<BlockState> aroundBlocks = new ArrayList<>(4);
//    private static void getAroundBlock(PipeCollisionEvent.Spill event) {
//        aroundBlocks.clear();
//        Level world = event.getLevel();
//        Fluid pf = event.getPipeFluid();
//        Fluid wf = event.getWorldFluid();
//        BlockPos eventPos = event.getPos();
//
//        BlockState above = world.getBlockState(eventPos.above());
//        Block aboveBlock = above.getBlock();
//        FluidState tmpFS = above.getFluidState();
//        if (!tmpFS.is(Fluids.EMPTY))
//            LOGGER.info("block {} have fluid state = {}", aboveBlock.getName(), tmpFS.getType());
//    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void handleSpillCollision(PipeCollisionEvent.Spill event) {
        Level world = event.getLevel();
        Fluid pf = event.getPipeFluid();
        Fluid wf = event.getWorldFluid();
        BlockPos eventPos = event.getPos();
        if (world.getFluidState(eventPos).isSource()) return;
        Block bottomBlock = world.getBlockState(eventPos.below()).getBlock();

        // TODO: 兼容“刷花岗岩”、“刷石头”模式
        List<WeightedWrapper> ls = null;
        for (ResultMapping m : ModConfig.getInstance().resultMapping) {
            if (m.genType != CobbleGen.class) continue;
            if (m.pipeFluid.isSame(pf) && m.impactFluid.isSame(wf) && m.bottomBlock == bottomBlock) {
                ls = m.results;
                break;
            }
        }
        if (ls == null || ls.isEmpty()) return;

        Optional<WeightedWrapper> it = WeightedRandom.getRandomItem(world.getRandom(), ls);
        if (it.isPresent()) {
            Block b = it.get().getBlock();
            event.setState(b.defaultBlockState());
        }
    }
}
