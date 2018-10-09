package me.egg82.tcpp.commands.internal;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.lists.MarkovSet;
import me.egg82.tcpp.registries.MarkovRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarkovCommand extends CommandHandler {
    //vars
    private IConcurrentSet<UUID> markovSet = ServiceLocator.getService(MarkovSet.class);
    private IRegistry<UUID, UUID> markovRegistry = ServiceLocator.getService(MarkovRegistry.class);

    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public MarkovCommand() {
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
        } else if (args.length == 2) {
            ArrayList<String> retVal = new ArrayList<String>();

            if (args[1].isEmpty()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    retVal.add(player.getName());
                }
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
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
        if (!sender.hasPermission(PermissionsType.COMMAND_MARKOV)) {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
            return;
        }
        if (!CommandUtil.isArrayOfAllowedLength(args, 1, 2)) {
            sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
            String name = getClass().getSimpleName();
            name = name.substring(0, name.length() - 7).toLowerCase();
            Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
            return;
        }

        List<Player> players1 = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
        Player player1 = null;

        if (players1.size() == 0) {
            player1 = CommandUtil.getPlayerByName(args[0]);
            if (player1 == null) {
                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }
        } else {
            player1 = players1.get(0);
        }

        Player player2 = null;
        if (args.length >= 2) {
            List<Player> players2 = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[1], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
            if (players2.size() == 0) {
                player2 = CommandUtil.getPlayerByName(args[1]);
                if (player2 == null) {
                    sender.sendMessage(ChatColor.RED + "Player could not be found.");
                    return;
                }
            } else {
                player2 = players2.get(0);
            }
        }

        if (player1.hasPermission(PermissionsType.IMMUNE)) {
            sender.sendMessage(ChatColor.RED + player1.getName() + " is immune.");
            return;
        }
        if (args.length >= 2) {
            if (player2.hasPermission(PermissionsType.IMMUNE)) {
                sender.sendMessage(ChatColor.RED + player2.getName() + " is immune.");
                return;
            }
        }

        e(player1, player2);
    }

    private void e(Player player1, Player player2) {
        metricsHelper.commandWasRun(this);

        if (player2 == null) {
            markovSet.add(player1.getUniqueId());
            sender.sendMessage(player1.getName() + " has been Markov'd.");
        } else {
            markovRegistry.setRegister(player1.getUniqueId(), player2.getUniqueId());
            sender.sendMessage(player1.getName() + " and " + player2.getName() + " have been Markov'd.");
        }
    }

    protected void onUndo() {
        Player player = CommandUtil.getPlayerByName(args[0]);
        if (player != null) {
            UUID uuid = player.getUniqueId();
            if (markovSet.contains(uuid) || markovRegistry.hasRegister(uuid)) {
                eUndo(uuid, player);
            }
        } else {
            OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
            UUID uuid = offlinePlayer.getUniqueId();
            if (markovSet.contains(uuid) || markovRegistry.hasRegister(uuid)) {
                eUndo(uuid, offlinePlayer);
            }
        }
    }

    private void eUndo(UUID uuid, Player player1) {
        if (markovSet.remove(uuid)) {
            sender.sendMessage(player1.getName() + " is no longer Markov'd.");
        } else {
            UUID uuid2 = markovRegistry.removeRegister(uuid);
            OfflinePlayer player2 = CommandUtil.getOfflinePlayerByUuid(uuid2);

            sender.sendMessage(player1.getName() + " and " + player2.getName() + " are no longer Markov'd.");
        }
    }
    private void eUndo(UUID uuid, OfflinePlayer player1) {
        if (markovSet.remove(uuid)) {
            sender.sendMessage(player1.getName() + " is no longer Markov'd.");
        } else {
            UUID uuid2 = markovRegistry.removeRegister(uuid);
            OfflinePlayer player2 = CommandUtil.getOfflinePlayerByUuid(uuid2);

            sender.sendMessage(player1.getName() + " and " + player2.getName() + " are no longer Markov'd.");
        }
    }
}
