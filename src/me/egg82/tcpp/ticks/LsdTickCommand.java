package me.egg82.tcpp.ticks;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.LsdRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.protocol.reflection.IFakeBlockHelper;
import ninja.egg82.utils.MathUtil;

public class LsdTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> lsdRegistry = ServiceLocator.getService(LsdRegistry.class);
	
	private IFakeBlockHelper fakeBlockHelper = ServiceLocator.getService(IFakeBlockHelper.class);
	
	//constructor
	public LsdTickCommand() {
		super();
		ticks = 5L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : lsdRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		Location[] locations = getFilledCircle(player.getLocation(), 8, false).toArray(new Location[0]);
		
		if (locations.length == 0) {
			locations = getFilledCircle(player.getLocation(), 8, true).toArray(new Location[0]);
		}
		
		Material[] materials = new Material[locations.length];
		short[] data = new short[locations.length];
		
		for (int i = 0; i < materials.length; i++) {
			materials[i] = Material.WOOL;
			data[i] = (short) MathUtil.fairRoundedRandom(0, 15);
		}
		
		fakeBlockHelper.updateBlocks(player, locations, materials, data);
	}
	
	private Set<Location> getFilledCircle(Location l, int radius, boolean useGround) {
		HashSet<Location> retVal = new HashSet<Location>();
		
		if (useGround) {
			l = BlockUtil.getTopWalkableBlock(l).subtract(0.0d, 1.0d, 0.0d);
			retVal.add(l);
		} else {
			l = LocationUtil.toBlockLocation(l);
			if (l.getBlock().getType().isSolid()) {
				retVal.add(l);
			}
		}
		
		for (int i = 1; i <= radius; i++) {
			Location[] locs = LocationUtil.getCircleAround(l, i, radius * 8);
			for (int j = 0; j < locs.length; j++) {
				for (int k = -radius; k < radius; k++) {
					Location t = locs[j].clone().add(0.0d, k, 0.0d);
					if (t.getBlock().getType().isSolid()) {
						retVal.add(t);
					}
				}
			}
		}
		
		return retVal;
	}
}