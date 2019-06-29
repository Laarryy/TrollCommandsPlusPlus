package me.egg82.tcpp.commands.internal;

import co.aikar.taskchain.TaskChain;
import java.util.Set;
import java.util.UUID;
import me.egg82.tcpp.services.AnalyticsHelper;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.utils.LogUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AnnoyCommand extends BaseCommand {
    private final String playerName;

    public AnnoyCommand(TaskChain<?> chain, CommandSender sender, String playerName) {
        super(chain, sender);
        this.playerName = playerName;
    }

    public void run() {
        getChain(playerName)
                .syncLast(v -> {
                    Set<UUID> set = CollectionProvider.getSet("annoy");

                    if (set.add(v)) {
                        AnalyticsHelper.incrementCommand("annoy");
                        sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + playerName + " is now being annoyed by villager sounds.");
                    } else if (set.remove(v)) {
                        sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + playerName + " is no longer being annoyed by villager sounds.");
                    }
                })
                .execute();
    }
}
