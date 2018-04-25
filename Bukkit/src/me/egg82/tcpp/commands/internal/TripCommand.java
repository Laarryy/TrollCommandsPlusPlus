package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.services.registries.TripRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableExpiringRegistry;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.PlayerNotFoundException;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;

public class TripCommand extends PluginCommand {
	//vars
	private IVariableExpiringRegistry<UUID> tripRegistry = ServiceLocator.getService(TripRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public TripCommand() {
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
		} else if (args.length == 2) {
			ArrayList<String> retVal = new ArrayList<String>();
			
			if ("true".startsWith(args[1].toLowerCase())) {
				retVal.add("true");
			} else if ("false".startsWith(args[1].toLowerCase())) {
				retVal.add("false");
			}
			
			return retVal;
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_TRIP)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_TRIP)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1, 2)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		boolean copy = false;
		if (args.length == 2) {
			copy = isTrue(args[1]);
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player.getUniqueId(), player, copy);
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
			
			e(player.getUniqueId(), player, copy);
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	@SuppressWarnings("deprecation")
	private void e(UUID uuid, Player player, boolean keepInventory) {
		World world = player.getWorld();
		Location location = player.getLocation();
		PlayerInventory inv = player.getInventory();
		ItemStack[] items = inv.getContents();
		
		tripRegistry.setRegister(uuid, null);
		
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getType() != Material.AIR) {
				world.dropItemNaturally(location, items[i]);
			}
		}
		
		if (!keepInventory) {
			items = new ItemStack[items.length];
			inv.setContents(items);
			player.updateInventory();
		}
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " tripped!");
	}
	
	protected void onUndo() {
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	
	private boolean isTrue(String input) {
		input = input.toLowerCase();
		
		if (input.equals("true") || input.equals("yes") || input.equals("y")) {
			return true;
		}
		return false;
	}
}
