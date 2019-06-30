package me.egg82.tcpp.tasks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TaskBludger implements Runnable {
    public TaskBludger() { }

    public void run() {
        ConcurrentMap<UUID, UUID> map = CollectionProvider.getMap("bludger");

        for (Map.Entry<UUID, UUID> kvp : map.entrySet()) {
            Player player = Bukkit.getPlayer(kvp.getKey());
            if (player == null) {
                continue;
            }

            Entity fireball = Bukkit.getEntity(kvp.getValue());
            if (!(fireball instanceof Fireball)) {
                map.remove(kvp.getKey());
                continue;
            }

            Vector vector = player.getLocation().toVector().subtract(fireball.getLocation().toVector()).normalize().multiply(0.35d);
            if (LocationUtil.isFinite(vector)) {
                fireball.setVelocity(vector);
            }
        }
    }
}
