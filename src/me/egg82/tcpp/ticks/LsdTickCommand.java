package me.egg82.tcpp.ticks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.registries.LsdRegistry;
import ninja.egg82.exceptionHandlers.IExceptionHandler;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.tuples.Triplet;
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
	private int radius = 8;
	
	//constructor
	public LsdTickCommand() {
		super();
		ticks = 5L;
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : lsdRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key), lsdRegistry.getRegister(key, Collection.class));
		}
	}
	private void e(Player player, Collection<Triplet<String, Integer, Integer>> bLocs) {
		if (player == null) {
			return;
		}
		
		Thread runner = new Thread(new Runnable() {
			public void run() {
				Set<Location> locations = getFilledCircle(player.getLocation(), radius, false);
				
				if (locations.size() <= radius * 2) {
					locations = getFilledCircle(player.getLocation(), radius, true);
				}
				
				Material[] materials = new Material[locations.size()];
				short[] data = new short[locations.size()];
				
				for (int i = 0; i < materials.length; i++) {
					materials[i] = Material.WOOL;
					data[i] = (short) MathUtil.fairRoundedRandom(0, 15);
				}
				
				synchronized (bLocs) {
					for (Location l : locations) {
						bLocs.add(new Triplet<String, Integer, Integer>(l.getWorld().getName(), l.getBlockX() >> 4, l.getBlockZ() >> 4));
					}
				}
				
				Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
				for (Player p : players) {
					fakeBlockHelper.updateBlocks(p, locations.toArray(new Location[0]), materials, data);
				}
			}
		});
		ServiceLocator.getService(IExceptionHandler.class).addThread(runner);
		runner.start();
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