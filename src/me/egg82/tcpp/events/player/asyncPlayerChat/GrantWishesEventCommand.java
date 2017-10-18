package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.services.registries.GrantWishesRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.plugin.utils.TaskUtil;

public class GrantWishesEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IRegistry<UUID> grantWishesRegistry = ServiceLocator.getService(GrantWishesRegistry.class);
	
	private EntityType shulker = null;
	private EntityType husk = null;
	private EntityType vindicator = null;
	private EntityType evoker = null;
	private EntityType vex = null;
	private EntityType illusioner = null;
	
	//constructor
	public GrantWishesEventCommand() {
		super();
		
		try {
			shulker = EntityType.valueOf("SHULKER");
		} catch (Exception ex) {
			
		}
		try {
			husk = EntityType.valueOf("HUSK");
		} catch (Exception ex) {
			
		}
		try {
			vindicator = EntityType.valueOf("VINDICATOR");
		} catch (Exception ex) {
			
		}
		try {
			evoker = EntityType.valueOf("EVOKER");
		} catch (Exception ex) {
			
		}
		try {
			vex = EntityType.valueOf("VEX");
		} catch (Exception ex) {
			
		}
		try {
			illusioner = EntityType.valueOf("ILLUSIONER");
		} catch (Exception ex) {
			
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
			
			frequency = substringFrequency("blaze", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.BLAZE, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			frequency = substringFrequency("creeper", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.CREEPER, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			frequency = substringFrequency("enderman", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.ENDERMAN, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			frequency = substringFrequency("endermite", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.ENDERMITE, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			frequency = substringFrequency("ghast", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.GHAST, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			frequency = substringFrequency("magma cube", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.MAGMA_CUBE, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			frequency = substringFrequency("silverfish", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.SILVERFISH, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			frequency = substringFrequency("skeleton", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.SKELETON, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			frequency = substringFrequency("skellington", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.SKELETON, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			frequency = substringFrequency("slime", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.SLIME, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			frequency = substringFrequency("witch", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.WITCH, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			
			if (shulker != null) {
				frequency = substringFrequency("shulker", search);
				for (int i = 0; i < frequency; i++) {
					spawn(shulker, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				}
			}
			if (husk != null) {
				frequency = substringFrequency("husk", search);
				for (int i = 0; i < frequency; i++) {
					spawn(husk, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				}
			}
			if (vindicator != null) {
				frequency = substringFrequency("vindicator", search);
				for (int i = 0; i < frequency; i++) {
					spawn(vindicator, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				}
			}
			if (evoker != null) {
				frequency = substringFrequency("evoker", search);
				for (int i = 0; i < frequency; i++) {
					spawn(evoker, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				}
			}
			if (vex != null) {
				frequency = substringFrequency("vex", search);
				for (int i = 0; i < frequency; i++) {
					spawn(vex, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				}
			}
			if (illusioner != null) {
				frequency = substringFrequency("illusioner", search);
				for (int i = 0; i < frequency; i++) {
					spawn(illusioner, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				}
			}
			
			frequency = substringFrequency("cave spider", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.CAVE_SPIDER, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (frequency == 0) {
				frequency = substringFrequency("spider", search);
				for (int i = 0; i < frequency; i++) {
					spawn(EntityType.SPIDER, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				}
			}
			
			frequency = substringFrequency("pig zombie", search);
			for (int i = 0; i < frequency; i++) {
				spawn(EntityType.PIG_ZOMBIE, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (frequency == 0) {
				frequency = substringFrequency("zombie", search);
				for (int i = 0; i < frequency; i++) {
					spawn(EntityType.ZOMBIE, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				}
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
