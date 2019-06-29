package me.egg82.tcpp.commands.internal;

import co.aikar.taskchain.TaskChain;
import java.util.Set;
import java.util.UUID;
import me.egg82.tcpp.services.AnalyticsHelper;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.services.player.PlayerVisibilityHandler;
import me.egg82.tcpp.utils.LogUtil;
import ninja.egg82.service.ServiceLocator;
import ninja.egg82.service.ServiceNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AloneCommand extends BaseCommand {
    private final Plugin plugin;
    private final String playerName;

    private PlayerVisibilityHandler playerVisibilityHandler;

    public AloneCommand(Plugin plugin, TaskChain<?> chain, CommandSender sender, String playerName) {
        super(chain, sender);
        this.plugin = plugin;
        this.playerName = playerName;

        try {
            playerVisibilityHandler = ServiceLocator.get(PlayerVisibilityHandler.class);
        } catch (InstantiationException | IllegalAccessException | ServiceNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void run() {
        getChain(playerName)
                .syncLast(v -> {
                    Set<UUID> set = CollectionProvider.getSet("alone");

                    if (set.add(v)) {
                        AnalyticsHelper.incrementCommand("alone");
                        hidePlayers(Bukkit.getPlayer(v));
                        sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + playerName + " is now all alone :(");
                    } else if (set.remove(v)) {
                        showPlayers(Bukkit.getPlayer(v));
                        sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + playerName + " is no longer alone in this world!");
                    }
                })
                .execute();
    }

    private void hidePlayers(Player player) {
        if (player == null) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            playerVisibilityHandler.hide(plugin, player, p);
        }
    }

    private void showPlayers(Player player) {
        if (player == null) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            playerVisibilityHandler.show(plugin, player, p);
        }
    }
}
