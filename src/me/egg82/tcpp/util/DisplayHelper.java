package me.egg82.tcpp.util;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;

import me.egg82.tcpp.services.DisplayBlockRegistry;
import me.egg82.tcpp.services.DisplayLocationRegistry;
import me.egg82.tcpp.services.DisplayRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;

public class DisplayHelper {
	//vars
	private IRegistry displayRegistry = ServiceLocator.getService(DisplayRegistry.class);
	private IRegistry displayBlockRegistry = ServiceLocator.getService(DisplayBlockRegistry.class);
	private IRegistry displayLocationRegistry = ServiceLocator.getService(DisplayLocationRegistry.class);
	
	private HashSet<Location> addedBlockLocations = new HashSet<Location>();
	
	//constructor
	public DisplayHelper() {
		
	}
	
	//public
	public HashSet<Location> getBlockLocationsAround(Location loc) {
		HashSet<Location> retVal = new HashSet<Location>();
		loc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				for (int y = -1; y < 3; y++) {
					retVal.add(loc.clone().add(x, y, z));
				}
			}
		}
		
		return retVal;
	}
	
	public void surround(Location loc, Material blockMaterial, Material sideMaterial) {
		loc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				for (int y = -1; y < 3; y++) {
					// Player is here, don't add blocks
					if (x == 0 && z == 0 && (y == 0 || y == 1)) {
						continue;
					}
					
					Location newLoc = loc.clone().add(x, y, z);
					if (newLoc.getBlock().getType() == Material.AIR) {
						if (y == -1 || y == 2) {
							// Top or bottom
							newLoc.getBlock().setType(blockMaterial);
						} else {
							// Sides
							newLoc.getBlock().setType(sideMaterial);
						}
						
						addedBlockLocations.add(newLoc);
					}
				}
			}
		}
	}
	public void unsurround(Location loc) {
		loc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				for (int y = -1; y < 3; y++) {
					// Player is here, don't remove blocks
					if (x == 0 && z == 0 && (y == 0 || y == 1)) {
						continue;
					}
					
					Location newLoc = loc.clone().add(x, y, z);
					if (addedBlockLocations.contains(newLoc)) {
						newLoc.getBlock().setType(Material.AIR);
						addedBlockLocations.remove(newLoc);
					}
				}
			}
		}
	}
	
	public void unsurroundAll() {
		displayRegistry.clear();
		displayBlockRegistry.clear();
		displayLocationRegistry.clear();
		
		addedBlockLocations.forEach((v) -> {
			v.getBlock().setType(Material.AIR);
		});
		addedBlockLocations.clear();
	}
	
	//private
	
}
