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
	private IRegistry aloneRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.ALONE_REGISTRY);
	
	//constructor
	public AloneCommand() {
		super();
	}
	
	//public
	public void onLogin(String uuid, Player player) {
		Player temp = (Player) aloneRegistry.getRegister(uuid);
		
		if (temp != null) {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				player.hidePlayer(p);
			}
			aloneRegistry.setRegister(uuid, player);
		} else {
			String[] names = aloneRegistry.registryNames();
			for (String n : names) {
				Player p = (Player) aloneRegistry.getRegister(n);
				if (p != null) {
					p.hidePlayer(player);
				}
			}
		}
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_ALONE, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (aloneRegistry.contains(uuid)) {
			sender.sendMessage(player.getName() + " is no longer alone in this wold!");
			aloneRegistry.setRegister(uuid, null);
			
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				player.showPlayer(p);
			}
		} else {
			sender.sendMessage(player.getName() + " is now all alone :(");
			aloneRegistry.setRegister(uuid, player);
			
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				player.hidePlayer(p);
			}
		}
	}
}
