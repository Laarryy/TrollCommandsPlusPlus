package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class ClumsyCommand extends BasePluginCommand {
	//vars
	private IRegistry clumsyRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CLUMSY_REGISTRY);
	
	//constructor
	public ClumsyCommand() {
		super();
	}
	
	//public
	public void onLogin(String uuid, Player player) {
		clumsyRegistry.computeIfPresent(uuid, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_CLUMSY, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (clumsyRegistry.contains(uuid)) {
			sender.sendMessage(player.getName() + " is no longer clumsy.");
			clumsyRegistry.setRegister(uuid, null);
		} else {
			sender.sendMessage(player.getName() + " is now very clumsy!");
			clumsyRegistry.setRegister(uuid, player);
		}
	}
}
