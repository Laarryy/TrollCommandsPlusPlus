package me.egg82.tcpp.events.block.blockBreak;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.services.RandomBreakRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.reflection.player.IPlayerUtil;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.MaterialHelper;
import ninja.egg82.utils.MathUtil;

public class RandomBreakEventCommand extends EventCommand {
	//vars
	private IRegistry randomBreakRegistry = (IRegistry) ServiceLocator.getService(RandomBreakRegistry.class);
	
	private IPlayerUtil playerUtil = (IPlayerUtil) ServiceLocator.getService(IPlayerUtil.class);
	private MaterialHelper materialHelper = (MaterialHelper) ServiceLocator.getService(MaterialHelper.class);
	private Material[] materials = null;
	
	//constructor
	public RandomBreakEventCommand(Event event) {
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
		BlockBreakEvent e = (BlockBreakEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (randomBreakRegistry.hasRegister(uuid)) {
			e.setCancelled(true);
			
			if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
				Collection<ItemStack> drops = e.getBlock().getDrops(playerUtil.getItemInMainHand(player));
				for (ItemStack item : drops) {
					player.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(materials[MathUtil.fairRoundedRandom(0, materials.length - 1)], item.getAmount()));
				}
			}
			
			BlockUtil.setBlock(e.getBlock(), new BlockData(null, null, Material.AIR), true);
		}
	}
}
