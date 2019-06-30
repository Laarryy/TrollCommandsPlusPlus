package me.egg82.tcpp.commands.internal;

import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainAbortAction;
import java.io.IOException;
import java.util.UUID;
import me.egg82.tcpp.services.lookup.PlayerLookup;
import me.egg82.tcpp.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCommand implements Runnable {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final TaskChain<?> chain;
    protected final CommandSender sender;

    protected BaseCommand(TaskChain<?> chain, CommandSender sender) {
        this.chain = chain;
        this.sender = sender;
    }

    protected BaseCommand(CommandSender sender) {
        this.chain = null;
        this.sender = sender;
    }

    protected TaskChain<UUID> getChain(String playerName) {
        return chain
                .<UUID>asyncCallback((v, f) -> f.accept(getPlayerUUID(playerName)))
                .abortIfNull(new TaskChainAbortAction<Object, Object, Object>() {
                    @Override
                    public void onAbort(TaskChain<?> chain, Object arg1) {
                        sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "Could not get UUID for " + ChatColor.WHITE + playerName + ChatColor.DARK_RED + " (rate-limited?)");
                    }
                });
    }

    protected boolean isOfflineOrImmune(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) {
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "Player could not be found.");
            return true;
        }
        if (p.hasPermission("tcpp.immune")) {
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "Player is immune.");
            return true;
        }
        return false;
    }

    protected String getPlayerName(UUID uuid) {
        try {
            return PlayerLookup.get(uuid).getName();
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    protected UUID getPlayerUUID(String name) {
        try {
            return PlayerLookup.get(name).getUUID();
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }
}
