package me.egg82.tcpp;

import java.util.logging.Level;

import net.md_5.bungee.api.ChatColor;
import ninja.egg82.bungeecord.BasePlugin;
import ninja.egg82.bungeecord.handlers.CommandHandler;
import ninja.egg82.bungeecord.handlers.EventListener;
import ninja.egg82.bungeecord.handlers.IMessageHandler;
import ninja.egg82.bungeecord.utils.BungeeReflectUtil;
import ninja.egg82.exceptionHandlers.GameAnalyticsExceptionHandler;
import ninja.egg82.exceptionHandlers.IExceptionHandler;
import ninja.egg82.exceptionHandlers.RollbarExceptionHandler;
import ninja.egg82.exceptionHandlers.builders.GameAnalyticsBuilder;
import ninja.egg82.exceptionHandlers.builders.RollbarBuilder;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.utils.ThreadUtil;

public class TrollCommandsPlusPlus extends BasePlugin {
	//vars
	private int numCommands = 0;
	private int numEvents = 0;
	private int numMessages = 0;
	
	private IExceptionHandler exceptionHandler = null;
	private String version = null;
	
	//constructor
	public TrollCommandsPlusPlus() {
		super();
	}
	
	//public
	public void onLoad() {
		super.onLoad();
		
		version = getDescription().getVersion();
		
		getLogger().setLevel(Level.WARNING);
		IExceptionHandler oldExceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
		ServiceLocator.removeServices(IExceptionHandler.class);
		
		ServiceLocator.provideService(RollbarExceptionHandler.class, false);
		exceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
		oldExceptionHandler.disconnect();
		exceptionHandler.connect(new RollbarBuilder("78062d4e18074560850d4d8e0805b564", "production", version, getServerId()), "TrollCommandsPlusPlus");
		exceptionHandler.setUnsentExceptions(oldExceptionHandler.getUnsentExceptions());
		exceptionHandler.setUnsentLogs(oldExceptionHandler.getUnsentLogs());
		
		BungeeReflectUtil.addServicesFromPackage("me.egg82.tcpp.registries");
	}
	
	public void onEnable() {
		super.onEnable();
		
		IMessageHandler messageHandler = ServiceLocator.getService(IMessageHandler.class);
		messageHandler.createChannel("Troll");
		
		numCommands = ServiceLocator.getService(CommandHandler.class).addCommandsFromPackage("me.egg82.tcpp.commands", BungeeReflectUtil.getCommandMapFromPackage("me.egg82.tcpp.commands", false, null, "Command"), false);
		numEvents = ServiceLocator.getService(EventListener.class).addEventsFromPackage("me.egg82.tcpp.events");
		numMessages = ServiceLocator.getService(IMessageHandler.class).addMessagesFromPackage("me.egg82.tcpp.messages");
		
		enableMessage();
		
		ThreadUtil.rename(getDescription().getName());
		ThreadUtil.scheduleAtFixedRate(checkExceptionLimitReached, 0L, 60L * 60L * 1000L);
	}
	public void onDisable() {
		super.onDisable();
		
		ThreadUtil.shutdown(1000L);
		
		BungeeReflectUtil.clearAll();
		disableMessage();
	}
	
	//private
	private Runnable checkExceptionLimitReached = new Runnable() {
		public void run() {
			if (exceptionHandler.isLimitReached()) {
				IExceptionHandler oldExceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
				ServiceLocator.removeServices(IExceptionHandler.class);
				
				ServiceLocator.provideService(GameAnalyticsExceptionHandler.class, false);
				exceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
				oldExceptionHandler.disconnect();
				exceptionHandler.connect(new GameAnalyticsBuilder("250e5c508c3dd844ed1f8bd2a449d1a6", "dfb50b06e598e7a7ad9b3c84f7b118c12800ffce", version, getServerId()), getDescription().getName());
				exceptionHandler.setUnsentExceptions(oldExceptionHandler.getUnsentExceptions());
				exceptionHandler.setUnsentLogs(oldExceptionHandler.getUnsentLogs());
			}
		}
	};
	
	private void enableMessage() {
		printInfo(ChatColor.AQUA + "TrollCommands++ enabled.");
		printInfo(ChatColor.GREEN + "[Version " + getDescription().getVersion() + "] " + ChatColor.RED + numCommands + " commands " + ChatColor.LIGHT_PURPLE + numEvents + " events " + ChatColor.BLUE + numMessages + " message handlers");
	}
	private void disableMessage() {
		printInfo(ChatColor.GREEN + "--== " + ChatColor.LIGHT_PURPLE + "TrollCommands++ Disabled" + ChatColor.GREEN + " ==--");
	}
}
