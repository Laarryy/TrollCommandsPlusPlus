package me.egg82.tcpp.bungee;

import ninja.egg82.bungeecord.messaging.EnhancedBungeeMessageHandler;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.messaging.IMessageHandler;

public class Loaders {
	//vars
	
	//constructor
	public Loaders() {
		
	}
	
	//public
	@SuppressWarnings("resource")
	public static void loadMessaging(String pluginName, String serverName, String serverId) {
		if (pluginName == null) {
			throw new IllegalArgumentException("pluginName cannot be null.");
		}
		if (serverId == null) {
			throw new IllegalArgumentException("serverId cannot be null.");
		}
		if (serverId.isEmpty()) {
			throw new IllegalArgumentException("serverId cannot be empty.");
		}
		
		if (serverName == null || serverName.isEmpty() || serverName.equalsIgnoreCase("unknown") || serverName.equalsIgnoreCase("unconfigured") || serverName.equalsIgnoreCase("unnamed") || serverName.equalsIgnoreCase("default")) {
			serverName = null;
		}
		
		if (!ServiceLocator.hasService(EnhancedBungeeMessageHandler.class)) {
			ServiceLocator.provideService(new EnhancedBungeeMessageHandler(pluginName, (serverName != null) ? serverName : serverId));
		}
		
		ServiceLocator.getService(IMessageHandler.class).createChannel("troll");
	}
	
	//private
	
}
