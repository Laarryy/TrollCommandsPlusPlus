package me.egg82.tcpp.reflection.teleport;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface ITeleportHelper {
    // functions
    void teleport(Entity entity, Location to);
    void teleport(Entity entity, Location to, Consumer<Entity> done);
}
