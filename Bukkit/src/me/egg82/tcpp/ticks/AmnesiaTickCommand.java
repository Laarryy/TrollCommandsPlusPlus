package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.registries.AmnesiaRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.concurrent.IConcurrentDeque;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;

public class AmnesiaTickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> amnesiaRegistry = ServiceLocator.getService(AmnesiaRegistry.class);
	
	//constructor
	public AmnesiaTickCommand() {
		super(0L, 20L);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : amnesiaRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key), amnesiaRegistry.getRegister(key, IConcurrentDeque.class));
		}
	}
	private void e(Player player, IConcurrentDeque<String> messages) {
		if (player == null) {
			return;
		}
		
		for (String v : messages) {
			if (Math.random() <= 0.1d) {
				player.sendMessage(v);
				messages.remove(v);
			}
		}
	}
}
