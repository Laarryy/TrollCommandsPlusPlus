package me.egg82.tcpp.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import me.egg82.tcpp.TrollCommandsPlusPlus;
import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.MetadataType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.registry.interfaces.IRegistry;

public class VegetableCommand extends BasePluginCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_REGISTRY);
	private IRegistry reg2 = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_INTERN_REGISTRY);
	
	//constructor
	public VegetableCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	@SuppressWarnings("unchecked")
	public void onQuit(String name, Player player) {
		reg.setRegister(name, null);
		
		if (reg2.contains(name)) {
			HashMap<String, Object> map = (HashMap<String, Object>) reg2.getRegister(name);
			Item potato = (Item) map.get("item");
			
			player.setGameMode((GameMode) map.get("mode"));
			
			reg2.setRegister(name, null);
			
			potato.remove();
		}
	}
	public void onDeath(String name, Player player) {
		onQuit(name, player);
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_VEGETABLE, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	@SuppressWarnings("unchecked")
	private void e(String name, Player player) {
		Location loc = player.getLocation();
		Item potato = null;
		HashMap<String, Object> map = null;
		
		if (reg.contains(name.toLowerCase())) {
			sender.sendMessage(name + " is no longer a potato.");
			
			reg.setRegister(name.toLowerCase(), null);
			
			player.teleport(BlockUtil.getTopAirBlock(loc));
			
			map = (HashMap<String, Object>) reg2.getRegister(name.toLowerCase());
			potato = (Item) map.get("item");
			
			if (player != null) {
				player.setGameMode((GameMode) map.get("mode"));
			}
			
			reg2.setRegister(name.toLowerCase(), null);
			
			if (potato != null) {
				potato.remove();
			}
		} else {
			sender.sendMessage(name + " is now a potato.");
			
			ItemStack potatoStack = new ItemStack(Material.POTATO_ITEM, 1);
			ItemMeta meta = potatoStack.getItemMeta();
			meta.setDisplayName(name);
			potatoStack.setItemMeta(meta);
			
			potato = loc.getWorld().dropItem(loc, potatoStack);
			potato.setMetadata(MetadataType.VEGETABLE, new FixedMetadataValue(TrollCommandsPlusPlus.getInstance(), true));
			
			map = new HashMap<String, Object>();
			map.put("mode", player.getGameMode());
			map.put("item", potato);
			reg2.setRegister(name.toLowerCase(), map);
			player.setGameMode(GameMode.SPECTATOR);
			player.teleport(new Location(loc.getWorld(), loc.getX(), loc.getBlockY() - 1.0d, loc.getZ()));
			
			reg.setRegister(name.toLowerCase(), player);
		}
	}
}
