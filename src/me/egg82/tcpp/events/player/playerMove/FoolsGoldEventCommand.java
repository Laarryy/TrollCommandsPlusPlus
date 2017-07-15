package me.egg82.tcpp.events.player.playerMove;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.services.FoolsGoldChunkRegistry;
import me.egg82.tcpp.services.FoolsGoldRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.Pair;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.protocol.IFakeBlockHelper;
import ninja.egg82.utils.MathUtil;

public class FoolsGoldEventCommand extends EventCommand<PlayerMoveEvent> {
	//vars
	private IRegistry foolsGoldRegistry = ServiceLocator.getService(FoolsGoldRegistry.class);
	private IRegistry foolsGoldChunkRegistry = ServiceLocator.getService(FoolsGoldChunkRegistry.class);
	
	private IFakeBlockHelper fakeBlockHelper = ServiceLocator.getService(IFakeBlockHelper.class);
	
	//constructor
	public FoolsGoldEventCommand(PlayerMoveEvent event) {
		super(event);
	}
	
	//public

	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (foolsGoldRegistry.hasRegister(uuid)) {
			new Thread(new Runnable() {
				public void run() {
					if (event.getTo().getChunk().getX() != event.getFrom().getChunk().getX() || event.getTo().getChunk().getZ() != event.getFrom().getChunk().getZ()) {
						List<Location> blocks = foolsGoldRegistry.getRegister(uuid, List.class);
						List<Pair<Integer, Integer>> chunks = foolsGoldChunkRegistry.getRegister(uuid, List.class);
						
						int chunkX = event.getTo().getChunk().getX();
						int chunkZ = event.getTo().getChunk().getZ();
						World world = event.getTo().getWorld();
						
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
			}).start();
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
