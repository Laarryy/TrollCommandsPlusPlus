package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.FoolsGoldChunkRegistry;
import me.egg82.tcpp.services.FoolsGoldRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.Pair;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.reflection.protocol.IFakeBlockHelper;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.utils.MathUtil;

public class FoolsGoldCommand extends PluginCommand {
	//vars
	private IRegistry foolsGoldRegistry = (IRegistry) ServiceLocator.getService(FoolsGoldRegistry.class);
	private IRegistry foolsGoldChunkRegistry = (IRegistry) ServiceLocator.getService(FoolsGoldChunkRegistry.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	private IFakeBlockHelper fakeBlockHelper = (IFakeBlockHelper) ServiceLocator.getService(IFakeBlockHelper.class);
	
	//constructor
	public FoolsGoldCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_FOOLS_GOLD)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		if (!fakeBlockHelper.isValidLibrary()) {
			sender.sendMessage(MessageType.NO_LIBRARY);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_LIBRARY);
			return;
		}
		
		Player player = CommandUtil.getPlayerByName(args[0]);
		
		if (player == null) {
			sender.sendMessage(SpigotMessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.PLAYER_NOT_FOUND);
			return;
		}
		if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return;
		}
		
		String uuid = player.getUniqueId().toString();
		
		if (!foolsGoldRegistry.hasRegister(uuid)) {
			e(uuid, player);
		} else {
			eUndo(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player) {
		ArrayList<Location> blocks = new ArrayList<Location>();
		ArrayList<Pair<Integer, Integer>> chunks = new ArrayList<Pair<Integer, Integer>>();
		
		new Thread(new Runnable() {
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
		}).start();
		
		foolsGoldRegistry.setRegister(uuid, List.class, blocks);
		foolsGoldChunkRegistry.setRegister(uuid, List.class, chunks);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now mining fake ore!");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		String uuid = player.getUniqueId().toString();
		
		if (foolsGoldRegistry.hasRegister(uuid)) {
			eUndo(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	@SuppressWarnings("unchecked")
	private void eUndo(String uuid, Player player) {
		List<Location> blocks = (List<Location>) foolsGoldRegistry.getRegister(uuid);
		
		new Thread(new Runnable() {
			public void run() {
				for (Location b : blocks) {
					fakeBlockHelper.updateBlock(player, b, b.getBlock().getType());
				}
			}
		}).start();
		
		foolsGoldRegistry.setRegister(uuid, List.class, null);
		foolsGoldChunkRegistry.setRegister(uuid, List.class, null);
		
		sender.sendMessage(player.getName() + " is no longer mining fake ore.");
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
