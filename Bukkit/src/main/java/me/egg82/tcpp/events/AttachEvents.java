package me.egg82.tcpp.events;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import java.util.List;
import java.util.Map;
import me.egg82.tcpp.api.trolls.AttachTroll;
import me.egg82.tcpp.utils.InventoryUtil;
import ninja.egg82.events.BukkitEventFilters;
import ninja.egg82.events.BukkitEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class AttachEvents extends EventHolder {
    private final Plugin plugin;

    public AttachEvents(Plugin plugin) {
        this.plugin = plugin;

        events.add(
                BukkitEvents.subscribe(plugin, InventoryClickEvent.class, EventPriority.HIGH)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::clickRun)
        );
        events.add(
                BukkitEvents.subscribe(plugin, InventoryClickEvent.class, EventPriority.HIGH)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::clickErase)
        );

        events.add(
                BukkitEvents.subscribe(plugin, InventoryDragEvent.class, EventPriority.HIGH)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::dragRun)
        );
        events.add(
                BukkitEvents.subscribe(plugin, InventoryDragEvent.class, EventPriority.HIGH)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::dragErase)
        );

        events.add(
                BukkitEvents.subscribe(plugin, InventoryMoveItemEvent.class, EventPriority.HIGH)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::moveRun)
        );
        events.add(
                BukkitEvents.subscribe(plugin, InventoryMoveItemEvent.class, EventPriority.HIGH)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::moveErase)
        );

        events.add(
                BukkitEvents.subscribe(plugin, PlayerPickupItemEvent.class, EventPriority.HIGH)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::pickupRun)
        );
        events.add(
                BukkitEvents.subscribe(plugin, PlayerDropItemEvent.class, EventPriority.HIGH)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::dropErase)
        );
    }

    private void clickRun(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if (tryRunCommand(event.getCurrentItem())) {
                Bukkit.getScheduler().runTaskLater(plugin, ((Player) event.getWhoClicked())::updateInventory, 1L);
            }
        } else if (
                event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD
                        || event.getAction() == InventoryAction.HOTBAR_SWAP
                        || event.getAction() == InventoryAction.PLACE_ALL
                        || event.getAction() == InventoryAction.PLACE_ONE
                        || event.getAction() == InventoryAction.PLACE_SOME
                        || event.getAction() == InventoryAction.SWAP_WITH_CURSOR
        ) {
            if (InventoryUtil.getClickedInventory(event) == event.getView().getBottomInventory()) {
                if (tryRunCommand(event.getCursor())) {
                    Bukkit.getScheduler().runTaskLater(plugin, ((Player) event.getWhoClicked())::updateInventory, 1L);
                }
            }
        }
    }

    private void dragRun(InventoryDragEvent event) {
        InventoryAction action;
        Inventory clicked = InventoryUtil.getClickedInventory(event);

        if (clicked == event.getView().getTopInventory()) {
            action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
        } else {
            action = (event.getCursor() == null || event.getCursor().getAmount() == 0) ? InventoryAction.PLACE_ALL : InventoryAction.PLACE_SOME;
        }

        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            boolean ran = false;
            for (Map.Entry<Integer, ItemStack> kvp : event.getNewItems().entrySet()) {
                if (tryRunCommand(kvp.getValue())) {
                    ran = true;
                }
            }
            if (ran) {
                Bukkit.getScheduler().runTaskLater(plugin, ((Player) event.getWhoClicked())::updateInventory, 1L);
            }
        } else if (
                action == InventoryAction.HOTBAR_MOVE_AND_READD
                        || action == InventoryAction.HOTBAR_SWAP
                        || action == InventoryAction.PLACE_ALL
                        || action == InventoryAction.PLACE_ONE
                        || action == InventoryAction.PLACE_SOME
                        || action == InventoryAction.SWAP_WITH_CURSOR
        ) {
            if (clicked == event.getView().getBottomInventory()) {
                boolean ran = false;
                for (Map.Entry<Integer, ItemStack> kvp : event.getNewItems().entrySet()) {
                    if (tryRunCommand(kvp.getValue())) {
                        ran = true;
                    }
                }
                if (ran) {
                    Bukkit.getScheduler().runTaskLater(plugin, ((Player) event.getWhoClicked())::updateInventory, 1L);
                }
            }
        }
    }

    private void moveRun(InventoryMoveItemEvent event) { tryRunCommand(event.getItem()); }

    private void pickupRun(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack().clone();
        if (tryRunCommand(item)) {
            event.getItem().setItemStack(item);
        }
    }

    private boolean tryRunCommand(ItemStack item) {
        if (hasLore(item)) {
            return false;
        }

        NbtCompound compound;
        try {
            compound = NbtFactory.asCompound(NbtFactory.fromItemTag(item));
        } catch (IllegalArgumentException ignored) { return false; }

        if (!compound.containsKey(AttachTroll.ATTACH_COMPOUND_NAME)) {
            return false;
        }

        String command = compound.getString(AttachTroll.ATTACH_COMPOUND_NAME);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command); // TODO: Somehow add @e, @a, etc support
        compound.remove(AttachTroll.ATTACH_COMPOUND_NAME);
        NbtFactory.setItemTag(item, compound);
        return true;
    }

    private void clickErase(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if (eraseLore(event.getCurrentItem())) {
                Bukkit.getScheduler().runTaskLater(plugin, ((Player) event.getWhoClicked())::updateInventory, 1L);
            }
        } else if (
                event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD
                        || event.getAction() == InventoryAction.HOTBAR_SWAP
                        || event.getAction() == InventoryAction.PLACE_ALL
                        || event.getAction() == InventoryAction.PLACE_ONE
                        || event.getAction() == InventoryAction.PLACE_SOME
                        || event.getAction() == InventoryAction.SWAP_WITH_CURSOR
        ) {
            if (InventoryUtil.getClickedInventory(event) == event.getView().getTopInventory()) {
                if (eraseLore(event.getCursor())) {
                    Bukkit.getScheduler().runTaskLater(plugin, ((Player) event.getWhoClicked())::updateInventory, 1L);
                }
            }
        }
    }

    private void dragErase(InventoryDragEvent event) {
        InventoryAction action;
        Inventory clicked = InventoryUtil.getClickedInventory(event);

        if (clicked == event.getView().getTopInventory()) {
            action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
        } else {
            action = (event.getCursor() == null || event.getCursor().getAmount() == 0) ? InventoryAction.PLACE_ALL : InventoryAction.PLACE_SOME;
        }

        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            boolean erased = false;
            for (Map.Entry<Integer, ItemStack> kvp : event.getNewItems().entrySet()) {
                if (eraseLore(kvp.getValue())) {
                    erased = true;
                }
            }
            if (erased) {
                Bukkit.getScheduler().runTaskLater(plugin, ((Player) event.getWhoClicked())::updateInventory, 1L);
            }
        } else if (
                action == InventoryAction.HOTBAR_MOVE_AND_READD
                        || action == InventoryAction.HOTBAR_SWAP
                        || action == InventoryAction.PLACE_ALL
                        || action == InventoryAction.PLACE_ONE
                        || action == InventoryAction.PLACE_SOME
                        || action == InventoryAction.SWAP_WITH_CURSOR
        ) {
            if (clicked == event.getView().getTopInventory()) {
                boolean erased = false;
                for (Map.Entry<Integer, ItemStack> kvp : event.getNewItems().entrySet()) {
                    if (eraseLore(kvp.getValue())) {
                        erased = true;
                    }
                }
                if (erased) {
                    Bukkit.getScheduler().runTaskLater(plugin, ((Player) event.getWhoClicked())::updateInventory, 1L);
                }
            }
        }
    }

    private void moveErase(InventoryMoveItemEvent event) { eraseLore(event.getItem()); }

    private void dropErase(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (eraseLore(item)) {
            event.getItemDrop().setItemStack(item);
        }
    }

    private boolean hasLore(ItemStack item) {
        if (item == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty()) {
            return false;
        }

        for (String v : lore) {
            if (ChatColor.stripColor(v).trim().startsWith("Command to run:")) {
                return true;
            }
        }

        return false;
    }

    private boolean eraseLore(ItemStack item) {
        if (item == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty()) {
            return false;
        }

        boolean retVal = lore.removeIf(v -> ChatColor.stripColor(v).trim().startsWith("Command to run:"));

        if (retVal) {
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return retVal;
    }
}
