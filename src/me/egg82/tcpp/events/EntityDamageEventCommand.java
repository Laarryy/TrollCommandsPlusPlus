package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import me.egg82.tcpp.events.individual.entityDamageEvent.BrittleEventCommand;
import ninja.egg82.plugin.commands.EventCommand;

public class EntityDamageEventCommand extends EventCommand {
	//vars
	private BrittleEventCommand brittle = null;
	
	//constructor
	public EntityDamageEventCommand(Event event) {
		super(event);
		
		brittle = new BrittleEventCommand(event);
	}
	
	//public
	
	//private
	protected void execute() {
		EntityDamageEvent e = (EntityDamageEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		brittle.start();
	}
}
