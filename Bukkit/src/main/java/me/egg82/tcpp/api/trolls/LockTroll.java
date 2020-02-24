package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import ninja.egg82.events.BukkitEventFilters;
import ninja.egg82.events.BukkitEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class LockTroll extends BukkitTroll {
    private final Plugin plugin;

    public LockTroll(Plugin plugin, UUID playerID, TrollType type) {
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
                BukkitEvents.subscribe(plugin, PlayerItemHeldEvent.class, EventPriority.LOW)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::hotbarLocker)
        );
        events.add(
                BukkitEvents.subscribe(plugin, InventoryDragEvent.class, EventPriority.LOW)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::dragLocker)
        );
        events.add(
                BukkitEvents.subscribe(plugin, InventoryClickEvent.class, EventPriority.LOW)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::clickLocker)
        );
        events.add(
                BukkitEvents.subscribe(plugin, PlayerDropItemEvent.class, EventPriority.LOW)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::dropLocker)
        );
        events.add(
                BukkitEvents.subscribe(plugin, PlayerSwapHandItemsEvent.class, EventPriority.LOW)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::swapLocker)
        );

        issuer.sendInfo(Message.LOCK__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        issuer.sendInfo(Message.LOCK__STOP, "{player}", player.getName());
    }

    private void hotbarLocker(PlayerItemHeldEvent ev) {
        if (playerID.equals(ev.getPlayer().getUniqueId()))
            ev.setCancelled(true);
    }

    public void dragLocker(InventoryDragEvent ev) {
        if (ev.getWhoClicked() instanceof Player) {
            Player p = (Player) ev.getWhoClicked();
            if (playerID.equals(p.getUniqueId())) {
                ev.setCancelled(true);
                p.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                p.updateInventory();
            }
        }
    }

    public void clickLocker(InventoryClickEvent ev) {
        if (ev.getWhoClicked() instanceof Player) {
            Player p = (Player) ev.getWhoClicked();
            if (playerID.equals(p.getUniqueId())) {
                ev.setCancelled(true);
                p.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                p.updateInventory();
            }
        }
    }

    public void dropLocker(PlayerDropItemEvent ev) {
        Player p = ev.getPlayer();
        if (playerID.equals(p.getUniqueId())) {
            ev.setCancelled(true);
            p.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            p.updateInventory();
        }
    }

    public void swapLocker(PlayerSwapHandItemsEvent ev) {
        Player p = ev.getPlayer();
        if (playerID.equals(p.getUniqueId())) {
            ev.setCancelled(true);
            p.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            p.updateInventory();
        }
    }
}
