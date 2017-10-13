package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidItemException;
import me.egg82.tcpp.exceptions.InvalidLibraryException;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.nbt.core.INBTCompound;
import ninja.egg82.nbt.reflection.INBTHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.SenderNotAllowedException;
import ninja.egg82.plugin.reflection.player.IPlayerHelper;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.utils.MathUtil;

public class LuckyBlockCommand extends PluginCommand {
	//vars
	private IPlayerHelper playerHelper = ServiceLocator.getService(IPlayerHelper.class);
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public LuckyBlockCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_LUCKY_BLOCK)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_LUCKY_BLOCK)));
			return;
		}
		if (!nbtHelper.isValidLibrary()) {
			sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_LIBRARY));
			onError().invoke(this, new ExceptionEventArgs<InvalidLibraryException>(new InvalidLibraryException(nbtHelper)));
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.SENDER_NOT_ALLOWED));
			onError().invoke(this, new ExceptionEventArgs<SenderNotAllowedException>(new SenderNotAllowedException(sender, this)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0, 1)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		ItemStack item = playerHelper.getItemInMainHand((Player) sender);
		
		if (item == null || item.getType() == Material.AIR || !item.getType().isBlock()) {
			sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_ITEM));
			onError().invoke(this, new ExceptionEventArgs<InvalidItemException>(new InvalidItemException(item)));
			return;
		}
		
		INBTCompound compound = nbtHelper.getCompound(item);
		double luckyChance = 0.5;
		
		if (args.length == 1) {
			try {
				luckyChance = Double.parseDouble(args[0]) / 100.0d;
			} catch (Exception ex) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				sender.getServer().dispatchCommand(sender, "troll help " + name);
				onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
				return;
			}
			
			luckyChance = MathUtil.clamp(0.0d, 1.0d, luckyChance);
		}
		
		if (!compound.hasTag("tcppLucky")) {
			e(item, compound, luckyChance);
		} else {
			eUndo(item, compound);
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(ItemStack item, INBTCompound compound, double luckyChance) {
		compound.setDouble("tcppLucky", luckyChance);
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GOLD + ChatColor.BOLD.toString() + "LUCKY?");
		if (meta.hasLore()) {
			lore.addAll(meta.getLore());
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage("This item is now a lucky block!");
	}
	
	protected void onUndo() {
		
	}
	private void eUndo(ItemStack item, INBTCompound compound) {
		compound.removeTag("tcppLucky");
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>(meta.getLore());
		int removeLine = -1;
		for (int i = 0; i < lore.size(); i++) {
			if (lore.get(i).contains("LUCKY?")) {
				removeLine = i;
				break;
			}
		}
		if (removeLine > -1) {
			lore.remove(removeLine);
			lore.remove(removeLine);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		sender.sendMessage("This item is no longer a lucky block.");
	}
}