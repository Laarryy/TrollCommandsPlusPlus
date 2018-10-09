package me.egg82.tcpp.reflection.teleport;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class BukkitTeleportHelper implements ITeleportHelper {
    // vars

    // constructor
    public BukkitTeleportHelper() {

    }

    // public
    public void teleport(Entity entity, Location to) {
        teleport(entity, to, null);
    }

    public void teleport(Entity entity, Location to, Consumer<Entity> done) {
        entity.teleport(to);
        if (done != null) {
            done.accept(entity);
        }
    }

    // private

}
