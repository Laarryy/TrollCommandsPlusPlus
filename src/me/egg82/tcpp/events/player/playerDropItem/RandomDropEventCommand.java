package me.egg82.tcpp.events.player.playerDropItem;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.services.RandomDropRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.MaterialHelper;
import ninja.egg82.utils.MathUtil;

public class RandomDropEventCommand extends EventCommand {
	//vars
	private IRegistry randomDropRegistry = (IRegistry) ServiceLocator.getService(RandomDropRegistry.class);
	
	private MaterialHelper materialHelper = (MaterialHelper) ServiceLocator.getService(MaterialHelper.class);
	private Material[] materials = null;
	
	//constructor
	public RandomDropEventCommand(Event event) {
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
		PlayerDropItemEvent e = (PlayerDropItemEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		if (randomDropRegistry.hasRegister(e.getPlayer().getUniqueId().toString())) {
			e.getItemDrop().setItemStack(new ItemStack(materials[MathUtil.fairRoundedRandom(0, materials.length - 1)], e.getItemDrop().getItemStack().getAmount()));
		}
	}
}
