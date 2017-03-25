package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class PopupCommand extends BasePluginCommand {
	//vars
	private IRegistry popupRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.POPUP_REGISTRY);
	
	//constructor
	public PopupCommand() {
		super();
	}
	
	//public
	public void onLogin(String uuid, Player player) {
		popupRegistry.computeIfPresent(uuid, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_POPUP, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (popupRegistry.contains(uuid)) {
			sender.sendMessage(player.getName() + "'s inventory is no longer opening and closing randomly.");
			popupRegistry.setRegister(uuid, null);
		} else {
			sender.sendMessage(player.getName() + "'s inventory is now opening and closing randomly.");
			popupRegistry.setRegister(uuid, player);
		}
	}
}
