package me.egg82.tcpp.reflection.rollback;

import org.bukkit.Location;
import org.bukkit.Material;

public class NullRollbackHelper implements IRollbackHelper {
	//vars
	
	//constructor
	public NullRollbackHelper() {
		
	}
	
	//public
	public void logBlockPlace(String playerName, Location loc, Material newMaterial, byte newData) {
		
	}
	public void logBlockRemove(String playerName, Location loc, Material oldMaterial, byte oldData) {
		
	}
	
	//private
	
}
