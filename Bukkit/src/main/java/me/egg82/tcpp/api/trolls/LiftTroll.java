package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class LiftTroll extends BukkitTroll {
    public LiftTroll(UUID playerID, TrollType type) {
        super(playerID, type);
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }

        Vector velocity = player.getVelocity();
        velocity.setY(25);
        player.setVelocity(velocity);
        issuer.sendInfo(Message.LIFT__START, "{player}", player.getName());
        api.stopTroll(this, issuer);
    }
}
