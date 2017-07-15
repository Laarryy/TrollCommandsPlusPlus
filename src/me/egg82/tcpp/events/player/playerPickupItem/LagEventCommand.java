package me.egg82.tcpp.events.player.playerPickupItem;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.egg82.tcpp.services.LagItemRegistry;
import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand<PlayerPickupItemEvent> {
	//vars
	private IRegistry lagRegistry = ServiceLocator.getService(LagRegistry.class);
	private IRegistry lagItemRegistry = ServiceLocator.getService(LagItemRegistry.class);
	
	//constructor
	public LagEventCommand(PlayerPickupItemEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (!lagRegistry.hasRegister(uuid)) {
			return;
		}
		
		String itemUuid = event.getItem().getUniqueId().toString();
		
		Long pickupTime = lagItemRegistry.getRegister(uuid + "," + itemUuid, Long.class);
		
		if (pickupTime == null) {
			event.setCancelled(true);
			lagItemRegistry.setRegister(uuid + "," + itemUuid, Long.class, System.currentTimeMillis() + MathUtil.fairRoundedRandom(1500, 2500));
		} else if (System.currentTimeMillis() < pickupTime) {
			event.setCancelled(true);
		} else {
			lagItemRegistry.setRegister(uuid + "," + itemUuid, Long.class, null);
		}
	}
}
