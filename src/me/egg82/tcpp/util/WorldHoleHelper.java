package me.egg82.tcpp.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.services.HoleBlockRegistry;
import me.egg82.tcpp.services.HotTubRegistry;
import me.egg82.tcpp.services.PortalRegistry;
import me.egg82.tcpp.services.VoidRadiusRegistry;
import me.egg82.tcpp.services.VoidRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.startup.InitRegistry;

public class WorldHoleHelper {
	//vars
	private IRegistry portalRegistry = (IRegistry) ServiceLocator.getService(PortalRegistry.class);
	private IRegistry voidRegistry = (IRegistry) ServiceLocator.getService(VoidRegistry.class);
	private IRegistry voidRadiusRegistry = (IRegistry) ServiceLocator.getService(VoidRadiusRegistry.class);
	private IRegistry hotTubRegistry = (IRegistry) ServiceLocator.getService(HotTubRegistry.class);
	private IRegistry holeBlockRegistry = (IRegistry) ServiceLocator.getService(HoleBlockRegistry.class);
	private JavaPlugin plugin = (JavaPlugin) ((IRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister("plugin");
	
	//constructor
	public WorldHoleHelper() {
		
	}
	
	//public
	public void portalHole(String uuid, Player player) {
		// Center should be three blocks below player, for a total of five blocks of depth minus a layer for portals
		Location centerLocation = player.getLocation().clone().subtract(0.0d, 3.0d, 0.0d);
		
		portalRegistry.setRegister(uuid, Location.class, centerLocation);
		
		// Get all blocks, 3x3x5 (LxWxH)
		List<BlockData> blockData = BlockUtil.getBlocks(centerLocation, 1, 2, 1);
		// Fill the previous 3x3x5 area with air
		BlockUtil.clearBlocks(centerLocation, Material.AIR, 1, 2, 1, false);
		// Fill bottom layer of new air blocks with portals
		BlockUtil.clearBlocks(centerLocation.clone().subtract(0.0d, 2.0d, 0.0d), Material.ENDER_PORTAL, 1, 0, 1, false);
		
		holeBlockRegistry.setRegister(uuid, List.class, blockData);
		
		// Wait five seconds
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				portalRegistry.setRegister(uuid, Location.class, null);
				holeBlockRegistry.setRegister(uuid, List.class, null);
				// Put all the blocks we took earlier back
				BlockUtil.setBlocks(blockData, centerLocation, 1, 2, 1, false);
			}
		}, 100);
	}
	public void voidHole(String uuid, Player player) {
		// Center should be halfway between player and zero
		Location centerLocation = player.getLocation().clone();
		int yRadius = (int) Math.floor(centerLocation.getY() / 2.0d);
		centerLocation.subtract(0.0d, centerLocation.getY() / 2.0d, 0.0d);
		
		voidRegistry.setRegister(uuid, Location.class, centerLocation);
		voidRadiusRegistry.setRegister(uuid, Integer.class, yRadius);
		
		// Get all blocks, 3x3xY (LxWxH)
		List<BlockData> blockData = BlockUtil.getBlocks(centerLocation, 1, yRadius, 1);
		// Fill the previous 3x3xY area with air
		BlockUtil.clearBlocks(centerLocation, Material.AIR, 1, yRadius, 1, false);
		
		holeBlockRegistry.setRegister(uuid, List.class, blockData);
		
		// Wait eight seconds
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				voidRegistry.setRegister(uuid, Location.class, null);
				voidRadiusRegistry.setRegister(uuid, Integer.class, null);
				holeBlockRegistry.setRegister(uuid, List.class, null);
				// Put all the blocks we took earlier back
				BlockUtil.setBlocks(blockData, centerLocation, 1, yRadius, 1, false);
			}
		}, 160);
	}
	public void hotTubHole(String uuid, Player player) {
		// Center should be at player level, for a total of one block of depth (minus a layer for lava)
		Location centerLocation = player.getLocation().clone();
		
		hotTubRegistry.setRegister(uuid, Location.class, centerLocation);
		
		// Get all blocks, 3x3x2 (LxWxH)
		List<BlockData> blockData = BlockUtil.getBlocks(centerLocation, 1, 1, 1);
		// Fill the previous 3x3x2 area with air
		BlockUtil.clearBlocks(centerLocation, Material.AIR, 1, 1, 1, false);
		// Fill bottom layer of new air blocks with lava
		BlockUtil.clearBlocks(centerLocation.clone().subtract(0.0d, 1.0d, 0.0d), Material.STATIONARY_LAVA, 1, 0, 1, false);
		
		holeBlockRegistry.setRegister(uuid, List.class, blockData);
		
		// Wait five seconds
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				hotTubRegistry.setRegister(uuid, Location.class, null);
				holeBlockRegistry.setRegister(uuid, List.class, null);
				// Put all the blocks we took earlier back
				BlockUtil.setBlocks(blockData, centerLocation, 1, 1, 1, true);
			}
		}, 100);
	}
	
	@SuppressWarnings("unchecked")
	public void undoAll() {
		String[] portalNames = portalRegistry.getRegistryNames();
		for (int i = 0; i < portalNames.length; i++) {
			BlockUtil.setBlocks((List<BlockData>) holeBlockRegistry.getRegister(portalNames[i]), (Location) portalRegistry.getRegister(portalNames[i]), 1, 2, 1, false);
		}
		portalRegistry.clear();
		
		String[] voidNames = voidRegistry.getRegistryNames();
		for (int i = 0; i < voidNames.length; i++) {
			BlockUtil.setBlocks((List<BlockData>) holeBlockRegistry.getRegister(voidNames[i]), (Location) voidRegistry.getRegister(voidNames[i]), 1, (Integer) voidRadiusRegistry.getRegister(voidNames[i]), 1, false);
		}
		voidRegistry.clear();
		voidRadiusRegistry.clear();
		
		holeBlockRegistry.clear();
	}
	
	//private
	
}
