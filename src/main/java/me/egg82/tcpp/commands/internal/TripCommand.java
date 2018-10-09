package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.egg82.tcpp.registries.TripRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableExpiringRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;

public class TripCommand extends CommandHandler {
    //vars
    private IVariableExpiringRegistry<UUID> tripRegistry = ServiceLocator.getService(TripRegistry.class);

    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public TripCommand() {
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

            if ("true".startsWith(args[1].toLowerCase())) {
                retVal.add("true");
            } else if ("false".startsWith(args[1].toLowerCase())) {
                retVal.add("false");
            }

            return retVal;
        }

        return null;
    }

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (!sender.hasPermission(PermissionsType.COMMAND_TRIP)) {
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

        boolean copy = false;
        if (args.length == 2) {
            copy = isTrue(args[1]);
        }

        List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
        if (players.size() > 0) {
            for (Player player : players) {
                if (player.hasPermission(PermissionsType.IMMUNE)) {
                    continue;
                }

                e(player.getUniqueId(), player, copy);
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

            e(player.getUniqueId(), player, copy);
        }
    }

    @SuppressWarnings("deprecation")
    private void e(UUID uuid, Player player, boolean keepInventory) {
        World world = player.getWorld();
        Location location = player.getLocation();
        PlayerInventory inv = player.getInventory();
        ItemStack[] items = inv.getContents();

        tripRegistry.setRegister(uuid, null);

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getType() != Material.AIR) {
                world.dropItemNaturally(location, items[i]);
            }
        }

        if (!keepInventory) {
            items = new ItemStack[items.length];
            inv.setContents(items);
            player.updateInventory();
        }

        metricsHelper.commandWasRun(this);

        sender.sendMessage(player.getName() + " tripped!");
    }

    protected void onUndo() {
    }

    private boolean isTrue(String input) {
        input = input.toLowerCase();

        if (input.equals("true") || input.equals("yes") || input.equals("y")) {
            return true;
        }
        return false;
    }
}
