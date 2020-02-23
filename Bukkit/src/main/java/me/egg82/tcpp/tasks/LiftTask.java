package me.egg82.tcpp.tasks;

import me.egg82.tcpp.APIException;
import me.egg82.tcpp.TrollAPI;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.api.trolls.LiftTroll;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Iterator;

public class LiftTask implements Runnable {
    private TrollAPI api;

    public LiftTask(TrollAPI api) {
        this.api = api;
    }

    @Override
    public void run() {
        Iterator<Player> flyerIterator = LiftTroll.getFlyers().iterator();
        while (flyerIterator.hasNext()) {
            Player target = flyerIterator.next();

            if (!target.isDead() && target.isValid() && target.isOnline()) {
                Vector velocity = target.getVelocity();
                velocity.setY(5);
                target.setVelocity(velocity);
            }
            else {
                try {
                    api.stopTroll(target.getUniqueId(), TrollType.LIFT, null);
                }
                catch (APIException ex) {
                    ex.printStackTrace();
                }
                flyerIterator.remove();
            }
        }
    }
}
