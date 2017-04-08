package me.egg82.tcpp.ticks;

import ninja.egg82.plugin.commands.TickCommand;

public class LagTickCommand extends TickCommand {
	//vars
	
	//constructor
	public LagTickCommand() {
		super();
		ticks = 5L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		
	}
}
