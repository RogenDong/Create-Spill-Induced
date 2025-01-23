package org.dong.spillinduced;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.utils.ModConfig;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(value = CreateSpillInduced.MOD_ID)
public class CreateSpillInduced {

    public static final String MOD_ID = "createspillinduced";

    public CreateSpillInduced() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModConfig config = new ModConfig();
        config.init();
    }
}
