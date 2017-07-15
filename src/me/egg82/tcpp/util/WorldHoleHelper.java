package me.egg82.tcpp.util;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.HoleBlockRegistry;
import me.egg82.tcpp.services.HotTubRegistry;
import me.egg82.tcpp.services.PortalRegistry;
import me.egg82.tcpp.services.VoidRadiusRegistry;
import me.egg82.tcpp.services.VoidRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.TaskUtil;

public class WorldHoleHelper {
	//vars
	private IRegistry portalRegistry = ServiceLocator.getService(PortalRegistry.class);
	private IRegistry voidRegistry = ServiceLocator.getService(VoidRegistry.class);
	private IRegistry voidRadiusRegistry = ServiceLocator.getService(VoidRadiusRegistry.class);
	private IRegistry hotTubRegistry = ServiceLocator.getService(HotTubRegistry.class);
	private IRegistry holeBlockRegistry = ServiceLocator.getService(HoleBlockRegistry.class);
	
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
		TaskUtil.runSync(new Runnable() {
			public void run() {
				portalRegistry.setRegister(uuid, Location.class, null);
				holeBlockRegistry.setRegister(uuid, List.class, null);
				// Put all the blocks we took earlier back
				BlockUtil.setBlocks(blockData, centerLocation, 1, 2, 1, false);
			}
		}, 100L);
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
		TaskUtil.runSync(new Runnable() {
			public void run() {
				voidRegistry.setRegister(uuid, Location.class, null);
				voidRadiusRegistry.setRegister(uuid, Integer.class, null);
				holeBlockRegistry.setRegister(uuid, List.class, null);
				// Put all the blocks we took earlier back
				BlockUtil.setBlocks(blockData, centerLocation, 1, yRadius, 1, false);
			}
		}, 160L);
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
		TaskUtil.runSync(new Runnable() {
			public void run() {
				hotTubRegistry.setRegister(uuid, Location.class, null);
				holeBlockRegistry.setRegister(uuid, List.class, null);
				// Put all the blocks we took earlier back
				BlockUtil.setBlocks(blockData, centerLocation, 1, 1, 1, true);
			}
		}, 100L);
	}
	
	@SuppressWarnings("unchecked")
	public void undoAll() {
		String[] portalNames = portalRegistry.getRegistryNames();
		for (int i = 0; i < portalNames.length; i++) {
			BlockUtil.setBlocks(holeBlockRegistry.getRegister(portalNames[i], List.class), portalRegistry.getRegister(portalNames[i], Location.class), 1, 2, 1, false);
		}
		portalRegistry.clear();
		
		String[] voidNames = voidRegistry.getRegistryNames();
		for (int i = 0; i < voidNames.length; i++) {
			BlockUtil.setBlocks(holeBlockRegistry.getRegister(voidNames[i], List.class), voidRegistry.getRegister(voidNames[i], Location.class), 1, voidRadiusRegistry.getRegister(voidNames[i], Integer.class), 1, false);
		}
		voidRegistry.clear();
		voidRadiusRegistry.clear();
		
		String[] hotTubNames = hotTubRegistry.getRegistryNames();
		for (int i = 0; i < hotTubNames.length; i++) {
			BlockUtil.setBlocks(holeBlockRegistry.getRegister(hotTubNames[i], List.class), hotTubRegistry.getRegister(hotTubNames[i], Location.class), 1, 1, 1, false);
		}
		hotTubRegistry.clear();
		
		holeBlockRegistry.clear();
	}
	
	//private
	
}
