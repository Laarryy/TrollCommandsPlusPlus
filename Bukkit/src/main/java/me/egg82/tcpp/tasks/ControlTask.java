package me.egg82.tcpp.tasks;

import me.egg82.tcpp.api.trolls.ControlTroll;
import me.egg82.tcpp.utils.DisguiseUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;

public class ControlTask implements Runnable {
    @Override
    public void run() {
        Iterator<Map.Entry<Player, Player>> puppetIterator = ControlTroll.getPuppets().entrySet().iterator();
        while (puppetIterator.hasNext()) {
            Map.Entry<Player, Player> entry = puppetIterator.next();
            Player controller = entry.getKey();
            Player target = entry.getValue();

            if (!target.isDead()
                    && target.isValid()
                    && target.isOnline()
                    && !controller.isDead()
                    && controller.isValid()
                    && controller.isOnline()) {

                if (target.getGameMode() != GameMode.SPECTATOR) {
                    target.setGameMode(GameMode.SPECTATOR);
                }
                if (target.getSpectatorTarget() == null || !target.getSpectatorTarget().equals(controller))
                    target.setSpectatorTarget(controller);
            }
            else {
                if (!controller.isDead()
                        && controller.isValid()
                        && controller.isOnline()) {
                    DisguiseUtil.get().undisguisePlayer(controller);
                }

                if (!target.isDead()
                        && target.isValid()
                        && target.isOnline()) {
                    target.setGameMode(GameMode.SURVIVAL);
                }

                puppetIterator.remove();
            }
        }
    }
}
