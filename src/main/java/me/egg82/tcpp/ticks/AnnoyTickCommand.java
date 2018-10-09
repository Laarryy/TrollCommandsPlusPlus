package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.egg82.tcpp.lists.AnnoySet;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.filters.EnumFilter;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.utils.MathUtil;

public class AnnoyTickCommand extends TickHandler {
    //vars
    private IConcurrentSet<UUID> annoySet = ServiceLocator.getService(AnnoySet.class);

    private Sound[] sounds = null;

    //constructor
    public AnnoyTickCommand() {
        super(0L, 20L);

        EnumFilter<Sound> soundFilterHelper = new EnumFilter<Sound>(Sound.class);
        sounds = soundFilterHelper.whitelist("villager").blacklist("zombie").build();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        for (UUID uuid : annoySet) {
            e(CommandUtil.getPlayerByUuid(uuid));
        }
    }

    private void e(Player player) {
        if (player == null) {
            return;
        }

        if (Math.random() <= 0.2d) {
            player.playSound(player.getLocation(), sounds[MathUtil.fairRoundedRandom(0, sounds.length - 1)], 1.0f, (float) MathUtil.random(0.5d, 2.0d));
        }
    }
}
