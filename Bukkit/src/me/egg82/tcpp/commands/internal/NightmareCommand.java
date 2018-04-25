package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidLibraryException;
import me.egg82.tcpp.exceptions.InvalidVersionException;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.services.registries.NightmareRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.concurrent.FixedConcurrentDeque;
import ninja.egg82.concurrent.IConcurrentDeque;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.BukkitInitType;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.PlayerNotFoundException;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.protocol.core.IFakeLivingEntity;
import ninja.egg82.protocol.reflection.IFakeEntityHelper;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.ThreadUtil;

public class NightmareCommand extends PluginCommand {
	//vars
	private IVariableRegistry<UUID> nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	private IFakeEntityHelper fakeEntityHelper = ServiceLocator.getService(IFakeEntityHelper.class);
	
	private String gameVersion = ServiceLocator.getService(InitRegistry.class).getRegister(BukkitInitType.GAME_VERSION, String.class);
	
	//constructor
	public NightmareCommand() {
		super();
	}
	
	//public
	public List<String> tabComplete() {
		if (args.length == 1) {
			ArrayList<String> retVal = new ArrayList<String>();
			
			if (args[0].isEmpty()) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					retVal.add(player.getName());
				}
			} else {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
						retVal.add(player.getName());
					}
				}
			}
			
			return retVal;
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_NIGHTMARE)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_NIGHTMARE)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		if (!fakeEntityHelper.isValidLibrary()) {
			sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_LIBRARY));
			onError().invoke(null, new ExceptionEventArgs<InvalidLibraryException>(new InvalidLibraryException(fakeEntityHelper)));
			return;
		}
		if (gameVersion.equals("1.8") || gameVersion.equals("1.8.1") || gameVersion.equals("1.8.3") || gameVersion.equals("1.8.8")) {
			sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_VERSION));
			onError().invoke(this, new ExceptionEventArgs<InvalidVersionException>(new InvalidVersionException(gameVersion, "1.9")));
			return;
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				UUID uuid = player.getUniqueId();
				
				if (!nightmareRegistry.hasRegister(uuid)) {
					if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
						continue;
					}
					
					e(uuid, player);
				} else {
					eUndo(uuid, player);
				}
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
				if (offlinePlayer != null) {
					UUID uuid = offlinePlayer.getUniqueId();
					if (nightmareRegistry.hasRegister(uuid)) {
						eUndo(uuid, offlinePlayer);
						onComplete().invoke(this, CompleteEventArgs.EMPTY);
						return;
					}
				}
				
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			
			UUID uuid = player.getUniqueId();
			
			if (!nightmareRegistry.hasRegister(uuid)) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
					onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player)));
					return;
				}
				
				e(uuid, player);
			} else {
				eUndo(uuid, player);
			}
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(UUID uuid, Player player) {
		Location[] zombieLocs = LocationUtil.getCircleAround(player.getLocation(), 3, MathUtil.fairRoundedRandom(6, 9));
		Location[] zombie2Locs = LocationUtil.getCircleAround(player.getLocation(), 5, MathUtil.fairRoundedRandom(8, 12));
		
		IConcurrentDeque<IFakeLivingEntity> entities = new FixedConcurrentDeque<IFakeLivingEntity>(zombieLocs.length + zombie2Locs.length);
		
		ThreadUtil.submit(new Runnable() {
			public void run() {
				for (int i = 0; i < zombieLocs.length; i++) {
					IFakeLivingEntity e = fakeEntityHelper.createEntity(zombieLocs[i], EntityType.ZOMBIE);
					e.addPlayer(player);
					Vector v = player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.23);
					if (LocationUtil.isFinite(v)) {
						e.moveTo(BlockUtil.getTopWalkableBlock(e.getLocation().add(v)));
					}
					e.lookTo(player.getEyeLocation());
					entities.add(e);
				}
				for (int i = 0; i < zombie2Locs.length; i++) {
					IFakeLivingEntity e = fakeEntityHelper.createEntity(zombie2Locs[i], EntityType.ZOMBIE);
					e.addPlayer(player);
					Vector v = player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.23);
					if (LocationUtil.isFinite(v)) {
						e.moveTo(BlockUtil.getTopWalkableBlock(e.getLocation().add(v)));
					}
					e.lookTo(player.getEyeLocation());
					entities.add(e);
				}
			}
		});
		
		nightmareRegistry.setRegister(uuid, entities);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now living in a nightmare!");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (nightmareRegistry.hasRegister(uuid)) {
				eUndo(uuid, player);
			}
		} else {
			OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
			UUID uuid = offlinePlayer.getUniqueId();
			if (nightmareRegistry.hasRegister(uuid)) {
				eUndo(uuid, offlinePlayer);
			}
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	@SuppressWarnings("unchecked")
	private void eUndo(UUID uuid, Player player) {
		IConcurrentDeque<IFakeLivingEntity> entities = nightmareRegistry.getRegister(uuid, IConcurrentDeque.class);
		
		ThreadUtil.submit(new Runnable() {
			public void run() {
				for (IFakeLivingEntity e : entities) {
					e.destroy();
				}
			}
		});
		
		nightmareRegistry.removeRegister(uuid);
		
		sender.sendMessage(player.getName() + " is no longer living in a nightmare.");
	}
	@SuppressWarnings("unchecked")
	private void eUndo(UUID uuid, OfflinePlayer player) {
		IConcurrentDeque<IFakeLivingEntity> entities = nightmareRegistry.getRegister(uuid, IConcurrentDeque.class);
		
		ThreadUtil.submit(new Runnable() {
			public void run() {
				for (IFakeLivingEntity e : entities) {
					e.destroy();
				}
			}
		});
		
		nightmareRegistry.removeRegister(uuid);
		
		sender.sendMessage(player.getName() + " is no longer living in a nightmare.");
	}
}
