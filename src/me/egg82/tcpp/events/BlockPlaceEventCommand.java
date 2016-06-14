package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.events.individual.blockPlaceEvent.ExplodeBuildEventCommand;
import me.egg82.tcpp.events.individual.blockPlaceEvent.LagEventCommand;
import me.egg82.tcpp.events.individual.blockPlaceEvent.SlowUndoEventCommand;
import ninja.egg82.plugin.commands.EventCommand;

public class BlockPlaceEventCommand extends EventCommand {
	//vars
	private LagEventCommand lag = null;
	private ExplodeBuildEventCommand explodeBuild = null;
	private SlowUndoEventCommand slowUndo = null;
	
	//constructor
	public BlockPlaceEventCommand(Event event) {
		super(event);
		
		lag = new LagEventCommand(event);
		explodeBuild = new ExplodeBuildEventCommand(event);
		slowUndo = new SlowUndoEventCommand(event);
	}
	
	//public
	
	//private
	protected void execute() {
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		lag.start();
		explodeBuild.start();
		slowUndo.start();
	}
}
