package me.egg82.tcpp.tasks;

import me.egg82.tcpp.APIException;
import me.egg82.tcpp.TrollAPI;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.api.trolls.InfinityTroll;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;

public class InfinityTask implements Runnable {
    private TrollAPI api;

    public InfinityTask(TrollAPI api) {
        this.api = api;
    }

    @Override
    public void run() {
        Iterator<Entry<Player, Double>> buzzIterator = InfinityTroll.getLightyear().entrySet().iterator();
        while (buzzIterator.hasNext()) {
            Entry<Player, Double> entry = buzzIterator.next();
            Player target = entry.getKey();
            double initialD = entry.getValue(); // Initial Y position

            if (!target.isDead() && target.isValid() && target.isOnline()) {
                Vector velocity = target.getVelocity().clone();
                double g = 150; // Gravity in minecraft measured in m/s^2, where 1 m = 1 block
                // Testing the command you need a value of 150 approx to never reach initialD
                // The player has to be trapped at initialD + 5
                // Considering no other forces than gravity:
                // time t = 20 ticks = 1 s
                // y = initialD + 5
                // y = v_y * t + 1/2 * a_y * t^2 + y_0
                // y0 = initialD + 5 - v_y - a_y / 2
                Location location = target.getLocation().clone();
                //Bukkit.getLogger().log(Level.INFO, initialD + 5 - velocity.getY() - g/2 + " = " + initialD + 5 + " - " + velocity.getY() + " - " + g + "/2");
                location.setY(initialD + 5 - velocity.getY() - g/2);
                target.teleport(location, TeleportCause.PLUGIN);
                target.setVelocity(velocity);
            }
            else {
                try {
                    api.stopTroll(target.getUniqueId(), TrollType.INFINITY, null);
                }
                catch (APIException ex) {
                    ex.printStackTrace();
                }
                buzzIterator.remove();
            }
        }
    }
}
