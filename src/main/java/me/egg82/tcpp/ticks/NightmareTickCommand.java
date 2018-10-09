package me.egg82.tcpp.ticks;

import java.util.Collection;
import java.util.UUID;

import me.egg82.tcpp.reflection.entity.IFakeLivingEntity;
import me.egg82.tcpp.registries.NightmareRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import ninja.egg82.bukkit.handlers.async.AsyncTickHandler;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IRegistry;

public class NightmareTickCommand extends AsyncTickHandler {
    //vars
    private IRegistry<UUID, IConcurrentSet<IFakeLivingEntity>> nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);

    //constructor
    public NightmareTickCommand() {
        super(0L, 2L);
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        for (UUID key : nightmareRegistry.getKeys()) {
            e(CommandUtil.getPlayerByUuid(key), nightmareRegistry.getRegister(key));
        }
    }

    private void e(Player player, IConcurrentSet<IFakeLivingEntity> entities) {
        if (player == null) {
            return;
        }

        for (IFakeLivingEntity e : entities) {
            if (!e.getLocation().getWorld().equals(player.getWorld())) {
                e.teleportTo(player.getLocation());
                continue;
            }

            Location to = player.getLocation().clone().add(player.getLocation().getX() % player.getLocation().getBlockX(), 1.0d, player.getLocation().getZ() % player.getLocation().getBlockZ());

            Vector v = to.toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.23);
            if (LocationUtil.isFinite(v)) {
                Location newLoc = e.getLocation().add(v);
                newLoc.setY(BlockUtil.getHighestSolidBlock(newLoc).getBlockY() + 1.0d);
                if (e.requiresTeleport(newLoc)) {
                    e.teleportTo(newLoc);
                } else {
                    e.moveToward(newLoc);
                }

                bounce(e, entities);
            }
            TaskUtil.runAsync(new Runnable() {
                public void run() {
                    e.lookToward(player.getEyeLocation());
                }
            }, 1L);

            if (e.getLocation().distanceSquared(player.getLocation()) <= 1.5625d) { // 1.25
                TaskUtil.runSync(new Runnable() {
                    public void run() {
                        e.attack(player, 1.0d);
                    }
                });
            }
        }
    }

    private void bounce(IFakeLivingEntity entity, Collection<IFakeLivingEntity> entities) {
        for (IFakeLivingEntity e : entities) {
            if (entity.equals(e)) {
                continue;
            }

            Location location = entity.getLocation();

            if (location.distanceSquared(e.getLocation()) >= 0.5625d) { // 0.75
                continue;
            }

            entity.moveToward(location.subtract(e.getLocation().toVector().subtract(location.toVector()).multiply(0.18d)));
            e.moveToward(e.getLocation().subtract(location.toVector().subtract(e.getLocation().toVector()).multiply(0.18d)));
        }
    }
}
