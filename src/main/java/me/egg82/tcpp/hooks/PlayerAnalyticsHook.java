package me.egg82.tcpp.hooks;

import com.djrapitops.plan.api.PlanAPI;
import com.djrapitops.plan.data.element.AnalysisContainer;
import com.djrapitops.plan.data.element.InspectContainer;
import com.djrapitops.plan.data.plugin.ContainerSize;
import com.djrapitops.plan.data.plugin.PluginData;
import com.djrapitops.plan.utilities.html.icon.Color;
import com.djrapitops.plan.utilities.html.icon.Icon;
import java.util.Collection;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerAnalyticsHook implements PluginHook {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PlayerAnalyticsHook() { PlanAPI.getInstance().addPluginDataSource(new Data()); }

    public void cancel() {}

    class Data extends PluginData {
        private Data() {
            super(ContainerSize.THIRD, "TrollCommands++");
            setPluginIcon(Icon.called("meh-rolling-eyes").of(Color.PURPLE).build());
        }

        public InspectContainer getPlayerData(UUID uuid, InspectContainer container) {
            return container;
        }

        public AnalysisContainer getServerData(Collection<UUID> uuids, AnalysisContainer container) {
            return container;
        }
    }
}
