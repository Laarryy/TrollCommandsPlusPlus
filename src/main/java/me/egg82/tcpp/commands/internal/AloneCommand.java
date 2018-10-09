package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.lists.AloneSet;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.core.PlayerInfoContainer;
import ninja.egg82.bukkit.reflection.player.IPlayerHelper;
import ninja.egg82.bukkit.reflection.uuid.IUUIDHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;

public class AloneCommand extends CommandHandler {
    //vars
    private IConcurrentSet<UUID> aloneSet = ServiceLocator.getService(AloneSet.class);

    private IPlayerHelper playerHelper = ServiceLocator.getService(IPlayerHelper.class);
    private IUUIDHelper uuidHelper = ServiceLocator.getService(IUUIDHelper.class);

    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public AloneCommand() {
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
        if (!sender.hasPermission(PermissionsType.COMMAND_ALONE)) {
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
                    if (aloneSet.remove(player.getUniqueId())) {
                        eUndo(player, player.getName());
                    }
                    continue;
                }

                if (aloneSet.add(player.getUniqueId())) {
                    e(player, player.getName());
                } else {
                    eUndo(player, player.getName());
                }
            }
        } else {
            PlayerInfoContainer info = uuidHelper.getPlayer(args[0], true);
            if (info == null) {
                sender.sendMessage(ChatColor.RED + "Could not fetch player info. Please try again later.");
                return;
            }

            Player player = CommandUtil.getPlayerByUuid(info.getUuid());
            if (player == null) {
                if (aloneSet.remove(info.getUuid())) {
                    eUndo(info.getName());
                    return;
                }

                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }

            if (player.hasPermission(PermissionsType.IMMUNE)) {
                if (aloneSet.remove(info.getUuid())) {
                    eUndo(player, info.getName());
                } else {
                    sender.sendMessage(ChatColor.RED + "Player is immune.");
                }
                return;
            }

            if (aloneSet.add(info.getUuid())) {
                e(player, info.getName());
            } else if (aloneSet.remove(info.getUuid())) {
                eUndo(player, info.getName());
            }
        }
    }

    private void e(Player player, String name) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            playerHelper.hidePlayer(player, p);
        }

        metricsHelper.commandWasRun(this);

        sender.sendMessage(ChatColor.GREEN + name + " is now all alone :(");
    }

    protected void onUndo() {
        PlayerInfoContainer info = uuidHelper.getPlayer(args[0], true);
        if (info == null) {
            sender.sendMessage(ChatColor.RED + "Could not fetch player info. Please try again later.");
            return;
        }

        Player player = CommandUtil.getPlayerByUuid(info.getUuid());
        if (player != null) {
            if (aloneSet.remove(info.getUuid())) {
                eUndo(player, info.getName());
            }
        } else {
            if (aloneSet.remove(info.getUuid())) {
                eUndo(info.getName());
            }
        }
    }

    private void eUndo(Player player, String name) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            playerHelper.showPlayer(player, p);
        }

        eUndo(name);
    }

    private void eUndo(String name) {
        sender.sendMessage(ChatColor.GREEN + name + " is no longer alone in this wold!");
    }
}
