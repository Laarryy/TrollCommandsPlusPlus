package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.egg82.tcpp.events.individual.playerPickupItemEvent.NopickupEventCommand;
import me.egg82.tcpp.events.individual.playerPickupItemEvent.VegetableEventCommand;
import ninja.egg82.plugin.commands.EventCommand;

public class PlayerPickupItemEventCommand extends EventCommand {
	//vars
	private VegetableEventCommand vegetable = null;
	private NopickupEventCommand nopickup = null;
	
	//constructor
	public PlayerPickupItemEventCommand(Event event) {
		super(event);
		
		vegetable = new VegetableEventCommand(event);
		nopickup = new NopickupEventCommand(event);
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerPickupItemEvent e = (PlayerPickupItemEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		vegetable.start();
		nopickup.start();
	}
}
