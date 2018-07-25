package me.egg82.tcpp.events.block.blockBreak;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.registries.RandomBreakRegistry;
import ninja.egg82.bukkit.reflection.player.IPlayerHelper;
import ninja.egg82.filters.EnumFilter;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.MathUtil;

public class RandomBreakEventCommand extends EventHandler<BlockBreakEvent> {
	//vars
	private IVariableRegistry<UUID> randomBreakRegistry = ServiceLocator.getService(RandomBreakRegistry.class);
	
	private IPlayerHelper playerHelper = ServiceLocator.getService(IPlayerHelper.class);
	private Material[] materials = null;
	
	//constructor
	public RandomBreakEventCommand() {
		super();
		
		EnumFilter<Material> materialFilterHelper = new EnumFilter<Material>(Material.class);
		materials = materialFilterHelper
				.blacklist("_block")
				.blacklist("barrier")
				.blacklist("air")
				.blacklist("stationary_")
				.blacklist("piston_")
				.blacklist("mob_spawner")
				.blacklist("torch_on")
				.blacklist("command")
				.blacklist("sponge")
				.blacklist("_portal")
				.blacklist("bedrock")
				.build();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (randomBreakRegistry.hasRegister(uuid)) {
			event.setCancelled(true);
			
			if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
				Collection<ItemStack> drops = event.getBlock().getDrops(playerHelper.getItemInMainHand(player));
				for (ItemStack item : drops) {
					player.getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(materials[MathUtil.fairRoundedRandom(0, materials.length - 1)], item.getAmount()));
				}
			}
			
			event.getBlock().setType(Material.AIR);
		}
	}
}
