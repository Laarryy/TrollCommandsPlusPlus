package me.egg82.ae.utils;

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
            to11(config);
        }
        if (config.getNode("version").getDouble() == 1.1d) {
            to12(config);
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

    private static void to11(ConfigurationNode config) {
        // Add ignore
        config.getNode("enchant-chance").setValue(0.0d);

        // Version
        config.getNode("version").setValue(1.1d);
    }

    private static void to12(ConfigurationNode config) {
        // Add unbreaking bypass(-bypass?)
        config.getNode("bypass-unbreaking").setValue(Boolean.TRUE);

        // Add particles
        config.getNode("particles").setValue(Boolean.TRUE);

        // Add loot chances
        config.getNode("loot-chance", "enchant").setValue(0.00045d);
        config.getNode("loot-chance", "curse").setValue(0.00126d);

        // Version
        config.getNode("version").setValue(1.2d);
    }
}
