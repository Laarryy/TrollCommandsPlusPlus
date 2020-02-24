package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.APIException;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.UUID;

public class LevitateTroll extends BukkitTroll {
    private final Plugin plugin;
    private CommandIssuer originalIssuer;

    public LevitateTroll(Plugin plugin, UUID playerID, TrollType type) {
        super(playerID, type);

        this.plugin = plugin;
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }
        originalIssuer = issuer;

        tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::keepFlying, 5, 20));

        issuer.sendInfo(Message.LEVITATE__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        player.removePotionEffect(PotionEffectType.LEVITATION);
        Vector velocity = player.getVelocity();
        velocity.setY(-10);
        player.setVelocity(velocity);
        issuer.sendInfo(Message.LEVITATE__STOP, "{player}", player.getName());
    }

    private void keepFlying() {
        Player target = Bukkit.getPlayer(playerID);

        if (target != null && !target.isDead() && target.isValid() && target.isOnline()) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 200, 1, false, false), true);
        }
        else {
            try {
                api.stopTroll(playerID, TrollType.LEVITATE, originalIssuer);
            }
            catch (APIException ex) {
                ex.printStackTrace();
            }
        }
    }
}
