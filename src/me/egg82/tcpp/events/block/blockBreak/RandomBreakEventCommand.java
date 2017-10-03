package me.egg82.tcpp.events.block.blockBreak;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.services.registries.RandomBreakRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.player.IPlayerHelper;
import ninja.egg82.plugin.reflection.type.TypeFilterHelper;
import ninja.egg82.utils.MathUtil;

public class RandomBreakEventCommand extends EventCommand<BlockBreakEvent> {
	//vars
	private IRegistry<UUID> randomBreakRegistry = ServiceLocator.getService(RandomBreakRegistry.class);
	
	private IPlayerHelper playerHelper = ServiceLocator.getService(IPlayerHelper.class);
	private Material[] materials = null;
	
	//constructor
	public RandomBreakEventCommand(BlockBreakEvent event) {
		super(event);
		
		TypeFilterHelper<Material> materialFilterHelper = new TypeFilterHelper<Material>(Material.class);
		materials = materialFilterHelper.filter(
			materialFilterHelper.filter(
			materialFilterHelper.filter(
			materialFilterHelper.filter(
			materialFilterHelper.filter(
			materialFilterHelper.filter(
			materialFilterHelper.filter(
			materialFilterHelper.filter(
			materialFilterHelper.filter(
			materialFilterHelper.filter(
				materialFilterHelper.getAllTypes(),
			"_block", false),
			"barrier", false),
			"air", false),
			"stationary_", false),
			"piston_", false),
			"mob_spawner", false),
			"torch_on", false),
			"command_", false),
			"sponge", false),
			"bedrock", false);
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
