package me.egg82.tcpp.events.player.playerPickupArrow;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import me.egg82.tcpp.services.LagItemRegistry;
import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand {
	//vars
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(LagRegistry.class);
	private IRegistry lagItemRegistry = (IRegistry) ServiceLocator.getService(LagItemRegistry.class);
	
	//constructor
	public LagEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerPickupArrowEvent e = (PlayerPickupArrowEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (!lagRegistry.hasRegister(uuid)) {
			return;
		}
		
		String itemUuid = e.getItem().getUniqueId().toString();
		
		Long pickupTime = (Long) lagItemRegistry.getRegister(uuid + "," + itemUuid);
		
		if (pickupTime == null) {
			e.setCancelled(true);
			lagItemRegistry.setRegister(uuid + "," + itemUuid, Long.class, System.currentTimeMillis() + MathUtil.fairRoundedRandom(1500, 2500));
		} else if (System.currentTimeMillis() < pickupTime) {
			e.setCancelled(true);
		} else {
			lagItemRegistry.setRegister(uuid + "," + itemUuid, Long.class, null);
		}
	}
}
