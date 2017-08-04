package me.egg82.tcpp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.Timer;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.reflection.disguise.DisguiseHelper;
import me.egg82.tcpp.reflection.disguise.LibsDisguisesHelper;
import me.egg82.tcpp.reflection.disguise.NullDisguiseHelper;
import me.egg82.tcpp.services.CommandRegistry;
import me.egg82.tcpp.services.CommandSearchDatabase;
import me.egg82.tcpp.services.KeywordRegistry;
import me.egg82.tcpp.util.ControlHelper;
import me.egg82.tcpp.util.DisplayHelper;
import me.egg82.tcpp.util.FoolsGoldHelper;
import me.egg82.tcpp.util.MetricsHelper;
import me.egg82.tcpp.util.VegetableHelper;
import me.egg82.tcpp.util.WhoAmIHelper;
import me.egg82.tcpp.util.WorldHoleHelper;
import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateResult;
import net.gravitydevelopment.updater.Updater.UpdateType;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.BasePlugin;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.reflection.exceptionHandlers.GameAnalyticsExceptionHandler;
import ninja.egg82.plugin.reflection.exceptionHandlers.IExceptionHandler;
import ninja.egg82.plugin.reflection.exceptionHandlers.RollbarExceptionHandler;
import ninja.egg82.plugin.reflection.exceptionHandlers.builders.GameAnalyticsBuilder;
import ninja.egg82.plugin.reflection.exceptionHandlers.builders.RollbarBuilder;
import ninja.egg82.plugin.services.LanguageRegistry;
import ninja.egg82.plugin.utils.SpigotReflectUtil;
import ninja.egg82.plugin.utils.VersionUtil;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.ReflectUtil;
import ninja.egg82.utils.StringUtil;

public class TrollCommandsPlusPlus extends BasePlugin {
	//vars
	private Metrics metrics = null;
	
	private Timer updateTimer = null;
	private Timer exceptionHandlerTimer = null;
	
	private int numCommands = 0;
	private int numEvents = 0;
	private int numPermissions = 0;
	private int numTicks = 0;
	
	private IExceptionHandler exceptionHandler = null;
	
	//constructor
	public TrollCommandsPlusPlus() {
		super();
		
		getLogger().setLevel(Level.WARNING);
		IExceptionHandler oldExceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
		ServiceLocator.removeServices(IExceptionHandler.class);
		
		ServiceLocator.provideService(RollbarExceptionHandler.class, false);
		exceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
		oldExceptionHandler.disconnect();
		exceptionHandler.connect(new RollbarBuilder("872a465ad3ed465a94136d1978e28ec0", "production"));
		exceptionHandler.setUnsentExceptions(oldExceptionHandler.getUnsentExceptions());
		exceptionHandler.setUnsentLogs(oldExceptionHandler.getUnsentLogs());
	}
	
