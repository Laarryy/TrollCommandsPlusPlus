package me.egg82.tcpp.exceptions;

import ninja.egg82.plugin.commands.PluginCommand;

public class CommandInUseException extends Exception {
	//vars
	public static CommandInUseException EMPTY = new CommandInUseException(null);
	private static final long serialVersionUID = -8060551294505180958L;
	
	private PluginCommand command = null;

	//constructor
	public CommandInUseException(PluginCommand command) {
		super();
		
		this.command = command;
	}
	
	//public
	public PluginCommand getCommand() {
		return command;
	}
	
	//private
	
}
