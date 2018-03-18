package me.egg82.tcpp.exceptions;

import org.bukkit.inventory.ItemStack;

public class InvalidItemException extends RuntimeException {
	//vars
	public static final InvalidItemException EMPTY = new InvalidItemException(null);
	private static final long serialVersionUID = -377366334054644201L;
	
	private ItemStack item = null;

	//constructor
	public InvalidItemException(ItemStack item) {
		super();
		
		this.item = item;
	}
	
	//public
	public ItemStack getItem() {
		return item;
	}
	
	//private
	
}
