package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;
import me.egg82.tcpp.APIException;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import ninja.egg82.events.BukkitEvents;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

public class AnvilTroll extends BukkitTroll {
    private final Plugin plugin;

    private UUID anvilID = null;

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

        events.add(
                BukkitEvents.subscribe(plugin, EntityChangeBlockEvent.class, EventPriority.LOW)
                        .handler(this::entityChangeBlock)
        );

        // TODO: save blocks and replace
        Location anvilLocation = player.getLocation();
        for (int i = 0; i < 3; i++) {
            anvilLocation.getBlock().setType(Material.AIR, false);
            anvilLocation.add(0.0d, 1.0d, 0.0d);
        }
        anvilLocation.add(0.0d, 1.0d, 0.0d);

        if (fallingBlockMethod != null) {
            try {
                anvilID = ((FallingBlock) fallingBlockMethod.invoke(anvilLocation.getWorld(), anvilLocation, Material.ANVIL.createBlockData())).getUniqueId();
            } catch (IllegalAccessException | InvocationTargetException ex) {
                logger.error(ex.getMessage(), ex);
            }
        } else {
            anvilLocation.getBlock().setType(Material.ANVIL);
            scheduleGetAnvil(anvilLocation, 40);
        }

        issuer.sendInfo(Message.ANVIL__START, "{player}", player.getName());
    }

    private void scheduleGetAnvil(Location anvilLocation, int tries) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Optional<FallingBlock> anvil = tryGetAnvil(anvilLocation);
            if (anvil.isPresent()) {
                anvilID = anvil.get().getUniqueId();
            } else {
                if (tries > 0) {
                    scheduleGetAnvil(anvilLocation, tries - 1);
                }
            }
        }, 1L);
    }

    private Optional<FallingBlock> tryGetAnvil(Location searchFrom) {
        for (Entity e : searchFrom.getWorld().getNearbyEntities(searchFrom, 1.5d, 1.5d, 1.5d)) {
            if (!(e instanceof FallingBlock)) {
                continue;
            }

            if (((FallingBlock) e).getMaterial() == Material.ANVIL) {
                return Optional.of((FallingBlock) e);
            }

            if (((FallingBlock) e).getBlockData().getMaterial() == Material.ANVIL) {
                return Optional.of((FallingBlock) e);
            }
        }
        return Optional.empty();
    }

    private void entityChangeBlock(EntityChangeBlockEvent event) {
        if (anvilID != null && anvilID.equals(event.getEntity().getUniqueId())) {
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            event.getEntity().remove();
            //event.setCancelled(true);
            try {
                api.stopTroll(this, null);
            } catch (APIException ex) {
                logger.error("[Hard: " + ex.isHard() + "] " + ex.getMessage(), ex);
            }
        }
    }
}
