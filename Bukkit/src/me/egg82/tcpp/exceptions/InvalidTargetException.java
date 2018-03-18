package me.egg82.tcpp.exceptions;

import org.bukkit.entity.Entity;

public class InvalidTargetException extends RuntimeException {
	//vars
	public static final InvalidTargetException EMPTY = new InvalidTargetException(null);
	private static final long serialVersionUID = -1891275105832560062L;
	
	private Entity target = null;

	//constructor
	public InvalidTargetException(Entity target) {
		super();
		
		this.target = target;
	}
	
	//public
	public Entity getTarget() {
		return target;
	}
	
	//private
	
}
