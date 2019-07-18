package me.egg82.tcpp.tasks;

import java.util.Set;
import java.util.UUID;
import me.egg82.tcpp.services.CollectionProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TaskBurn implements Runnable {
    public TaskBurn() { }

    public void run() {
        Set<UUID> set = CollectionProvider.getSet("burn");

        for (UUID u : set) {
            Player player = Bukkit.getPlayer(u);
            if (player == null) {
                continue;
            }

            player.setFireTicks(41);
        }
    }
}
