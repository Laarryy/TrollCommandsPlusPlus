package me.egg82.tcpp.events.block.blockPlace;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.registries.RandomBuildRegistry;
import ninja.egg82.filters.EnumFilter;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.MathUtil;

public class RandomBuildEventCommand extends EventHandler<BlockPlaceEvent> {
	//vars
	private IVariableRegistry<UUID> randomBuildRegistry = ServiceLocator.getService(RandomBuildRegistry.class);
	
	private Material[] materials = null;

	//constructor
	public RandomBuildEventCommand() {
		super();
		
		EnumFilter<Material> materialFilterHelper = new EnumFilter<Material>(Material.class);
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
			materialFilterHelper.filter(
			materialFilterHelper.filter(
				materialFilterHelper.getAllTypes(),
			"_item", false),
			"barrier", false),
			"air", false),
			"stationary_", false),
			"piston_", false),
			"mob_spawner", false),
			"torch_on", false),
			"command_", false),
			"sponge", false),
			"bedrock", false),
			"_portal", false),
			"sign", false);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (randomBuildRegistry.hasRegister(uuid)) {
			int tries = 0;
			do {
				event.getBlock().setType(materials[MathUtil.fairRoundedRandom(0, materials.length - 1)]);
				tries++;
			} while (event.getBlock().getType() == Material.AIR && tries <= 100);
		}
	}
}
