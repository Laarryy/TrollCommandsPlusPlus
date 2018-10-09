package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.egg82.tcpp.registries.ConvertRegistry;
import me.egg82.tcpp.registries.MaterialNameRegistry;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;

public class ConvertCommand extends CommandHandler {
    //vars
    private IVariableRegistry<UUID> convertRegistry = ServiceLocator.getService(ConvertRegistry.class);
    private ArrayList<String> materialNames = new ArrayList<String>();
    private IVariableRegistry<String> materialNameRegistry = ServiceLocator.getService(MaterialNameRegistry.class);

    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public ConvertCommand() {
        super();

        for (String key : materialNameRegistry.getKeys()) {
            if (!materialNameRegistry.hasRegister(key + "_ITEM")) {
                materialNames.add(WordUtils.capitalize(key.toLowerCase().replace('_', ' ')));
            }
        }
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
            if (args[1].isEmpty()) {
                return materialNames;
            }

            ArrayList<String> retVal = new ArrayList<String>();

            for (String name : materialNames) {
                if (name.toLowerCase().startsWith(args[1].toLowerCase())) {
                    retVal.add(name);
                }
            }

            return retVal;
        }

        return null;
    }

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (!sender.hasPermission(PermissionsType.COMMAND_CONVERT)) {
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

        Material type = null;
        if (args.length == 2) {
            type = Material.getMaterial(args[1].replaceAll(" ", "_").toUpperCase() + "_ITEM");
            if (type == null) {
                type = Material.getMaterial(args[1].replaceAll(" ", "_").toUpperCase());
                if (type == null) {
                    sender.sendMessage(ChatColor.RED + "Searched type is invalid or was not found.");
                    return;
                }
            }
        }

        List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
        if (players.size() > 0) {
            for (Player player : players) {
                if (type != null) {
                    if (player.hasPermission(PermissionsType.IMMUNE)) {
                        continue;
                    }

                    e(player.getUniqueId(), player, type);
                } else {
                    eUndo(player.getUniqueId(), player);
                }
            }
        } else {
            Player player = CommandUtil.getPlayerByName(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }

            if (type != null) {
                if (player.hasPermission(PermissionsType.IMMUNE)) {
                    sender.sendMessage(ChatColor.RED + "Player is immune.");
                    return;
                }

                e(player.getUniqueId(), player, type);
            } else {
                eUndo(player.getUniqueId(), player);
            }
        }
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    private void e(UUID uuid, Player player, Material type) {
        PlayerInventory inv = player.getInventory();
        ItemStack[] items = inv.getContents();
        List<ItemStack[]> conversions = null;

        if (convertRegistry.hasRegister(uuid)) {
            conversions = convertRegistry.getRegister(uuid, List.class);
        } else {
            conversions = new ArrayList<ItemStack[]>();
            convertRegistry.setRegister(uuid, conversions);
        }

        conversions.add(items.clone());

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getType() != Material.AIR) {
                items[i] = new ItemStack(type, items[i].getAmount());
            }
        }

        inv.setContents(items);
        player.updateInventory();

        metricsHelper.commandWasRun(this);

        String name = type.name().toLowerCase();
        if (name.length() > 5 && name.substring(name.length() - 5).equals("_item")) {
            name = name.substring(0, name.length() - 5);
        }
        name = name.replace('_', ' ');

        sender.sendMessage(player.getName() + "'s inventory is now " + name + ".");
    }

    protected void onUndo() {
        Player player = CommandUtil.getPlayerByName(args[0]);
        if (player != null) {
            UUID uuid = player.getUniqueId();
            if (convertRegistry.hasRegister(uuid)) {
                eUndo(uuid, player);
            }
        }
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    private void eUndo(UUID uuid, Player player) {
        if (!convertRegistry.hasRegister(uuid)) {
            sender.sendMessage("No conversions left to undo for " + player.getName() + ".");
            return;
        }

        List<ItemStack[]> conversions = convertRegistry.getRegister(uuid, List.class);
        player.getInventory().setContents(conversions.remove(conversions.size() - 1));
        player.updateInventory();
        if (conversions.size() == 0) {
            convertRegistry.removeRegister(uuid);
        }

        sender.sendMessage("Undid one conversion for " + player.getName() + ".");
    }
}