	//public
	public void onLoad() {
		super.onLoad();
		
		SpigotReflectUtil.addServicesFromPackage("me.egg82.tcpp.services");
		
		PluginManager manager = getServer().getPluginManager();
		
		if (manager.getPlugin("LibsDisguises") != null) {
			if (manager.getPlugin("ProtocolLib") != null) {
				info(ChatColor.GREEN + "[TrollCommands++] Enabling support for LibsDisguises.");
				ServiceLocator.provideService(LibsDisguisesHelper.class);
			} else {
				warning(ChatColor.RED + "[TrollCommands++] LibsDisguises requires ProtocolLib to function, which was not found. The /control and /scare commands have been disabled.");
				ServiceLocator.provideService(NullDisguiseHelper.class);
			}
		} else if (manager.getPlugin("iDisguise") != null) {
			if (manager.getPlugin("ProtocolLib") != null) {
				info(ChatColor.GREEN + "[TrollCommands++] Enabling support for iDisguise.");
				ServiceLocator.provideService(DisguiseHelper.class);
			} else {
				warning(ChatColor.RED + "[TrollCommands++] iDisguise requires ProtocolLib to function, which was not found. The /control and /scare commands have been disabled.");
				ServiceLocator.provideService(NullDisguiseHelper.class);
			}
		} else {
			warning(ChatColor.RED + "[TrollCommands++] Neither LibsDisguises nor iDisguise was found. The /control and /scare commands have been disabled.");
			ServiceLocator.provideService(NullDisguiseHelper.class);
		}
		
		if (manager.getPlugin("ProtocolLib") != null) {
			info(ChatColor.GREEN + "[TrollCommands++] Enabling support for ProtocolLib.");
		} else {
			warning(ChatColor.RED + "[TrollCommands++] ProtocolLib was not found. The /foolsgold, /nightmare, and /rewind commands have been disabled.");
		}
		
		if (manager.getPlugin("PowerNBT") != null) {
			info(ChatColor.GREEN + "[TrollCommands++] Enabling support for PowerNBT.");
		} else if (manager.getPlugin("ItemNBTAPI") != null) {
			info(ChatColor.GREEN + "[TrollCommands++] Enabling support for ItemNBTAPI.");
		} else {
			warning(ChatColor.RED + "[TrollCommands++] Neither PowerNBT nor NBTAPI were found. The /attachcommand command has been disabled.");
		}
		
		ServiceLocator.provideService(ControlHelper.class);
		ServiceLocator.provideService(DisplayHelper.class);
		ServiceLocator.provideService(FoolsGoldHelper.class);
		ServiceLocator.provideService(VegetableHelper.class);
		ServiceLocator.provideService(WhoAmIHelper.class);
		ServiceLocator.provideService(WorldHoleHelper.class);
		ServiceLocator.provideService(MetricsHelper.class);
		ServiceLocator.provideService(CommandSearchDatabase.class, false);
		
		populateCommandDatabase();
		populateLanguage();
		
		updateTimer = new Timer(24 * 60 * 60 * 1000, onUpdateTimer);
		exceptionHandlerTimer = new Timer(60 * 60 * 1000, onExceptionHandlerTimer);
	}
	
	public void onEnable() {
		super.onEnable();
		
		try {
			metrics = new Metrics(this);
		} catch (Exception ex) {
			info(ChatColor.YELLOW + "[TrollCommands++] WARNING: Connection to metrics server could not be established. This affects nothing for server owners, but it does make me sad :(");
		}
		
		if (metrics != null) {
			metrics.addCustomChart(new Metrics.AdvancedPie("commands", () -> {
				IRegistry<String> commandRegistry = ServiceLocator.getService(CommandRegistry.class);
				HashMap<String, Integer> values = new HashMap<String, Integer>();
				for (String key : commandRegistry.getKeys()) {
					values.put(key, commandRegistry.getRegister(key, Integer.class));
				}
				return values;
			}));
			/*metrics.addCustomChart(new Metrics.MultiLineChart("commands-ml", () -> {
				IRegistry<String> commandRegistry = ServiceLocator.getService(CommandRegistry.class);
				HashMap<String, Integer> values = new HashMap<String, Integer>();
				for (String key : commandRegistry.getKeys()) {
					values.put(key, commandRegistry.getRegister(key, Integer.class));
				}
				commandRegistry.clear();
				return values;
			}));*/
		}
		
		HashMap<String, String[]> aliasMap = new HashMap<String, String[]>();
		aliasMap.put("TrollCommand", new String[] {"t"});
		
		SpigotReflectUtil.addCommandsFromPackage("me.egg82.tcpp.commands", aliasMap);
		numCommands = ReflectUtil.getClasses(PluginCommand.class, "me.egg82.tcpp.commands.internal").size();
		numEvents = SpigotReflectUtil.addEventsFromPackage("me.egg82.tcpp.events");
		numPermissions = SpigotReflectUtil.addPermissionsFromClass(PermissionsType.class);
		numTicks = SpigotReflectUtil.addTicksFromPackage("me.egg82.tcpp.ticks");
		
		enableMessage();
		checkUpdate();
		updateTimer.setRepeats(true);
		updateTimer.start();
		checkExceptionLimitReached();
		exceptionHandlerTimer.setRepeats(true);
		exceptionHandlerTimer.start();
	}
	public void onDisable() {
		super.onDisable();
		
		ControlHelper controlHelper = ServiceLocator.getService(ControlHelper.class);
		controlHelper.uncontrolAll();
		
		DisplayHelper displayHelper = ServiceLocator.getService(DisplayHelper.class);
		displayHelper.unsurroundAll();
		
		VegetableHelper vegetableHelper = ServiceLocator.getService(VegetableHelper.class);
		vegetableHelper.unvegetableAll();
		
		WhoAmIHelper whoAmIHelper = ServiceLocator.getService(WhoAmIHelper.class);
		whoAmIHelper.stopAll();
		
		WorldHoleHelper worldHoleHelper = ServiceLocator.getService(WorldHoleHelper.class);
		worldHoleHelper.undoAll();
		
		SpigotReflectUtil.clearAll();
		disableMessage();
	}
	
