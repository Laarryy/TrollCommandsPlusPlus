package me.egg82.tcpp;

import org.bukkit.Bukkit;

import ninja.egg82.bukkit.BasePlugin;
import ninja.egg82.bukkit.messaging.EnhancedBungeeMessageHandler;
import ninja.egg82.bukkit.services.ConfigRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.enums.SenderType;
import ninja.egg82.plugin.messaging.IMessageHandler;
import ninja.egg82.plugin.messaging.RabbitMessageHandler;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Loaders {
	//vars
	
	//constructor
	public Loaders() {
		
	}
	
	//public
	@SuppressWarnings("resource")
	public static void loadRedis() {
		IVariableRegistry<String> configRegistry = ServiceLocator.getService(ConfigRegistry.class);
		
		if (configRegistry.hasRegister("redis.enabled")) {
			if (configRegistry.getRegister("redis.enabled", Boolean.class).booleanValue()) {
				if (
					configRegistry.hasRegister("redis.address")
					&& configRegistry.hasRegister("redis.port")
				) {
					JedisPoolConfig redisPoolConfig = new JedisPoolConfig();
					redisPoolConfig.setMaxTotal(16);
					redisPoolConfig.setMaxIdle(2);
					redisPoolConfig.setBlockWhenExhausted(false);
					redisPoolConfig.setTestOnBorrow(false);
					redisPoolConfig.setTestOnCreate(false);
					redisPoolConfig.setTestOnReturn(false);
					redisPoolConfig.setTestWhileIdle(true);
					redisPoolConfig.setMaxWaitMillis(30000L);
					JedisPool redisPool = new JedisPool(
						redisPoolConfig,
						configRegistry.getRegister("redis.address", String.class),
						configRegistry.getRegister("redis.port", Number.class).intValue()
					);
					ServiceLocator.provideService(redisPool);
				} else {
					throw new RuntimeException("\"redis.address\" or \"redis.port\" missing from config. Aborting plugin load.");
				}
			}
		} else {
			throw new RuntimeException("\"redis.enabled\" missing from config. Aborting plugin load.");
		}
	}
	@SuppressWarnings("resource")
	public static void loadMessaging() {
		BasePlugin plugin = ServiceLocator.getService(BasePlugin.class);
		IVariableRegistry<String> configRegistry = ServiceLocator.getService(ConfigRegistry.class);
		
		if (!configRegistry.hasRegister("messaging.enabled")) {
			return;
		}
		if (configRegistry.getRegister("messaging.enabled", Boolean.class).booleanValue()) {
			if (configRegistry.hasRegister("messaging.type")) {
				String type = configRegistry.getRegister("messaging.type", String.class);
				
				if (type.equalsIgnoreCase("default") || type.equalsIgnoreCase("bungee") || type.equalsIgnoreCase("bungeecord")) {
					if (!ServiceLocator.hasService(EnhancedBungeeMessageHandler.class)) {
						ServiceLocator.provideService(new EnhancedBungeeMessageHandler(plugin.getName(), plugin.getServerId()));
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
								(Bukkit.getServerName() != null && !Bukkit.getServerName().isEmpty()) ? Bukkit.getServerName() : plugin.getServerId(),
								SenderType.SERVER
							)
						);
					} else {
						throw new RuntimeException("\"messaging.rabbit.address\", \"messaging.rabbit.port\", or \"messaging.rabbit.user\" missing from config. Aborting plugin load.");
					}
				} else {
					plugin.printWarning("Config \"messaging.queueType\" does not match expected values. Using Bungeecord default.");
					if (!ServiceLocator.hasService(EnhancedBungeeMessageHandler.class)) {
						ServiceLocator.provideService(new EnhancedBungeeMessageHandler(plugin.getName(), plugin.getServerId()));
					}
				}
				
				String name = Bukkit.getServerName();
				if (name == null || name.isEmpty() || name.equalsIgnoreCase("unknown") || name.equalsIgnoreCase("unconfigured") || name.equalsIgnoreCase("unnamed") || name.equalsIgnoreCase("default")) {
					name = null;
				}
				
				IMessageHandler messageHandler = ServiceLocator.getService(IMessageHandler.class);
				if (name != null) {
					messageHandler.setSenderId(name);
				} else {
					messageHandler.setSenderId(plugin.getServerId());
				}
				
				messageHandler.createChannel("Troll");
			} else {
				throw new RuntimeException("\"messaging.type\" missing from config. Aborting plugin load.");
			}
		} else {
			throw new RuntimeException("\"messaging.enabled\" missing from config. Aborting plugin load.");
		}
	}
	
	//private
	
}
