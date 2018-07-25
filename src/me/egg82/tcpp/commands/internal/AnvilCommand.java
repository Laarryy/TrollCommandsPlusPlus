package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.AnvilRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.utils.ReflectUtil;

public class AnvilCommand extends CommandHandler {
	//vars
	private boolean hasFallingBlockMethod = ReflectUtil.hasMethod("spawnFallingBlock", Bukkit.getWorlds().iterator().next());
	
	private IVariableRegistry<UUID> anvilRegistry = ServiceLocator.getService(AnvilRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public AnvilCommand() {
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
		if (!sender.hasPermission(PermissionsType.COMMAND_ANVIL)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player);
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Player could not be found.");
				return;
			}
			if (player.hasPermission(PermissionsType.IMMUNE)) {
				sender.sendMessage(ChatColor.RED + "Player is immune.");
				return;
			}
			
			e(player);
		}
	}
	private void e(Player player) {
		Location loc = player.getLocation().clone();
		for (int i = 0; i < 4; i++) {
			loc.add(0.0d, 1.0d, 0.0d);
			loc.getBlock().setType(Material.AIR);
		}
		loc.add(0.0d, 1.0d, 0.0d);
		
		if (hasFallingBlockMethod) {
			anvilRegistry.setRegister(loc.getWorld().spawnFallingBlock(loc, new MaterialData(Material.ANVIL)).getUniqueId(), null);
		} else {
			loc.getBlock().setType(Material.ANVIL);
			TaskUtil.runSync(new Runnable() {
				public void run() {
					tryGetAnvil(loc, 1);
				}
			}, 1L);
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
