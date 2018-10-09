package me.egg82.tcpp.ticks;

import java.util.UUID;

import me.egg82.tcpp.registries.SpartaArrowRegistry;
import me.egg82.tcpp.registries.SpartaRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.utils.MathUtil;

public class SpartaTickCommand extends TickHandler {
    //vars
    private IVariableRegistry<UUID> spartaRegistry = ServiceLocator.getService(SpartaRegistry.class);
    private IVariableRegistry<UUID> spartaArrowRegistry = ServiceLocator.getService(SpartaArrowRegistry.class);

    //constructor
    public SpartaTickCommand() {
        super(0L, 10L);
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        for (UUID key : spartaRegistry.getKeys()) {
            e(CommandUtil.getPlayerByUuid(key));
        }
    }

    private void e(Player player) {
        if (player == null) {
            return;
        }

        int numArrows = MathUtil.fairRoundedRandom(5, 10);
        Location playerLocation = player.getLocation().clone();

        for (int i = 0; i < numArrows; i++) {
            Arrow arrow = player.getWorld().spawn(playerLocation.clone().add(MathUtil.random(-10.0d, 10.0d), MathUtil.random(5.0d, 10.0d), MathUtil.random(-10.0d, 10.0d)), Arrow.class);
            spartaArrowRegistry.setRegister(arrow.getUniqueId(), arrow);
            Location arrowLocation = arrow.getLocation();
            Vector arrowAngle = new Vector(playerLocation.getX() - arrowLocation.getX(), playerLocation.getY() - arrowLocation.getY(), playerLocation.getZ() - arrowLocation.getZ());
            arrow.setVelocity(arrowAngle.normalize().multiply(2.0d));
        }
    }
}
