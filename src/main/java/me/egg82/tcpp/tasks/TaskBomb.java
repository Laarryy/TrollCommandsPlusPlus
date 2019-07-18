package me.egg82.tcpp.tasks;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TaskBomb implements Runnable {
    private final Random rand = new Random();

    public TaskBomb() { }

    public void run() {
        Set<UUID> set = CollectionProvider.getSet("bomb");

        for (UUID u : set) {
            Player player = Bukkit.getPlayer(u);
            if (player == null) {
                continue;
            }

            int numFireballs = rand.nextInt(4) + 1;
            Location playerLocation = player.getLocation();

            for (int i = 0; i < numFireballs; i++) {
                Fireball fireball = player.getWorld().spawn(playerLocation.clone().add(rand.nextDouble() * 14.0d - 7.0d, rand.nextDouble() * 8.0d + 5.0d, rand.nextDouble() * 14.0d - 7.0d), Fireball.class);
                Vector v = playerLocation.toVector().subtract(fireball.getLocation().toVector()).normalize().multiply(2.0d);
                if (LocationUtil.isFinite(v)) {
                    fireball.setVelocity(v);
                }
            }
        }
    }
}
