package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;

public class InspectCommand extends CommandHandler {
    //vars
    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public InspectCommand() {
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
        if (!sender.hasPermission(PermissionsType.COMMAND_INSPECT)) {
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
        if (!CommandUtil.isPlayer((CommandSender) sender.getHandle())) {
            sender.sendMessage(ChatColor.RED + "Console cannot run this command!");
            return;
        }

        Player player = CommandUtil.getPlayerByName(args[0]);

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player could not be found.");
            return;
        }
        if (player.hasPermission(PermissionsType.IMMUNE)) {
            sender.sendMessage(ChatColor.RED + "Player is immune.");
            return;
        }

        UUID uuid = player.getUniqueId();

        if (uuid.equals(sender.getUuid())) {
            sender.sendMessage(ChatColor.RED + "The target you've chosen is invalid.");
            return;
        }

        e(player);
    }

    private void e(Player player) {
        PlayerInventory inv = player.getInventory();
        ((Player) sender.getHandle()).closeInventory();
        ((Player) sender.getHandle()).openInventory(inv);

        metricsHelper.commandWasRun(this);

        sender.sendMessage("You are now \"inspecting\" " + player.getName() + "'s inventory.");
    }

    protected void onUndo() {

    }
}
