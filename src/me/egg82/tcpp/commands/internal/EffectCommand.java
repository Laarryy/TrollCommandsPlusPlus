package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.EffectRegistry;
import me.egg82.tcpp.services.PotionNameRegistry;
import me.egg82.tcpp.services.PotionTypeSearchDatabase;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.sql.LanguageDatabase;

public class EffectCommand extends PluginCommand {
	//vars
	private IRegistry effectRegistry = ServiceLocator.getService(EffectRegistry.class);
	private IRegistry potionNameRegistry = ServiceLocator.getService(PotionNameRegistry.class);
	
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
				for (String name : potionNameRegistry.getRegistryNames()) {
					retVal.add(potionNameRegistry.getRegister(name, String.class));
				}
			} else {
				for (String name : potionNameRegistry.getRegistryNames()) {
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
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (args.length < 2) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
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
				sender.sendMessage(MessageType.POTION_NOT_FOUND);
				dispatch(CommandEvent.ERROR, CommandErrorType.POTION_NOT_FOUND);
				return;
			}
			
			type = PotionEffectType.getByName(types[0].toUpperCase());
			if (type == null) {
				sender.sendMessage(MessageType.POTION_NOT_FOUND);
				dispatch(CommandEvent.ERROR, CommandErrorType.POTION_NOT_FOUND);
				return;
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				String uuid = player.getUniqueId().toString();
				
				List<PotionEffectType> currentEffects = (List<PotionEffectType>) effectRegistry.getRegister(uuid);
				
				if (currentEffects == null || !currentEffects.contains(type)) {
					e(uuid, player, type, currentEffects);
				} else {
					eUndo(uuid, player, type, currentEffects);
				}
			}
		} else {
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
			
			List<PotionEffectType> currentEffects = effectRegistry.getRegister(uuid, List.class);
			
			if (currentEffects == null || !currentEffects.contains(type)) {
				e(uuid, player, type, currentEffects);
			} else {
				eUndo(uuid, player, type, currentEffects);
			}
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player, PotionEffectType potionType, List<PotionEffectType> currentEffects) {
		if (currentEffects == null) {
			currentEffects = new ArrayList<PotionEffectType>();
			effectRegistry.setRegister(uuid, List.class, currentEffects);
		}
		
		currentEffects.add(potionType);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now affected by " + potionNameRegistry.getRegister(potionType.getName()) + "!");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		String uuid = player.getUniqueId().toString();
		
		if (effectRegistry.hasRegister(uuid)) {
			eUndo(player.getUniqueId().toString(), player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void eUndo(String uuid, Player player, PotionEffectType potionType, List<PotionEffectType> currentEffects) {
		currentEffects.remove(potionType);
		player.removePotionEffect(potionType);
		
		sender.sendMessage(player.getName() + " is no longer being affected by " + potionNameRegistry.getRegister(potionType.getName(), String.class) + ".");
	}
	@SuppressWarnings("unchecked")
	private void eUndo(String uuid, Player player) {
		List<PotionEffectType> effects = effectRegistry.getRegister(uuid, List.class);
		for (PotionEffectType potionType : effects) {
			player.removePotionEffect(potionType);
		}
		
		effectRegistry.setRegister(uuid, List.class, null);
		
		sender.sendMessage(player.getName() + " no longer has any permanent potion effects.");
	}
}
