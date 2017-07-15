package me.egg82.tcpp.events.entity.playerDeath;

import java.util.ArrayDeque;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.NightmareRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.protocol.IFakeLivingEntity;

public class NightmareEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);
	
	//constructor
	public NightmareEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getEntity();
		String uuid = player.getUniqueId().toString();
		
		if (!nightmareRegistry.hasRegister(uuid)) {
			return;
		}
		
		for (IFakeLivingEntity entity : (ArrayDeque<IFakeLivingEntity>) nightmareRegistry.getRegister(uuid)) {
			entity.destroy();
		}
		nightmareRegistry.setRegister(uuid, ArrayDeque.class, null);
	}
}
