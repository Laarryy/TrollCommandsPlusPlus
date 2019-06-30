package me.egg82.tcpp.commands.internal;

import co.aikar.taskchain.TaskChain;
import me.egg82.tcpp.services.AnalyticsHelper;
import me.egg82.tcpp.utils.BlockUtil;
import me.egg82.tcpp.utils.LogUtil;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanishCommand extends BaseCommand {
    private final String playerName;
    private final long range;

    public BanishCommand(TaskChain<?> chain, CommandSender sender, String playerName, long range) {
        super(chain, sender);
        this.playerName = playerName;
        this.range = range;
    }

    public void run() {
        getChain(playerName)
                .syncLast(v -> {
                    if (isOfflineOrImmune(v)) {
                        return;
                    }

                    Player p = Bukkit.getPlayer(v);

                    double maxX = getMaxX(p.getLocation(), range);
                    double minX = maxX - (maxX / 2.5d);
                    double maxZ = getMaxZ(p.getLocation(), range);
                    double minZ = maxZ - (maxZ / 2.5d);

                    Location newLocation;
                    Material belowBlock;
                    Material footBlock;
                    Material headBlock;
                    int tries = 100;
                    do {
                        double newX = Math.random() * (maxX - minX) + minX;
                        double newZ = Math.random() * (maxZ - minZ) + minZ;

                        newLocation = BlockUtil.getHighestSolidBlock(new Location(p.getLocation().getWorld(), newX, Math.random() * (p.getLocation().getWorld().getMaxHeight() - 5.0d) + 5.0d, newZ)).getLocation().add(0.0d, 1.0d, 0.0d);
                        belowBlock = newLocation.clone().add(0.0d, -1.0d, 0.0d).getBlock().getType();
                        footBlock = newLocation.getBlock().getType();
                        headBlock = newLocation.clone().add(0.0d, 1.0d, 0.0d).getBlock().getType();
                        tries--;
                    } while (!isHospitable(headBlock, footBlock, belowBlock) && tries > 0);

                    if (tries <= 0 && !isHospitable(headBlock, footBlock, belowBlock)) {
                        sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + playerName + " could not be banished because all of the locations chosen were inhospitable!");
                        return;
                    }

                    p.teleport(newLocation.add(0.5d, 0.0d, 0.5d));

                    AnalyticsHelper.incrementCommand("banish");
                    sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + playerName + " has been banished.");
                })
                .execute();
    }

    private double getMaxX(Location current, long radiusX) {
        // Get world border
        WorldBorder border = current.getWorld().getWorldBorder();
        // get border center
        Location center = border.getCenter();
        // Translate border center to player location and adjust for border size
        double borderSizeX = (border.getSize() / 2.0d) - Math.abs(current.getX() - center.getX()) - 1.0d;

        // Return the min between border size and original max (effectively capping "max" at the world border if needed)
        return Math.min(borderSizeX, radiusX);
    }

    private double getMaxZ(Location current, long radiusZ) {
        // Get world border
        WorldBorder border = current.getWorld().getWorldBorder();
        // get border center
        Location center = border.getCenter();
        // Translate border center to player location and adjust for border size
        double borderSizeZ = (border.getSize() / 2.0d) - Math.abs(current.getZ() - center.getZ()) - 1.0d;

        // Return the min between border size and original max (effectively capping "max" at the world border if needed)
        return Math.min(borderSizeZ, radiusZ);
    }

    private boolean isHospitable(Material headBlock, Material footBlock, Material belowBlock) {
        if (
                headBlock != Material.AIR
                        || footBlock == Material.LAVA
                        || footBlock.name().equals("STATIONARY_LAVA")
                        || footBlock.name().equals("LEGACY_STATIONARY_LAVA")
                        || (footBlock == Material.WATER && headBlock == Material.WATER)
                        || belowBlock == Material.CACTUS
                        || belowBlock.name().equals("MAGMA")
                        || belowBlock == Material.FIRE
        ) {
            return false;
        }

        return true;
    }
}
