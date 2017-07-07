package me.egg82.tcpp.util;

import me.egg82.tcpp.services.CommandRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;

public class MetricsHelper {
	//vars
	private IRegistry commandRegistry = (IRegistry) ServiceLocator.getService(CommandRegistry.class);
	
	//constructor
	public MetricsHelper() {
		
	}
	
	//public
	public void commandWasRun(PluginCommand command) {
		commandWasRun(command, 1);
	}
	public void commandWasRun(PluginCommand command, int numTimes) {
		String name = command.getClass().getSimpleName();
		name = name.substring(0, name.length() - 7).toLowerCase();
		
		Integer currentRuns = (Integer) commandRegistry.getRegister(name);
		
		if (currentRuns == null) {
			currentRuns = 0;
		}
		currentRuns += numTimes;
		
		commandRegistry.setRegister(name, Integer.class, currentRuns);
	}
	
	public void clear() {
		commandRegistry.clear();
	}
	
	//private
	
}
