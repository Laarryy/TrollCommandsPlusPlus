package me.egg82.tcpp.events.individual.blockBreakEvent;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.TrollCommandsPlusPlus;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.ReflectionUtil;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand {
	//vars
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.LAG_REGISTRY);
	
	//constructor
	public LagEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		BlockBreakEvent e = (BlockBreakEvent) event;
		
		if (lagRegistry.contains(e.getPlayer().getName().toLowerCase())) {
			e.setCancelled(true);
			
			Player player = e.getPlayer();
			ItemStack stack = ReflectionUtil.getItemInMainHand(player);
			GameMode mode = player.getGameMode();
			
			//Bit of a hack.
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TrollCommandsPlusPlus.getInstance(), new Runnable() {
				public void run() {
					if (mode == GameMode.CREATIVE) {
						e.getBlock().setType(Material.AIR);
					} else {
						e.getBlock().breakNaturally(stack);
					}
				}
			}, MathUtil.fairRoundedRandom(40, 60));
		}
	}
}
