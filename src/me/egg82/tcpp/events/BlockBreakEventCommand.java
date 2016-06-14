package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.events.individual.blockBreakEvent.DisplayEventCommand;
import me.egg82.tcpp.events.individual.blockBreakEvent.ExplodeBreakEventCommand;
import me.egg82.tcpp.events.individual.blockBreakEvent.LagEventCommand;
import me.egg82.tcpp.events.individual.blockBreakEvent.LavaBreakEventCommand;
import me.egg82.tcpp.events.individual.blockBreakEvent.SlowUndoEventCommand;
import ninja.egg82.plugin.commands.EventCommand;

public class BlockBreakEventCommand extends EventCommand {
	//vars
	private LavaBreakEventCommand lavaBreak = null;
	private LagEventCommand lag = null;
	private ExplodeBreakEventCommand explodeBreak = null;
	private DisplayEventCommand display = null;
	private SlowUndoEventCommand slowUndo = null;
	
	//constructor
	public BlockBreakEventCommand(Event event) {
		super(event);
		
		lavaBreak = new LavaBreakEventCommand(event);
		lag = new LagEventCommand(event);
		explodeBreak = new ExplodeBreakEventCommand(event);
		display = new DisplayEventCommand(event);
		slowUndo = new SlowUndoEventCommand(event);
	}
	
	//public
	
	//private
	protected void execute() {
		BlockBreakEvent e = (BlockBreakEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		lavaBreak.start();
		lag.start();
		explodeBreak.start();
		display.start();
		slowUndo.start();
	}
}
