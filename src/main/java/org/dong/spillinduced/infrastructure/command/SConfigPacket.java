package org.dong.spillinduced.infrastructure.command;

import com.simibubi.create.foundation.utility.Components;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.CreateSpillInduced;
import org.dong.spillinduced.utils.ModConfig;

import java.util.function.Supplier;

/**
 * 网络数据包：服务端配置
 */
public class SConfigPacket {
    private static final Logger LOGGER = CreateSpillInduced.LOGGER;

    private final String configJson;

    public SConfigPacket(String json) {
        this.configJson = json;
    }

    public SConfigPacket(ModConfig config) {
        this.configJson = config.getConfigJson();
    }

    public static SConfigPacket read(FriendlyByteBuf buffer) {
        return new SConfigPacket(buffer.readUtf());
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(configJson);
    }

    public static class Handler {
        public static void onMessage(SConfigPacket msg, Supplier<Context> ctx) {
            String s = msg.configJson;
            Context cc = ctx.get();
            cc.enqueueWork(() -> trySetConfig(s, cc))
                    .exceptionally(e -> {
                        LOGGER.error("Failed to apply config from the server: {}", s);
                        LOGGER.catching(e);
                        return null;
                    });
            cc.setPacketHandled(true);
        }
    }

    private static void trySetConfig(String json, Context ctx) {
        if (ctx.getDirection() != NetworkDirection.PLAY_TO_CLIENT && !CreateSpillInduced.isJeiLoaded()) return;

//        LOGGER.info("Received config from the server: {}", json);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        try {
            ModConfig.getInstance().reload(json);
//            player.displayClientMessage(Components.literal("已同步服务端配置。"), false);
        } catch (Exception e) {
            player.displayClientMessage(Components.literal("Something went wrong while trying to synchronizing server config. Check the client logs for more information"), false);
            LOGGER.warn("Exception during client-side config value set:", e);
        }
    }
}
