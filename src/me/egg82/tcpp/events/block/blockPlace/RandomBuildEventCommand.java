package me.egg82.tcpp.events.block.blockPlace;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.services.RandomBuildRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.reflection.type.TypeFilterHelper;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.utils.MathUtil;

public class RandomBuildEventCommand extends EventCommand<BlockPlaceEvent> {
	//vars
	private IRegistry<UUID> randomBuildRegistry = ServiceLocator.getService(RandomBuildRegistry.class);
	
	private Material[] materials = null;

	//constructor
	public RandomBuildEventCommand(BlockPlaceEvent event) {
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
				BlockUtil.setBlock(event.getBlock(), new BlockData(null, null, materials[MathUtil.fairRoundedRandom(0, materials.length - 1)]), true);
				tries++;
			} while (event.getBlock().getType() == Material.AIR && tries <= 100);
		}
	}
}
