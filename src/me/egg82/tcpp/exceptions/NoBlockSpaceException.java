package me.egg82.tcpp.exceptions;

import org.bukkit.Location;

public class NoBlockSpaceException extends RuntimeException {
	//vars
	public static final NoBlockSpaceException EMPTY = new NoBlockSpaceException(null);
	private static final long serialVersionUID = 7715219849768888399L;
	
	private Location location = null;

	//constructor
	public NoBlockSpaceException(Location location) {
		super();
		
		this.location = location;
	}
	
	//public
	public Location getLocation() {
		return location;
	}
	
	//private
	
}
