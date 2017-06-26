package me.egg82.tcpp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.utils.ReflectUtil;

public class StopTrollsCommand extends PluginCommand {
	//vars
	private ArrayList<PluginCommand> commands = new ArrayList<PluginCommand>();
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public StopTrollsCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
		
		List<Class<? extends PluginCommand>> commands = ReflectUtil.getClasses(PluginCommand.class, "me.egg82.tcpp.commands");
		for (int i = 0; i < commands.size(); i++) {
			PluginCommand run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(CommandSender.class, Command.class, String.class, String[].class).newInstance(sender, command, label, args);
			} catch (Exception ex) {
				continue;
			}
			this.commands.add(run);
		}
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_STOP_TROLLS)) {
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
		
		Player player = Bukkit.getPlayer(args[0]);
		
		if (player == null) {
			sender.sendMessage(SpigotMessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.PLAYER_NOT_FOUND);
			return;
		}
		
		for (int i = 0; i < commands.size(); i++) {
			PluginCommand run = commands.get(i);
			run.setSender(sender);
			run.setArgs(args);
			run.undo();
		}
		
		metricsHelper.commandWasRun(command.getName());
		
		sender.sendMessage("All active trolls against " + player.getName() + " have been stopped.");
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	
	protected void onUndo() {
		
	}
}