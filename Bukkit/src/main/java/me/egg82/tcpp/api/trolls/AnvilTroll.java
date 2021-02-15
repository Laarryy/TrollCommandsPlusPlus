package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AnvilTroll extends BukkitTroll {
    private final Plugin plugin;

    private Location anvilLocation;
    private BlockData[] oldBlockData = new BlockData[5];

    private static Method fallingBlockMethod;
    static {
        try {
            fallingBlockMethod = World.class.getMethod("spawnFallingBlock", Location.class, BlockData.class);
        } catch (NoSuchMethodException ignored) {
            fallingBlockMethod = null;
        }
    }

    public AnvilTroll(Plugin plugin, UUID playerID, TrollType type) {
        super(playerID, type);
        this.plugin = plugin;
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }

        // TODO: inventory support (restore not working with chests or shulker boxes)
        anvilLocation = player.getLocation();
        for (int i = 0; i < 4; i++) {
            oldBlockData[i] = anvilLocation.getBlock().getBlockData();
            anvilLocation.getBlock().setType(Material.AIR, false);
            anvilLocation.add(0.0d, 1.0d, 0.0d);
        }
        oldBlockData[4] = anvilLocation.getBlock().getBlockData();
        anvilLocation.getBlock().setType(Material.AIR, false);

        if (fallingBlockMethod != null) {
            try {
                FallingBlock anvil = (FallingBlock) fallingBlockMethod.invoke(anvilLocation.getWorld(), anvilLocation, Material.ANVIL.createBlockData());
                anvil.setHurtEntities(true);
                anvil.setDropItem(false);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                logger.error(ex.getMessage(), ex);
            }
        } else {
            anvilLocation.getBlock().setType(Material.ANVIL);
        }
        anvilLocation.add(0d, -4d, 0d);

        issuer.sendInfo(Message.ANVIL__START, "{player}", player.getName());

        Bukkit.getScheduler().runTaskLater(plugin, this::restoreBlocks, 40L);

        api.stopTroll(this, issuer);
    }

    private void restoreBlocks() {
        for (int i = 0; i < 4; i++) {
            anvilLocation.getBlock().setType(oldBlockData[i].getMaterial(), true);
            anvilLocation.getBlock().setBlockData(oldBlockData[i]);
            anvilLocation.add(0.0d, 1.0d, 0.0d);
        }
        anvilLocation.getBlock().setType(oldBlockData[4].getMaterial(), true);
        anvilLocation.getBlock().setBlockData(oldBlockData[4]);
    }
}
