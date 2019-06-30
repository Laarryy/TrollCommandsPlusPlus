package me.egg82.tcpp.events.inventory.inventoryMoveItem;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import java.util.function.Consumer;
import me.egg82.tcpp.extended.ServiceKeys;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryMoveItemAttach implements Consumer<InventoryMoveItemEvent> {
    public InventoryMoveItemAttach() { }

    public void accept(InventoryMoveItemEvent event) {
        tryRunCommand(event.getItem());
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
