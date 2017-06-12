package me.egg82.tcpp.commands;

import java.util.Arrays;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.GuiRegistry;
import me.egg82.tcpp.services.TrollInventoryRegistry;
import me.egg82.tcpp.services.TrollPlayerRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.startup.InitRegistry;

public class TrollCommand extends PluginCommand {
	//vars
	private LanguageDatabase ldb = (LanguageDatabase) ServiceLocator.getService(LanguageDatabase.class);
	private Map<String, Map<String, Object>> commands = null;
	private String[] allCommands = null;
	
	private IRegistry trollInventoryRegistry = (IRegistry) ServiceLocator.getService(TrollInventoryRegistry.class);
	private IRegistry trollPlayerRegistry = (IRegistry) ServiceLocator.getService(TrollPlayerRegistry.class);
	private IRegistry guiRegistry = (IRegistry) ServiceLocator.getService(GuiRegistry.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public TrollCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
		commands = ((PluginDescriptionFile) ((JavaPlugin) ((IRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister("plugin")).getDescription()).getCommands();
		allCommands = commands.keySet().toArray(new String[0]);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_TROLL)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(SpigotMessageType.CONSOLE_NOT_ALLOWED);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.CONSOLE_NOT_ALLOWED);
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
		
		String search = "";
		if (args.length >= 2) {
			for (int i = 1; i < args.length; i++) {
				search += args[i] + " ";
			}
			search = search.trim();
		}
		
		e(player.getUniqueId().toString(), player, ((Player) sender).getUniqueId().toString(), (Player) sender, search);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player, String senderUuid, Player senderPlayer, String search) {
		Inventory inv = createInventory(senderPlayer, search, 0);
		trollInventoryRegistry.setRegister(senderUuid, Inventory.class, inv);
		trollPlayerRegistry.setRegister(senderUuid, Player.class, player);
		
		senderPlayer.openInventory(inv);
		
		metricsHelper.commandWasRun(command.getName());
	}
	
	private Inventory createInventory(Player permissionsPlayer, String search, int page) {
		if (page < 0) {
			page = 0;
		}
		
		Inventory retVal = Bukkit.createInventory(null, 27, "TrollCommands++");
		String[] commands = null;
		
		if (search == null || search.isEmpty()) {
			commands = allCommands;
		} else {
			commands = ldb.getValues(ldb.naturalLanguage(search, false, ' '), 0);
		}
		
		int numCommands = Math.min(Math.max(0, commands.length - (19 * page)), 19);
		
		for (int i = 19 * page; i < numCommands; i++) {
			if (i < 9) {
				retVal.setItem(retVal.firstEmpty(), getItemStack(commands[i]));
			} else if (i >= 9 && i < 14) {
				retVal.setItem(i + 2, getItemStack(commands[i]));
			} else {
				retVal.setItem(i + 6, getItemStack(commands[i]));
			}
		}
		
		if (commands.length >= page * 19) {
			retVal.setItem(26, getNextItem());
		}
		if (page != 0) {
			retVal.setItem(18, getPreviousItem());
		}
		
		if (inventoryIsEmpty(retVal)) {
			retVal.setItem(13, getCloseItem());
		}
		
		return retVal;
	}
	private ItemStack getItemStack(String command) {
		ItemStack item = null;
		
		if (guiRegistry.hasRegister(command)) {
			if (guiRegistry.hasRegister(command + "_data")) {
				item = new ItemStack((Material) guiRegistry.getRegister(command), 1, (short) guiRegistry.getRegister(command + "_data"));
			} else {
				item = new ItemStack((Material) guiRegistry.getRegister(command));
			}
		} else {
			item = new ItemStack(Material.STONE);
		}
		
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.WHITE + "/" + command);
		meta.setLocalizedName(command);
		meta.setLore(Arrays.asList(new String[] {
			ChatColor.GRAY + ChatColor.ITALIC.toString() + (String) commands.get(command).get("description"),
			ChatColor.YELLOW + (String) commands.get(command).get("usage")
		}));
		
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getNextItem() {
		ItemStack item = new ItemStack(Material.REDSTONE_TORCH_ON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Next");
		meta.setLocalizedName("next");
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getPreviousItem() {
		ItemStack item = new ItemStack(Material.REDSTONE_TORCH_OFF);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Previous");
		meta.setLocalizedName("previous");
		item.setItemMeta(meta);
		return item;
	}
	private ItemStack getCloseItem() {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Close");
		meta.setLocalizedName("close");
		item.setItemMeta(meta);
		return item;
	}
	
	private boolean inventoryIsEmpty(Inventory inv) {
		ItemStack[] contents = inv.getContents();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] != null && contents[i].getType() != Material.AIR) {
				return false;
			}
		}
		return true;
	}
}