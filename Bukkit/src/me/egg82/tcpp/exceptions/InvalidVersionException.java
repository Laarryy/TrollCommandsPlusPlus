package me.egg82.tcpp.exceptions;

public class InvalidVersionException extends Exception {
	//vars
	public static final InvalidVersionException EMPTY = new InvalidVersionException(null, null);
	
	private String currentVersion = null;
	private String minRequiredVersion = null;
	private static final long serialVersionUID = 6518259363964567595L;

	//constructor
	public InvalidVersionException(String currentVersion, String minRequiredVersion) {
		super();
		
		this.currentVersion = currentVersion;
		this.minRequiredVersion = minRequiredVersion;
	}
	
	//public
	public String getCurrentVersion() {
		return currentVersion;
	}
	public String getMinRequiredVersion() {
		return minRequiredVersion;
	}
	
	//private
	
}
