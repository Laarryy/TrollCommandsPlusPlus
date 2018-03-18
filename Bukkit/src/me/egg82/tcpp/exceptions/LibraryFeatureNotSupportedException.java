package me.egg82.tcpp.exceptions;

public class LibraryFeatureNotSupportedException extends Exception {
	//vars
	private static final long serialVersionUID = -5912122064000538865L;
	public static LibraryFeatureNotSupportedException EMPTY = new LibraryFeatureNotSupportedException(null, null);
	
	private Object library = null;
	private String methodName = null;

	//constructor
	public LibraryFeatureNotSupportedException(Object library, String methodName) {
		super();
		
		this.library = library;
		this.methodName = methodName;
	}
	
	//public
	public Object getLibrary() {
		return library;
	}
	public String getMethodName() {
		return methodName;
	}
	
	//private
	
}
