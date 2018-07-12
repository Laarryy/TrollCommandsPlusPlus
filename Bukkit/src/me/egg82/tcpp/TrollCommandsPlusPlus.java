package me.egg82.tcpp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.registries.CommandRegistry;
import me.egg82.tcpp.registries.KeywordRegistry;
import me.egg82.tcpp.rollback.reflection.CoreProtectRollbackHelper;
import me.egg82.tcpp.rollback.reflection.NullRollbackHelper;
import me.egg82.tcpp.services.databases.CommandSearchDatabase;
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
import ninja.egg82.bukkit.BasePlugin;
import ninja.egg82.bukkit.processors.CommandProcessor;
import ninja.egg82.bukkit.processors.EventProcessor;
import ninja.egg82.bukkit.services.ConfigRegistry;
import ninja.egg82.bukkit.utils.VersionUtil;
import ninja.egg82.bukkit.utils.YamlUtil;
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
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.permissions.reflection.LuckPermissionsHelper;
import ninja.egg82.permissions.reflection.NullPermissionsHelper;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.plugin.messaging.IMessageHandler;
import ninja.egg82.plugin.utils.PluginReflectUtil;
import ninja.egg82.protocol.reflection.NullFakeBlockHelper;
import ninja.egg82.protocol.reflection.NullFakeEntityHelper;
import ninja.egg82.protocol.reflection.ProtocolLibFakeBlockHelper;
import ninja.egg82.protocol.reflection.ProtocolLibFakeEntityHelper;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.utils.FileUtil;
import ninja.egg82.utils.ReflectUtil;
import ninja.egg82.utils.StringUtil;
import ninja.egg82.utils.ThreadUtil;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class TrollCommandsPlusPlus extends BasePlugin {
	//vars
	private Metrics metrics = null;
	
	private int numMessages = 0;
	private int numCommands = 0;
	private int numEvents = 0;
	private int numTicks = 0;
	
	private IExceptionHandler exceptionHandler = null;
	private String version = getDescription().getVersion();
	
	//constructor
	public TrollCommandsPlusPlus() {
		super();
		
		getLogger().setLevel(Level.WARNING);
		IExceptionHandler oldExceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
		ServiceLocator.removeServices(IExceptionHandler.class);
		
		ServiceLocator.provideService(RollbarExceptionHandler.class, false);
		exceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
		oldExceptionHandler.disconnect();
		exceptionHandler.connect(new RollbarBuilder("78062d4e18074560850d4d8e0805b564", "production", version, getServerId()), "TrollCommandsPlusPlus");
		exceptionHandler.setUnsentExceptions(oldExceptionHandler.getUnsentExceptions());
		exceptionHandler.setUnsentLogs(oldExceptionHandler.getUnsentLogs());
	}
	
	//public
	public void onLoad() {
		super.onLoad();
		
		PluginReflectUtil.addServicesFromPackage("me.egg82.tcpp.registries", true);
		PluginReflectUtil.addServicesFromPackage("me.egg82.tcpp.lists", true);
		
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
		
		if (manager.getPlugin("LuckPerms") != null) {
			printInfo(ChatColor.GREEN + "[TrollCommands++] Enabling support for LuckPerms.");
			ServiceLocator.provideService(LuckPermissionsHelper.class);
		} else {
			printWarning(ChatColor.YELLOW + "[TrollCommands++] LP was not found. Using default Bukkit permissions.");
			ServiceLocator.provideService(NullPermissionsHelper.class);
		}
		
		if (manager.getPlugin("CoreProtect") != null) {
			printInfo(ChatColor.GREEN + "[TrollCommands++] Enabling support for CoreProtect.");
			ServiceLocator.provideService(CoreProtectRollbackHelper.class);
		} else {
			printWarning(ChatColor.YELLOW + "[TrollCommands++] Neither BlocksHub nor CoreProtect were found. Not logging any block changes.");
			ServiceLocator.provideService(NullRollbackHelper.class);
		}
		
		ServiceLocator.getService(ConfigRegistry.class).load(YamlUtil.getOrLoadDefaults(getDataFolder().getAbsolutePath() + FileUtil.DIRECTORY_SEPARATOR_CHAR + "config.yml", "config.yml", true));
		
		ServiceLocator.provideService(ControlHelper.class);
		ServiceLocator.provideService(DisplayHelper.class);
		ServiceLocator.provideService(FoolsGoldHelper.class);
		ServiceLocator.provideService(VegetableHelper.class);
		ServiceLocator.provideService(WhoAmIHelper.class);
		ServiceLocator.provideService(WorldHoleHelper.class);
		ServiceLocator.provideService(MetricsHelper.class);
		ServiceLocator.provideService(CommandSearchDatabase.class, false);
		
		populateCommandDatabase();
	}
	
	public void onEnable() {
		super.onEnable();
		
		Loaders.loadMessaging();
		
		numCommands = ServiceLocator.getService(CommandProcessor.class).addHandlersFromPackage("me.egg82.tcpp.commands", PluginReflectUtil.getCommandMapFromPackage("me.egg82.tcpp.commands", false, null, "Command"), false);
		numCommands += ReflectUtil.getClasses(CommandHandler.class, "me.egg82.tcpp.commands.internal", false, false, false).size();
		ServiceLocator.getService(CommandProcessor.class).addAliases("gtroll",  "gtr");
		ServiceLocator.getService(CommandProcessor.class).addAliases("troll",  "tr");
		numEvents = ServiceLocator.getService(EventProcessor.class).addHandlersFromPackage("me.egg82.tcpp.events");
		numMessages = ServiceLocator.getService(IMessageHandler.class).addHandlersFromPackage("me.egg82.tcpp.messages");
		numTicks = PluginReflectUtil.addServicesFromPackage("me.egg82.tcpp.ticks", false);
		
		ThreadUtil.rename(getName());
		ThreadUtil.submit(new Runnable() {
			public void run() {
				try {
					metrics = new Metrics(ServiceLocator.getService(JavaPlugin.class));
				} catch (Exception ex) {
					printInfo(ChatColor.YELLOW + "[TrollCommands++] WARNING: Connection to metrics server could not be established. This affects nothing for server owners, but it does make me sad :(");
				}
				
				if (metrics != null) {
					metrics.addCustomChart(new Metrics.AdvancedPie("commands", () -> {
						IVariableRegistry<String> commandRegistry = ServiceLocator.getService(CommandRegistry.class);
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
		ThreadUtil.schedule(checkUpdate, 24L * 60L * 60L * 1000L);
		ThreadUtil.schedule(checkExceptionLimitReached, 60L * 60L * 1000L);
		
		ThreadUtil.submit(new Runnable() {
			public void run() {
				getTagger();
			}
		});
		
		enableMessage();
	}
	public void onDisable() {
		super.onDisable();
		
		ThreadUtil.shutdown(1000L);
		
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
		
		List<IMessageHandler> services = ServiceLocator.removeServices(IMessageHandler.class);
		for (IMessageHandler handler : services) {
			try {
				handler.close();
			} catch (Exception ex) {
				
			}
		}
		
		ServiceLocator.getService(CommandProcessor.class).clear();
		ServiceLocator.getService(EventProcessor.class).clear();
		
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
			
			ThreadUtil.schedule(checkUpdate, 24L * 60L * 60L * 1000L);
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
				exceptionHandler.connect(new GameAnalyticsBuilder("250e5c508c3dd844ed1f8bd2a449d1a6", "dfb50b06e598e7a7ad9b3c84f7b118c12800ffce", version, getServerId()), getName());
				exceptionHandler.setUnsentExceptions(oldExceptionHandler.getUnsentExceptions());
				exceptionHandler.setUnsentLogs(oldExceptionHandler.getUnsentLogs());
			}
			
			ThreadUtil.schedule(checkExceptionLimitReached, 60L * 60L * 1000L);
		}
	};
	
	private void enableMessage() {
		printInfo(ChatColor.AQUA + "  _______        _ _  _____                                          _                 ");
		printInfo(ChatColor.AQUA + " |__   __|      | | |/ ____|                                        | |      _     _   ");
		printInfo(ChatColor.AQUA + "    | |_ __ ___ | | | |     ___  _ __ ___  _ __ ___   __ _ _ __   __| |___ _| |_ _| |_ ");
		printInfo(ChatColor.AQUA + "    | | '__/ _ \\| | | |    / _ \\| '_ ` _ \\| '_ ` _ \\ / _` | '_ \\ / _` / __|_   _|_   _|");
		printInfo(ChatColor.AQUA + "    | | | | (_) | | | |___| (_) | | | | | | | | | | | (_| | | | | (_| \\__ \\ |_|   |_|  ");
		printInfo(ChatColor.AQUA + "    |_|_|  \\___/|_|_|\\_____\\___/|_| |_| |_|_| |_| |_|\\__,_|_| |_|\\__,_|___/            ");
		printInfo(ChatColor.GREEN + "[Version " + getDescription().getVersion() + "] " + ChatColor.RED + numCommands + " commands " + ChatColor.LIGHT_PURPLE + numEvents + " events " + ChatColor.YELLOW + numTicks + " tick handlers " + ChatColor.BLUE + numMessages + " message handlers");
		printInfo(ChatColor.WHITE + "[TrollCommands++] " + ChatColor.GRAY + "Attempting to load compatibility with Bukkit version " + getGameVersion());
	}
	private void disableMessage() {
		printInfo(ChatColor.GREEN + "--== " + ChatColor.LIGHT_PURPLE + "TrollCommands++ Disabled" + ChatColor.GREEN + " ==--");
	}
	
	private void populateCommandDatabase() {
		LanguageDatabase commandNameDatabase = ServiceLocator.getService(CommandSearchDatabase.class);
		IVariableRegistry<String> keywordRegistry = ServiceLocator.getService(KeywordRegistry.class);
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
	
	private void getTagger() {
		try {
			POSModel model = new POSModel(new URL("http://opennlp.sourceforge.net/models-1.5/en-pos-maxent.bin"));
			ServiceLocator.provideService(new POSTaggerME(model));
		} catch (Exception ex) {
			exceptionHandler.silentException(ex);
			ex.printStackTrace();
			try {
				POSModel model = new POSModel(new URL("http://opennlp.sourceforge.net/models-1.5/en-pos-perceptron.bin"));
				ServiceLocator.provideService(new POSTaggerME(model));
			} catch (Exception ex2) {
				exceptionHandler.silentException(ex2);
				ex2.printStackTrace();
				ThreadUtil.schedule(new Runnable() {
					public void run() {
						getTagger();
					}
				}, 10L * 1000L);
			}
		}
	}
}