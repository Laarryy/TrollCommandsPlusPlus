package me.egg82.tcpp.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.FoolsGoldChunkRegistry;
import me.egg82.tcpp.services.FoolsGoldRegistry;
import ninja.egg82.exceptionHandlers.IExceptionHandler;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.tuples.Pair;
import ninja.egg82.plugin.utils.TaskUtil;
import ninja.egg82.protocol.reflection.IFakeBlockHelper;
import ninja.egg82.utils.MathUtil;

public class FoolsGoldHelper {
	//vars
	private IRegistry<UUID> foolsGoldRegistry = ServiceLocator.getService(FoolsGoldRegistry.class);
	private IRegistry<UUID> foolsGoldChunkRegistry = ServiceLocator.getService(FoolsGoldChunkRegistry.class);
	
	private IFakeBlockHelper fakeBlockHelper = ServiceLocator.getService(IFakeBlockHelper.class);
	
	//constructor
	public FoolsGoldHelper() {
		
	}
	
	//public
	public void addPlayer(UUID uuid, Player player) {
		Collection<Location> blocks = Collections.synchronizedCollection(new ArrayDeque<Location>());
		Collection<Pair<Integer, Integer>> chunks = Collections.synchronizedCollection(new ArrayDeque<Pair<Integer, Integer>>());
		
		Thread runner = new Thread(new Runnable() {
			public void run() {
				addFakeBlocks(player, blocks, chunks, 1);
			}
		});
		ServiceLocator.getService(IExceptionHandler.class).addThread(runner);
		runner.start();
		
		foolsGoldRegistry.setRegister(uuid, blocks);
		foolsGoldChunkRegistry.setRegister(uuid, chunks);
	}
	@SuppressWarnings("unchecked")
	public void removePlayer(UUID uuid, Player player) {
		if (foolsGoldRegistry.hasRegister(uuid)) {
			Collection<Location> blocks = foolsGoldRegistry.getRegister(uuid, Collection.class);
			
			Thread runner = new Thread(new Runnable() {
				public void run() {
					removeFakeBlocks(player, blocks, 1);
				}
			});
			ServiceLocator.getService(IExceptionHandler.class).addThread(runner);
			runner.start();
		}
		
		foolsGoldRegistry.removeRegister(uuid);
		foolsGoldChunkRegistry.removeRegister(uuid);
	}
	public void removePlayer(UUID uuid, OfflinePlayer player) {
		foolsGoldRegistry.removeRegister(uuid);
		foolsGoldChunkRegistry.removeRegister(uuid);
	}
	
	@SuppressWarnings("unchecked")
	public void updatePlayer(UUID uuid, Player player, Location from, Location to) {
		Collection<Location> blocks = foolsGoldRegistry.getRegister(uuid, Collection.class);
		Collection<Pair<Integer, Integer>> chunks = foolsGoldChunkRegistry.getRegister(uuid, Collection.class);
		
		Thread runner = new Thread(new Runnable() {
			public void run() {
				updateFakeBlocks(player, from, to, blocks, chunks, 1);
			}
		});
		ServiceLocator.getService(IExceptionHandler.class).addThread(runner);
		runner.start();
	}
	
	@SuppressWarnings("unchecked")
	public void updatePlayer(UUID uuid, Player player, Location newLocation) {
		Collection<Location> blocks = foolsGoldRegistry.getRegister(uuid, Collection.class);
		Collection<Pair<Integer, Integer>> chunks = foolsGoldChunkRegistry.getRegister(uuid, Collection.class);
		
		Thread runner = new Thread(new Runnable() {
			public void run() {
				updateFakeBlocks(player, player.getLocation(), newLocation, blocks, chunks, 1);
			}
		});
		ServiceLocator.getService(IExceptionHandler.class).addThread(runner);
		runner.start();
	}
	
