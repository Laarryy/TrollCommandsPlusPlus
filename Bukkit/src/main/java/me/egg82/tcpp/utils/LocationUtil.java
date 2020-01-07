package me.egg82.tcpp.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class LocationUtil {
    private LocationUtil() { }

    public static boolean isBlockEqual(Location one, Location two) {
        return
                one.getBlockX() == two.getBlockX()
                && one.getBlockY() == two.getBlockY()
                && one.getBlockZ() == two.getBlockZ();
    }

    public static boolean canIgnite(Material type) { return !type.isSolid() && !type.name().contains("WATER") && !type.name().contains("LAVA"); }

    public static boolean isFinite(Vector vec) {
        try {
            NumberConversions.checkFinite(vec.getX(), "x not finite");
            NumberConversions.checkFinite(vec.getY(), "y not finite");
            NumberConversions.checkFinite(vec.getZ(), "z not finite");
        } catch (IllegalArgumentException ignored) {
            return false;
        }

        return true;
    }

    public static BlockFace getFacingDirection(double yaw, boolean cardinal) {
        yaw += 180.0d;

        while (yaw < 0.0d) {
            yaw += 360.0d;
        }
        while (yaw > 360.0d) {
            yaw -= 360.0d;
        }

        if (cardinal) {
            if (yaw >= 315.0d || yaw < 45.0d) {
                return BlockFace.NORTH;
            } else if (yaw >= 45.0d && yaw < 135.0d) {
                return BlockFace.EAST;
            } else if (yaw >= 135.0d && yaw < 225.0d) {
                return BlockFace.SOUTH;
            }

            return BlockFace.WEST;
        }

        if (yaw >= 348.75d || yaw < 11.25d) {
            return BlockFace.NORTH;
        } else if (yaw >= 11.25d && yaw < 33.75d) {
            return BlockFace.NORTH_NORTH_EAST;
        } else if (yaw >= 33.75d && yaw < 56.25d) {
            return BlockFace.NORTH_EAST;
        } else if (yaw >= 56.25d && yaw < 78.75d) {
            return BlockFace.EAST_NORTH_EAST;
        } else if (yaw >= 78.75d && yaw < 101.25d) {
            return BlockFace.EAST;
        } else if (yaw >= 101.25d && yaw < 123.75d) {
            return BlockFace.EAST_SOUTH_EAST;
        } else if (yaw >= 123.75d && yaw < 146.25d) {
            return BlockFace.SOUTH_EAST;
        } else if (yaw >= 146.25d && yaw < 168.75d) {
            return BlockFace.SOUTH_SOUTH_EAST;
        } else if (yaw >= 168.75d && yaw < 191.25d) {
            return BlockFace.SOUTH;
        } else if (yaw >= 191.25d && yaw < 213.75d) {
            return BlockFace.SOUTH_SOUTH_WEST;
        } else if (yaw >= 213.75d && yaw < 236.25d) {
            return BlockFace.SOUTH_WEST;
        } else if (yaw >= 236.25d && yaw < 258.75d) {
            return BlockFace.WEST_SOUTH_WEST;
        } else if (yaw >= 258.75d && yaw < 281.25d) {
            return BlockFace.WEST;
        } else if (yaw >= 281.25d && yaw < 303.75d) {
            return BlockFace.WEST_NORTH_WEST;
        } else if (yaw >= 303.75d && yaw < 326.25d) {
            return BlockFace.NORTH_WEST;
        }

        return BlockFace.NORTH_NORTH_WEST;
    }

    public static Location getRandomPointAround(Location loc, double radius, boolean includeY) {
        double angle = Math.random() * Math.PI * 2.0d;
        double sin = Math.sin(angle);
        return new Location(loc.getWorld(), loc.getX() + radius * Math.cos(angle), (includeY) ? loc.getY() + radius * sin * sin : loc.getY(), loc.getZ() + radius * sin);
    }

    public static Location getLocationInFront(Location loc, double distance, boolean includeY) {
        double angle = loc.getYaw();

        angle += 90.0d;

        while (angle < 0.0d) {
            angle += 360.0d;
        }
        while (angle > 360.0d) {
            angle -= 360.0d;
        }

        angle = angle * Math.PI / 180.0d;
        double sin = Math.sin(angle);

        return new Location(loc.getWorld(), loc.getX() + distance * Math.cos(angle), (includeY) ? loc.getY() + distance * sin * sin : loc.getY(), loc.getZ() + distance * sin);
    }
}
