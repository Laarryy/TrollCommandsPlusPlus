package me.egg82.tcpp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.WhoAmINameRegistry;
import me.egg82.tcpp.services.WhoAmIRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class WhoAmICommand extends PluginCommand {
	//vars
	private IRegistry whoAmIRegistry = (IRegistry) ServiceLocator.getService(WhoAmIRegistry.class);
	private IRegistry whoAmINameRegistry = (IRegistry) ServiceLocator.getService(WhoAmINameRegistry.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public WhoAmICommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_WHO_AM_I)) {
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
		
		e(player.getUniqueId().toString(), player);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	@SuppressWarnings("unchecked")
	private void e(String uuid, Player player) {
		if (whoAmIRegistry.hasRegister(uuid)) {
			whoAmIRegistry.setRegister(uuid, Player.class, null);
			
			List<String> names = (List<String>) whoAmINameRegistry.getRegister(uuid);
			player.setDisplayName(names.get(0));
			player.setPlayerListName(names.get(1));
			player.setCustomName(names.get(2));
			
			whoAmINameRegistry.setRegister(uuid, List.class, null);
			
			sender.sendMessage(player.getName() + " is no longer having an identity crisis.");
		} else {
			ArrayList<String> names = new ArrayList<String>();
			names.add(player.getDisplayName());
			names.add(player.getPlayerListName());
			names.add(player.getCustomName());
			
			whoAmIRegistry.setRegister(uuid, Player.class, player);
			whoAmINameRegistry.setRegister(uuid, List.class, names);
			metricsHelper.commandWasRun(command.getName());
			
			sender.sendMessage(player.getName() + " is now having an identity crisis.");
		}
	}
}
