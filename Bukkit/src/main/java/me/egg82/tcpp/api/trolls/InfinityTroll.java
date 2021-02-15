package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.APIException;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.UUID;

public class InfinityTroll extends BukkitTroll {
    private final Plugin plugin;
    private double baseY;
    private CommandIssuer originalIssuer;

    public InfinityTroll(Plugin plugin, UUID playerID, TrollType type) {
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

        baseY = player.getLocation().getY();
        tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::infinityFall, 5, 20));

        issuer.sendInfo(Message.INFINITY__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        issuer.sendInfo(Message.INFINITY__STOP, "{player}", player.getName());
    }

    private void infinityFall() {
        Player target = Bukkit.getPlayer(playerID);

        if (target != null && !target.isDead() && target.isValid() && target.isOnline()) {
            // Testing the command you need a value of 150 approx to never reach baseY
            Vector velocity = target.getVelocity().clone();
            double g = -150; // Gravity value
            double y0 = baseY + 5 - velocity.getY() - g / 2;
            //double y0 = baseY + 5 - velocity.getY() * 0.5 - 0.25 * g / 2; // For 10 tick task use this and g = -300
            //double y0 = baseY + 10 - 392 * (6 - 10 * Math.pow(0.98, 20)); // Not working, from gamepedia
            Location location = target.getLocation().clone();
            location.setY(y0);
            target.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            target.setVelocity(velocity);
        }
        else {
            try {
                api.stopTroll(playerID, TrollType.INFINITY, originalIssuer);
            }
            catch (APIException ex) {
                ex.printStackTrace();
            }
        }
    }
}
