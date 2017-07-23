package me.egg82.tcpp.exceptions;

public class InvalidCommandException extends Exception {
	//vars
	public static final InvalidCommandException EMPTY = new InvalidCommandException(null);
	private static final long serialVersionUID = -5030618373748458224L;
	
	private String commandName = null;

	//constructor
	public InvalidCommandException(String commandName) {
		super();
		
		this.commandName = commandName;
	}
	
	//public
	public String getCommandName() {
		return commandName;
	}
	
	//private
	
}
