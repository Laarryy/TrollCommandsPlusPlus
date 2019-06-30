package me.egg82.tcpp.commands.internal;

import co.aikar.taskchain.TaskChain;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import me.egg82.tcpp.services.AnalyticsHelper;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.utils.LocationUtil;
import me.egg82.tcpp.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BludgerCommand extends BaseCommand {
    private final String playerName;

    public BludgerCommand(TaskChain<?> chain, CommandSender sender, String playerName) {
        super(chain, sender);
        this.playerName = playerName;
    }

    public void run() {
        getChain(playerName)
                .syncLast(v -> {
                    Set<UUID> set = CollectionProvider.getSet("bludger");

                    if (set.add(v)) {
                        if (isOfflineOrImmune(v)) {
                            return;
                        }

                        Player p = Bukkit.getPlayer(v);

                        Location spawnLocation;
                        if (
                                !(sender instanceof Player)
                                        || ((Player) sender).getUniqueId().equals(p.getUniqueId())
                                        || ((Player) sender).getLocation().getWorld() != p.getLocation().getWorld()
                                        || ((Player) sender).getLocation().distanceSquared(p.getLocation()) >= 3600.0d // 60.0d
                        ) {
                            spawnLocation = LocationUtil.getRandomPointAround(p.getLocation(), Math.random() * 10.0d + 5.0d, true);
                        } else {
                            spawnLocation = LocationUtil.getLocationInFront(((Player) sender).getLocation(), 1.5d, true);
                        }

                        Fireball fireball = spawnLocation.getWorld().spawn(spawnLocation, Fireball.class);
                        Vector vector = p.getLocation().toVector().subtract(spawnLocation.toVector()).normalize().multiply(0.35d);
                        if (LocationUtil.isFinite(vector)) {
                            fireball.setVelocity(vector);
                        }

                        ConcurrentMap<UUID, UUID> map = CollectionProvider.getMap("bludger");
                        map.put(v, fireball.getUniqueId());

                        AnalyticsHelper.incrementCommand("bludger");
                        sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + playerName + " is now being chased by a bludger!");
                    } else if (set.remove(v)) {
                        ConcurrentMap<UUID, UUID> map = CollectionProvider.getMap("bludger");
                        UUID fireballUUUID = map.remove(v);
                        if (fireballUUUID != null) {
                            Entity fireball = Bukkit.getEntity(fireballUUUID);
                            if (fireball instanceof Fireball) {
                                fireball.remove();
                            }
                        }

                        sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + playerName + " is no longer being chased by a bludger.");
                    }
                })
                .execute();
    }
}
