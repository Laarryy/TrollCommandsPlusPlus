package me.egg82.tcpp.events;

import org.bukkit.event.Event;

import me.egg82.tcpp.events.individual.moveEvent.ControllerEventCommand;
import me.egg82.tcpp.events.individual.moveEvent.FreezeEventCommand;
import ninja.egg82.plugin.commands.EventCommand;

public class PlayerMoveEventCommand extends EventCommand {
	//vars
	private FreezeEventCommand freeze = null;
	private ControllerEventCommand controller = null;
	
	//constructor
	public PlayerMoveEventCommand(Event event) {
		super(event);
		
		freeze = new FreezeEventCommand(event);
		controller = new ControllerEventCommand(event);
	}
	
	//public
	
	//private
	protected void execute() {
		freeze.start();
		controller.start();
	}
}
