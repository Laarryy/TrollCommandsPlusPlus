package me.egg82.tcpp.reflection.rollback;

import org.bukkit.Location;
import org.bukkit.Material;

public interface IRollbackHelper {
    //functions
    void logBlockPlace(String playerName, Location loc, Material newMaterial, byte newData);

    void logBlockRemove(String playerName, Location loc, Material oldMaterial, byte oldData);
}
