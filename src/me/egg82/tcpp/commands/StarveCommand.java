package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class StarveCommand extends BasePluginCommand {
	//vars
	IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.STARVE_REGISTRY);
	
	//constructor
	public StarveCommand() {
		super();
	}
	
	//public
	public void onDeath(String uuid, Player player) {
		reg.computeIfPresent(uuid, (k,v) -> {
			return null;
		});
	}
	public void onLogin(String uuid, Player player) {
		reg.computeIfPresent(uuid, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_STARVE, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (reg.contains(uuid)) {
			sender.sendMessage(player.getName() + " is no longer starving to death.");
			reg.setRegister(uuid, null);
		} else {
			sender.sendMessage(player.getName() + " is now starving to death.");
			reg.setRegister(uuid, player);
		}
	}
}
