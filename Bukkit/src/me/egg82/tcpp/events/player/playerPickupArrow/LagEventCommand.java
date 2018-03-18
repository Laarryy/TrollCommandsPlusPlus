package me.egg82.tcpp.events.player.playerPickupArrow;

import java.util.UUID;

import org.bukkit.event.player.PlayerPickupArrowEvent;

import me.egg82.tcpp.services.registries.LagItemRegistry;
import me.egg82.tcpp.services.registries.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand<PlayerPickupArrowEvent> {
	//vars
	private IRegistry<UUID> lagRegistry = ServiceLocator.getService(LagRegistry.class);
	private IRegistry<UUID> lagItemRegistry = ServiceLocator.getService(LagItemRegistry.class);
	
	//constructor
	public LagEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (!lagRegistry.hasRegister(event.getPlayer().getUniqueId())) {
			return;
		}
		
		UUID uuid = event.getItem().getUniqueId();
		
		Long pickupTime = lagItemRegistry.getRegister(uuid, Long.class);
		
		if (pickupTime == null) {
			event.setCancelled(true);
			lagItemRegistry.setRegister(uuid, System.currentTimeMillis() + MathUtil.fairRoundedRandom(1500, 2500));
		} else if (System.currentTimeMillis() < pickupTime) {
			event.setCancelled(true);
		} else {
			lagItemRegistry.removeRegister(uuid);
		}
	}
}
