package me.egg82.tcpp.util;

import me.egg82.tcpp.registries.CommandRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;

public class MetricsHelper {
	//vars
	private IVariableRegistry<String> commandRegistry = ServiceLocator.getService(CommandRegistry.class);
	
	//constructor
	public MetricsHelper() {
		
	}
	
	//public
	public void commandWasRun(CommandHandler command) {
		commandWasRun(command, 1);
	}
	public void commandWasRun(CommandHandler command, int numTimes) {
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
