package me.egg82.ae.hooks;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.utils.CombatUtil;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

public class TownyHook implements PluginHook {
    private Towny plugin;

    public TownyHook(Plugin plugin) { this.plugin = (Towny) plugin; }

    public void cancel() { }

    public boolean ignoreCancelled(EntityDamageByEntityEvent event) { return !CombatUtil.preventDamageCall(plugin, event.getDamager(), event.getEntity()); }
}
