package me.egg82.tcpp.events.inventory.inventoryClick;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import java.util.function.Consumer;
import me.egg82.tcpp.extended.ServiceKeys;
import me.egg82.tcpp.utils.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class InventoryClickAttach implements Consumer<InventoryClickEvent> {
    private final Plugin plugin;

    public InventoryClickAttach(Plugin plugin) {
        this.plugin = plugin;
    }

    public void accept(InventoryClickEvent event) {
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
