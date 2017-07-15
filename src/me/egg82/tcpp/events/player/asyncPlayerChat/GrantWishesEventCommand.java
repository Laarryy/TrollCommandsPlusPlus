package me.egg82.tcpp.events.player.asyncPlayerChat;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.services.GrantWishesRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.plugin.utils.TaskUtil;

public class GrantWishesEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IRegistry grantWishesRegistry = ServiceLocator.getService(GrantWishesRegistry.class);
	
	//constructor
	public GrantWishesEventCommand(AsyncPlayerChatEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		Location playerLoc = player.getLocation();
		
		if (grantWishesRegistry.hasRegister(player.getUniqueId().toString())) {
			String search = event.getMessage().toLowerCase();
			
			if (search.contains("blaze")) {
				spawn(EntityType.BLAZE, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (search.contains("creeper")) {
				spawn(EntityType.CREEPER, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (search.contains("enderman")) {
				spawn(EntityType.ENDERMAN, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (search.contains("endermite")) {
				spawn(EntityType.ENDERMITE, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (search.contains("ghast")) {
				spawn(EntityType.GHAST, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (search.contains("magma cube")) {
				spawn(EntityType.MAGMA_CUBE, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (search.contains("silverfish")) {
				spawn(EntityType.SILVERFISH, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (search.contains("skeleton") || search.contains("skellington")) {
				spawn(EntityType.SKELETON, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (search.contains("slime")) {
				spawn(EntityType.SLIME, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (search.contains("witch")) {
				spawn(EntityType.WITCH, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			
			if (search.contains("shulker")) {
				try {
					EntityType type = EntityType.valueOf("SHULKER");
					spawn(type, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				} catch (Exception ex) {
					
				}
			}
			if (search.contains("husk")) {
				try {
					EntityType type = EntityType.valueOf("HUSK");
					spawn(type, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				} catch (Exception ex) {
					
				}
			}
			if (search.contains("vindicator")) {
				try {
					EntityType type = EntityType.valueOf("VINDICATOR");
					spawn(type, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				} catch (Exception ex) {
					
				}
			}
			if (search.contains("evoker")) {
				try {
					EntityType type = EntityType.valueOf("EVOKER");
					spawn(type, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				} catch (Exception ex) {
					
				}
			}
			if (search.contains("vex")) {
				try {
					EntityType type = EntityType.valueOf("VEX");
					spawn(type, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				} catch (Exception ex) {
					
				}
			}
			if (search.contains("illusioner")) {
				try {
					EntityType type = EntityType.valueOf("ILLUSIONER");
					spawn(type, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
				} catch (Exception ex) {
					
				}
			}
			
			if (search.contains("cave spider")) {
				spawn(EntityType.CAVE_SPIDER, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			} else if (search.contains("spider")) {
				spawn(EntityType.SPIDER, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			}
			if (search.contains("pig zombie")) {
				spawn(EntityType.PIG_ZOMBIE, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
			} else if (search.contains("zombie")) {
				spawn(EntityType.ZOMBIE, LocationUtil.getRandomPointAround(playerLoc, 5.0d), player);
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
}
