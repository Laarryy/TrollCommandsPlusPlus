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

import me.egg82.tcpp.services.FoolsGoldChunkRegistry;
import me.egg82.tcpp.services.FoolsGoldRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.tuples.Pair;
import ninja.egg82.plugin.reflection.exceptionHandlers.IExceptionHandler;
import ninja.egg82.plugin.reflection.protocol.IFakeBlockHelper;
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
		ArrayList<Location> blocks = new ArrayList<Location>();
		ArrayList<Pair<Integer, Integer>> chunks = new ArrayList<Pair<Integer, Integer>>();
		
		Thread runner = new Thread(new Runnable() {
			public void run() {
				int chunkX = player.getLocation().getChunk().getX();
				int chunkZ = player.getLocation().getChunk().getZ();
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
		});
		ServiceLocator.getService(IExceptionHandler.class).addThread(runner);
		runner.start();
		
		foolsGoldRegistry.setRegister(uuid, blocks);
		foolsGoldChunkRegistry.setRegister(uuid, chunks);
	}
	@SuppressWarnings("unchecked")
	public void removePlayer(UUID uuid, Player player) {
		if (foolsGoldRegistry.hasRegister(uuid)) {
			List<Location> blocks = foolsGoldRegistry.getRegister(uuid, List.class);
			
			Thread runner = new Thread(new Runnable() {
				public void run() {
					for (Location b : blocks) {
						fakeBlockHelper.updateBlock(player, b, b.getBlock().getType());
					}
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
		Thread runner = new Thread(new Runnable() {
			public void run() {
				if (to.getChunk().getX() != from.getChunk().getX() || to.getChunk().getZ() != from.getChunk().getZ()) {
					List<Location> blocks = foolsGoldRegistry.getRegister(uuid, List.class);
					List<Pair<Integer, Integer>> chunks = foolsGoldChunkRegistry.getRegister(uuid, List.class);
					
					int chunkX = to.getChunk().getX();
					int chunkZ = to.getChunk().getZ();
					World world = to.getWorld();
					
					ArrayList<Pair<Integer, Integer>> currentChunks = new ArrayList<Pair<Integer, Integer>>();
					
					for (int x = -1; x <= 1; x++) {
						for (int z = -1; z <= 1; z++) {
							currentChunks.add(new Pair<Integer, Integer>(chunkX + x, chunkZ + z));
						}
					}
					
					ArrayList<Location> removedBlocks = new ArrayList<Location>();
					for (int i = 0; i < blocks.size(); i++) {
						Pair<Integer, Integer> chunk = new Pair<Integer, Integer>(blocks.get(i).getChunk().getX(), blocks.get(i).getChunk().getZ());
						if (!currentChunks.contains(chunk)) {
							fakeBlockHelper.updateBlock(player, blocks.get(i), blocks.get(i).getBlock().getType());
							removedBlocks.add(blocks.get(i));
							chunks.remove(chunk);
						}
					}
					
					blocks.removeAll(removedBlocks);
					
					if (world.getEnvironment() == Environment.NORMAL) {
						for (int i = 0; i < currentChunks.size(); i++) {
							Pair<Integer, Integer> chunk = currentChunks.get(i);
							if (chunks.contains(chunk)) {
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
							if (chunks.contains(chunk)) {
								continue;
							}
							
							chunks.add(chunk);
							blocks.addAll(spawn(Material.QUARTZ_ORE, 15, 120, player, world, chunk.getLeft(), chunk.getRight()));
						}
					}
				}
			}
		});
		ServiceLocator.getService(IExceptionHandler.class).addThread(runner);
		runner.start();
	}
	
	@SuppressWarnings("unchecked")
	public void updatePlayer(UUID uuid, Player player, Location newLocation) {
		Thread runner = new Thread(new Runnable() {
			public void run() {
				List<Location> blocks = foolsGoldRegistry.getRegister(uuid, List.class);
				List<Pair<Integer, Integer>> chunks = foolsGoldChunkRegistry.getRegister(uuid, List.class);
				
				int chunkX = newLocation.getChunk().getX();
				int chunkZ = newLocation.getChunk().getZ();
				World world = newLocation.getWorld();
				
				ArrayList<Pair<Integer, Integer>> currentChunks = new ArrayList<Pair<Integer, Integer>>();
				
				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {
						currentChunks.add(new Pair<Integer, Integer>(chunkX + x, chunkZ + z));
					}
				}
				
				ArrayList<Location> removedBlocks = new ArrayList<Location>();
				for (int i = 0; i < blocks.size(); i++) {
					Pair<Integer, Integer> chunk = new Pair<Integer, Integer>(blocks.get(i).getChunk().getX(), blocks.get(i).getChunk().getZ());
					if (!currentChunks.contains(chunk)) {
						fakeBlockHelper.updateBlock(player, blocks.get(i), blocks.get(i).getBlock().getType());
						removedBlocks.add(blocks.get(i));
						chunks.remove(chunk);
					}
				}
				
				blocks.removeAll(removedBlocks);
				
				if (world.getEnvironment() == Environment.NORMAL) {
					for (int i = 0; i < currentChunks.size(); i++) {
						Pair<Integer, Integer> chunk = currentChunks.get(i);
						if (chunks.contains(chunk)) {
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
						if (chunks.contains(chunk)) {
							continue;
						}
						
						chunks.add(chunk);
						blocks.addAll(spawn(Material.QUARTZ_ORE, 15, 120, player, world, chunk.getLeft(), chunk.getRight()));
					}
				}
			}
		});
		ServiceLocator.getService(IExceptionHandler.class).addThread(runner);
		runner.start();
	}
	
	//private
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
			} while (mat != Material.STONE && mat != Material.SANDSTONE && tries <= 100);
			
			if (mat != Material.STONE && mat != Material.SANDSTONE) {
				continue;
			}
			
			retVal.add(loc);
		}
		
		fakeBlockHelper.updateBlocks(player, retVal.toArray(new Location[0]), type);
		return retVal;
	}
}
