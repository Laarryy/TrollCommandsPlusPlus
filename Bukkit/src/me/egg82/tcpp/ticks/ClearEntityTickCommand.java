package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Entity;

import me.egg82.tcpp.registries.SpartaArrowRegistry;
import me.egg82.tcpp.registries.SquidDeathRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;

public class ClearEntityTickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> squidDeathRegistry = ServiceLocator.getService(SquidDeathRegistry.class);
	private IVariableRegistry<UUID> spartaArrowRegistry = ServiceLocator.getService(SpartaArrowRegistry.class);
	
	//constructor
	public ClearEntityTickCommand() {
		super(0L, 50L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : squidDeathRegistry.getKeys()) {
			e(squidDeathRegistry, key, squidDeathRegistry.getRegister(key, Entity.class));
		}
		
		for (UUID key : spartaArrowRegistry.getKeys()) {
			e(spartaArrowRegistry, key, spartaArrowRegistry.getRegister(key, Entity.class));
		}
	}
	private void e(IVariableRegistry<UUID> registry, UUID uuid, Entity entity) {
		if (entity.getTicksLived() >= 100L) {
			entity.remove();
			registry.removeRegister(uuid);
		}
	}
}
