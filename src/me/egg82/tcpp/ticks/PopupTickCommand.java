package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.PopupRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class PopupTickCommand extends TickCommand {
	//vars
	private IRegistry popupRegistry = ServiceLocator.getService(PopupRegistry.class);
	
	//constructor
	public PopupTickCommand() {
		super();
		ticks = 20L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = popupRegistry.getRegistryNames();
		for (String name : names) {
			e(name, popupRegistry.getRegister(name, Player.class));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		if (Math.random() <= 0.1d) {
			player.openInventory(player.getInventory());
		} else if (Math.random() <= 0.1d) {
			player.closeInventory();
		}
	}
}
