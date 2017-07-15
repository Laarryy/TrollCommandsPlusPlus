package me.egg82.tcpp.commands.internal;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.EmpowerRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class EmpowerCommand extends PluginCommand {
	//vars
	private IRegistry empowerRegistry = ServiceLocator.getService(EmpowerRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public EmpowerCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_EMPOWER)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(SpigotMessageType.CONSOLE_NOT_ALLOWED);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.CONSOLE_NOT_ALLOWED);
			return;
		}
		
		Player player = (Player) sender;
		
		e(player.getUniqueId().toString(), player);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player) {
		if (empowerRegistry.hasRegister(uuid)) {
			empowerRegistry.setRegister(uuid, Player.class, null);
			
			sender.sendMessage("You will no longer empower (or disempower) the next player or mob you right-click.");
		} else {
			empowerRegistry.setRegister(uuid, Player.class, player);
			metricsHelper.commandWasRun(this);
			
			sender.sendMessage("You will now empower (or disempower) the next player or mob you right-click!");
		}
	}
	
	protected void onUndo() {
		
	}
}