package me.egg82.tcpp.ticks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.egg82.tcpp.reflection.block.IFakeBlockHelper;
import me.egg82.tcpp.registries.LsdRegistry;
import ninja.egg82.bukkit.core.BlockData;
import ninja.egg82.bukkit.handlers.async.AsyncTickHandler;
import ninja.egg82.bukkit.reflection.material.IMaterialHelper;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IRegistry;
import ninja.egg82.utils.MathUtil;

public class LsdTickCommand extends AsyncTickHandler {
	//vars
	private IRegistry<UUID, IConcurrentSet<Location>> lsdRegistry = ServiceLocator.getService(LsdRegistry.class);
	
	private IFakeBlockHelper fakeBlockHelper = ServiceLocator.getService(IFakeBlockHelper.class);
	private int radius = 8;
	
	private Material wool = ServiceLocator.getService(IMaterialHelper.class).getByName("WOOL");
	
	//constructor
	public LsdTickCommand() {
		super(0L, 2L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : lsdRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key), lsdRegistry.getRegister(key));
		}
	}
	private void e(Player player, IConcurrentSet<Location> bLocs) {
		if (player == null) {
			return;
		}
		
		Set<Location> locations = new HashSet<Location>();
		locations.addAll(getFilledCircle(player.getLocation(), radius, false));
		if (locations.size() <= radius * 2) {
			locations.clear();
			locations.addAll(getFilledCircle(player.getLocation(), radius, true));
		}
		
		List<BlockData> list = new ArrayList<BlockData>();
		for (Location l : locations) {
			bLocs.add(l);
			list.add(new BlockData(l, wool, (byte) MathUtil.fairRoundedRandom(0, 15), null));
		}
		
		fakeBlockHelper.sendAllMulti(list);
	}
	
	private Set<Location> getFilledCircle(Location l, int radius, boolean useGround) {
		HashSet<Location> retVal = new HashSet<Location>();
		
		if (useGround) {
			l = BlockUtil.getHighestSolidBlock(l);
			retVal.add(l);
		} else {
			if (l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) {
				l = LocationUtil.toBlockLocation(l);
				if (l.getBlock().getType().isSolid()) {
					retVal.add(l);
				}
			}
		}
		
		for (int i = 1; i <= radius; i++) {
			Location[] locs = LocationUtil.getCircleAround(l, i, radius * 8);
			for (int j = 0; j < locs.length; j++) {
				for (int k = -radius; k < radius; k++) {
					Location t = locs[j].clone().add(0.0d, k, 0.0d);
					if (t.getWorld().isChunkLoaded(t.getBlockX() >> 4, t.getBlockZ() >> 4)) {
						if (t.getBlock().getType().isSolid()) {
							retVal.add(t);
						}
					}
				}
			}
		}
		
		return retVal;
	}
}