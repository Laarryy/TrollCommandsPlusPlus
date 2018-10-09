package me.egg82.tcpp.reflection.teleport;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class PaperTeleportHelper implements ITeleportHelper {
    // vars

    // constructor
    public PaperTeleportHelper() {

    }

    // public
    public void teleport(Entity entity, Location to) {
        teleport(entity, to, null);
    }

    public void teleport(Entity entity, Location to, Consumer<Entity> done) {
        to.getWorld().getChunkAtAsync(to, (c) -> {
            entity.teleport(to);
            if (done != null) {
                done.accept(entity);
            }
        });
    }

    // private

}
