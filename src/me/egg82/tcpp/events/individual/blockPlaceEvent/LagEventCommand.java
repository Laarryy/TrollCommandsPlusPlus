package me.egg82.tcpp.events.individual.blockPlaceEvent;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.TrollCommandsPlusPlus;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
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
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		
		if (lagRegistry.contains(e.getPlayer().getName().toLowerCase())) {
			e.setCancelled(true);
			
			Player player = e.getPlayer();
			ItemStack stack = player.getInventory().getItemInMainHand();
			Block block = e.getBlock();
			Material material = e.getBlockPlaced().getType();
			GameMode mode = player.getGameMode();
			
			//Bit of a hack.
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TrollCommandsPlusPlus.getInstance(), new Runnable() {
				public void run() {
					block.setType(material);
					if (mode != GameMode.CREATIVE) {
						int amount = stack.getAmount();
						if (amount == 1) {
							player.getInventory().setItemInMainHand(null);
						} else {
							stack.setAmount(amount - 1);
						}
					}
				}
			}, MathUtil.fairRoundedRandom(40, 60));
		}
	}
}
