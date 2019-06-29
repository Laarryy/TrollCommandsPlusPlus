package me.egg82.tcpp.tasks;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.services.EnumFilter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TaskAnnoy implements Runnable {
    private static final Sound[] sounds;

    static {
        sounds = EnumFilter.builder(Sound.class)
                .whitelist("villager")
                .blacklist("zombie")
                .blacklist("work")
                .build();
    }

    private final Random rand = new Random();

    public TaskAnnoy() { }

    public void run() {
        Set<UUID> set = CollectionProvider.getSet("annoy");

        for (UUID u : set) {
            Player player = Bukkit.getPlayer(u);
            if (player == null) {
                continue;
            }

            if (Math.random() <= 0.2d) {
                player.playSound(player.getEyeLocation(), sounds[rand.nextInt(sounds.length)], rand.nextFloat() * 0.75f + 0.25f, rand.nextFloat() * 1.5f + 0.5f);
            }
        }
    }
}
