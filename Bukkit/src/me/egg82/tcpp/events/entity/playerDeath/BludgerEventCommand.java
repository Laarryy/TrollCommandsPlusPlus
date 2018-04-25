package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import org.bukkit.entity.Fireball;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.registries.BludgerRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class BludgerEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IVariableRegistry<UUID> bludgerRegistry = ServiceLocator.getService(BludgerRegistry.class);
	
	//constructor
	public BludgerEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID uuid = event.getEntity().getUniqueId();
		
		if (!bludgerRegistry.hasRegister(uuid)) {
			return;
		}
		
		bludgerRegistry.getRegister(uuid, Fireball.class).remove();
		bludgerRegistry.removeRegister(uuid);
	}
}
