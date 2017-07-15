package me.egg82.tcpp.ticks;

import org.bukkit.entity.Entity;

import me.egg82.tcpp.services.SpartaArrowRegistry;
import me.egg82.tcpp.services.SquidDeathRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class ClearEntityTickCommand extends TickCommand {
	//vars
	private IRegistry squidDeathRegistry = ServiceLocator.getService(SquidDeathRegistry.class);
	private IRegistry spartaArrowRegistry = ServiceLocator.getService(SpartaArrowRegistry.class);
	
	//constructor
	public ClearEntityTickCommand() {
		super();
		ticks = 50L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = squidDeathRegistry.getRegistryNames();
		for (String name : names) {
			e(squidDeathRegistry, name, squidDeathRegistry.getRegister(name, Entity.class));
		}
		
		names = spartaArrowRegistry.getRegistryNames();
		for (String name : names) {
			e(spartaArrowRegistry, name, spartaArrowRegistry.getRegister(name, Entity.class));
		}
	}
	private void e(IRegistry registry, String uuid, Entity entity) {
		if (entity.getTicksLived() >= 100L) {
			entity.remove();
			registry.setRegister(uuid, Entity.class, null);
		}
	}
}
