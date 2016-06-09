package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import me.egg82.tcpp.events.individual.playerInteractEvent.ControlEventCommand;
import me.egg82.tcpp.events.individual.playerInteractEvent.LagEventCommand;
import me.egg82.tcpp.events.individual.playerInteractEvent.VegetableEventCommand;
import ninja.egg82.plugin.commands.EventCommand;

public class PlayerInteractEventCommand extends EventCommand {
	//vars
	private ControlEventCommand control = null;
	private VegetableEventCommand vegetable = null;
	private LagEventCommand lag = null;
	
	//constructor
	public PlayerInteractEventCommand(Event event) {
		super(event);
		
		control = new ControlEventCommand(event);
		vegetable = new VegetableEventCommand(event);
		lag = new LagEventCommand(event);
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerInteractEvent e = (PlayerInteractEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		control.start();
		vegetable.start();
		lag.start();
	}
}
