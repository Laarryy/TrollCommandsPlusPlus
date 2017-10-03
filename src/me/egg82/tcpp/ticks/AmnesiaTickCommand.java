package me.egg82.tcpp.ticks;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.registries.AmnesiaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class AmnesiaTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> amnesiaRegistry = ServiceLocator.getService(AmnesiaRegistry.class);
	
	//constructor
	public AmnesiaTickCommand() {
		super();
		ticks = 20L;
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : amnesiaRegistry.getKeys()) {
			e(key, CommandUtil.getPlayerByUuid(key), amnesiaRegistry.getRegister(key, List.class));
		}
	}
	private void e(UUID uuid, Player player, List<String> messages) {
		if (player == null) {
			return;
		}
		
		synchronized(messages) {
			Iterator<String> i = messages.iterator();
			while (i.hasNext()) {
				String v = i.next();
				
				if (Math.random() <= 0.1d) {
					player.sendMessage(v);
					i.remove();
				}
			}
		}
	}
}
