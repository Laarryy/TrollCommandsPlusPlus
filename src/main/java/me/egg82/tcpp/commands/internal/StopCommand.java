package me.egg82.tcpp.commands.internal;

import co.aikar.taskchain.TaskChain;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import me.egg82.tcpp.services.AnalyticsHelper;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class StopCommand extends BaseCommand {
    private final String playerName;

    public StopCommand(TaskChain<?> chain, CommandSender sender, String playerName) {
        super(chain, sender);
        this.playerName = playerName;
    }

    public void run() {
        getChain(playerName)
                .syncLast(v -> {
                    ConcurrentMap<String, Set<UUID>> setCache = CollectionProvider.getSetCache();

                    for (Map.Entry<String, Set<UUID>> kvp : setCache.entrySet()) {
                        if (!kvp.getValue().contains(v)) {
                            continue;
                        }

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "trollcommandsplusplus " + kvp.getKey() + " " + playerName);
                    }

                    AnalyticsHelper.incrementCommand("stop");
                    sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + "All active trolls against " + playerName + " have been stopped.");
                })
                .execute();
    }
}
