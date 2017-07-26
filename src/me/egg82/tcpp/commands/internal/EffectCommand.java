package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidTypeException;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.services.EffectRegistry;
import me.egg82.tcpp.services.PotionNameRegistry;
import me.egg82.tcpp.services.PotionTypeSearchDatabase;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.PlayerNotFoundException;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.sql.LanguageDatabase;

public class EffectCommand extends PluginCommand {
	//vars
	private IRegistry<UUID> effectRegistry = ServiceLocator.getService(EffectRegistry.class);
	private IRegistry<String> potionNameRegistry = ServiceLocator.getService(PotionNameRegistry.class);
	
	private LanguageDatabase potionTypeDatabase = ServiceLocator.getService(PotionTypeSearchDatabase.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public EffectCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
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
		} else if(args.length == 2) {
			ArrayList<String> retVal = new ArrayList<String>();
			
			if (args[1].isEmpty()) {
				for (String name : potionNameRegistry.getKeys()) {
					retVal.add(potionNameRegistry.getRegister(name, String.class));
				}
			} else {
				for (String name : potionNameRegistry.getKeys()) {
					String value = potionNameRegistry.getRegister(name, String.class);
					if (value.toLowerCase().startsWith(args[1].toLowerCase())) {
						retVal.add(value);
					}
				}
			}
			
			return retVal;
		}
		
		return null;
	}
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_EFFECT)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_EFFECT)));
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		if (args.length == 1) {
			Player player = CommandUtil.getPlayerByName(args[0]);
			if (player == null) {
				OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
				if (offlinePlayer != null) {
					UUID uuid = offlinePlayer.getUniqueId();
					if (effectRegistry.hasRegister(uuid)) {
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
			
			eUndo(uuid, player);
			onComplete().invoke(this, CompleteEventArgs.EMPTY);
			return;
		}
		
		String search = "";
		for (int i = 1; i < args.length; i++) {
			search += args[i] + " ";
		}
		search = search.trim();
		
		PotionEffectType type = PotionEffectType.getByName(search.replaceAll(" ", "_").toUpperCase());
		
		if (type == null) {
			// Effect not found. It's possible it was just misspelled. Search the database.
			String[] types = potionTypeDatabase.getValues(potionTypeDatabase.naturalLanguage(search, false), 0);
			
			if (types == null || types.length == 0) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_TYPE));
				onError().invoke(this, new ExceptionEventArgs<InvalidTypeException>(new InvalidTypeException(search)));
				return;
			}
			
			type = PotionEffectType.getByName(types[0].toUpperCase());
			if (type == null) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_TYPE));
				onError().invoke(this, new ExceptionEventArgs<InvalidTypeException>(new InvalidTypeException(search)));
				return;
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				UUID uuid = player.getUniqueId();
				
				List<PotionEffectType> currentEffects = (List<PotionEffectType>) effectRegistry.getRegister(uuid);
				
				if (currentEffects == null || !currentEffects.contains(type)) {
					if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
						continue;
					}
					
					e(uuid, player, type, currentEffects);
				} else {
					eUndo(uuid, player, type, currentEffects);
				}
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
				if (offlinePlayer != null) {
					UUID uuid = offlinePlayer.getUniqueId();
					if (effectRegistry.hasRegister(uuid)) {
						List<PotionEffectType> currentEffects = effectRegistry.getRegister(uuid, List.class);
						eUndo(uuid, offlinePlayer, type, currentEffects);
						onComplete().invoke(this, CompleteEventArgs.EMPTY);
						return;
					}
				}
				
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			
			UUID uuid = player.getUniqueId();
			
			List<PotionEffectType> currentEffects = effectRegistry.getRegister(uuid, List.class);
			
			if (currentEffects == null || !currentEffects.contains(type)) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
					onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player)));
					return;
				}
				
				e(uuid, player, type, currentEffects);
			} else {
				eUndo(uuid, player, type, currentEffects);
			}
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(UUID uuid, Player player, PotionEffectType potionType, List<PotionEffectType> currentEffects) {
		if (currentEffects == null) {
			currentEffects = new ArrayList<PotionEffectType>();
			effectRegistry.setRegister(uuid, currentEffects);
		}
		
		currentEffects.add(potionType);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now affected by " + potionNameRegistry.getRegister(potionType.getName()) + "!");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (effectRegistry.hasRegister(uuid)) {
				eUndo(uuid, player);
			}
		} else {
			OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
			UUID uuid = offlinePlayer.getUniqueId();
			if (effectRegistry.hasRegister(uuid)) {
				eUndo(uuid, offlinePlayer);
			}
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void eUndo(UUID uuid, Player player, PotionEffectType potionType, List<PotionEffectType> currentEffects) {
		currentEffects.remove(potionType);
		player.removePotionEffect(potionType);
		
		if (currentEffects.size() == 0) {
			effectRegistry.removeRegister(uuid);
		}
		
		sender.sendMessage(player.getName() + " is no longer being affected by " + potionNameRegistry.getRegister(potionType.getName(), String.class) + ".");
	}
	private void eUndo(UUID uuid, OfflinePlayer player, PotionEffectType potionType, List<PotionEffectType> currentEffects) {
		currentEffects.remove(potionType);
		
		if (currentEffects.size() == 0) {
			effectRegistry.removeRegister(uuid);
		}
		
		sender.sendMessage(player.getName() + " is no longer being affected by " + potionNameRegistry.getRegister(potionType.getName(), String.class) + ".");
	}
	@SuppressWarnings("unchecked")
	private void eUndo(UUID uuid, Player player) {
		if (effectRegistry.hasRegister(uuid)) {
			List<PotionEffectType> effects = effectRegistry.getRegister(uuid, List.class);
			for (PotionEffectType potionType : effects) {
				player.removePotionEffect(potionType);
			}
			effectRegistry.removeRegister(uuid);
		}
		
		sender.sendMessage(player.getName() + " no longer has any permanent potion effects.");
	}
	private void eUndo(UUID uuid, OfflinePlayer player) {
		effectRegistry.removeRegister(uuid);
		
		sender.sendMessage(player.getName() + " no longer has any permanent potion effects.");
	}
}
