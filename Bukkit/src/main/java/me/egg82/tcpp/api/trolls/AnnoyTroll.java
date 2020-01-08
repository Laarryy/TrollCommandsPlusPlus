package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import java.util.Random;
import java.util.UUID;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import me.egg82.tcpp.services.EnumFilter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AnnoyTroll extends BukkitTroll {
    private final Plugin plugin;
    private final Random rand = new Random();

    private static final Sound[] sounds;
    static {
        sounds = EnumFilter.builder(Sound.class)
                .whitelist("villager")
                .blacklist("zombie")
                .blacklist("work")
                .build();
    }

    public AnnoyTroll(Plugin plugin, UUID playerID, TrollType type) {
        super(playerID, type);
        this.plugin = plugin;
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }

        tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::playSound, 0L, 45L));

        issuer.sendInfo(Message.ANNOY__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);

        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        issuer.sendInfo(Message.ANNOY__STOP, "{player}", player.getName());
    }

    private void playSound() {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        if (Math.random() <= 0.2d) {
            player.playSound(player.getEyeLocation(), sounds[rand.nextInt(sounds.length)], rand.nextFloat() * 0.75f + 0.25f, rand.nextFloat() * 1.5f + 0.5f);
        }
    }
}
