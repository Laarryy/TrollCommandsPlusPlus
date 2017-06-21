package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.services.RandomBuildRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.MaterialHelper;
import ninja.egg82.utils.MathUtil;

public class RandomBuildEventCommand extends EventCommand {
	//vars
	private IRegistry randomBuildRegistry = (IRegistry) ServiceLocator.getService(RandomBuildRegistry.class);
	
	private MaterialHelper materialHelper = (MaterialHelper) ServiceLocator.getService(MaterialHelper.class);
	private Material[] materials = null;

	//constructor
	public RandomBuildEventCommand(Event event) {
		super(event);
		materials = materialHelper.filter(
			materialHelper.filter(
			materialHelper.filter(
			materialHelper.filter(
			materialHelper.filter(
			materialHelper.filter(
			materialHelper.filter(
			materialHelper.filter(
			materialHelper.filter(
			materialHelper.filter(
				materialHelper.getAllMaterials(),
			"_item", false),
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
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (randomBuildRegistry.hasRegister(uuid)) {
			int tries = 0;
			do {
				BlockUtil.setBlock(e.getBlock(), new BlockData(null, null, materials[MathUtil.fairRoundedRandom(0, materials.length - 1)]), true);
				tries++;
			} while (e.getBlock().getType() == Material.AIR && tries <= 100);
		}
	}
}
