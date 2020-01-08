package me.egg82.tcpp.api;

import co.aikar.commands.CommandIssuer;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.egg82.tcpp.hooks.TownyHook;
import ninja.egg82.events.BukkitEventSubscriber;
import ninja.egg82.service.ServiceLocator;
import ninja.egg82.service.ServiceNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class BukkitTroll extends Troll {
    private static final List<BukkitTroll> activeTrolls = new ArrayList<>();

    protected final List<BukkitEventSubscriber<?>> events = new ArrayList<>();
    protected final List<Integer> tasks = new ArrayList<>();

    public final int numEvents() { return events.size(); }

    public BukkitTroll(UUID playerID, TrollType type) {
        super(playerID, type);
        activeTrolls.add(this);
    }

    public void stop(CommandIssuer issuer) throws Exception {
        for (BukkitEventSubscriber<?> event : events) {
            event.cancel();
        }
        for (Integer task : tasks) {
            Bukkit.getScheduler().cancelTask(task);
        }
        activeTrolls.remove(this);
    }

    public static ImmutableList<BukkitTroll> getActiveTrolls() { return ImmutableList.copyOf(activeTrolls); }

    protected final boolean townyIgnoreCancelled(EntityDamageByEntityEvent event) {
        try {
            TownyHook townyHook = ServiceLocator.get(TownyHook.class);
            return townyHook.ignoreCancelled(event);
        } catch (InstantiationException | IllegalAccessException | ServiceNotFoundException ignored) { return true; }
    }
}