	//private
	private ActionListener onUpdateTimer = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			exceptionHandler.addThread(Thread.currentThread());
			checkUpdate();
		}
	};
	private void checkUpdate() {
		Updater updater = new Updater(this, 100359, getFile(), UpdateType.NO_DOWNLOAD, false);
		if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
			String latestVersion = updater.getLatestName();
			latestVersion = latestVersion.substring(latestVersion.lastIndexOf('v') + 1);
			String currentVersion = getDescription().getVersion();
			
			int[] latest = VersionUtil.parseVersion(latestVersion, '.');
			int[] current = VersionUtil.parseVersion(currentVersion, '.');
			
			for (int i = 0; i < Math.min(latest.length, current.length); i++) {
				if (latest[i] < current[i]) {
					return;
				}
			}
			
			warning(ChatColor.GREEN + "--== " + ChatColor.YELLOW + "TrollCommands++ UPDATE AVAILABLE (Latest: " + latestVersion + " Current: " + currentVersion + ") " + ChatColor.GREEN + " ==--");
		}
	}
	
	private ActionListener onExceptionHandlerTimer = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			checkExceptionLimitReached();
		}
	};
	private void checkExceptionLimitReached() {
		if (exceptionHandler.isLimitReached()) {
			IExceptionHandler oldExceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
			ServiceLocator.removeServices(IExceptionHandler.class);
			
			ServiceLocator.provideService(GameAnalyticsExceptionHandler.class, false);
			exceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
			oldExceptionHandler.disconnect();
			exceptionHandler.connect(new GameAnalyticsBuilder("250e5c508c3dd844ed1f8bd2a449d1a6", "dfb50b06e598e7a7ad9b3c84f7b118c12800ffce"));
			exceptionHandler.setUnsentExceptions(oldExceptionHandler.getUnsentExceptions());
			exceptionHandler.setUnsentLogs(oldExceptionHandler.getUnsentLogs());
		}
	}
	
	private void enableMessage() {
		info(ChatColor.AQUA + "  _______        _ _  _____                                          _                 ");
		info(ChatColor.AQUA + " |__   __|      | | |/ ____|                                        | |      _     _   ");
		info(ChatColor.AQUA + "    | |_ __ ___ | | | |     ___  _ __ ___  _ __ ___   __ _ _ __   __| |___ _| |_ _| |_ ");
		info(ChatColor.AQUA + "    | | '__/ _ \\| | | |    / _ \\| '_ ` _ \\| '_ ` _ \\ / _` | '_ \\ / _` / __|_   _|_   _|");
		info(ChatColor.AQUA + "    | | | | (_) | | | |___| (_) | | | | | | | | | | | (_| | | | | (_| \\__ \\ |_|   |_|  ");
		info(ChatColor.AQUA + "    |_|_|  \\___/|_|_|\\_____\\___/|_| |_| |_|_| |_| |_|\\__,_|_| |_|\\__,_|___/            ");
		info(ChatColor.GREEN + "[Version " + getDescription().getVersion() + "] " + ChatColor.RED + numCommands + " commands " + ChatColor.LIGHT_PURPLE + numEvents + " events " + ChatColor.WHITE + numPermissions + " permissions " + ChatColor.YELLOW + numTicks + " tick handlers");
		info(ChatColor.WHITE + "[TrollCommands++] " + ChatColor.GRAY + "Attempting to load compatibility with Bukkit version " + ((InitRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister("game.version"));
	}
	private void disableMessage() {
		info(ChatColor.GREEN + "--== " + ChatColor.LIGHT_PURPLE + "TrollCommands++ Disabled" + ChatColor.GREEN + " ==--");
	}
	
	private void populateCommandDatabase() {
		LanguageDatabase commandNameDatabase = ServiceLocator.getService(CommandSearchDatabase.class);
		IRegistry<String> keywordRegistry = ServiceLocator.getService(KeywordRegistry.class);
		PluginDescriptionFile descriptionFile = getDescription();
		
		String[] commands = ((String) descriptionFile.getCommands().get("troll").get("usage")).replaceAll("\r\n", "\n").split("\n");
		
		for (String entry : commands) {
			if (entry.contains("-= Available Commands =-")) {
				continue;
			}
			
			String command = entry.substring(0, entry.indexOf(':')).trim().split(" ")[1];
			String description = entry.substring(entry.indexOf(':') + 1).trim();
			
			if (command.equals("search") || command.equals("help")) {
				continue;
			}
			
			ArrayList<String> row = new ArrayList<String>();
			row.add(command);
			if (keywordRegistry.hasRegister(command)) {
				row.addAll(Arrays.asList((String[]) keywordRegistry.getRegister(command)));
			}
			
			ArrayList<String> v2 = new ArrayList<String>(Arrays.asList(description.split("\\s+")));
			StringUtil.stripSpecialChars(v2);
			StringUtil.stripCommonWords(v2);
			row.addAll(v2);
			
			commandNameDatabase.addRow(row.toArray(new String[0]));
		}
	}
	private void populateLanguage() {
		IRegistry<String> languageRegistry = ServiceLocator.getService(LanguageRegistry.class);
		
		languageRegistry.setRegister(LanguageType.PLAYER_IMMUNE, ChatColor.RED + "Player is immune.");
		languageRegistry.setRegister(LanguageType.INVALID_TARGET, ChatColor.RED + "The target you've chosen is invalid.");
		languageRegistry.setRegister(LanguageType.INVALID_LIBRARY, ChatColor.RED + "This command has been disabled because there is no recognized backing library available. Please install one and restart the server to enable this command.");
		languageRegistry.setRegister(LanguageType.COMMAND_IN_USE, ChatColor.RED + "This command is currently in use against this player. Please wait for it to complete before using it again.");
		languageRegistry.setRegister(LanguageType.NO_CHAT_CONTROL, ChatColor.RED + "You do not have permissions to chat while being controlled!");
		languageRegistry.setRegister(LanguageType.NO_CHAT_FROZEN, ChatColor.RED + "You do not have permissions to chat while frozen!");
		languageRegistry.setRegister(LanguageType.NOT_LIVING, ChatColor.RED + "The entity you have selected is neither a player nor a mob!");
		languageRegistry.setRegister(LanguageType.EMPOWERED, "The entity you have selected is now empowered!");
		languageRegistry.setRegister(LanguageType.DISEMPOWERED, "The entity you have selected is now disempowered!");
		languageRegistry.setRegister(LanguageType.INVALID_VERSION, ChatColor.RED + "This command has been disabled because this version of Minecraft doesn't support it.");
		languageRegistry.setRegister(LanguageType.INVALID_TYPE, ChatColor.RED + "Searched type is invalid or was not found.");
		languageRegistry.setRegister(LanguageType.INVALID_COMMAND, ChatColor.RED + "Command is invalid.");
		languageRegistry.setRegister(LanguageType.INVALID_ITEM, ChatColor.RED + "Item is invalid.");
	}
}