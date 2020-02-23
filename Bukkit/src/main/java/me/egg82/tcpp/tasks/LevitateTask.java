package me.egg82.tcpp.tasks;

import me.egg82.tcpp.APIException;
import me.egg82.tcpp.TrollAPI;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.api.trolls.LevitateTroll;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;

public class LevitateTask implements Runnable {
    private TrollAPI api;

    public LevitateTask(TrollAPI api) {
        this.api = api;
    }

    @Override
    public void run() {
        Iterator<Player> flyerIterator = LevitateTroll.getFlyers().iterator();
        while (flyerIterator.hasNext()) {
            Player target = flyerIterator.next();

            if (!target.isDead() && target.isValid() && target.isOnline()) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 200, 1, false, false), true);
            }
            else {
                try {
                    api.stopTroll(target.getUniqueId(), TrollType.LEVITATE, null);
                }
                catch (APIException ex) {
                    ex.printStackTrace();
                }
                flyerIterator.remove();
            }
        }
    }
}
