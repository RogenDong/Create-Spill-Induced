package org.dong.leakinduced;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(value = CreateLeakInduced.MOD_ID)
public class CreateLeakInduced {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "CreateLeakInduced";

    public CreateLeakInduced() {
        //MinecraftForge.EVENT_BUS.register(ForgeEventHandler.class);
        //FMLJavaModLoadingContext.get().getModEventBus().register(ModEventHandler.class);
    }
}
