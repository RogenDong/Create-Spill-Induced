package org.dong.spillinduced.content;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.CreateSpillInduced;
import org.dong.spillinduced.CsiPackets;
import org.dong.spillinduced.utils.ModConfig;
import org.dong.spillinduced.utils.Utils;

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

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (Utils.serverIsNotReady()) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        CsiPackets.syncServerConfig(player);
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier b,
                                          ResourceManager m,
                                          ProfilerFiller preparationsProfiler,
                                          ProfilerFiller reloadProfiler,
                                          Executor backgroundExecutor,
                                          Executor gameExecutor) {
        LOGGER.info("reload...");
        return CompletableFuture
                .runAsync(this::run, gameExecutor)
                .thenCompose(b::wait);
    }

    private void run() {
        CONFIG.reload();

        if (Utils.serverIsNotReady()) return;
        PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();
        if (playerList.getPlayerCount() < 1) return;

        CsiPackets.syncServerConfig();
    }
}
