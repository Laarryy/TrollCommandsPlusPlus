package me.egg82.tcpp.commands.internal;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import java.util.*;
import me.egg82.tcpp.extended.ServiceKeys;
import me.egg82.tcpp.services.AnalyticsHelper;
import me.egg82.tcpp.services.entity.EntityItemHandler;
import me.egg82.tcpp.utils.LogUtil;
import ninja.egg82.service.ServiceLocator;
import ninja.egg82.service.ServiceNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AttachCommand extends BaseCommand {
    private final String[] topic;

    public AttachCommand(CommandSender sender, String... topic) {
        super(sender);
        this.topic = topic;
    }

    public void run() {
        if (!(sender instanceof LivingEntity)) {
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "Console cannot run this command!");
            return;
        }

        EntityItemHandler entityItemHandler;
        try {
            entityItemHandler = ServiceLocator.get(EntityItemHandler.class);
        } catch (InstantiationException | IllegalAccessException | ServiceNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return;
        }

        Optional<ItemStack> mainHand = entityItemHandler.getItemInMainHand((LivingEntity) sender);
        Optional<ItemStack> offHand = entityItemHandler.getItemInOffHand((LivingEntity) sender);

        NbtCompound compound;
        if (mainHand.isPresent()) {
            compound = NbtFactory.asCompound(NbtFactory.fromItemTag(mainHand.get()));
            if (compound.containsKey(ServiceKeys.ATTACH_COMPOUND_NAME)) {
                rewriteLore(mainHand.get(), null);
            } else {
                rewriteLore(mainHand.get(), String.join(" ", topic));
            }
            compound = NbtFactory.asCompound(NbtFactory.fromItemTag(mainHand.get()));
        } else if (offHand.isPresent()) {
            compound = NbtFactory.asCompound(NbtFactory.fromItemTag(offHand.get()));
            if (compound.containsKey(ServiceKeys.ATTACH_COMPOUND_NAME)) {
                rewriteLore(offHand.get(), null);
            } else {
                rewriteLore(offHand.get(), String.join(" ", topic));
            }
            compound = NbtFactory.asCompound(NbtFactory.fromItemTag(offHand.get()));
        } else {
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "You must be holding an item first!");
            return;
        }

        if (compound.containsKey(ServiceKeys.ATTACH_COMPOUND_NAME)) {
            compound.remove(ServiceKeys.ATTACH_COMPOUND_NAME);
            sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + "There is no longer a command attached to your held item.");
        } else {
            compound.put(ServiceKeys.ATTACH_COMPOUND_NAME, String.join(" ", topic));
            AnalyticsHelper.incrementCommand("attach");
            sender.sendMessage(LogUtil.getHeading() + ChatColor.WHITE + "\"/" + String.join(" ", topic) + "\" is now attached to your held item!");
        }

        if (mainHand.isPresent()) {
            NbtFactory.setItemTag(mainHand.get(), compound);
        } else if (offHand.isPresent()) {
            NbtFactory.setItemTag(offHand.get(), compound);
        }
    }

    private void rewriteLore(ItemStack item, String command) {
        ItemMeta meta = getMeta(item);
        List<String> lore = getLore(meta);

        if (command == null) {
            lore.removeIf(v -> ChatColor.stripColor(v).trim().startsWith("Command to run: "));
        } else {
            lore.add(ChatColor.RESET + "Command to run: " + ChatColor.GRAY + ChatColor.ITALIC + "/" + command);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private ItemMeta getMeta(ItemStack item) {
        ItemMeta retVal = item.getItemMeta();
        if (retVal != null) {
            return retVal;
        }
        return Bukkit.getItemFactory().getItemMeta(item.getType());
    }

    private List<String> getLore(ItemMeta meta) {
        if (meta.hasLore()) {
            return meta.getLore();
        }
        return new ArrayList<>();
    }
}
