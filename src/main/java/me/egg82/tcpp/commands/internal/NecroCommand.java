package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.egg82.tcpp.registries.NecroRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.reflection.entity.IEntityHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.disguise.reflection.IDisguiseHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;

public class NecroCommand extends CommandHandler {
    //vars
    private IVariableRegistry<UUID> necroRegistry = ServiceLocator.getService(NecroRegistry.class);

    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
    private IDisguiseHelper disguiseHelper = ServiceLocator.getService(IDisguiseHelper.class);
    private IEntityHelper entityHelper = ServiceLocator.getService(IEntityHelper.class);

    //constructor
    public NecroCommand() {
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
        if (!sender.hasPermission(PermissionsType.COMMAND_NECRO)) {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
            return;
        }
        if (!disguiseHelper.isValidLibrary()) {
            sender.sendMessage(ChatColor.RED + "This command has been disabled because there is no recognized backing library available. Please install one and restart the server to enable this command.");
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

                if (!necroRegistry.hasRegister(uuid)) {
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
                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }

            UUID uuid = player.getUniqueId();

            if (!necroRegistry.hasRegister(uuid)) {
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
        disguiseHelper.disguiseAsEntity(player, EntityType.SKELETON);

        ItemStack[] inventory = player.getInventory().getContents();
        player.getInventory().clear();
        entityHelper.setItemInMainHand(player, getInfinityBow());
        player.getInventory().addItem(new ItemStack(Material.ARROW, 1));

        if (Math.random() <= 0.01) {
            Spider spider = player.getWorld().spawn(player.getLocation(), Spider.class);
            entityHelper.addPassenger(spider, player);
        }

        necroRegistry.setRegister(uuid, inventory);
        metricsHelper.commandWasRun(this);

        sender.sendMessage(player.getName() + " has been necro'd.");
    }

    protected void onUndo() {
        Player player = CommandUtil.getPlayerByName(args[0]);
        if (player != null) {
            UUID uuid = player.getUniqueId();
            if (necroRegistry.hasRegister(uuid)) {
                eUndo(uuid, player);
            }
        }
    }

    private void eUndo(UUID uuid, Player player) {
        if (player.isInsideVehicle()) {
            if (player.getVehicle() instanceof Spider) {
                entityHelper.removePassenger(player.getVehicle(), player);
            }
        }
        disguiseHelper.undisguise(player);
        ItemStack[] inv = necroRegistry.removeRegister(uuid, ItemStack[].class);
        player.getInventory().setContents(inv);

        sender.sendMessage(player.getName() + " is no longer necro'd.");
    }

    private ItemStack getInfinityBow() {
        ItemStack retVal = new ItemStack(Material.BOW, 1);
        retVal.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        retVal.addEnchantment(Enchantment.DURABILITY, 3);
        return retVal;
    }
}
