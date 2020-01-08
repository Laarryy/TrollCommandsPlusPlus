package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import me.egg82.tcpp.services.entity.EntityItemHandler;
import ninja.egg82.service.ServiceLocator;
import ninja.egg82.service.ServiceNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AttachTroll extends BukkitTroll {
    private final String command;
    private final EntityItemHandler entityItemHandler;

    public static final String ATTACH_COMPOUND_NAME = "tcppCommand";

    public AttachTroll(UUID playerID, String command, TrollType type) throws InstantiationException, IllegalAccessException, ServiceNotFoundException {
        super(playerID, type);
        this.command = command;

        entityItemHandler = ServiceLocator.get(EntityItemHandler.class);
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }

        Optional<ItemStack> mainHand = entityItemHandler.getItemInMainHand(player);
        Optional<ItemStack> offHand = entityItemHandler.getItemInOffHand(player);

        NbtCompound compound;
        if (mainHand.isPresent()) {
            compound = NbtFactory.asCompound(NbtFactory.fromItemTag(mainHand.get()));
            if (compound.containsKey(ATTACH_COMPOUND_NAME)) {
                rewriteLore(mainHand.get(), null);
            } else {
                rewriteLore(mainHand.get(), String.join(" ", command));
            }
            compound = NbtFactory.asCompound(NbtFactory.fromItemTag(mainHand.get()));
        } else if (offHand.isPresent()) {
            compound = NbtFactory.asCompound(NbtFactory.fromItemTag(offHand.get()));
            if (compound.containsKey(ATTACH_COMPOUND_NAME)) {
                rewriteLore(offHand.get(), null);
            } else {
                rewriteLore(offHand.get(), String.join(" ", command));
            }
            compound = NbtFactory.asCompound(NbtFactory.fromItemTag(offHand.get()));
        } else {
            issuer.sendError(Message.ATTACH__NEED_ITEM);
            api.stopTroll(this, null);
            return;
        }

        if (compound.containsKey(ATTACH_COMPOUND_NAME)) {
            compound.remove(ATTACH_COMPOUND_NAME);
            issuer.sendInfo(Message.ATTACH__STOP);
        } else {
            compound.put(ATTACH_COMPOUND_NAME, String.join(" ", command));
            issuer.sendInfo(Message.ATTACH__START, "{command}", command);
        }

        if (mainHand.isPresent()) {
            NbtFactory.setItemTag(mainHand.get(), compound);
        } else {
            NbtFactory.setItemTag(offHand.get(), compound);
        }

        api.stopTroll(this, null);
    }

    private void rewriteLore(ItemStack item, String command) {
        ItemMeta meta = getMeta(item);
        List<String> lore = !meta.hasLore() ? new ArrayList<>() : meta.getLore();

        if (command == null) {
            lore.removeIf(v -> ChatColor.stripColor(v).trim().startsWith("Command to run:"));
        } else {
            lore.add(ChatColor.RESET + "Command to run: " + ChatColor.GRAY + ChatColor.ITALIC + "/" + command);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private static ItemMeta getMeta(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(meta);
        }
        return meta;
    }
}
