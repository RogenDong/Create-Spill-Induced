package org.dong.spillinduced.content;

import com.simibubi.create.api.event.PipeCollisionEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber
public class HandleSpillEvents {
    private final Logger LOGGER = LogManager.getLogger();

    private static final Block[] POOL = new Block[]{Blocks.NETHERRACK, Blocks.DIORITE, Blocks.GRANITE, Blocks.ANDESITE, Blocks.TUFF};

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void spillWithFlowingWater(PipeCollisionEvent.Spill event) {
        Fluid pf = event.getPipeFluid();
        Fluid wf = event.getWorldFluid();
        if (pf != Fluids.LAVA || wf != Fluids.FLOWING_WATER) return;

        Level world = event.getLevel();

        BlockState below = world.getBlockState(event.getPos().below());
        if (below.getBlock() == Blocks.BEDROCK) {
            Block block = POOL[world.random.nextInt(0, POOL.length)];
            event.setState(block.defaultBlockState());
        }
    }
}
