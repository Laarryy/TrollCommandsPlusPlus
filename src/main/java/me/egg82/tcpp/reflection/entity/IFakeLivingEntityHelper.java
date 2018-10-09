package me.egg82.tcpp.reflection.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public interface IFakeLivingEntityHelper {
    //functions
    IFakeLivingEntity spawn(Location spawnLocation, EntityType type);

    boolean isValidLibrary();
}
