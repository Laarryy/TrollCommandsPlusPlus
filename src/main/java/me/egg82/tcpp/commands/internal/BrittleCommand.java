package me.egg82.tcpp.commands.internal;

import co.aikar.taskchain.TaskChain;
import java.util.Set;
import java.util.UUID;
import me.egg82.tcpp.services.AnalyticsHelper;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.utils.LogUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class BrittleCommand extends BaseCommand {
    private final String playerName;

    public BrittleCommand(TaskChain<?> chain, CommandSender sender, String playerName) {
        super(chain, sender);
        this.playerName = playerName;
    }

    public void run() {
        getChain(playerName)
                .syncLast(v -> {
                    Set<UUID> set = CollectionProvider.getSet("brittle");

                    if (set.add(v)) {
                        if (isOfflineOrImmune(v)) {
                            return;
                        }

                        AnalyticsHelper.incrementCommand("brittle");
                        sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + playerName + " is now very brittle!");
                    } else if (set.remove(v)) {
                        sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + playerName + " is no longer brittle.");
                    }
                })
                .execute();
    }
}
