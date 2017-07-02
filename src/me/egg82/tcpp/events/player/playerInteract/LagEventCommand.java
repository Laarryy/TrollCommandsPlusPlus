package me.egg82.tcpp.events.player.playerInteract;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.services.LagBlockRegistry;
import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand {
	//vars
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(LagRegistry.class);
	private IRegistry lagBlockRegistry = (IRegistry) ServiceLocator.getService(LagBlockRegistry.class);
	private JavaPlugin plugin = (JavaPlugin) ((IRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister("plugin");
	
	//constauctor
	public LagEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerInteractEvent e = (PlayerInteractEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		
		Block block = e.getClickedBlock();
		Location blockLocation = block.getLocation();
		String locationString = blockLocation.getWorld() + "," + blockLocation.getX() + "," + blockLocation.getY() + "," + blockLocation.getZ();
		
		// Block is currently being lagged. Nobody should interact with it.
		if (lagBlockRegistry.hasRegister(locationString)) {
			e.setCancelled(true);
			return;
		}
		if (!lagRegistry.hasRegister(player.getUniqueId().toString())) {
			return;
		}
		
		// Create a "snapshot" of what's happening
		BlockState blockState = block.getState();
		Action action = e.getAction();
		
		// Either BlockBreakEvent or BlockPlaceEvent
		if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR || (action == Action.RIGHT_CLICK_BLOCK && !(blockState instanceof InventoryHolder))) {
			return;
		}
		
		e.setCancelled(true);
		lagBlockRegistry.setRegister(locationString, Location.class, blockLocation);
		
		// Manually doing the event after 1.5-2.5 seconds
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				// Opened inventory event
				if (action == Action.RIGHT_CLICK_BLOCK && blockState instanceof InventoryHolder) {
					player.openInventory(((InventoryHolder) blockState).getInventory());
				}
				
				lagBlockRegistry.setRegister(locationString, Location.class, null);
			}
		}, MathUtil.fairRoundedRandom(30, 50));
	}
}
