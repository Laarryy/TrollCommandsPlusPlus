package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.registries.PopupRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class PopupTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> popupRegistry = ServiceLocator.getService(PopupRegistry.class);
	
	//constructor
	public PopupTickCommand() {
		super();
		ticks = 20L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : popupRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		if (Math.random() <= 0.1d) {
			player.openInventory(player.getInventory());
		} else if (Math.random() <= 0.1d) {
			player.closeInventory();
		}
	}
}
