package me.egg82.tcpp.events;

import org.bukkit.event.Event;

import me.egg82.tcpp.events.individual.chatEvent.ControlEventCommand;
import me.egg82.tcpp.events.individual.chatEvent.ControllerEventCommand;
import me.egg82.tcpp.events.individual.chatEvent.GarbleEventCommand;
import ninja.egg82.plugin.commands.EventCommand;

public class AsyncPlayerChatEventCommand extends EventCommand {
	//vars
	private GarbleEventCommand garble = null;
	private ControlEventCommand control = null;
	private ControllerEventCommand controller = null;
	
	//constructor
	public AsyncPlayerChatEventCommand(Event event) {
		super(event);
		
		garble = new GarbleEventCommand(event);
		control = new ControlEventCommand(event);
		controller = new ControllerEventCommand(event);
	}
	
	//public
	
	//private
	protected void execute() {
		garble.start();
		control.start();
		controller.start();
	}
}
