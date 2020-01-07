package me.egg82.tcpp.utils;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationVersionUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationVersionUtil.class);

    private ConfigurationVersionUtil() {}

    public static void conformVersion(ConfigurationLoader<ConfigurationNode> loader, ConfigurationNode config, File fileOnDisk) throws IOException {
        double oldVersion = config.getNode("version").getDouble(1.0d);

        if (config.getNode("version").getDouble(1.0d) == 1.0d) {
            to20(config);
        }
        if (config.getNode("version").getDouble() == 2.0d) {
            to21(config);
        }
        if (config.getNode("version").getDouble() == 2.1d) {
            to31(config);
        }

        if (config.getNode("version").getDouble() != oldVersion) {
            File backupFile = new File(fileOnDisk.getParent(), fileOnDisk.getName() + ".bak");
            if (backupFile.exists()) {
                java.nio.file.Files.delete(backupFile.toPath());
            }

            Files.copy(fileOnDisk, backupFile);
            loader.save(config);
        }
    }

    private static void to20(ConfigurationNode config) {
        // Redis
        config.getNode("redis", "enabled").setValue(Boolean.FALSE);
        config.getNode("redis", "address").setValue("");
        config.getNode("redis", "port").setValue(6379);
        config.getNode("redis", "pass").setValue("");

        // Dump -> Messaging
        String queueType = config.getNode("queueType").getString("bungee");
        config.removeChild("queueType");
        String rabbitIp = config.getNode("rabbitIp").getString("");
        config.removeChild("rabbitIp");
        int rabbitPort = config.getNode("rabbitPort").getInt(5672);
        config.removeChild("rabbitPort");
        String rabbitUser = config.getNode("rabbitUser").getString("guest");
        config.removeChild("rabbitUser");
        String rabbitPass = config.getNode("rabbitPass").getString("guest");
        config.removeChild("rabbitPass");

        config.getNode("messaging", "enabled").setValue((queueType.equalsIgnoreCase("rabbitmq") || queueType.equalsIgnoreCase("rabbit")) ? Boolean.TRUE : Boolean.FALSE);
        config.getNode("messaging", "type").setValue(queueType);
        config.getNode("messaging", "rabbit", "address").setValue(rabbitIp);
        config.getNode("messaging", "rabbit", "port").setValue(rabbitPort);
        config.getNode("messaging", "rabbit", "user").setValue(rabbitUser);
        config.getNode("messaging", "rabbit", "pass").setValue(rabbitPass);

        // Version
        config.getNode("version").setValue(2.0d);
    }

    private static void to21(ConfigurationNode config) {
        // Redis -> Messaging Redis
        String redisAddress = config.getNode("redis", "address").getString("");
        int redisPort = config.getNode("redis", "port").getInt(5672);
        String redisPass = config.getNode("redis", "pass").getString("");
        config.removeChild("redis");

        config.getNode("messaging", "redis", "address").setValue(redisAddress);
        config.getNode("messaging", "redis", "port").setValue(redisPort);
        config.getNode("messaging", "redis", "pass").setValue(redisPass);

        // Version
        config.getNode("version").setValue(2.1d);
    }

    private static void to31(ConfigurationNode config) {
        // Remove messaging
        config.removeChild("messaging");

        // Add debug
        config.getNode("debug").setValue(Boolean.FALSE);

        // Add stats
        config.getNode("status", "usage").setValue(Boolean.TRUE);
        config.getNode("status", "errors").setValue(Boolean.TRUE);

        // Add update
        config.getNode("update", "check").setValue(Boolean.TRUE);
        config.getNode("update", "notify").setValue(Boolean.TRUE);

        // Version
        config.getNode("version").setValue(3.1d);
    }
}
