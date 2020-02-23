package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SnowballFightTroll extends BukkitTroll {
    private static final Set<UUID> snowy = new HashSet<>();

    public SnowballFightTroll(UUID playerID, TrollType type) {
        super(playerID, type);
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }

        snowy.add(playerID);
        issuer.sendInfo(Message.SNOWBALLFIGHT__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        snowy.remove(playerID);
        issuer.sendInfo(Message.SNOWBALLFIGHT__STOP, "{player}", player.getName());
    }

    public static Set<UUID> getSnowy() {
        return snowy;
    }
}
