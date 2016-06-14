package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.events.individual.playerChatEvent.AmnesiaEventCommand;
import me.egg82.tcpp.events.individual.playerChatEvent.ControlEventCommand;
import me.egg82.tcpp.events.individual.playerChatEvent.ControllerEventCommand;
import me.egg82.tcpp.events.individual.playerChatEvent.GarbleEventCommand;
import me.egg82.tcpp.events.individual.playerChatEvent.LagEventCommand;
import me.egg82.tcpp.events.individual.playerChatEvent.VegetableEventCommand;
import ninja.egg82.plugin.commands.EventCommand;

public class AsyncPlayerChatEventCommand extends EventCommand {
	//vars
	private GarbleEventCommand garble = null;
	private ControlEventCommand control = null;
	private ControllerEventCommand controller = null;
	private VegetableEventCommand vegetable = null;
	private LagEventCommand lag = null;
	private AmnesiaEventCommand amnesia = null;
	
	//constructor
	public AsyncPlayerChatEventCommand(Event event) {
		super(event);
		
		garble = new GarbleEventCommand(event);
		control = new ControlEventCommand(event);
		controller = new ControllerEventCommand(event);
		vegetable = new VegetableEventCommand(event);
		lag = new LagEventCommand(event);
		amnesia = new AmnesiaEventCommand(event);
	}
	
	//public
	
	//private
	protected void execute() {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		garble.start();
		control.start();
		controller.start();
		vegetable.start();
		lag.start();
		amnesia.start();
	}
}
