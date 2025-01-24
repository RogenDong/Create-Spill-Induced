package org.dong.spillinduced.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.Logger;
import org.dong.spillinduced.CreateSpillInduced;
import org.dong.spillinduced.infrastructure.model.BasaltGen;
import org.dong.spillinduced.infrastructure.model.CollisionType;
import org.dong.spillinduced.infrastructure.model.ConfigRootNode;
import org.dong.spillinduced.infrastructure.model.ResultMapping;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

/**
 * 读取配置
 */
public class ModConfig {
    private static final Logger LOGGER = CreateSpillInduced.LOGGER;
    private static ModConfig instance;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    /**
     * 配置文件
     */
    private final File FILE_CFG = new File("config", "create-spill-induced.json");
    public final List<ResultMapping> resultMapping = new ArrayList<>(8);

    public static ModConfig getInstance() {
        return instance;
    }

    public void init() {
        instance = this;
        ConfigRootNode config = null;
        // 检查配置文件是否存在
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

        // mapping
        config.basaltLike.forEach(this::mapping);
        config.cobbleLike.forEach(this::mapping);
        config.stoneLike.forEach(this::mapping);
    }

    private void mapping(CollisionType gen) {
        try {
            ResultMapping rm = new ResultMapping(gen);
            String onBlock = gen instanceof BasaltGen bg
                    ? bg.bottomBlock + ',' + bg.otherBlock
                    : gen.bottomBlock;
            List<String> tmp = gen.results.entrySet().stream()
                    .map(e -> e.getKey() + '|' + e.getValue()).toList();
            LOGGER.info("{}: ({} + {} on {})=[{}]",
                    rm.genType.getSimpleName(), gen.pipeFluid, gen.impactFluid, onBlock, String.join(", ", tmp));
            resultMapping.add(rm);
        } catch (InvalidPropertiesFormatException ie) {
            LOGGER.error(ie.getMessage());
        } catch (Exception e) {
            LOGGER.error("An error occurred while creating the mapping!", e);
        }
    }
}
