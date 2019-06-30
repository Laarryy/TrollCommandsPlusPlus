package me.egg82.tcpp.events.player.playerPickupItem;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import java.util.function.Consumer;
import me.egg82.tcpp.extended.ServiceKeys;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerPickupItemAttach implements Consumer<PlayerPickupItemEvent> {
    public PlayerPickupItemAttach() { }

    public void accept(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (tryRunCommand(item)) {
            event.getItem().setItemStack(item);
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
