package me.egg82.tcpp.utils;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DisguiseUtil {
    private final boolean isEnabled;
    private final boolean hasLibsDisguises;
    private final boolean hasIDisguise;
    private static final DisguiseUtil i = new DisguiseUtil();

    private DisguiseUtil() {
        hasLibsDisguises = Bukkit.getPluginManager().isPluginEnabled("LibsDisguises");
        hasIDisguise = Bukkit.getPluginManager().isPluginEnabled("iDisguise");
        isEnabled = hasLibsDisguises || hasIDisguise;
    }

    public static DisguiseUtil get() {
        return i;
    }

    public boolean disguiseAsPlayer(Player toDisguise, Player disguise) {
        if (isEnabled) {
            if (hasLibsDisguises) {
                PlayerDisguise pd = new PlayerDisguise(disguise);
                DisguiseAPI.disguiseToAll(toDisguise, pd);
            }
            else {
                throw new UnsupportedOperationException("Support for iDisguise not yet implemented");
            }
        }

        return isEnabled;
    }

    public boolean undisguisePlayer(Player player) {
        if (isEnabled) {
            if (hasLibsDisguises) {
                if (DisguiseAPI.isDisguised(player))
                    DisguiseAPI.undisguiseToAll(player);
            }
            else {
                throw new UnsupportedOperationException("Support for iDisguise not yet implemented");
            }
        }

        return isEnabled;
    }
}
