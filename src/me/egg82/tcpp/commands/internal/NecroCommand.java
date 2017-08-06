package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidLibraryException;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.reflection.disguise.IDisguiseHelper;
import me.egg82.tcpp.services.NecroRegistry;
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
import ninja.egg82.plugin.reflection.entity.IEntityHelper;
import ninja.egg82.plugin.reflection.player.IPlayerHelper;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;

public class NecroCommand extends PluginCommand {
	//vars
	private IRegistry<UUID> necroRegistry = ServiceLocator.getService(NecroRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	private IDisguiseHelper disguiseHelper = ServiceLocator.getService(IDisguiseHelper.class);
	private IPlayerHelper playerHelper = ServiceLocator.getService(IPlayerHelper.class);
	private IEntityHelper entityHelper = ServiceLocator.getService(IEntityHelper.class);
	
	//constructor
	public NecroCommand(CommandSender sender, Command command, String label, String[] args) {
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
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_NECRO)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_NECRO)));
			return;
		}
		if (!disguiseHelper.isValidLibrary()) {
			sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_LIBRARY));
			onError().invoke(this, new ExceptionEventArgs<InvalidLibraryException>(new InvalidLibraryException(disguiseHelper)));
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
				UUID uuid = player.getUniqueId();
				
				if (!necroRegistry.hasRegister(uuid)) {
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
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			
			UUID uuid = player.getUniqueId();
			
			if (!necroRegistry.hasRegister(uuid)) {
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
		disguiseHelper.disguiseAsEntity(player, EntityType.SKELETON);
		
		ItemStack[] inventory = player.getInventory().getContents();
		player.getInventory().clear();
		playerHelper.setItemInMainHand(player, getInfinityBow());
		player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
		
		if (Math.random() <= 0.01) {
			Spider spider = player.getWorld().spawn(player.getLocation(), Spider.class);
			entityHelper.addPassenger(spider, player);
		}
		
		necroRegistry.setRegister(uuid, inventory);
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " has been necro'd.");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			UUID uuid = player.getUniqueId();
			if (necroRegistry.hasRegister(uuid)) {
				eUndo(uuid, player);
			}
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void eUndo(UUID uuid, Player player) {
		if (player.isInsideVehicle()) {
			if (player.getVehicle() instanceof Spider) {
				entityHelper.removePassenger(player.getVehicle(), player);
			}
		}
		disguiseHelper.undisguise(player);
		ItemStack[] inv = necroRegistry.removeRegister(uuid, ItemStack[].class);
		player.getInventory().setContents(inv);
		
		sender.sendMessage(player.getName() + " is no longer necro'd.");
	}
	
	private ItemStack getInfinityBow() {
		ItemStack retVal = new ItemStack(Material.BOW, 1);
		retVal.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		retVal.addEnchantment(Enchantment.DURABILITY, 3);
		return retVal;
	}
}
