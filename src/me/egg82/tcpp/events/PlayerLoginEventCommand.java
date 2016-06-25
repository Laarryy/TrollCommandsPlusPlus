package me.egg82.tcpp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.utils.interfaces.ICommandHandler;

public class PlayerLoginEventCommand extends EventCommand {
	//vars
	private static ICommandHandler commandHandler = (ICommandHandler) ServiceLocator.getService(SpigotServiceType.COMMAND_HANDLER);
	
	//constructor
	public PlayerLoginEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		PluginCommand[] commands = commandHandler.getInitializedCommands();
		Player player = ((PlayerLoginEvent) event).getPlayer();
		String lowerName = player.getName().toLowerCase();
		
		for (int i = 0; i < commands.length; i++) {
			commands[i].onLogin(lowerName, player);
		}
	}
}
