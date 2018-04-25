package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.services.registries.GrantWishesRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.plugin.utils.TaskUtil;
import ninja.egg82.utils.ReflectUtil;

public class GrantWishesEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IVariableRegistry<UUID> grantWishesRegistry = ServiceLocator.getService(GrantWishesRegistry.class);
	
	private HashMap<String, EntityType> entities = new HashMap<String, EntityType>();
	
	//constructor
	public GrantWishesEventCommand() {
		super();
		
		EntityType[] types = EntityType.values();
		
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				continue;
			}
			if (!ReflectUtil.doesExtend(Monster.class, types[i].getEntityClass())) {
				continue;
			}
			
			entities.put(String.join(" ", types[i].name().toLowerCase().split("_")), types[i]);
		}
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		Location playerLoc = player.getLocation();
		
		if (grantWishesRegistry.hasRegister(player.getUniqueId())) {
			String search = event.getMessage().toLowerCase();
			int frequency = 0;
			
			for (Entry<String, EntityType> kvp : entities.entrySet()) {
				frequency = substringFrequency(kvp.getKey(), search);
				for (int i = 0; i < frequency; i++) {
					spawn(kvp.getValue(), LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				}
			}
			
			frequency = substringFrequency("skellington", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.SKELETON, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
		}
	}
	private void spawn(EntityType type, Location loc, Player player) {
		TaskUtil.runSync(new Runnable() {
			public void run() {
				Entity entity = loc.getWorld().spawn(loc, type.getEntityClass());
				entity.setVelocity(LocationUtil.moveSmoothly(loc, player.getLocation()));
				
				if (type == EntityType.PIG_ZOMBIE) {
					PigZombie pig = (PigZombie) entity;
					pig.setAngry(true);
				}
				
				try {
					((Creature) entity).setTarget(player);
				} catch (Exception ex) {
					
				}
			}
		}, 1L);
	}
	
	private int substringFrequency(String needle, String haystack) {
		int frequency = 0;
		int last = -1;
		
		do {
			last = haystack.indexOf(needle, last + 1);
			if (last > -1) {
				frequency++;
			}
		} while (last > -1);
		
		return frequency;
	}
}
