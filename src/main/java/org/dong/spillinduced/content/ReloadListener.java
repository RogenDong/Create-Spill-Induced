package org.dong.spillinduced.content;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.CreateSpillInduced;
import org.dong.spillinduced.utils.ModConfig;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@EventBusSubscriber
public class ReloadListener implements PreparableReloadListener {
    private static final Logger LOGGER = CreateSpillInduced.LOGGER;
    private static ReloadListener INSTANCE = null;
    private static ModConfig CONFIG = null;

    /**
     * 监听服务端 ReloadListener
     */
    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        LOGGER.info("注册服务端 ReloadListener...");
        if (INSTANCE == null) INSTANCE = new ReloadListener();
        if (CONFIG == null) CONFIG = ModConfig.getInstance();
        event.addListener(INSTANCE);
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier b,
                                          ResourceManager m,
                                          ProfilerFiller preparationsProfiler,
                                          ProfilerFiller reloadProfiler,
                                          Executor backgroundExecutor,
                                          Executor gameExecutor) {
        LOGGER.info("服务端 reload...");
        return CompletableFuture
                .runAsync(CONFIG::reload, gameExecutor)
                .thenCompose(b::wait);
    }
}
