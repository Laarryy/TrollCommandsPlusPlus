package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class PopupTickCommand extends TickCommand {
	//vars
	private IRegistry popupRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.POPUP_REGISTRY);
	
	//constructor
	public PopupTickCommand() {
		super();
		ticks = 20l;
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = popupRegistry.registryNames();
		for (String name : names) {
			e((Player) popupRegistry.getRegister(name));
		}
	}
	private void e(Player player) {
		if(player == null) {
			return;
		}
		
		if (Math.random() <= 0.08d) {
			player.openInventory(player.getInventory());
		} else if (Math.random() <= 0.1d) {
			player.closeInventory();
		}
	}
}
