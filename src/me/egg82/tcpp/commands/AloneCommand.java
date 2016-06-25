package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class AloneCommand extends BasePluginCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.ALONE_REGISTRY);
	
	//constructor
	public AloneCommand() {
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
		if (isValid(false, PermissionsType.COMMAND_ALONE, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		String lowerName = name.toLowerCase();
		
		if (reg.contains(lowerName)) {
			sender.sendMessage(name + " is no longer alone in this wold!");
			reg.setRegister(lowerName, null);
			
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				player.showPlayer(p);
			}
		} else {
			sender.sendMessage(name + " is now all alone :(");
			reg.setRegister(lowerName, player);
		}
	}
}
