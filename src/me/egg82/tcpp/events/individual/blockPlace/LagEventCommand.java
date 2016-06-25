package me.egg82.tcpp.events.individual.blockPlace;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.enums.ServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.enums.SpigotReflectType;
import ninja.egg82.plugin.enums.SpigotRegType;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.reflection.player.interfaces.IPlayerUtil;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand {
	//vars
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.LAG_REGISTRY);
	private IRegistry initRegistry = (IRegistry) ServiceLocator.getService(ServiceType.INIT_REGISTRY);
	private IPlayerUtil playerUtil = (IPlayerUtil) ((IRegistry) ServiceLocator.getService(SpigotServiceType.REFLECT_REGISTRY)).getRegister(SpigotReflectType.PLAYER);
	
	//constructor
	public LagEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		
		if (lagRegistry.contains(e.getPlayer().getName().toLowerCase())) {
			e.setCancelled(true);
			
			Player player = e.getPlayer();
			ItemStack stack = playerUtil.getItemInMainHand(player);
			Block block = e.getBlock();
			Material material = e.getBlockPlaced().getType();
			GameMode mode = player.getGameMode();
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) initRegistry.getRegister(SpigotRegType.PLUGIN), new Runnable() {
				public void run() {
					block.setType(material);
					if (mode != GameMode.CREATIVE) {
						int amount = stack.getAmount();
						if (amount == 1) {
							playerUtil.setItemInMainHand(player, null);
						} else {
							stack.setAmount(amount - 1);
						}
					}
				}
			}, MathUtil.fairRoundedRandom(40, 60));
		}
	}
}
