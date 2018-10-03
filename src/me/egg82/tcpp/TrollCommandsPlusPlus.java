package me.egg82.tcpp;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.databases.CommandSearchDatabase;
import me.egg82.tcpp.reflection.block.ProtocolLibFakeBlockHelper;
import me.egg82.tcpp.reflection.entity.NullFakeLivingEntityHelper;
import me.egg82.tcpp.reflection.entity.ProtocolLibFakeLivingEntityHelper;
import me.egg82.tcpp.reflection.rollback.CoreProtectRollbackHelper;
import me.egg82.tcpp.reflection.rollback.NullRollbackHelper;
import me.egg82.tcpp.registries.CommandRegistry;
import me.egg82.tcpp.registries.KeywordRegistry;
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
import ninja.egg82.analytics.exceptions.GameAnalyticsExceptionHandler;
import ninja.egg82.analytics.exceptions.IExceptionHandler;
import ninja.egg82.analytics.exceptions.RollbarExceptionHandler;
import ninja.egg82.bukkit.BasePlugin;
import ninja.egg82.bukkit.processors.CommandProcessor;
import ninja.egg82.bukkit.processors.EventProcessor;
import ninja.egg82.disguise.reflection.DisguiseHelper;
import ninja.egg82.disguise.reflection.LibsDisguisesHelper;
import ninja.egg82.disguise.reflection.NullDisguiseHelper;
import ninja.egg82.nbt.reflection.NullNBTHelper;
import ninja.egg82.nbt.reflection.PowerNBTHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.permissions.reflection.LuckPermissionsHelper;
import ninja.egg82.permissions.reflection.NullPermissionsHelper;
import ninja.egg82.plugin.enums.SenderType;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.plugin.messaging.IMessageHandler;
import ninja.egg82.plugin.utils.DirectoryUtil;
import ninja.egg82.plugin.utils.PluginReflectUtil;
import ninja.egg82.plugin.utils.VersionUtil;
import ninja.egg82.sql.LanguageDatabase;
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
		
		exceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
		getLogger().setLevel(Level.WARNING);
	}
	
	//public
	public void onLoad() {
		super.onLoad();
		
        if (!Bukkit.getName().equals("Paper") && !Bukkit.getName().equals("PaperSpigot")) {
            printWarning(ChatColor.AQUA + "============================================");
            printWarning("Please note that TC++ works better with Paper!");
            printWarning("https://whypaper.emc.gs/");
            printWarning(ChatColor.AQUA + "============================================");
        }

		PluginReflectUtil.addServicesFromPackage("me.egg82.tcpp.databases", true);
		PluginReflectUtil.addServicesFromPackage("me.egg82.tcpp.registries", true);
		PluginReflectUtil.addServicesFromPackage("me.egg82.tcpp.lists", true);
		
		PluginManager manager = getServer().getPluginManager();
		
		if (manager.getPlugin("LibsDisguises") != null) {
			printInfo(ChatColor.GREEN + "Enabling support for LibsDisguises.");
			ServiceLocator.provideService(LibsDisguisesHelper.class);
		} else if (manager.getPlugin("iDisguise") != null) {
			printInfo(ChatColor.GREEN + "Enabling support for iDisguise.");
			ServiceLocator.provideService(DisguiseHelper.class);
		} else {
			printWarning(ChatColor.RED + "Neither LibsDisguises nor iDisguise was found. The /troll control and /troll duck commands have been disabled.");
			ServiceLocator.provideService(NullDisguiseHelper.class);
		}
		
		if (manager.getPlugin("ProtocolLib") != null) {
			printInfo(ChatColor.GREEN + "Enabling support for ProtocolLib.");
			ServiceLocator.provideService(ProtocolLibFakeBlockHelper.class);
			ServiceLocator.provideService(ProtocolLibFakeLivingEntityHelper.class);
		} else {
			printWarning(ChatColor.YELLOW + "ProtocolLib was not found. Reverting to basic protocol support. The /troll nightmare command has been disabled.");
			reflect(getGameVersion(), "me.egg82.tcpp.reflection.block");
			ServiceLocator.provideService(NullFakeLivingEntityHelper.class);
		}
		
		if (manager.getPlugin("PowerNBT") != null) {
			printInfo(ChatColor.GREEN + "Enabling support for PowerNBT.");
			ServiceLocator.provideService(PowerNBTHelper.class);
		} else {
			printWarning(ChatColor.RED + "PowerNBT was not found. The /troll attachcommand command has been disabled.");
			ServiceLocator.provideService(NullNBTHelper.class);
		}
		
		if (manager.getPlugin("LuckPerms") != null) {
			printInfo(ChatColor.GREEN + "Enabling support for LuckPerms.");
			ServiceLocator.provideService(LuckPermissionsHelper.class);
		} else {
			printWarning(ChatColor.YELLOW + "LP was not found. Using default Bukkit permissions.");
			ServiceLocator.provideService(NullPermissionsHelper.class);
		}
		
		if (manager.getPlugin("CoreProtect") != null) {
			printInfo(ChatColor.GREEN + "Enabling support for CoreProtect.");
			ServiceLocator.provideService(CoreProtectRollbackHelper.class);
		} else {
			printWarning(ChatColor.YELLOW + "CoreProtect was not found. Not logging any block changes.");
			ServiceLocator.provideService(NullRollbackHelper.class);
		}
		
		ConfigLoader.getConfig("config.yml", "config.yml");
		
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
		
		swapExceptionHandlers(new RollbarExceptionHandler("78062d4e18074560850d4d8e0805b564", "production", version, getServerId(), getName()));
		
		List<IMessageHandler> services = ServiceLocator.removeServices(IMessageHandler.class);
		for (IMessageHandler handler : services) {
			try {
				handler.close();
			} catch (Exception ex) {
				
			}
		}
		
		Loaders.loadMessaging(getDescription().getName(), Bukkit.getServerName(), getServerId(), SenderType.SERVER);
		
		numCommands = ServiceLocator.getService(CommandProcessor.class).addHandlersFromPackage("me.egg82.tcpp.commands", PluginReflectUtil.getCommandMapFromPackage("me.egg82.tcpp.commands", false, null, "Command"), false);
		numCommands += ReflectUtil.getClasses(CommandHandler.class, "me.egg82.tcpp.commands.internal", false, false, false).size();
		ServiceLocator.getService(CommandProcessor.class).addAliases("gtroll",  "gtr");
		ServiceLocator.getService(CommandProcessor.class).addAliases("troll",  "tr");
		numEvents = ServiceLocator.getService(EventProcessor.class).addHandlersFromPackage("me.egg82.tcpp.events");
		if (ServiceLocator.getService(IMessageHandler.class) != null) {
			numMessages = ServiceLocator.getService(IMessageHandler.class).addHandlersFromPackage("me.egg82.tcpp.messages");
		}
		numTicks = PluginReflectUtil.addServicesFromPackage("me.egg82.tcpp.ticks", false);
		
		ThreadUtil.rename(getName());
		ThreadUtil.submit(new Runnable() {
			public void run() {
				try {
					metrics = new Metrics(ServiceLocator.getService(JavaPlugin.class));
				} catch (Exception ex) {
					printWarning("Connection to metrics server could not be established. This affects nothing for server owners, but it does make me sad :(");
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
		if (exceptionHandler.hasLimit()) {
			ThreadUtil.schedule(checkExceptionLimitReached, 2L * 60L * 1000L);
		}
		
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
		
		exceptionHandler.close();
		
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
						ThreadUtil.schedule(checkUpdate, 24L * 60L * 60L * 1000L);
						return;
					}
				}
				
				printWarning(ChatColor.GREEN + "--== " + ChatColor.YELLOW + "UPDATE AVAILABLE (Latest: " + latestVersion + " Current: " + currentVersion + ") " + ChatColor.GREEN + " ==--");
			}
			
			ThreadUtil.schedule(checkUpdate, 24L * 60L * 60L * 1000L);
		}
	};
	
	private Runnable checkExceptionLimitReached = new Runnable() {
		public void run() {
			if (exceptionHandler.isLimitReached()) {
				swapExceptionHandlers(new GameAnalyticsExceptionHandler("250e5c508c3dd844ed1f8bd2a449d1a6", "dfb50b06e598e7a7ad9b3c84f7b118c12800ffce", version, getServerId(), getName()));
			}
			
			if (exceptionHandler.hasLimit()) {
				ThreadUtil.schedule(checkExceptionLimitReached, 10L * 60L * 1000L);
			}
		}
	};
	
	private void swapExceptionHandlers(IExceptionHandler newHandler) {
		List<IExceptionHandler> oldHandlers = ServiceLocator.removeServices(IExceptionHandler.class);
		
		exceptionHandler = newHandler;
		ServiceLocator.provideService(exceptionHandler);
		
		Logger logger = getLogger();
		if (exceptionHandler instanceof Handler) {
			logger.addHandler((Handler) exceptionHandler);
		}
		
		for (IExceptionHandler handler : oldHandlers) {
			if (handler instanceof Handler) {
				logger.removeHandler((Handler) handler);
			}
			
			handler.close();
			exceptionHandler.addLogs(handler.getUnsentLogs());
		}
	}
	
	private void enableMessage() {
		printInfo(ChatColor.GREEN + "Enabled.");
		printInfo(ChatColor.AQUA + "[Version " + getDescription().getVersion() + "] " + ChatColor.DARK_GREEN + numCommands + " commands " + ChatColor.LIGHT_PURPLE + numEvents + " events " + ChatColor.GOLD + numTicks + " tick handlers " + ChatColor.BLUE + numMessages + " message handlers");
		printInfo("Attempting to load compatibility with Bukkit version " + getGameVersion());
	}
	private void disableMessage() {
		printInfo(ChatColor.RED + "Disabled");
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
		if (getDataFolder().exists() && !getDataFolder().isDirectory()) {
			getDataFolder().delete();
		}
		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		
		File maxent = new File(getDataFolder(), "en-pos-maxent.bin");
		File perceptron = new File(getDataFolder(), "en-pos-perceptron.bin");
		
		try {
			if (maxent.exists() && maxent.isDirectory()) {
				DirectoryUtil.delete(maxent);
			}
			
			if (!maxent.exists()) {
				URL url = new URL("http://opennlp.sourceforge.net/models-1.5/en-pos-maxent.bin");
				try (InputStream in = url.openStream()) {
					Files.copy(in, maxent.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
			}
			
			POSModel model = new POSModel(maxent);
			ServiceLocator.provideService(new POSTaggerME(model));
		} catch (Exception ex) {
			try {
				if (perceptron.exists() && perceptron.isDirectory()) {
					DirectoryUtil.delete(perceptron);
				}
				
				if (!perceptron.exists()) {
					URL url = new URL("http://opennlp.sourceforge.net/models-1.5/en-pos-perceptron.bin");
					try (InputStream in = url.openStream()) {
						Files.copy(in, perceptron.toPath(), StandardCopyOption.REPLACE_EXISTING);
					}
				}
				
				POSModel model = new POSModel(perceptron);
				ServiceLocator.provideService(new POSTaggerME(model));
			} catch (Exception ex2) {
				printWarning("Speech model data coule not be loaded. /troll moist will be disabled until it can be. Retrying in 10 seconds..");
				
				ThreadUtil.schedule(new Runnable() {
					public void run() {
						getTagger();
					}
				}, 10L * 1000L);
			}
		}
	}
}