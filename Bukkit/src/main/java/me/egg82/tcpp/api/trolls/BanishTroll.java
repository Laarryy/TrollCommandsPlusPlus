package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import java.util.UUID;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import me.egg82.tcpp.utils.BlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class BanishTroll extends BukkitTroll {
    private final long range;

    public BanishTroll(UUID playerID, Long range, TrollType type) {
        super(playerID, type);
        this.range = range;
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }

        double maxX = getMaxX(player.getLocation(), range);
        double minX = maxX - (maxX / 2.5d);
        double maxZ = getMaxZ(player.getLocation(), range);
        double minZ = maxZ - (maxZ / 2.5d);

        Location newLocation;
        Material belowBlock;
        Material footBlock;
        Material headBlock;
        int tries = 100;
        do {
            double newX = Math.random() * (maxX - minX) + minX;
            double newZ = Math.random() * (maxZ - minZ) + minZ;

            newLocation = BlockUtil.getHighestSolidBlock(new Location(player.getLocation().getWorld(), newX, Math.random() * (player.getLocation().getWorld().getMaxHeight() - 5.0d) + 5.0d, newZ)).getLocation().add(0.0d, 1.0d, 0.0d);
            belowBlock = newLocation.clone().add(0.0d, -1.0d, 0.0d).getBlock().getType();
            footBlock = newLocation.getBlock().getType();
            headBlock = newLocation.clone().add(0.0d, 1.0d, 0.0d).getBlock().getType();
            tries--;
        } while (!isHospitable(headBlock, footBlock, belowBlock) && tries > 0);

        if (tries <= 0 && !isHospitable(headBlock, footBlock, belowBlock)) {
            issuer.sendError(Message.BANISH__NO_LOCATIONS, "{player}", player.getName());
            api.stopTroll(this, null);
            return;
        }

        player.teleport(newLocation.add(0.5d, 0.0d, 0.5d));

        issuer.sendInfo(Message.BANISH__START, "{player}", player.getName());
        api.stopTroll(this, null);
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
