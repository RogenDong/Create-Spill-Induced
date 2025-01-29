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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

/**
 * 读取配置
 */
public class ModConfig {
    private static final Logger LOGGER = CreateSpillInduced.LOGGER;
    private static ModConfig instance;
    private MessageDigest md5Handler;
    private byte[] configCache = null;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File FILE_CFG = new File("config", "create-spill-induced.json");
    private final static long reloadInterval = 10000;
    private static long preReloadTime = 0;

    public final List<ResultMapping> resultMapping = new ArrayList<>(8);

    public static ModConfig getInstance() {
        return instance;
    }

    public void init() {
        instance = this;
        ConfigRootNode config = null;
        try {
            md5Handler = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("获取 MD5 实例失败!!", e);
        }

        // 尝试读取配置文件
        if (FILE_CFG.exists()) {
            try {
                FileBytes bs = getConfigBytes();
                preReloadTime = System.currentTimeMillis();
                if (bs == null) return;

                configCache = bs.md5;
                String json = new String(bs.raw, StandardCharsets.UTF_8);
                config = gson.fromJson(json, ConfigRootNode.class);
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }

        // 缺少配置文件；或反序列化失败
        if (config == null) {
            boolean ok = false;
            try {
                config = ConfigRootNode.defaultConfig();
                if (FILE_CFG.exists()) FILE_CFG.delete();
                Files.writeString(FILE_CFG.toPath(), gson.toJson(config), StandardCharsets.UTF_8);
                ok = true;
            } catch (IOException ioe) {
                LOGGER.error("无法写入配置文件!!", ioe);
            } catch (SecurityException se) {
                LOGGER.error("没有配置文件的写入权限!!", se);
            } catch (Exception e) {
                LOGGER.error("写入配置文件失败!!", e);
            }
            if (!ok) return;
        }

        // mapping
        config.basaltLike.forEach(this::mapping);
        config.cobbleLike.forEach(this::mapping);
        config.stoneLike.forEach(this::mapping);
    }

    public void reload() {
        long now = System.currentTimeMillis();
        if (now - preReloadTime < reloadInterval) return;

        FileBytes bs = getConfigBytes();
        preReloadTime = now;
        if (bs == null || Arrays.equals(configCache, bs.md5)) return;

        LOGGER.info("重载配置文件...");
        configCache = bs.md5;
        String json = new String(bs.raw, StandardCharsets.UTF_8);
        ConfigRootNode config = gson.fromJson(json, ConfigRootNode.class);

        LOGGER.info("重新生成映射...");
        resultMapping.clear();
        config.basaltLike.forEach(this::mapping);
        config.cobbleLike.forEach(this::mapping);
        config.stoneLike.forEach(this::mapping);
    }

    private void mapping(CollisionType gen) {
        try {
            ResultMapping rm = new ResultMapping(gen);
            resultMapping.add(rm);
            if (LOGGER.isDebugEnabled()) {
                String onBlock = gen instanceof BasaltGen bg
                        ? bg.bottomBlock + ',' + bg.otherBlock
                        : gen.bottomBlock;
                List<String> tmp = gen.results.entrySet().stream()
                        .map(e -> e.getKey() + '@' + e.getValue()).toList();
                LOGGER.info("{}: ({} + {} on {})=[{}]",
                        rm.genType.getSimpleName(), gen.pipeFluid, gen.impactFluid, onBlock, String.join(", ", tmp));
            }
        } catch (InvalidPropertiesFormatException ie) {
            LOGGER.error(ie.getMessage());
        } catch (Exception e) {
            LOGGER.error("An error occurred while creating the mapping!", e);
        }
    }

    private FileBytes getConfigBytes() {
        try {
            byte[] raw = Files.readAllBytes(FILE_CFG.toPath());
            if (raw.length <= 84) return null;
            byte[] md5 = (md5Handler == null) ? null : md5Handler.digest(raw);
            return new FileBytes(raw, md5);
        } catch (IOException ioe) {
            LOGGER.error("无法读取配置文件!!", ioe);
        } catch (OutOfMemoryError ome) {
            LOGGER.error("配置文件过大!!", ome);
        } catch (SecurityException se) {
            LOGGER.error("没有配置文件的读取权限!!", se);
        } catch (Exception e) {
            LOGGER.error("读取配置文件失败!!", e);
        }
        return null;
    }

    private record FileBytes(byte[] raw, byte[] md5) {
    }
}
