package me.egg82.tcpp.hooks;

import com.djrapitops.plan.capability.CapabilityService;
import com.djrapitops.plan.extension.CallEvents;
import com.djrapitops.plan.extension.DataExtension;
import com.djrapitops.plan.extension.ExtensionService;
import com.djrapitops.plan.extension.FormatType;
import com.djrapitops.plan.extension.annotation.NumberProvider;
import com.djrapitops.plan.extension.annotation.PluginInfo;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;
import java.util.*;
import me.egg82.tcpp.APIException;
import me.egg82.tcpp.TrollAPI;
import me.egg82.tcpp.api.TrollType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerAnalyticsHook implements PluginHook {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CapabilityService capabilities;

    public PlayerAnalyticsHook() {
        capabilities = CapabilityService.getInstance();

        if (isCapabilityAvailable("DATA_EXTENSION_VALUES")) {
            try {
                ExtensionService.getInstance().register(new Data());
            } catch (NoClassDefFoundError ex) {
                // Plan not installed
                logger.error("Plan is not installed.", ex);
            } catch (IllegalStateException ex) {
                // Plan not enabled
                logger.error("Plan is not enabled.", ex);
            } catch (IllegalArgumentException ex) {
                // DataExtension impl error
                logger.error("DataExtension implementation exception.", ex);
            }
        }
    }

    public void cancel() { }

    private boolean isCapabilityAvailable(String capability) {
        try {
            return capabilities.hasCapability(capability);
        } catch (NoClassDefFoundError ignored) {
            return false;
        }
    }

    @PluginInfo(
            name = "TrollCommands++",
            iconName = "theater-masks",
            iconFamily = Family.SOLID,
            color = Color.PURPLE
    )
    class Data implements DataExtension {
        private final TrollAPI api = TrollAPI.getInstance();
        private final CallEvents[] events = new CallEvents[]{CallEvents.SERVER_PERIODICAL, CallEvents.PLAYER_JOIN, CallEvents.PLAYER_LEAVE, CallEvents.SERVER_EXTENSION_REGISTER};
        private final int numTrolls = TrollType.values().size();

        private Data() { }

        @NumberProvider(
                text = "Registered Trolls",
                description = "Number of registered trolls.",
                priority = 1,
                iconName = "theater-masks",
                iconFamily = Family.SOLID,
                iconColor = Color.NONE,
                format = FormatType.NONE
        )
        public long getTrolls() { return numTrolls; }

        @NumberProvider(
                text = "Running Trolls",
                description = "Number of running trolls.",
                priority = 2,
                iconName = "theater-masks",
                iconFamily = Family.SOLID,
                iconColor = Color.NONE,
                format = FormatType.NONE
        )
        public long getRunningTrolls() {
            try {
                return api.getNumRunningTrolls();
            } catch (APIException ex) {
                logger.error("[Hard: " + ex.isHard() + "] " + ex.getMessage(), ex);
            }
            return 0L;
        }

        @NumberProvider(
                text = "Trolls",
                description = "Number of trolls running on the player.",
                priority = 1,
                iconName = "mask",
                iconFamily = Family.SOLID,
                iconColor = Color.NONE,
                format = FormatType.NONE
        )
        public long getTrolls(UUID playerUUID) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) {
                return 0L;
            }

            try {
                return api.getNumRunningTrolls(playerUUID);
            } catch (APIException ex) {
                logger.error("[Hard: " + ex.isHard() + "] " + ex.getMessage(), ex);
            }
            return 0L;
        }

        public CallEvents[] callExtensionMethodsOn() { return events; }
    }
}
