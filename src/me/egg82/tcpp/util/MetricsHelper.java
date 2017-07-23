package me.egg82.tcpp.util;

import me.egg82.tcpp.services.CommandRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;

public class MetricsHelper {
	//vars
	private IRegistry<String> commandRegistry = ServiceLocator.getService(CommandRegistry.class);
	
	//constructor
	public MetricsHelper() {
		
	}
	
	//public
	public void commandWasRun(PluginCommand command) {
		commandWasRun(command, 1);
	}
	public void commandWasRun(PluginCommand command, int numTimes) {
		String name = command.getClass().getSimpleName().toLowerCase();
		if (name.substring(name.length() - 7).equals("command")) {
			name = name.substring(0, name.length() - 7);
		}
		
		Integer currentRuns = commandRegistry.getRegister(name, Integer.class);
		
		if (currentRuns == null) {
			currentRuns = 0;
		}
		currentRuns += numTimes;
		
		commandRegistry.setRegister(name, currentRuns);
	}
	
	public void clear() {
		commandRegistry.clear();
	}
	
	//private
	
}
