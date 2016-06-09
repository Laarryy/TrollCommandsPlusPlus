package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.events.individual.playerMoveEvent.ControllerEventCommand;
import me.egg82.tcpp.events.individual.playerMoveEvent.FreezeEventCommand;
import me.egg82.tcpp.events.individual.playerMoveEvent.InfinityEventCommand;
import me.egg82.tcpp.events.individual.playerMoveEvent.LagEventCommand;
import me.egg82.tcpp.events.individual.playerMoveEvent.VegetableEventCommand;
import ninja.egg82.plugin.commands.EventCommand;

public class PlayerMoveEventCommand extends EventCommand {
	//vars
	private FreezeEventCommand freeze = null;
	private ControllerEventCommand controller = null;
	private VegetableEventCommand vegetable = null;
	private InfinityEventCommand infinity = null;
	private LagEventCommand lag = null;
	
	//constructor
	public PlayerMoveEventCommand(Event event) {
		super(event);
		
		freeze = new FreezeEventCommand(event);
		controller = new ControllerEventCommand(event);
		vegetable = new VegetableEventCommand(event);
		infinity = new InfinityEventCommand(event);
		lag = new LagEventCommand(event);
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		freeze.start();
		controller.start();
		vegetable.start();
		infinity.start();
		lag.start();
	}
}
