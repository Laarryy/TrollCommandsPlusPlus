package me.egg82.tcpp.commands.internal;

import co.aikar.taskchain.TaskChain;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import me.egg82.tcpp.services.AnalyticsHelper;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.utils.LogUtil;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

public class AnvilCommand extends BaseCommand {
    private static Method fallingBlockMethod;

    static {
        try {
            fallingBlockMethod = World.class.getMethod("spawnFallingBlock", Location.class, MaterialData.class);
        } catch (NoSuchMethodException ignored) {
            fallingBlockMethod = null;
        }
    }

    private final Plugin plugin;
    private final String playerName;

    public AnvilCommand(Plugin plugin, TaskChain<?> chain, CommandSender sender, String playerName) {
        super(chain, sender);
        this.plugin = plugin;
        this.playerName = playerName;
    }

    public void run() {
        getChain(playerName)
                .syncLast(v -> {
                    if (isOfflineOrImmune(v)) {
                        return;
                    }

                    Set<UUID> set = CollectionProvider.getSet("anvil");

                    Player player = Bukkit.getPlayer(v);
                    if (player == null) {
                        sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "Player is not online!");
                        return;
                    }

                    Location anvilLocation = player.getLocation().toBlockLocation();
                    for (int i = 0; i < 3; i++) {
                        anvilLocation.getBlock().setType(Material.AIR, false);
                        anvilLocation.add(0.0d, 1.0d, 0.0d);
                    }
                    anvilLocation.add(0.0d, 1.0d, 0.0d);

                    if (fallingBlockMethod != null) {
                        try {
                            set.add(((FallingBlock) fallingBlockMethod.invoke(anvilLocation.getWorld(), anvilLocation, new MaterialData(Material.ANVIL))).getUniqueId());
                        } catch (IllegalAccessException | InvocationTargetException ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                    } else {
                        anvilLocation.getBlock().setType(Material.ANVIL);
                        scheduleGetAnvil(set, anvilLocation, 40);
                    }

                    AnalyticsHelper.incrementCommand("anvil");
                    sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + "The " + ChatColor.STRIKETHROUGH + ChatColor.ITALIC + "base" + ChatColor.RESET + " anvil has been dropped on " + playerName + ".");
                })
                .execute();
    }

    private void scheduleGetAnvil(Set<UUID> set, Location anvilLocation, int tries) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Optional<FallingBlock> anvil = tryGetAnvil(anvilLocation);
            if (anvil.isPresent()) {
                set.add(anvil.get().getUniqueId());
            } else {
                if (tries > 0) {
                    scheduleGetAnvil(set, anvilLocation, tries - 1);
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
}
