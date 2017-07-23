package me.egg82.tcpp.util;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.HoleBlockRegistry;
import me.egg82.tcpp.services.HotTubRegistry;
import me.egg82.tcpp.services.PortalRegistry;
import me.egg82.tcpp.services.HoleRegistry;
import me.egg82.tcpp.services.VoidRadiusRegistry;
import me.egg82.tcpp.services.VoidRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.TaskUtil;

public class WorldHoleHelper {
	//vars
	private IRegistry<UUID> holeRegistry = ServiceLocator.getService(HoleRegistry.class);
	private IRegistry<UUID> portalRegistry = ServiceLocator.getService(PortalRegistry.class);
	private IRegistry<UUID> voidRegistry = ServiceLocator.getService(VoidRegistry.class);
	private IRegistry<UUID> voidRadiusRegistry = ServiceLocator.getService(VoidRadiusRegistry.class);
	private IRegistry<UUID> hotTubRegistry = ServiceLocator.getService(HotTubRegistry.class);
	private IRegistry<UUID> holeBlockRegistry = ServiceLocator.getService(HoleBlockRegistry.class);
	
	//constructor
	public WorldHoleHelper() {
		
	}
	
	//public
	public void portalHole(UUID uuid, Player player) {
		// Center should be three blocks below player, for a total of five blocks of depth minus a layer for portals
		Location centerLocation = player.getLocation().clone().subtract(0.0d, 3.0d, 0.0d);
		
		holeRegistry.setRegister(uuid, null);
		portalRegistry.setRegister(uuid, centerLocation);
		
		// Get all blocks, 3x3x5 (LxWxH)
		List<BlockData> blockData = BlockUtil.getBlocks(centerLocation, 1, 2, 1);
		// Fill the previous 3x3x5 area with air
		BlockUtil.clearBlocks(centerLocation, Material.AIR, 1, 2, 1, false);
		// Fill bottom layer of new air blocks with portals
		BlockUtil.clearBlocks(centerLocation.clone().subtract(0.0d, 2.0d, 0.0d), Material.ENDER_PORTAL, 1, 0, 1, false);
		
		holeBlockRegistry.setRegister(uuid, blockData);
		
		player.setFlying(false);
		
		// Wait five seconds
		TaskUtil.runSync(new Runnable() {
			public void run() {
				holeRegistry.removeRegister(uuid);
				portalRegistry.removeRegister(uuid);
				holeBlockRegistry.removeRegister(uuid);
				// Put all the blocks we took earlier back
				BlockUtil.setBlocks(blockData, centerLocation, 1, 2, 1, false);
			}
		}, 100L);
	}
	public void voidHole(UUID uuid, Player player) {
		// Center should be halfway between player and zero
		Location centerLocation = player.getLocation().clone();
		int yRadius = (int) Math.floor(centerLocation.getY() / 2.0d);
		centerLocation.subtract(0.0d, centerLocation.getY() / 2.0d, 0.0d);
		
		holeRegistry.setRegister(uuid, null);
		voidRegistry.setRegister(uuid, centerLocation);
		voidRadiusRegistry.setRegister(uuid, yRadius);
		
		// Get all blocks, 3x3xY (LxWxH)
		List<BlockData> blockData = BlockUtil.getBlocks(centerLocation, 1, yRadius, 1);
		// Fill the previous 3x3xY area with air
		BlockUtil.clearBlocks(centerLocation, Material.AIR, 1, yRadius, 1, false);
		
		holeBlockRegistry.setRegister(uuid, blockData);
		
		player.setFlying(false);
		
		// Wait eight seconds
		TaskUtil.runSync(new Runnable() {
			public void run() {
				holeRegistry.removeRegister(uuid);
				voidRegistry.removeRegister(uuid);
				voidRadiusRegistry.removeRegister(uuid);
				holeBlockRegistry.removeRegister(uuid);
				// Put all the blocks we took earlier back
				BlockUtil.setBlocks(blockData, centerLocation, 1, yRadius, 1, false);
			}
		}, 160L);
	}
	public void hotTubHole(UUID uuid, Player player) {
		// Center should be at player level, for a total of one block of depth (minus a layer for lava)
		Location centerLocation = player.getLocation().clone();
		
		holeRegistry.setRegister(uuid, null);
		hotTubRegistry.setRegister(uuid, centerLocation);
		
		// Get all blocks, 3x3x2 (LxWxH)
		List<BlockData> blockData = BlockUtil.getBlocks(centerLocation, 1, 1, 1);
		// Fill the previous 3x3x2 area with air
		BlockUtil.clearBlocks(centerLocation, Material.AIR, 1, 1, 1, false);
		// Fill bottom layer of new air blocks with lava
		BlockUtil.clearBlocks(centerLocation.clone().subtract(0.0d, 1.0d, 0.0d), Material.STATIONARY_LAVA, 1, 0, 1, false);
		
		holeBlockRegistry.setRegister(uuid, blockData);
		
		player.setFlying(false);
		
		// Wait five seconds
		TaskUtil.runSync(new Runnable() {
			public void run() {
				holeRegistry.removeRegister(uuid);
				hotTubRegistry.removeRegister(uuid);
				holeBlockRegistry.removeRegister(uuid);
				// Put all the blocks we took earlier back
				BlockUtil.setBlocks(blockData, centerLocation, 1, 1, 1, true);
			}
		}, 100L);
	}
	
	@SuppressWarnings("unchecked")
	public void undoAll() {
		UUID[] portalKeys = portalRegistry.getRegistryKeys();
		for (int i = 0; i < portalKeys.length; i++) {
			BlockUtil.setBlocks(holeBlockRegistry.getRegister(portalKeys[i], List.class), portalRegistry.getRegister(portalKeys[i], Location.class), 1, 2, 1, false);
		}
		portalRegistry.clear();
		
		UUID[] voidKeys = voidRegistry.getRegistryKeys();
		for (int i = 0; i < voidKeys.length; i++) {
			BlockUtil.setBlocks(holeBlockRegistry.getRegister(voidKeys[i], List.class), voidRegistry.getRegister(voidKeys[i], Location.class), 1, voidRadiusRegistry.getRegister(voidKeys[i], Integer.class), 1, false);
		}
		voidRegistry.clear();
		voidRadiusRegistry.clear();
		
		UUID[] hotTubKeys = hotTubRegistry.getRegistryKeys();
		for (int i = 0; i < hotTubKeys.length; i++) {
			BlockUtil.setBlocks(holeBlockRegistry.getRegister(hotTubKeys[i], List.class), hotTubRegistry.getRegister(hotTubKeys[i], Location.class), 1, 1, 1, false);
		}
		hotTubRegistry.clear();
		
		holeBlockRegistry.clear();
		holeRegistry.clear();
	}
	
	//private
	
}
