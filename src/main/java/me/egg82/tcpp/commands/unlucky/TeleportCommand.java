package me.egg82.tcpp.commands.unlucky;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;

import me.egg82.tcpp.core.LuckyCommand;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.patterns.tuples.pair.DoubleDoublePair;
import ninja.egg82.utils.MathUtil;

public class TeleportCommand extends LuckyCommand {
    //vars

    //constructor
    public TeleportCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        Location currentLocation = player.getLocation();
        DoubleDoublePair maxXZ = getMax(currentLocation, 300.0d);
        DoubleDoublePair minXZ = new DoubleDoublePair(maxXZ.getLeft() - (maxXZ.getLeft() / 5.0d), maxXZ.getRight() - (maxXZ.getRight() / 5.0d));
        double currentX = currentLocation.getX();
        double newX = currentX;
        double currentZ = currentLocation.getZ();
        double newZ = currentZ;

        Location newLocation = null;
        Material headBlock = null;
        Material footBlock = null;
        Material belowBlock = null;
        int retryCount = 0;

        do {
            do {
                newX = MathUtil.random(currentX - maxXZ.getLeft(), currentX + maxXZ.getLeft());
            } while (newX >= currentX - minXZ.getLeft() && newX <= currentX + minXZ.getLeft());
            do {
                newZ = MathUtil.random(currentZ - maxXZ.getRight(), currentZ + maxXZ.getRight());
            } while (newZ >= currentZ - minXZ.getRight() && newZ <= currentZ + minXZ.getRight());

            newLocation = BlockUtil.getHighestSolidBlock(new Location(currentLocation.getWorld(), newX, MathUtil.random(5.0d, currentLocation.getWorld().getMaxHeight()), newZ)).add(0.0d, 1.0d, 0.0d);
            belowBlock = newLocation.add(0.0d, -1.0d, 0.0d).getBlock().getType();
            footBlock = newLocation.getBlock().getType();
            headBlock = newLocation.clone().add(0.0d, 1.0d, 0.0d).getBlock().getType();
            retryCount++;
        } while (!isHospitable(headBlock, footBlock, belowBlock) && retryCount <= 500);

        if (!isHospitable(headBlock, footBlock, belowBlock)) {
            return;
        }

        player.teleport(newLocation.add(0.5d, 0.0d, 0.5d));
        return;
    }

    private DoubleDoublePair getMax(Location current, double max) {
        // Get world border
        WorldBorder border = current.getWorld().getWorldBorder();
        // get border center
        Location center = border.getCenter();
        // Translate border center to player location and adjust for border size
        double borderSizeX = border.getSize() - Math.abs(current.getX() - center.getX()) - 1.0d;
        double borderSizeZ = border.getSize() - Math.abs(current.getZ() - center.getZ()) - 1.0d;

        // Return the min between border size and original max (effectively capping "max" at the world border if needed)
        return new DoubleDoublePair(Math.min(borderSizeX, max), Math.min(borderSizeZ, max));
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
