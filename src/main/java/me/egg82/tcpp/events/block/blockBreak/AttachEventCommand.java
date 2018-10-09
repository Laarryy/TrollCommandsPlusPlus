package me.egg82.tcpp.events.block.blockBreak;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import ninja.egg82.bukkit.reflection.entity.IEntityHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.nbt.core.INBTCompound;
import ninja.egg82.nbt.reflection.INBTHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class AttachEventCommand extends EventHandler<BlockBreakEvent> {
    //vars
    private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
    private IEntityHelper entityHelper = ServiceLocator.getService(IEntityHelper.class);

    //constructor
    public AttachEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        Player player = event.getPlayer();

        INBTCompound blockCompound = nbtHelper.getCompound(block);
        if (!blockCompound.hasTag("tcppCommand")) {
            return;
        }

        event.setCancelled(true);

        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
            Collection<ItemStack> drops = event.getBlock().getDrops(entityHelper.getItemInMainHand(player));
            for (ItemStack item : drops) {
                INBTCompound itemCompound = nbtHelper.getCompound(item);
                itemCompound.setString("tcppSender", blockCompound.getString("tcppSender"));
                itemCompound.setString("tcppCommand", blockCompound.getString("tcppCommand"));
                player.getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
            }
        } else {
            Player sender = CommandUtil.getPlayerByUuid(blockCompound.getString("tcppSender"));
            if (sender != null) {
                CommandUtil.dispatchCommandAtSenderLocation(sender, player, blockCompound.getString("tcppCommand"));
            } else {
                if (CommandUtil.getOfflinePlayerByUuid(blockCompound.getString("tcppSender")).isOp()) {
                    CommandUtil.dispatchCommandAtSenderLocation(Bukkit.getConsoleSender(), player, blockCompound.getString("tcppCommand"));
                } else {
                    Bukkit.dispatchCommand(player, blockCompound.getString("tcppCommand"));
                }
            }
        }

        blockCompound.removeTag("tcppSender");
        blockCompound.removeTag("tcppCommand");
        block.setType(Material.AIR);
    }
}
