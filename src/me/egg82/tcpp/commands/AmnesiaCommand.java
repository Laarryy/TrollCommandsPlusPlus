package me.egg82.tcpp.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class AmnesiaCommand extends BasePluginCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.AMNESIA_REGISTRY);
	private IRegistry reg2 = (IRegistry) ServiceLocator.getService(PluginServiceType.AMNESIA_INTERN_REGISTRY);
	
	//constructor
	public AmnesiaCommand() {
		super();
	}
	
	//public
	public void onLogin(String name, Player player) {
		reg.computeIfPresent(name, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_AMNESIA, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		String lowerName = name.toLowerCase();
		
		if (reg.contains(lowerName)) {
			sender.sendMessage(name + " is no longer an amnesiac.");
			reg.setRegister(lowerName, null);
			reg2.setRegister(lowerName, null);
		} else {
			sender.sendMessage(name + " is now an amnesiac.");
			reg.setRegister(lowerName, player);
			reg2.setRegister(lowerName, new ArrayList<ImmutableMap<String, Object>>());
		}
	}
}
