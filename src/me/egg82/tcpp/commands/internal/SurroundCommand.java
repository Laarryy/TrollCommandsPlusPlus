package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidTypeException;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.services.MobTypeSearchDatabase;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.PlayerNotFoundException;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.ReflectUtil;

public class SurroundCommand extends PluginCommand {
	//vars
	private ArrayList<String> mobNames = new ArrayList<String>();
	private LanguageDatabase mobTypeDatabase = ServiceLocator.getService(MobTypeSearchDatabase.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public SurroundCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
		
		EntityType[] types = EntityType.values();
		
		Arrays.sort(types, (a, b) -> {
			if (a == null) {
				if (b == null) {
					return 0;
				}
				return -1;
			}
			if (b == null) {
				return 1;
			}
			
			return a.name().compareTo(b.name());
		});
		
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				continue;
			}
			if (!ReflectUtil.doesExtend(Monster.class, types[i].getEntityClass())) {
				continue;
			}
			
			mobNames.add(WordUtils.capitalize(String.join(" ", types[i].name().toLowerCase().split("_"))));
		}
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
		} else if (args.length == 2) {
			if (args[1].isEmpty()) {
				return mobNames;
			} else {
				ArrayList<String> retVal = new ArrayList<String>();
				for (int i = 0; i < mobNames.size(); i++) {
					if (mobNames.get(i).toLowerCase().startsWith(args[1].toLowerCase())) {
						retVal.add(mobNames.get(i));
					}
				}
				return retVal;
			}
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_SURROUND)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_SURROUND)));
			return;
		}
		if (args.length < 2) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		String search = "";
		for (int i = 1; i < args.length; i++) {
			search += args[i] + " ";
		}
		search = search.trim();
		
		EntityType type = null;
		
		try {
			type = EntityType.valueOf(search.toUpperCase().replaceAll(" ", "_"));
		} catch (Exception ex) {
			
		}
		
		if (type == null || !ReflectUtil.doesExtend(Monster.class, type.getEntityClass())) {
			// Not found or not a monster. Try to search the database.
			String[] types = mobTypeDatabase.getValues(mobTypeDatabase.naturalLanguage(search, false), 0);
			
			if (types == null || types.length == 0) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_TYPE));
				onError().invoke(this, new ExceptionEventArgs<InvalidTypeException>(new InvalidTypeException(search)));
				return;
			}
			
			try {
				type = EntityType.valueOf(types[0].toUpperCase());
			} catch (Exception ex) {
				
			}
			if (type == null) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_TYPE));
				onError().invoke(this, new ExceptionEventArgs<InvalidTypeException>(new InvalidTypeException(search)));
				return;
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player.getUniqueId().toString(), player, type);
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
				onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player)));
				return;
			}
			
			e(player.getUniqueId().toString(), player, type);
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(String uuid, Player player, EntityType mobType) {
		Location[] mobLocations = LocationUtil.getCircleAround(player.getLocation(), MathUtil.random(4.0d,  6.0d), MathUtil.fairRoundedRandom(8, 12));
		
		for (int i = 0; i < mobLocations.length; i++) {
			Location creatureLocation = BlockUtil.getTopWalkableBlock(mobLocations[i]);
			
			Monster m = (Monster) player.getWorld().spawn(creatureLocation, mobType.getEntityClass());
			if (m instanceof PigZombie) {
				((PigZombie) m).setAngry(true);
			}
			m.setTarget(player);
			m.setVelocity(player.getLocation().toVector().subtract(creatureLocation.toVector()).normalize().multiply(0.23d));
		}
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now surrounded by " + mobType.name().replace('_', ' ').toLowerCase() + "s!");
	}
	
	protected void onUndo() {
		
	}
}
