package org.dong.spillinduced;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(value = CreateSpillInduced.MOD_ID)
public class CreateSpillInduced {

    public static final String MOD_ID = "createspillinduced";
    public static final Logger LOGGER = LogManager.getLogger();

    public CreateSpillInduced() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
