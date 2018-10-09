package me.egg82.tcpp.reflection.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class NullFakeLivingEntityHelper implements IFakeLivingEntityHelper {
    //vars

    //constructor
    public NullFakeLivingEntityHelper() {

    }

    //public
    public IFakeLivingEntity spawn(Location spawnLocation, EntityType type) {
        return null;
    }

    public boolean isValidLibrary() {
        return false;
    }

    //private

}
