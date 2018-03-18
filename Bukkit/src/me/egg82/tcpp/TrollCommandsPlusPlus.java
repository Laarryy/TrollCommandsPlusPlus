package me.egg82.tcpp;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.databases.CommandSearchDatabase;
import me.egg82.tcpp.services.registries.CommandRegistry;
import me.egg82.tcpp.services.registries.KeywordRegistry;
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
import ninja.egg82.disguise.reflection.DisguiseHelper;
import ninja.egg82.disguise.reflection.LibsDisguisesHelper;
import ninja.egg82.disguise.reflection.NullDisguiseHelper;
import ninja.egg82.exceptionHandlers.GameAnalyticsExceptionHandler;
import ninja.egg82.exceptionHandlers.IExceptionHandler;
import ninja.egg82.exceptionHandlers.RollbarExceptionHandler;
import ninja.egg82.exceptionHandlers.builders.GameAnalyticsBuilder;
import ninja.egg82.exceptionHandlers.builders.RollbarBuilder;
import ninja.egg82.nbt.reflection.NullNBTHelper;
import ninja.egg82.nbt.reflection.PowerNBTHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.BasePlugin;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.BukkitInitType;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.plugin.handlers.EventListener;
import ninja.egg82.plugin.handlers.IMessageHandler;
import ninja.egg82.plugin.handlers.PermissionsManager;
import ninja.egg82.plugin.handlers.RabbitMessageHandler;
import ninja.egg82.plugin.handlers.TickHandler;
import ninja.egg82.plugin.services.LanguageRegistry;
import ninja.egg82.plugin.utils.BukkitReflectUtil;
import ninja.egg82.plugin.utils.ConfigUtil;
import ninja.egg82.plugin.utils.VersionUtil;
import ninja.egg82.plugin.utils.YamlUtil;
import ninja.egg82.protocol.reflection.NullFakeBlockHelper;
import ninja.egg82.protocol.reflection.NullFakeEntityHelper;
import ninja.egg82.protocol.reflection.ProtocolLibFakeBlockHelper;
import ninja.egg82.protocol.reflection.ProtocolLibFakeEntityHelper;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.FileUtil;
import ninja.egg82.utils.ReflectUtil;
import ninja.egg82.utils.StringUtil;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class TrollCommandsPlusPlus extends BasePlugin {
	//vars
	private Metrics metrics = null;
	
	private int numMessages = 0;
	private int numCommands = 0;
	private int numEvents = 0;
	private int numPermissions = 0;
	private int numTicks = 0;
	
	private IExceptionHandler exceptionHandler = null;
	private String version = getDescription().getVersion();
	private String userId = Bukkit.getServerId().trim();
	
	private ScheduledExecutorService mainThreadPool = null;
	
	//constructor
	public TrollCommandsPlusPlus() {
		super();
		
		if (userId.isEmpty() || userId.equalsIgnoreCase("unnamed") || userId.equalsIgnoreCase("unknown") || userId.equalsIgnoreCase("default")) {
			userId = UUID.randomUUID().toString();
			writeProperties();
		}
		
		getLogger().setLevel(Level.WARNING);
		IExceptionHandler oldExceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
		ServiceLocator.removeServices(IExceptionHandler.class);
		
		ServiceLocator.provideService(RollbarExceptionHandler.class, false);
		exceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
		oldExceptionHandler.disconnect();
		exceptionHandler.connect(new RollbarBuilder("78062d4e18074560850d4d8e0805b564", "production", version, userId));
		exceptionHandler.setUnsentExceptions(oldExceptionHandler.getUnsentExceptions());
		exceptionHandler.setUnsentLogs(oldExceptionHandler.getUnsentLogs());
	}
	
	//public
	public void onLoad() {
		super.onLoad();
		
		BukkitReflectUtil.addServicesFromPackage("me.egg82.tcpp.services");
		
		PluginManager manager = getServer().getPluginManager();
		
		if (manager.getPlugin("LibsDisguises") != null) {
			if (manager.getPlugin("ProtocolLib") != null) {
				printInfo(ChatColor.GREEN + "[TrollCommands++] Enabling support for LibsDisguises.");
				ServiceLocator.provideService(LibsDisguisesHelper.class);
			} else {
				printWarning(ChatColor.RED + "[TrollCommands++] LibsDisguises requires ProtocolLib to function, which was not found. The /troll control and /troll duck commands have been disabled.");
				ServiceLocator.provideService(NullDisguiseHelper.class);
			}
		} else if (manager.getPlugin("iDisguise") != null) {
			if (manager.getPlugin("ProtocolLib") != null) {
				printInfo(ChatColor.GREEN + "[TrollCommands++] Enabling support for iDisguise.");
				ServiceLocator.provideService(DisguiseHelper.class);
			} else {
				printWarning(ChatColor.RED + "[TrollCommands++] iDisguise requires ProtocolLib to function, which was not found. The /troll control and /troll duck commands have been disabled.");
				ServiceLocator.provideService(NullDisguiseHelper.class);
			}
		} else {
			printWarning(ChatColor.RED + "[TrollCommands++] Neither LibsDisguises nor iDisguise was found. The /troll control and /troll duck commands have been disabled.");
			ServiceLocator.provideService(NullDisguiseHelper.class);
		}
		
		if (manager.getPlugin("ProtocolLib") != null) {
			printInfo(ChatColor.GREEN + "[TrollCommands++] Enabling support for ProtocolLib.");
			ServiceLocator.provideService(ProtocolLibFakeEntityHelper.class);
			ServiceLocator.provideService(ProtocolLibFakeBlockHelper.class);
		} else {
			printWarning(ChatColor.RED + "[TrollCommands++] ProtocolLib was not found. The /troll foolsgold, /troll nightmare, and /troll rewind commands have been disabled.");
			ServiceLocator.provideService(NullFakeEntityHelper.class);
			ServiceLocator.provideService(NullFakeBlockHelper.class);
		}
		
		if (manager.getPlugin("PowerNBT") != null) {
			printInfo(ChatColor.GREEN + "[TrollCommands++] Enabling support for PowerNBT.");
			ServiceLocator.provideService(PowerNBTHelper.class);
		} else {
			printWarning(ChatColor.RED + "[TrollCommands++] PowerNBT was not found. The /troll attachcommand command has been disabled.");
			ServiceLocator.provideService(NullNBTHelper.class);
		}
		
		ConfigUtil.fillRegistry(YamlUtil.getOrLoadDefaults(getDataFolder().getAbsolutePath() + FileUtil.DIRECTORY_SEPARATOR_CHAR + "config.yml", "config.yml", true));
		
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
	}
	
	public void onEnable() {
		super.onEnable();
		
		Config.globalThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat(getName() + "-%d").build());
		mainThreadPool = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat(getName() + "-main-%d").build());
		
		mainThreadPool.submit(new Runnable() {
			public void run() {
				try {
					metrics = new Metrics(ServiceLocator.getService(JavaPlugin.class));
				} catch (Exception ex) {
					printInfo(ChatColor.YELLOW + "[TrollCommands++] WARNING: Connection to metrics server could not be established. This affects nothing for server owners, but it does make me sad :(");
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
			}
		});
		
		IRegistry<String> configRegistry = ConfigUtil.getRegistry();
		String queueType = configRegistry.getRegister("queueType", String.class);
		if (queueType.equalsIgnoreCase("default") || queueType.equalsIgnoreCase("bungee") || queueType.equalsIgnoreCase("bungeecord")) {
			// Do nothing, as it's the default
			printInfo(ChatColor.GREEN + "[TrollCommands++] Enabling support for BungeeCord.");
		} else if (queueType.equalsIgnoreCase("rabbit") || queueType.equalsIgnoreCase("rabbitmq")) {
			List<IMessageHandler> services = ServiceLocator.removeServices(IMessageHandler.class);
			for (IMessageHandler handler : services) {
				handler.destroy();
			}
			ServiceLocator.provideService(new RabbitMessageHandler(configRegistry.getRegister("rabbitIp", String.class), configRegistry.getRegister("rabbitPort", Number.class).intValue(), configRegistry.getRegister("rabbitUser", String.class), configRegistry.getRegister("rabbitPass", String.class)));
			printInfo(ChatColor.GREEN + "[TrollCommands++] Enabling support for RabbitMQ.");
		} else {
			printWarning("Config \"queueType\" does not match expected values. Using Bungeecord default.");
		}
		
		IMessageHandler messageHandler = ServiceLocator.getService(IMessageHandler.class);
		messageHandler.createChannel("Troll");
		
		numMessages = ServiceLocator.getService(IMessageHandler.class).addMessagesFromPackage("me.egg82.tcpp.messages");
		numCommands = ServiceLocator.getService(CommandHandler.class).addCommandsFromPackage("me.egg82.tcpp.commands", BukkitReflectUtil.getCommandMapFromPackage("me.egg82.tcpp.commands", false, null, "Command"), false);
		numCommands += ReflectUtil.getClasses(PluginCommand.class, "me.egg82.tcpp.commands.internal", false, false, false).size();
		numEvents = ServiceLocator.getService(EventListener.class).addEventsFromPackage("me.egg82.tcpp.events");
		numPermissions = ServiceLocator.getService(PermissionsManager.class).addPermissionsFromClass(PermissionsType.class);
		numTicks = ServiceLocator.getService(TickHandler.class).addTicksFromPackage("me.egg82.tcpp.ticks");
		
		enableMessage();
		
		mainThreadPool.scheduleAtFixedRate(checkUpdate, 0L, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
		mainThreadPool.scheduleAtFixedRate(checkExceptionLimitReached, 0L, 60 * 60 * 1000, TimeUnit.MILLISECONDS);
		
		mainThreadPool.submit(new Runnable() {
			public void run() {
				getTagger();
			}
		});
	}
	public void onDisable() {
		super.onDisable();
		
		mainThreadPool.shutdownNow();
		Config.globalThreadPool.shutdownNow();
		
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
		
		BukkitReflectUtil.clearAll();
		disableMessage();
	}
	
	//private
	private Runnable checkUpdate = new Runnable() {
		public void run() {
			Updater updater = new Updater(ServiceLocator.getService(JavaPlugin.class), 100359, getFile(), UpdateType.NO_DOWNLOAD, false);
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
				
				printWarning(ChatColor.GREEN + "--== " + ChatColor.YELLOW + "TrollCommands++ UPDATE AVAILABLE (Latest: " + latestVersion + " Current: " + currentVersion + ") " + ChatColor.GREEN + " ==--");
			}
		}
	};
	
	private Runnable checkExceptionLimitReached = new Runnable() {
		public void run() {
			if (exceptionHandler.isLimitReached()) {
				IExceptionHandler oldExceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
				ServiceLocator.removeServices(IExceptionHandler.class);
				
				ServiceLocator.provideService(GameAnalyticsExceptionHandler.class, false);
				exceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
				oldExceptionHandler.disconnect();
				exceptionHandler.connect(new GameAnalyticsBuilder("250e5c508c3dd844ed1f8bd2a449d1a6", "dfb50b06e598e7a7ad9b3c84f7b118c12800ffce", version, userId));
				exceptionHandler.setUnsentExceptions(oldExceptionHandler.getUnsentExceptions());
				exceptionHandler.setUnsentLogs(oldExceptionHandler.getUnsentLogs());
			}
		}
	};
	
	private void enableMessage() {
		printInfo(ChatColor.AQUA + "  _______        _ _  _____                                          _                 ");
		printInfo(ChatColor.AQUA + " |__   __|      | | |/ ____|                                        | |      _     _   ");
		printInfo(ChatColor.AQUA + "    | |_ __ ___ | | | |     ___  _ __ ___  _ __ ___   __ _ _ __   __| |___ _| |_ _| |_ ");
		printInfo(ChatColor.AQUA + "    | | '__/ _ \\| | | |    / _ \\| '_ ` _ \\| '_ ` _ \\ / _` | '_ \\ / _` / __|_   _|_   _|");
		printInfo(ChatColor.AQUA + "    | | | | (_) | | | |___| (_) | | | | | | | | | | | (_| | | | | (_| \\__ \\ |_|   |_|  ");
		printInfo(ChatColor.AQUA + "    |_|_|  \\___/|_|_|\\_____\\___/|_| |_| |_|_| |_| |_|\\__,_|_| |_|\\__,_|___/            ");
		printInfo(ChatColor.GREEN + "[Version " + getDescription().getVersion() + "] " + ChatColor.RED + numCommands + " commands " + ChatColor.LIGHT_PURPLE + numEvents + " events " + ChatColor.WHITE + numPermissions + " permissions " + ChatColor.YELLOW + numTicks + " tick handlers " + ChatColor.BLUE + numMessages + " message handlers");
		printInfo(ChatColor.WHITE + "[TrollCommands++] " + ChatColor.GRAY + "Attempting to load compatibility with Bukkit version " + ((InitRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister(BukkitInitType.GAME_VERSION));
	}
	private void disableMessage() {
		printInfo(ChatColor.GREEN + "--== " + ChatColor.LIGHT_PURPLE + "TrollCommands++ Disabled" + ChatColor.GREEN + " ==--");
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
		languageRegistry.setRegister(LanguageType.NOT_MOB, ChatColor.RED + "The entity you have selected is not a mob!");
		languageRegistry.setRegister(LanguageType.EMPOWERED, "The entity you have selected is now empowered!");
		languageRegistry.setRegister(LanguageType.DISEMPOWERED, "The entity you have selected is now disempowered!");
		languageRegistry.setRegister(LanguageType.HYDRA_ENABLED, "The entity you have selected is now hydra-powered!");
		languageRegistry.setRegister(LanguageType.HYDRA_DISABLED, "The entity you have selected (and its related entities) are no longer hydra-powered!");
		languageRegistry.setRegister(LanguageType.INVALID_VERSION, ChatColor.RED + "This command has been disabled because this version of Minecraft doesn't support it.");
		languageRegistry.setRegister(LanguageType.INVALID_TYPE, ChatColor.RED + "Searched type is invalid or was not found.");
		languageRegistry.setRegister(LanguageType.INVALID_COMMAND, ChatColor.RED + "Command is invalid.");
		languageRegistry.setRegister(LanguageType.INVALID_ITEM, ChatColor.RED + "Item is invalid.");
	}
	
	private void getTagger() {
		exceptionHandler.addThread(Thread.currentThread());
		
		try {
			POSModel model = new POSModel(new URL("http://opennlp.sourceforge.net/models-1.5/en-pos-maxent.bin"));
			ServiceLocator.provideService(new POSTaggerME(model));
			System.gc();
		} catch (Exception ex) {
			try {
				POSModel model = new POSModel(new URL("http://opennlp.sourceforge.net/models-1.5/en-pos-perceptron.bin"));
				ServiceLocator.provideService(new POSTaggerME(model));
				System.gc();
			} catch (Exception ex2) {
				mainThreadPool.schedule(new Runnable() {
					public void run() {
						getTagger();
					}
				}, 10 * 1000L, TimeUnit.MILLISECONDS);
			}
		}
		
		exceptionHandler.removeThread(Thread.currentThread());
	}
	
	private void writeProperties() {
		File propertiesFile = new File(Bukkit.getWorldContainer(), "server.properties");
		String path = propertiesFile.getAbsolutePath();
		
		if (!FileUtil.pathExists(path) || !FileUtil.pathIsFile(path)) {
			return;
		}
		
		try {
			FileUtil.open(path);
			
			String[] lines = toString(FileUtil.read(path, 0L), Charset.forName("UTF-8")).replaceAll("\r", "").split("\n");
			boolean found = false;
			for (int i = 0; i < lines.length; i++) {
				if (lines[i].trim().startsWith("server-id=")) {
					found = true;
					lines[i] = "server-id=" + userId;
				}
			}
			if (!found) {
				ArrayList<String> temp = new ArrayList<String>(Arrays.asList(lines));
				temp.add("server-id=" + userId);
				lines = temp.toArray(new String[0]);
			}
			
			FileUtil.erase(path);
			FileUtil.write(path, toBytes(String.join(FileUtil.LINE_SEPARATOR, lines), Charset.forName("UTF-8")), 0L);
			FileUtil.close(path);
		} catch (Exception ex) {
			
		}
	}
	
	private byte[] toBytes(String input, Charset enc) {
		return input.getBytes(enc);
	}
	private String toString(byte[] input, Charset enc) {
		return new String(input, enc);
	}
}