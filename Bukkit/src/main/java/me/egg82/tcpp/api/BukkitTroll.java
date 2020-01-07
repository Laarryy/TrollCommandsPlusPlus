package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.egg82.tcpp.api.Troll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.hooks.TownyHook;
import ninja.egg82.events.BukkitEventSubscriber;
import ninja.egg82.service.ServiceLocator;
import ninja.egg82.service.ServiceNotFoundException;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class BukkitTroll extends Troll {
    protected final CommandIssuer issuer;

    protected final List<BukkitEventSubscriber<?>> events = new ArrayList<>();

    public final int numEvents() { return events.size(); }

    public BukkitTroll(CommandIssuer issuer, UUID playerID, TrollType type) {
        super(playerID, type);
        this.issuer = issuer;
    }

    public void stop() throws Exception {
        for (BukkitEventSubscriber<?> event : events) {
            event.cancel();
        }
    }

    public CommandIssuer getIssuer() { return issuer; }

    protected final boolean townyIgnoreCancelled(EntityDamageByEntityEvent event) {
        try {
            TownyHook townyHook = ServiceLocator.get(TownyHook.class);
            return townyHook.ignoreCancelled(event);
        } catch (InstantiationException | IllegalAccessException | ServiceNotFoundException ignored) { return true; }
    }
}
