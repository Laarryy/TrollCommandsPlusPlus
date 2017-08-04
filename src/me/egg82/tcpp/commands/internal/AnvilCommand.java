package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.services.AnvilRegistry;
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
import ninja.egg82.plugin.utils.TaskUtil;
import ninja.egg82.startup.InitRegistry;

public class AnvilCommand extends PluginCommand {
	//vars
	private IRegistry<UUID> anvilRegistry = ServiceLocator.getService(AnvilRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	private String gameVersion = ServiceLocator.getService(InitRegistry.class).getRegister("game.version", String.class);
	
	//constructor
	public AnvilCommand(CommandSender sender, Command command, String label, String[] args) {
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
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_ANVIL)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_ANVIL)));
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
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player);
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
			
			e(player);
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(Player player) {
		Location loc = player.getLocation().clone();
		for (int i = 0; i < 4; i++) {
			loc.add(0.0d, 1.0d, 0.0d);
			loc.getBlock().setType(Material.AIR);
		}
		loc.add(0.0d, 1.0d, 0.0d);
		
		if (gameVersion.equals("1.8") || gameVersion.equals("1.8.1") || gameVersion.equals("1.8.3") || gameVersion.equals("1.8.8")) {
			loc.getBlock().setType(Material.ANVIL);
			TaskUtil.runSync(new Runnable() {
				public void run() {
					tryGetAnvil(loc, 1);
				}
			}, 1L);
		} else {
			anvilRegistry.setRegister(loc.getWorld().spawnFallingBlock(loc, new MaterialData(Material.ANVIL)).getUniqueId(), null);
		}
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage("The " + ChatColor.STRIKETHROUGH + ChatColor.ITALIC + "base" + ChatColor.RESET + " anvil has been dropped on " + player.getName() + ".");
	}
	private void tryGetAnvil(Location loc, int tries) {
		Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 2.0d, 2.0d, 2.0d);
		for (Entity e : entities) {
			if (e instanceof FallingBlock) {
				if (((FallingBlock) e).getMaterial() == Material.ANVIL) {
					anvilRegistry.setRegister(e.getUniqueId(), null);
					return;
				}
			}
		}
		
		if (tries < 40) {
			TaskUtil.runSync(new Runnable() {
				public void run() {
					tryGetAnvil(loc, tries + 1);
				}
			}, 1L);
		}
	}
	
	protected void onUndo() {
		
	}
}
