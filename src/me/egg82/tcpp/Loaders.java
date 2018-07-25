package me.egg82.tcpp;

import ninja.egg82.bukkit.BasePlugin;
import ninja.egg82.bukkit.messaging.EnhancedBungeeMessageHandler;
import ninja.egg82.patterns.ServiceLocator;
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
	public static void loadMessaging(String pluginName, String serverName, String serverId, SenderType senderType) {
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
		
		Configuration config = ServiceLocator.getService(Configuration.class);
		
		if (config.getNode("messaging", "enabled").getBoolean()) {
			String type = config.getNode("messaging", "type").getString("bungee");
			
			if (type.equalsIgnoreCase("default") || type.equalsIgnoreCase("bungee") || type.equalsIgnoreCase("bungeecord")) {
				if (!ServiceLocator.hasService(EnhancedBungeeMessageHandler.class)) {
					ServiceLocator.provideService(new EnhancedBungeeMessageHandler(pluginName, (serverName != null) ? serverName : serverId));
				}
			} else if (type.equalsIgnoreCase("redis")) {
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
					config.getNode("messaging", "redis", "address").getString("127.0.0.1"),
					config.getNode("messaging", "redis", "port").getInt(6379)
				);
				ServiceLocator.provideService(redisPool);
			} else if (type.equalsIgnoreCase("rabbit") || type.equalsIgnoreCase("rabbitmq")) {
				ServiceLocator.provideService(
					new RabbitMessageHandler(
						config.getNode("messaging", "rabbit", "address").getString("127.0.0.1"),
						config.getNode("messaging", "rabbit", "port").getInt(5672),
						config.getNode("messaging", "rabbit", "user").getString("guest"),
						config.getNode("messaging", "rabbit", "pass").getString("guest"),
						pluginName,
						(serverName != null) ? serverName : serverId,
						senderType
					)
				);
			} else {
				ServiceLocator.getService(BasePlugin.class).printWarning("Config \"messaging.type\" does not match expected values. Using Bungeecord default.");
				if (!ServiceLocator.hasService(EnhancedBungeeMessageHandler.class)) {
					ServiceLocator.provideService(new EnhancedBungeeMessageHandler(pluginName, (serverName != null) ? serverName : serverId));
				}
			}
			
			ServiceLocator.getService(IMessageHandler.class).createChannel("troll");
		}
	}
	
	//private
	
}
