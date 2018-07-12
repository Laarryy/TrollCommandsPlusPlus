package me.egg82.tcpp;

import ninja.egg82.bungeecord.BasePlugin;
import ninja.egg82.bungeecord.messaging.EnhancedBungeeMessageHandler;
import ninja.egg82.bungeecord.services.ConfigRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.enums.SenderType;
import ninja.egg82.plugin.messaging.IMessageHandler;
import ninja.egg82.plugin.messaging.RabbitMessageHandler;

public class Loaders {
	//vars
	
	//constructor
	public Loaders() {
		
	}
	
	//public
	@SuppressWarnings("resource")
	public static void loadMessaging() {
		BasePlugin plugin = ServiceLocator.getService(BasePlugin.class);
		IVariableRegistry<String> configRegistry = ServiceLocator.getService(ConfigRegistry.class);
		
		if (configRegistry.hasRegister("messaging.type")) {
			String type = configRegistry.getRegister("messaging.type", String.class);
			
			if (type.equalsIgnoreCase("default") || type.equalsIgnoreCase("bungee") || type.equalsIgnoreCase("bungeecord")) {
				if (!ServiceLocator.hasService(EnhancedBungeeMessageHandler.class)) {
					ServiceLocator.provideService(new EnhancedBungeeMessageHandler(plugin.getDescription().getName(), plugin.getServerId()));
				}
			} else if (type.equalsIgnoreCase("rabbit") || type.equalsIgnoreCase("rabbitmq")) {
				if (
					configRegistry.hasRegister("messaging.rabbit.address")
					&& configRegistry.hasRegister("messaging.rabbit.port")
					&& configRegistry.hasRegister("messaging.rabbit.user")
				) {
					ServiceLocator.provideService(
						new RabbitMessageHandler(
							configRegistry.getRegister("messaging.rabbit.address", String.class),
							configRegistry.getRegister("messaging.rabbit.port", Number.class).intValue(),
							configRegistry.getRegister("messaging.rabbit.user", String.class),
							configRegistry.hasRegister("messaging.rabbit.pass") ? configRegistry.getRegister("messaging.rabbit.pass", String.class) : "",
							plugin.getDescription().getName(),
							plugin.getServerId(),
							SenderType.PROXY
						)
					);
				} else {
					throw new RuntimeException("\"messaging.rabbit.address\", \"messaging.rabbit.port\", or \"messaging.rabbit.user\" missing from config. Aborting plugin load.");
				}
			} else {
				plugin.printWarning("Config \"messaging.queueType\" does not match expected values. Using Bungeecord default.");
				if (!ServiceLocator.hasService(EnhancedBungeeMessageHandler.class)) {
					ServiceLocator.provideService(new EnhancedBungeeMessageHandler(plugin.getDescription().getName(), plugin.getServerId()));
				}
			}
			
			IMessageHandler messageHandler = ServiceLocator.getService(IMessageHandler.class);
			messageHandler.setSenderId(plugin.getServerId());
			
			messageHandler.createChannel("Troll");
		} else {
			throw new RuntimeException("\"messaging.type\" missing from config. Aborting plugin load.");
		}
	}
	
	//private
	
}
