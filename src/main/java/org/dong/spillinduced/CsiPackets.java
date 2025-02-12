package org.dong.spillinduced;

import com.mojang.authlib.GameProfile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.infrastructure.command.SConfigPacket;
import org.dong.spillinduced.utils.ModConfig;

import java.util.Optional;

public class CsiPackets {
    private static final Logger LOGGER = CreateSpillInduced.LOGGER;
    private static final ResourceLocation CHANNEL_NAME = new ResourceLocation(CreateSpillInduced.MOD_ID, "net");
    public static final String NETWORK_VERSION_STR = "1";
    private static SimpleChannel NETWORK;
    private static ModConfig CONFIG;

    public static void registerPackets() {
        CONFIG = ModConfig.getInstance();
        NETWORK = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
                .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
                .networkProtocolVersion(() -> NETWORK_VERSION_STR)
                .simpleChannel();
        NETWORK.registerMessage(
                0,
                SConfigPacket.class,
                SConfigPacket::write,
                SConfigPacket::read,
                SConfigPacket.Handler::onMessage,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void syncServerConfig() {
        LOGGER.info("发送服务端配置数据包...");
        try {
            NETWORK.send(PacketDistributor.ALL.noArg(), new SConfigPacket(CONFIG.getConfigJson()));
        } catch (Exception e) {
            LOGGER.warn("fail to sync server config", e);
        }
    }

    public static void syncServerConfig(ServerPlayer player) {
        if (player == null) return;
        GameProfile profile = player.getGameProfile();
        LOGGER.info("Syncing config to {} ({})", profile.getName(), profile.getId());
        try {
            NETWORK.sendTo(
                    new SConfigPacket(CONFIG.getConfigJson()),
                    player.connection.connection,
                    NetworkDirection.PLAY_TO_CLIENT);
        } catch (Exception e) {
            LOGGER.warn("fail to sync server config", e);
        }
    }
}
