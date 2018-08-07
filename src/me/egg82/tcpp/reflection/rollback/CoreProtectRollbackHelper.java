package me.egg82.tcpp.reflection.rollback;

import org.bukkit.Location;
import org.bukkit.Material;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

public class CoreProtectRollbackHelper implements IRollbackHelper {
	//vars
	private CoreProtectAPI api = CoreProtect.getInstance().getAPI();
	
	//constructor
	public CoreProtectRollbackHelper() {
		
	}
	
	//public
	public void logBlockPlace(String playerName, Location loc, Material newMaterial, byte newData) {
		if (!api.isEnabled()) {
			return;
		}
		
		api.logPlacement(playerName, loc, newMaterial, newData);
	}
	public void logBlockRemove(String playerName, Location loc, Material oldMaterial, byte oldData) {
		if (!api.isEnabled()) {
			return;
		}
		
		api.logRemoval(playerName, loc, oldMaterial, oldData);
	}
	
	//private
	
}
