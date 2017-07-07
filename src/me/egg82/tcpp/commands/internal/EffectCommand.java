package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.EffectRegistry;
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
	private IRegistry effectRegistry = (IRegistry) ServiceLocator.getService(EffectRegistry.class);
	
	private LanguageDatabase potionTypeDatabase = (LanguageDatabase) ServiceLocator.getService(PotionTypeSearchDatabase.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public EffectCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
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
		
		String search = "";
		for (int i = 1; i < args.length; i++) {
			search += args[i] + " ";
		}
		search = search.trim();
		
		PotionEffectType effect = PotionEffectType.getByName(search.replaceAll(" ", "_"));
		
		if (effect == null) {
			// Effect not found. It's possible it was just misspelled. Search the database.
			String[] types = potionTypeDatabase.getValues(potionTypeDatabase.naturalLanguage(search, false), 0);
			
			if (types == null || types.length == 0) {
				sender.sendMessage(MessageType.POTION_NOT_FOUND);
				dispatch(CommandEvent.ERROR, CommandErrorType.POTION_NOT_FOUND);
				return;
			}
			
			effect = PotionEffectType.getByName(types[0]);
		}
		
		List<PotionEffectType> currentEffects = (List<PotionEffectType>) effectRegistry.getRegister(uuid);
		
		if (currentEffects == null || !currentEffects.contains(effect)) {
			e(uuid, player, effect, currentEffects);
		} else {
			eUndo(uuid, player, effect, currentEffects);
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
		
		sender.sendMessage(player.getName() + " is now affected by " + potionType.getName().replace('_', ' ').toLowerCase() + "!");
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
		
		sender.sendMessage(player.getName() + " is no longer being affected by " + potionType.getName().replace('_', ' ').toLowerCase() + ".");
	}
	private void eUndo(String uuid, Player player) {
		effectRegistry.setRegister(uuid, List.class, null);
		
		sender.sendMessage(player.getName() + " no longer has any permanent affects.");
	}
}
