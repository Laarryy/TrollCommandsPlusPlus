package me.egg82.tcpp.reflection.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class ProtocolLibFakeLivingEntityHelper implements IFakeLivingEntityHelper {
    //vars

    //constructor
    public ProtocolLibFakeLivingEntityHelper() {

    }

    //public
    public IFakeLivingEntity spawn(Location spawnLocation, EntityType type) {
        return new ProtocolLibFakeLivingEntity(spawnLocation, type);
    }

    public boolean isValidLibrary() {
        return true;
    }

    //private

}
