package me.egg82.tcpp.util;

import me.egg82.tcpp.services.CommandRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;

public class MetricsHelper {
	//vars
	private IRegistry commandRegistry = (IRegistry) ServiceLocator.getService(CommandRegistry.class);
	
	//constructor
	public MetricsHelper() {
		
	}
	
	//public
	public void commandWasRun(String commandName) {
		commandWasRun(commandName, 1);
	}
	public void commandWasRun(String commandName, int numTimes) {
		Integer currentRuns = (Integer) commandRegistry.getRegister(commandName);
		
		if (currentRuns == null) {
			currentRuns = 0;
		}
		currentRuns += numTimes;
		
		commandRegistry.setRegister(commandName, Integer.class, currentRuns);
	}
	
	public void clear() {
		commandRegistry.clear();
	}
	
	//private
	
}
