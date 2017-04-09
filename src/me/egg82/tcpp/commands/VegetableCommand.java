package me.egg82.tcpp.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.MetadataType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.enums.ServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.enums.SpigotRegType;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.registry.interfaces.IRegistry;

public class VegetableCommand extends BasePluginCommand {
	//vars
	private IRegistry initReg = (IRegistry) ServiceLocator.getService(ServiceType.INIT_REGISTRY);
	private IRegistry vegetableRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_REGISTRY);
	private IRegistry vegetableInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_INTERN_REGISTRY);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public VegetableCommand() {
		super();
	}
	
	//public
	@SuppressWarnings("unchecked")
	public void onQuit(String uuid, Player player) {
		vegetableRegistry.computeIfPresent(uuid, (k,v) -> {
			return null;
		});
		
		vegetableInternRegistry.computeIfPresent(uuid, (k,v) -> {
			HashMap<String, Object> map = (HashMap<String, Object>) v;
			Item potato = (Item) map.get("item");
			
			player.setGameMode((GameMode) map.get("mode"));
			potato.remove();
			
			return null;
		});
	}
	public void onDeath(String uuid, Player player) {
		onQuit(uuid, player);
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_VEGETABLE, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	@SuppressWarnings("unchecked")
	private void e(String uuid, Player player) {
		Location loc = player.getLocation();
		
		if (vegetableRegistry.contains(uuid)) {
			sender.sendMessage(player.getName() + " is no longer a potato.");
			
			vegetableRegistry.setRegister(uuid, null);
			
			player.teleport(BlockUtil.getTopAirBlock(loc));
			
			vegetableInternRegistry.computeIfPresent(uuid, (k,v) -> {
				HashMap<String, Object> map = (HashMap<String, Object>) v;
				Item potato = (Item) map.get("item");
				
				if (player != null) {
					player.setGameMode((GameMode) map.get("mode"));
				}
				
				if (potato != null) {
					potato.remove();
				}
				
				return null;
			});
		} else {
			sender.sendMessage(player.getName() + " is now a potato.");
			
			ItemStack potatoStack = new ItemStack(Material.POTATO_ITEM, 1);
			ItemMeta meta = potatoStack.getItemMeta();
			meta.setDisplayName(player.getDisplayName());
			potatoStack.setItemMeta(meta);
			
			Item potato = loc.getWorld().dropItem(loc, potatoStack);
			potato.setMetadata(MetadataType.VEGETABLE, new FixedMetadataValue((Plugin) initReg.getRegister(SpigotRegType.PLUGIN), true));
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("mode", player.getGameMode());
			map.put("item", potato);
			vegetableInternRegistry.setRegister(uuid, map);
			player.setGameMode(GameMode.SPECTATOR);
			player.teleport(new Location(loc.getWorld(), loc.getX(), loc.getBlockY() - 1.0d, loc.getZ()));
			
			vegetableRegistry.setRegister(uuid, player);
		}
	}
}
