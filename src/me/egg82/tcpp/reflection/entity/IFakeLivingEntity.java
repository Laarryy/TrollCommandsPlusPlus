package me.egg82.tcpp.reflection.entity;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

public interface IFakeLivingEntity {
	//functions
	int getId();
	UUID getUuid();
	
	boolean isVisibleTo(Player player);
	void addVisibilityToPlayer(Player player);
	void removeVisibilityFromPlayer(Player player);
	
	double getHealth();
	void setHealth(double health);
	
	void lookToward(Location targetLocation);
	
	void moveToward(Location location);
	void teleportTo(Location location);
	boolean requiresTeleport(Location newLocation);
	Location getLocation();
	
	void animate(AnimationType type);
	void attack(Damageable entity, double damage);
	
	void kill();
}
