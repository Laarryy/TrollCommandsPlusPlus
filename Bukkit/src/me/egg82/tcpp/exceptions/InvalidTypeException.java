package me.egg82.tcpp.exceptions;

public class InvalidTypeException extends IllegalArgumentException {
	//vars
	public static final InvalidTypeException EMPTY = new InvalidTypeException(null);
	private static final long serialVersionUID = -1406177876129625432L;
	
	private String type = null;
	
	//constructor
	public InvalidTypeException(String type) {
		super();
		
		this.type = type;
	}
	
	//public
	public String getType() {
		return type;
	}
	
	//private
	
}
