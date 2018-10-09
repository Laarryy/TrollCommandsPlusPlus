package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.egg82.tcpp.registries.PopupRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;

public class PopupCommand extends CommandHandler {
    //vars
    private IVariableRegistry<UUID> popupRegistry = ServiceLocator.getService(PopupRegistry.class);

    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public PopupCommand() {
        super();
    }

    //public
    public List<String> tabComplete() {
        if (args.length == 1) {
            ArrayList<String> retVal = new ArrayList<String>();

            if (args[0].isEmpty()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    retVal.add(player.getName());
                }
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                        retVal.add(player.getName());
                    }
                }
            }

            return retVal;
        }

        return null;
    }

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (!sender.hasPermission(PermissionsType.COMMAND_POPUP)) {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
            return;
        }
        if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
            sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
            String name = getClass().getSimpleName();
            name = name.substring(0, name.length() - 7).toLowerCase();
            Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
            return;
        }

        List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
        if (players.size() > 0) {
            for (Player player : players) {
                UUID uuid = player.getUniqueId();

                if (!popupRegistry.hasRegister(uuid)) {
                    if (player.hasPermission(PermissionsType.IMMUNE)) {
                        continue;
                    }

                    e(uuid, player);
                } else {
                    eUndo(uuid, player);
                }
            }
        } else {
            Player player = CommandUtil.getPlayerByName(args[0]);

            if (player == null) {
                OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
                if (offlinePlayer != null) {
                    UUID uuid = offlinePlayer.getUniqueId();
                    if (popupRegistry.hasRegister(uuid)) {
                        eUndo(uuid, offlinePlayer);
                        return;
                    }
                }

                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }

            UUID uuid = player.getUniqueId();

            if (!popupRegistry.hasRegister(uuid)) {
                if (player.hasPermission(PermissionsType.IMMUNE)) {
                    sender.sendMessage(ChatColor.RED + "Player is immune.");
                    return;
                }

                e(uuid, player);
            } else {
                eUndo(uuid, player);
            }
        }
    }

    private void e(UUID uuid, Player player) {
        popupRegistry.setRegister(uuid, null);
        metricsHelper.commandWasRun(this);

        sender.sendMessage(player.getName() + "'s inventory is now opening and closing randomly.");
    }

    protected void onUndo() {
        Player player = CommandUtil.getPlayerByName(args[0]);
        if (player != null) {
            UUID uuid = player.getUniqueId();
            if (popupRegistry.hasRegister(uuid)) {
                eUndo(uuid, player);
            }
        } else {
            OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
            UUID uuid = offlinePlayer.getUniqueId();
            if (popupRegistry.hasRegister(uuid)) {
                eUndo(uuid, offlinePlayer);
            }
        }
    }

    private void eUndo(UUID uuid, Player player) {
        popupRegistry.removeRegister(uuid);

        sender.sendMessage(player.getName() + "'s inventory is no longer opening and closing randomly.");
    }

    private void eUndo(UUID uuid, OfflinePlayer player) {
        popupRegistry.removeRegister(uuid);

        sender.sendMessage(player.getName() + "'s inventory is no longer opening and closing randomly.");
    }
}
