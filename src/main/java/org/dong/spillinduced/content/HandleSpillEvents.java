package org.dong.spillinduced.content;

import com.simibubi.create.api.event.PipeCollisionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.CreateSpillInduced;
import org.dong.spillinduced.infrastructure.model.ResultMapping;
import org.dong.spillinduced.infrastructure.model.WeightedWrapper;
import org.dong.spillinduced.utils.ModConfig;
import org.dong.spillinduced.utils.Utils;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber
public class HandleSpillEvents {
    private static final Logger LOGGER = CreateSpillInduced.LOGGER;

    private static Block getAround(Level world, BlockPos dir) {
        BlockState s = world.getBlockState(dir);
        return s.isAir() ? null : s.getBlock();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void handleSpillCollision(PipeCollisionEvent.Spill event) {
        Level world = event.getLevel();
        Fluid pf = event.getPipeFluid();
        Fluid wf = event.getWorldFluid();
        BlockPos eventPos = event.getPos();
        if (world.getFluidState(eventPos).isSource()) return;
        Block bottomBlock = world.getBlockState(eventPos.below()).getBlock();

        // get around blocks
        Block a = getAround(world, eventPos.above()),
                e = getAround(world, eventPos.east()),
                s = getAround(world, eventPos.south()),
                w = getAround(world, eventPos.west()),
                n = getAround(world, eventPos.north());

        List<WeightedWrapper> ls = null;
        for (ResultMapping m : ModConfig.getInstance().resultMapping) {
            if (!m.pipeFluid.isSame(pf) || !m.impactFluid.isSame(wf))
                continue;

            // 如果配置下方方块为空气，表示任意匹配
            if (m.bottomBlock != Blocks.AIR && m.bottomBlock != bottomBlock)
                continue;

            // 不接受配置其他方块为空气
            if (m.otherBlock != null && m.otherBlock != Blocks.AIR) {
                if (a == m.otherBlock || e == m.otherBlock || s == m.otherBlock || w == m.otherBlock || n == m.otherBlock) {
                    ls = m.results;
                    break;
                }
                continue;
            }

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
