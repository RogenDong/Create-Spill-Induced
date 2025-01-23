package org.dong.spillinduced.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.infrastructure.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * 读取配置
 */
public class ModConfig {
    private final Logger LOGGER = LogManager.getLogger();

    private static ModConfig instance;

    public ConfigRootNode config;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 配置文件
     */
    private final File FILE_CFG = new File("config", "create-spill-induced.json5");

    public static ModConfig getInstance() {
        return instance;
    }

    public void init() {
        instance = this;
        if (FILE_CFG.exists()) {
            try {
                config = gson.fromJson(Files.readString(FILE_CFG.toPath(), StandardCharsets.UTF_8), ConfigRootNode.class);
            } catch (IOException ioe) {
                LOGGER.error("无法读取配置文件!!", ioe);
            } catch (OutOfMemoryError ome) {
                LOGGER.error("配置文件过大!!", ome);
            } catch (SecurityException se) {
                LOGGER.error("没有配置文件的读取权限!!", se);
            } catch (Exception e) {
                LOGGER.error("读取配置文件失败!!", e);
            }
        }
        if (config == null) {
            config = ConfigRootNode.defaultConfig();
            if (FILE_CFG.exists()) FILE_CFG.delete();
            try {
                Files.write(FILE_CFG.toPath(), gson.toJson(config).getBytes(StandardCharsets.UTF_8));
            } catch (IOException ioe) {
                LOGGER.error("无法写入配置文件!!", ioe);
            } catch (SecurityException se) {
                LOGGER.error("没有配置文件的写入权限!!", se);
            } catch (Exception e) {
                LOGGER.error("写入配置文件失败!!", e);
            }
        }
    }
}
