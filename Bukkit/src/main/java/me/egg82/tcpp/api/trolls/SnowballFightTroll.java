package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import ninja.egg82.events.BukkitEventFilters;
import ninja.egg82.events.BukkitEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class SnowballFightTroll extends BukkitTroll {
    private final Plugin plugin;

    public SnowballFightTroll(Plugin plugin, UUID playerID, TrollType type) {
        super(playerID, type);

        this.plugin = plugin;
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }

        events.add(
                BukkitEvents.subscribe(plugin, ProjectileLaunchEvent.class, EventPriority.LOW)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::snowballFight)
        );

        issuer.sendInfo(Message.SNOWBALLFIGHT__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        issuer.sendInfo(Message.SNOWBALLFIGHT__STOP, "{player}", player.getName());
    }

    private void snowballFight(ProjectileLaunchEvent ev) {
        Projectile proj = ev.getEntity();
        if (proj.getType() != EntityType.SNOWBALL && proj.getShooter() instanceof Player) {
            Player p = (Player) proj.getShooter();

            if (playerID.equals(p.getUniqueId())) {
                p.launchProjectile(Snowball.class, proj.getVelocity());
                ev.setCancelled(true);
            }
        }
    }
}
