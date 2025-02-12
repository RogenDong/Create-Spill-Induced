package org.dong.spillinduced.infrastructure.command;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
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

	public boolean handle(Context context) {
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> trySetConfig(configJson)));
		return true;
	}

	public static class Handler {
		public static void onMessage(SConfigPacket message, Supplier<Context> context) {
			String s = message.configJson;
			context.get().enqueueWork(() -> {
				trySetConfig(s); // clear the server config last time we applied
				LOGGER.info("Received config from the server: {}", s);
			}).exceptionally(e -> {
				LOGGER.error("Failed to apply config from the server: {}", s);
				LOGGER.catching(e);
				return null;
			});
			context.get().setPacketHandled(true);
		}
	}

	private static void trySetConfig(String json) {
		LOGGER.info("接收服务端配置数据包，同步配置...");
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;

		try {
			ModConfig.getInstance().reload(json);

			player.displayClientMessage(Components.literal("已同步服务端配置。"), false);
		} catch (Exception e) {
			player.displayClientMessage(Components.literal("Something went wrong while trying to synchronizing server config. Check the client logs for more information"), false);
			LOGGER.warn("Exception during client-side config value set:", e);
		}
	}
}
