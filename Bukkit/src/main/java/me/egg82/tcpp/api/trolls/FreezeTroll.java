package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.APIException;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.UUID;

public class FreezeTroll extends BukkitTroll {
    private final Plugin plugin;
    private Location location;
    private float walkSpeed;
    private float flySpeed;
    private Vector oldVelocity;
    private boolean oldSprinting;
    private boolean oldFlyEnabled;
    private boolean oldFlying;
    private CommandIssuer originalIssuer;

    public FreezeTroll(Plugin plugin, UUID playerID, TrollType type) {
        super(playerID, type);

        this.plugin = plugin;
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }
        originalIssuer = issuer;

        location = player.getLocation().clone();
        walkSpeed = player.getWalkSpeed();
        flySpeed = player.getFlySpeed();
        oldSprinting = player.isSprinting();
        oldVelocity = player.getVelocity().clone();
        oldFlyEnabled = player.getAllowFlight();
        oldFlying = player.isFlying();
        player.setVelocity(new Vector(0, 0, 0));
        player.setWalkSpeed(0);
        player.setFlySpeed(0);
        player.setSprinting(false);
        player.setAllowFlight(true);
        player.setFlying(true);

        tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::freezePlayer, 5, 20));

        issuer.sendInfo(Message.FREEZE__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        player.setWalkSpeed(walkSpeed);
        player.setFlySpeed(flySpeed);
        player.setVelocity(oldVelocity);
        player.setAllowFlight(oldFlyEnabled);
        player.setFlying(oldFlying);
        player.setSprinting(oldSprinting);
        issuer.sendInfo(Message.FREEZE__STOP, "{player}", player.getName());
    }

    private void freezePlayer() {
        Player player = Bukkit.getPlayer(playerID);

        if (player != null && !player.isDead() && player.isValid() && player.isOnline()) {
            player.teleport(location);
            player.setFlying(true);
        }
        else {
            try {
                api.stopTroll(playerID, TrollType.FREEZE, originalIssuer);
            }
            catch (APIException ex) {
                ex.printStackTrace();
            }
        }
    }
}
