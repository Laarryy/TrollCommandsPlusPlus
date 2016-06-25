package me.egg82.tcpp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.utils.interfaces.ICommandHandler;

public class PlayerQuitEventCommand extends EventCommand {
	//vars
	private static ICommandHandler commandHandler = (ICommandHandler) ServiceLocator.getService(SpigotServiceType.COMMAND_HANDLER);
	
	//constructor
	public PlayerQuitEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		PluginCommand[] commands = commandHandler.getInitializedCommands();
		Player player = ((PlayerQuitEvent) event).getPlayer();
		String lowerName = player.getName().toLowerCase();
		
		for (int i = 0; i < commands.length; i++) {
			commands[i].onQuit(lowerName, player);
		}
	}
}
