package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.utils.MathUtil;

public class CreepCommand extends CommandHandler {
    //vars
    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public CreepCommand() {
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
        if (!sender.hasPermission(PermissionsType.COMMAND_CREEP)) {
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
                if (player.hasPermission(PermissionsType.IMMUNE)) {
                    continue;
                }

                e(player);
            }
        } else {
            Player player = CommandUtil.getPlayerByName(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }
            if (player.hasPermission(PermissionsType.IMMUNE)) {
                sender.sendMessage(ChatColor.RED + "Player is immune.");
                return;
            }

            e(player);
        }
    }

    private void e(Player player) {
        Location[] creeperLocations = LocationUtil.getCircleAround(player.getLocation(), 1.0d, MathUtil.fairRoundedRandom(3, 5));

        for (int i = 0; i < creeperLocations.length; i++) {
            Location creeperLocation = BlockUtil.getHighestSolidBlock(creeperLocations[i]).add(0.0d, 1.0d, 0.0d);

            Creeper c = player.getWorld().spawn(creeperLocation, Creeper.class);
            c.setPowered(true);
            c.setVelocity(LocationUtil.moveSmoothly(creeperLocation, player.getLocation()));
            c.setTarget(player);
        }

        metricsHelper.commandWasRun(this);

        sender.sendMessage(player.getName() + " is now playing with explosives.");
    }

    protected void onUndo() {

    }
}