	//private
	private void addFakeBlocks(Player player, Collection<Location> blocks, Collection<Pair<Integer, Integer>> chunks, int tries) {
		if (!player.getWorld().isChunkLoaded(player.getLocation().getBlockX() >> 4, player.getLocation().getBlockZ() >> 4)) {
			if (tries < 10) {
				TaskUtil.runAsync(new Runnable() {
					public void run() {
						addFakeBlocks(player, blocks, chunks, tries + 1);
					}
				}, 10L);
			}
			return;
		}
		
		int chunkX = player.getLocation().getChunk().getX();
		int chunkZ = player.getLocation().getChunk().getZ();
		World world = player.getLocation().getWorld();
		
		if (world.getEnvironment() == Environment.NORMAL) {
			synchronized (player) {
				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {
						chunks.add(new Pair<Integer, Integer>(chunkX + x, chunkZ + z));
						blocks.addAll(spawn(Material.COAL_ORE, 5, 52, player, world, chunkX + x, chunkZ + z));
						blocks.addAll(spawn(Material.IRON_ORE, 5, 54, player, world, chunkX + x, chunkZ + z));
						blocks.addAll(spawn(Material.LAPIS_ORE, 14, 16, player, world, chunkX + x, chunkZ + z));
						blocks.addAll(spawn(Material.GOLD_ORE, 5, 29, player, world, chunkX + x, chunkZ + z));
						blocks.addAll(spawn(Material.GOLD_ORE, 32, 63, player, world, chunkX + x, chunkZ + z));
						blocks.addAll(spawn(Material.DIAMOND_ORE, 5, 12, player, world, chunkX + x, chunkZ + z));
						blocks.addAll(spawn(Material.REDSTONE_ORE, 5, 12, player, world, chunkX + x, chunkZ + z));
						blocks.addAll(spawn(Material.EMERALD_ORE, 5, 29, player, world, chunkX + x, chunkZ + z));
					}
				}
			}
		} else if (world.getEnvironment() == Environment.NETHER) {
			synchronized (player) {
				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {
						chunks.add(new Pair<Integer, Integer>(chunkX + x, chunkZ + z));
						blocks.addAll(spawn(Material.QUARTZ_ORE, 15, 120, player, world, chunkX + x, chunkZ + z));
					}
				}
			}
		}
	}
	private void removeFakeBlocks(Player player, Collection<Location> blocks, int tries) {
		if (!player.getWorld().isChunkLoaded(player.getLocation().getBlockX() >> 4, player.getLocation().getBlockZ() >> 4)) {
			if (tries < 10) {
				TaskUtil.runAsync(new Runnable() {
					public void run() {
						removeFakeBlocks(player, blocks, tries + 1);
					}
				}, 10L);
			}
			return;
		}
		
		synchronized (player) {
			for (Location b : blocks) {
				fakeBlockHelper.updateBlock(player, b, b.getBlock().getType());
			}
		}
	}
	private void updateFakeBlocks(Player player, Location from, Location to, Collection<Location> blocks, Collection<Pair<Integer, Integer>> chunks, int tries) {
		if (!from.getWorld().isChunkLoaded(from.getBlockX() >> 4, from.getBlockZ() >> 4) || !to.getWorld().isChunkLoaded(to.getBlockX() >> 4, to.getBlockZ() >> 4)) {
			if (tries < 10) {
				TaskUtil.runAsync(new Runnable() {
					public void run() {
						updateFakeBlocks(player, from, to, blocks, chunks, tries + 1);
					}
				}, 10L);
			}
			return;
		}
		
		if (to.getChunk().getX() != from.getChunk().getX() || to.getChunk().getZ() != from.getChunk().getZ()) {
			int chunkX = to.getChunk().getX();
			int chunkZ = to.getChunk().getZ();
			World world = to.getWorld();
			
			ArrayList<Pair<Integer, Integer>> currentChunks = new ArrayList<Pair<Integer, Integer>>();
			
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					currentChunks.add(new Pair<Integer, Integer>(chunkX + x, chunkZ + z));
				}
			}
			
			synchronized (player) {
				ArrayList<Location> removedBlocks = new ArrayList<Location>();
				blocks.forEach((loc) -> {
					if (from.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4)) {
						Pair<Integer, Integer> chunk = new Pair<Integer, Integer>(loc.getChunk().getX(), loc.getChunk().getZ());
						if (!currentChunks.contains(chunk)) {
							fakeBlockHelper.updateBlock(player, loc, loc.getBlock().getType());
							removedBlocks.add(loc);
							chunks.remove(chunk);
						}
					} else {
						removedBlocks.add(loc);
						chunks.remove(new Pair<Integer, Integer>(loc.getBlockX() >> 4, loc.getBlockZ() >> 4));
					}
				});
				
				blocks.removeAll(removedBlocks);
			}
			
			if (world.getEnvironment() == Environment.NORMAL) {
				synchronized (player) {
					for (int i = 0; i < currentChunks.size(); i++) {
						Pair<Integer, Integer> chunk = currentChunks.get(i);
						if (chunks.contains(chunk) || !world.isChunkLoaded(chunk.getLeft(), chunk.getRight())) {
							continue;
						}
						
						chunks.add(chunk);
						blocks.addAll(spawn(Material.COAL_ORE, 5, 52, player, world, chunk.getLeft(), chunk.getRight()));
						blocks.addAll(spawn(Material.IRON_ORE, 5, 54, player, world, chunk.getLeft(), chunk.getRight()));
						blocks.addAll(spawn(Material.LAPIS_ORE, 14, 16, player, world, chunk.getLeft(), chunk.getRight()));
						blocks.addAll(spawn(Material.GOLD_ORE, 5, 29, player, world, chunk.getLeft(), chunk.getRight()));
						blocks.addAll(spawn(Material.GOLD_ORE, 32, 63, player, world, chunk.getLeft(), chunk.getRight()));
						blocks.addAll(spawn(Material.DIAMOND_ORE, 5, 12, player, world, chunk.getLeft(), chunk.getRight()));
						blocks.addAll(spawn(Material.REDSTONE_ORE, 5, 12, player, world, chunk.getLeft(), chunk.getRight()));
						blocks.addAll(spawn(Material.EMERALD_ORE, 5, 29, player, world, chunk.getLeft(), chunk.getRight()));
					}
				}
			} else if (world.getEnvironment() == Environment.NETHER) {
				synchronized (player) {
					for (int i = 0; i < currentChunks.size(); i++) {
						Pair<Integer, Integer> chunk = currentChunks.get(i);
						if (chunks.contains(chunk) || !world.isChunkLoaded(chunk.getLeft(), chunk.getRight())) {
							continue;
						}
						
						chunks.add(chunk);
						blocks.addAll(spawn(Material.QUARTZ_ORE, 15, 120, player, world, chunk.getLeft(), chunk.getRight()));
					}
				}
			}
		}
	}
	
	private List<Location> spawn(Material type, int minLevel, int maxLevel, Player player, World world, int chunkX, int chunkZ) {
		ArrayList<Location> retVal = new ArrayList<Location>();
		int numBlocks = MathUtil.fairRoundedRandom(20, 35);
		
		for (int i = 0; i < numBlocks; i++) {
			Location loc = null;
			Material mat = null;
			int tries = 0;
			
			do {
				loc = new Location(world, chunkX * 16 + MathUtil.fairRoundedRandom(0, 15), MathUtil.fairRoundedRandom(minLevel, maxLevel), chunkZ * 16 + MathUtil.fairRoundedRandom(0, 15));
				mat = loc.getBlock().getType();
				tries++;
			} while (mat != Material.STONE && mat != Material.SANDSTONE && mat != Material.NETHERRACK && tries <= 100);
			
			if (mat != Material.STONE && mat != Material.SANDSTONE && mat != Material.NETHERRACK) {
				continue;
			}
			
			retVal.add(loc);
		}
		
		fakeBlockHelper.updateBlocks(player, retVal.toArray(new Location[0]), type);
		return retVal;
	}
}
