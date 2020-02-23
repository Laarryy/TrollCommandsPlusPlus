package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LevitateTroll extends BukkitTroll {
    private static final Set<Player> flyers = new HashSet<>();

    public LevitateTroll(UUID playerID, TrollType type) {
        super(playerID, type);
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }

        flyers.add(player);
        issuer.sendInfo(Message.LEVITATE__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        flyers.remove(player);
        player.removePotionEffect(PotionEffectType.LEVITATION);
        Vector velocity = player.getVelocity();
        velocity.setY(-10);
        player.setVelocity(velocity);
        issuer.sendInfo(Message.LEVITATE__STOP, "{player}", player.getName());
    }

    public static Set<Player> getFlyers() {
        return flyers;
    }
}
