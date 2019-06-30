package me.egg82.tcpp.events.inventory.inventoryDrag;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import java.util.Map;
import java.util.function.Consumer;
import me.egg82.tcpp.extended.ServiceKeys;
import me.egg82.tcpp.utils.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class InventoryDragAttach implements Consumer<InventoryDragEvent> {
    private final Plugin plugin;

    public InventoryDragAttach(Plugin plugin) {
        this.plugin = plugin;
    }

    public void accept(InventoryDragEvent event) {
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

    private boolean tryRunCommand(ItemStack item) {
        NbtCompound compound;
        try {
            compound = NbtFactory.asCompound(NbtFactory.fromItemTag(item));
        } catch (IllegalArgumentException ignored) { return false; }

        if (!compound.containsKey(ServiceKeys.ATTACH_COMPOUND_NAME)) {
            return false;
        }

        String command = compound.getString(ServiceKeys.ATTACH_COMPOUND_NAME);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command); // TODO: Somehow add @e, @a, etc support
        compound.remove(ServiceKeys.ATTACH_COMPOUND_NAME);
        NbtFactory.setItemTag(item, compound);
        return true;
    }
}
