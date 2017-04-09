package me.egg82.tcpp.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.PortalRegistry;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.startup.InitRegistry;

public class PortalCommand extends PluginCommand {
	//vars
	private IRegistry portalRegistry = (IRegistry) ServiceLocator.getService(PortalRegistry.class);
	
	private IRegistry initRegistry = (IRegistry) ServiceLocator.getService(InitRegistry.class);
	
	//constructor
	public PortalCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_PORTAL)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
		Player player = CommandUtil.getPlayerByName(args[0]);
		
		if (player == null) {
			sender.sendMessage(SpigotMessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.PLAYER_NOT_FOUND);
			return;
		}
		if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return;
		}
		
		String uuid = player.getUniqueId().toString();
		
		if (portalRegistry.hasRegister(uuid)) {
			sender.sendMessage(MessageType.ALREADY_USED);
			dispatch(CommandEvent.ERROR, CommandErrorType.ALREADY_USED);
			return;
		}
		
		e(uuid, player);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player) {
		// Center should be three blocks below player, for a total of five blocks of depth minus a layer for portals
		Location centerLocation = player.getLocation().clone().subtract(0.0d, 3.0d, 0.0d);
		
		// Get all blocks, 3x3x5 (LxWxH)
		List<BlockData> blockData = BlockUtil.getBlocks(centerLocation, 1, 2, 1);
		// Fill the previous 3x3x5 area with air
		BlockUtil.clearBlocks(centerLocation, Material.AIR, 1, 2, 1);
		// Fill bottom layer of new air blocks with portals
		BlockUtil.clearBlocks(centerLocation.clone().subtract(0.0d, 2.0d, 0.0d), Material.ENDER_PORTAL, 1, 0, 1);
		
		// Wait five seconds
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((JavaPlugin) initRegistry.getRegister("plugin"), new Runnable() {
			public void run() {
				// Put all the blocks back
				BlockUtil.setBlocks(blockData, centerLocation, 1, 2, 1);
			}
		}, 100);
		
		sender.sendMessage(player.getName() + " is now falling to The(ir) End.");
	}
}
