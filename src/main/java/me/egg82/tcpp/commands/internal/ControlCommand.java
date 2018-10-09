package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.egg82.tcpp.registries.ControlRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.ControlHelper;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.disguise.reflection.IDisguiseHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;

public class ControlCommand extends CommandHandler {
    //vars
    private IVariableRegistry<UUID> controlRegistry = ServiceLocator.getService(ControlRegistry.class);

    private IDisguiseHelper disguiseHelper = ServiceLocator.getService(IDisguiseHelper.class);
    private ControlHelper controlHelper = ServiceLocator.getService(ControlHelper.class);

    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public ControlCommand() {
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
        if (!sender.hasPermission(PermissionsType.COMMAND_CONTROL)) {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
            return;
        }
        if (!disguiseHelper.isValidLibrary()) {
            sender.sendMessage(ChatColor.RED + "This command has been disabled because there is no recognized backing library available. Please install one and restart the server to enable this command.");
            return;
        }
        if (!CommandUtil.isPlayer((CommandSender) sender.getHandle())) {
            sender.sendMessage(ChatColor.RED + "Console cannot run this command!");
            return;
        }
        if (!CommandUtil.isArrayOfAllowedLength(args, 0, 1)) {
            sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
            String name = getClass().getSimpleName();
            name = name.substring(0, name.length() - 7).toLowerCase();
            Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
            return;
        }

        Player controller = (Player) sender.getHandle();
        UUID controllerUuid = controller.getUniqueId();

        if (args.length == 0) {
            if (!controlRegistry.hasRegister(controllerUuid)) {
                sender.sendMessage(ChatColor.RED + "The target you've chosen is invalid.");
                return;
            }

            controlHelper.uncontrol(controllerUuid, controller);
        } else if (args.length == 1) {
            Player player = CommandUtil.getPlayerByName(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }

            UUID playerUuid = player.getUniqueId();

            if (controllerUuid.equals(playerUuid)) {
                sender.sendMessage(ChatColor.RED + "The target you've chosen is invalid.");
                return;
            }

            Player controlledPlayer = CommandUtil.getPlayerByUuid(controlRegistry.getRegister(controllerUuid, UUID.class));
            if (controlledPlayer != null) {
                controlHelper.uncontrol(controllerUuid, controller);
                if (controlledPlayer.getUniqueId().equals(playerUuid)) {
                    metricsHelper.commandWasRun(this);
                    return;
                }
            }

            if (player.hasPermission(PermissionsType.IMMUNE)) {
                sender.sendMessage(ChatColor.RED + "Player is immune.");
                return;
            }

            controlHelper.control(controller.getUniqueId(), controller, playerUuid, player);

            metricsHelper.commandWasRun(this);
        }
    }

    protected void onUndo() {
        Player player = CommandUtil.getPlayerByName(args[0]);

        if (player == null) {
            return;
        }

        UUID playerUuid = player.getUniqueId();

        for (UUID controllerUuid : controlRegistry.getKeys()) {
            Player controlledPlayer = CommandUtil.getPlayerByUuid(controlRegistry.getRegister(controllerUuid, UUID.class));

            if (controlledPlayer != null && controlledPlayer.getUniqueId().equals(playerUuid)) {
                controlHelper.uncontrol(controllerUuid, CommandUtil.getPlayerByUuid(controllerUuid));
                break;
            }
        }
    }
}
