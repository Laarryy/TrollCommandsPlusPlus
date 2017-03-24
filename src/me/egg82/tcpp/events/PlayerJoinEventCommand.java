package me.egg82.tcpp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.utils.interfaces.ICommandHandler;

public class PlayerJoinEventCommand extends EventCommand {
	//vars
	private static ICommandHandler commandHandler = (ICommandHandler) ServiceLocator.getService(SpigotServiceType.COMMAND_HANDLER);
	
	//constructor
	public PlayerJoinEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		PluginCommand[] commands = commandHandler.getInitializedCommands();
		Player player = ((PlayerJoinEvent) event).getPlayer();
		String uuid = player.getUniqueId().toString();
		
		for (int i = 0; i < commands.length; i++) {
			commands[i].onLogin(uuid, player);
		}
	}
}
