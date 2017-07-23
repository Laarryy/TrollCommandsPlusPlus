package me.egg82.tcpp.exceptions;

public class InvalidLibraryException extends Exception {
	//vars
	public static InvalidLibraryException EMPTY = new InvalidLibraryException(null);
	private static final long serialVersionUID = -745064293666290999L;
	
	private Object library = null;

	//constructor
	public InvalidLibraryException(Object library) {
		super();
		
		this.library = library;
	}
	
	//public
	public Object getLibrary() {
		return library;
	}
	
	//private
	
}
