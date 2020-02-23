package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfinityTroll extends BukkitTroll {
    private static final Map<Player, Double> lightyear = new HashMap<>();

    public InfinityTroll(UUID playerID, TrollType type) {
        super(playerID, type);
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }

        lightyear.put(player, player.getLocation().getY());
        issuer.sendInfo(Message.INFINITY__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        lightyear.remove(player);
        issuer.sendInfo(Message.INFINITY__STOP, "{player}", player.getName());
    }

    public static Map<Player, Double> getLightyear() {
        return lightyear;
    }
}
