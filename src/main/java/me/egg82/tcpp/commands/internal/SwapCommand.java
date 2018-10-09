package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;

public class SwapCommand extends CommandHandler {
    //vars
    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public SwapCommand() {
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
        if (!sender.hasPermission(PermissionsType.COMMAND_SWAP)) {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
            return;
        }
        if (!CommandUtil.isArrayOfAllowedLength(args, 2)) {
            sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
            String name = getClass().getSimpleName();
            name = name.substring(0, name.length() - 7).toLowerCase();
            Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
            return;
        }

        List<Player> players1 = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
        List<Player> players2 = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[1], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
        Player player1 = null;
        Player player2 = null;

        if (players1.size() == 0) {
            player1 = CommandUtil.getPlayerByName(args[0]);
            if (player1 == null) {
                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }
        } else {
            player1 = players1.get(0);
        }
        if (players2.size() == 0) {
            player2 = CommandUtil.getPlayerByName(args[1]);
            if (player2 == null) {
                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }
        } else {
            player2 = players2.get(0);
        }
        if (player1.hasPermission(PermissionsType.IMMUNE)) {
            sender.sendMessage(ChatColor.RED + player1.getName() + " is immune.");
            return;
        }
        if (player2.hasPermission(PermissionsType.IMMUNE)) {
            sender.sendMessage(ChatColor.RED + player2.getName() + " is immune.");
            return;
        }

        e(player1, player2);
    }

    private void e(Player player1, Player player2) {
        Location player1Location = player1.getLocation().clone();
        Vector player1Velocity = player1.getVelocity();
        Location player2Location = player2.getLocation().clone();
        Vector player2Velocity = player2.getVelocity();

        player1.teleport(player2Location);
        player1.setVelocity(player2Velocity);
        player2.teleport(player1Location);
        player2.setVelocity(player1Velocity);

        metricsHelper.commandWasRun(this);

        sender.sendMessage(player1.getName() + " and " + player2.getName() + "have been swapped.");
    }

    protected void onUndo() {

    }
}
