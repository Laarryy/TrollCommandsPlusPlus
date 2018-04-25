package me.egg82.tcpp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.registries.FoolsGoldChunkRegistry;
import me.egg82.tcpp.services.registries.FoolsGoldRegistry;
import ninja.egg82.concurrent.DynamicConcurrentDeque;
import ninja.egg82.concurrent.IConcurrentDeque;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.patterns.tuples.pair.Pair;
import ninja.egg82.plugin.utils.TaskUtil;
import ninja.egg82.protocol.reflection.IFakeBlockHelper;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.ThreadUtil;

public class FoolsGoldHelper {
	//vars
	private IVariableRegistry<UUID> foolsGoldRegistry = ServiceLocator.getService(FoolsGoldRegistry.class);
	private IVariableRegistry<UUID> foolsGoldChunkRegistry = ServiceLocator.getService(FoolsGoldChunkRegistry.class);
	
	private IFakeBlockHelper fakeBlockHelper = ServiceLocator.getService(IFakeBlockHelper.class);
	
	//constructor
	public FoolsGoldHelper() {
		
	}
	
	//public
	public void addPlayer(UUID uuid, Player player) {
		IConcurrentDeque<Location> blocks = new DynamicConcurrentDeque<Location>();
		IConcurrentDeque<Pair<Integer, Integer>> chunks = new DynamicConcurrentDeque<Pair<Integer, Integer>>();
		
		ThreadUtil.submit(new Runnable() {
			public void run() {
				addFakeBlocks(player, blocks, chunks, 1);
			}
		});
		
		foolsGoldRegistry.setRegister(uuid, blocks);
		foolsGoldChunkRegistry.setRegister(uuid, chunks);
	}
	@SuppressWarnings("unchecked")
	public void removePlayer(UUID uuid, Player player) {
		if (foolsGoldRegistry.hasRegister(uuid)) {
			IConcurrentDeque<Location> blocks = foolsGoldRegistry.getRegister(uuid, IConcurrentDeque.class);
			
			ThreadUtil.submit(new Runnable() {
				public void run() {
					removeFakeBlocks(player, blocks, 1);
				}
			});
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
		IConcurrentDeque<Location> blocks = foolsGoldRegistry.getRegister(uuid, IConcurrentDeque.class);
		IConcurrentDeque<Pair<Integer, Integer>> chunks = foolsGoldChunkRegistry.getRegister(uuid, IConcurrentDeque.class);
		
		ThreadUtil.submit(new Runnable() {
			public void run() {
				updateFakeBlocks(player, from, to, blocks, chunks, 1);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public void updatePlayer(UUID uuid, Player player, Location newLocation) {
		IConcurrentDeque<Location> blocks = foolsGoldRegistry.getRegister(uuid, IConcurrentDeque.class);
		IConcurrentDeque<Pair<Integer, Integer>> chunks = foolsGoldChunkRegistry.getRegister(uuid, IConcurrentDeque.class);
		
		ThreadUtil.submit(new Runnable() {
			public void run() {
				updateFakeBlocks(player, player.getLocation(), newLocation, blocks, chunks, 1);
			}
		});
	}
	
	//private
	private void addFakeBlocks(Player player, IConcurrentDeque<Location> blocks, IConcurrentDeque<Pair<Integer, Integer>> chunks, int tries) {
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
		
		int chunkX = player.getLocation().getBlockX() >> 4;
		int chunkZ = player.getLocation().getBlockZ() >> 4;
		World world = player.getLocation().getWorld();
		
		if (world.getEnvironment() == Environment.NORMAL) {
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
		} else if (world.getEnvironment() == Environment.NETHER) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					chunks.add(new Pair<Integer, Integer>(chunkX + x, chunkZ + z));
					blocks.addAll(spawn(Material.QUARTZ_ORE, 15, 120, player, world, chunkX + x, chunkZ + z));
				}
			}
		}
	}
	private void removeFakeBlocks(Player player, IConcurrentDeque<Location> blocks, int tries) {
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
		
		for (Location b : blocks) {
			fakeBlockHelper.updateBlock(player, b, b.getBlock().getType());
		}
	}
	private void updateFakeBlocks(Player player, Location from, Location to, IConcurrentDeque<Location> blocks, IConcurrentDeque<Pair<Integer, Integer>> chunks, int tries) {
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
		
		if (to.getBlockX() >> 4 != from.getBlockX() >> 4 || to.getBlockZ() >> 4 != from.getBlockZ() >> 4) {
			int chunkX = to.getBlockX() >> 4;
			int chunkZ = to.getBlockZ() >> 4;
			World world = to.getWorld();
			
			ArrayList<Pair<Integer, Integer>> currentChunks = new ArrayList<Pair<Integer, Integer>>();
			
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					currentChunks.add(new Pair<Integer, Integer>(chunkX + x, chunkZ + z));
				}
			}
			
			ArrayList<Location> removedBlocks = new ArrayList<Location>();
			blocks.forEach((loc) -> {
				if (from.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4)) {
					Pair<Integer, Integer> chunk = new Pair<Integer, Integer>(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
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
			
			if (world.getEnvironment() == Environment.NORMAL) {
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
			} else if (world.getEnvironment() == Environment.NETHER) {
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
