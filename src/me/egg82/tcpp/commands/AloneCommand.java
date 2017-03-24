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
	public void onLogin(String uuid, Player player) {
		Player temp = (Player) reg.getRegister(uuid);
		
		if (temp != null) {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				player.hidePlayer(p);
			}
			reg.setRegister(uuid, player);
		} else {
			String[] names = reg.registryNames();
			for (String n : names) {
				Player p = (Player) reg.getRegister(n);
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
		if (reg.contains(uuid)) {
			sender.sendMessage(player.getName() + " is no longer alone in this wold!");
			reg.setRegister(uuid, null);
			
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				player.showPlayer(p);
			}
		} else {
			sender.sendMessage(player.getName() + " is now all alone :(");
			reg.setRegister(uuid, player);
			
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				player.hidePlayer(p);
			}
		}
	}
}
