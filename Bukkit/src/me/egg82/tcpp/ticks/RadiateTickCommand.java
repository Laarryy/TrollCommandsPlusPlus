package me.egg82.tcpp.ticks;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.FlowerPot;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.material.MaterialData;

import me.egg82.tcpp.services.registries.RadiateRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.reflection.entity.IEntityHelper;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;

public class RadiateTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> radiationRegistry = ServiceLocator.getService(RadiateRegistry.class);
	
	private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
	
	//constructor
	public RadiateTickCommand() {
		super();
		ticks = 10L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : radiationRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		if (Math.random() <= 0.05d) {
			List<BlockData> blocks = BlockUtil.getBlocks(player.getLocation(), 3, 3, 3);
			Collections.shuffle(blocks);
			
			for (int i = 0; i < blocks.size(); i++) {
				Material mat = blocks.get(i).getMaterial();
				String matString = mat.name().toLowerCase();
				Location loc = blocks.get(i).getState().getLocation();
				
				if (mat == Material.GRASS) {
					loc.getBlock().setType(Material.DIRT);
					break;
				} else if (mat == Material.SAPLING) {
					loc.getBlock().setType(Material.DEAD_BUSH);
					break;
				} else if (matString.contains("leaves")) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (matString.contains("grass")) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (matString.contains("flower") && !matString.contains("pot")) {
					loc.getBlock().setType(Material.DEAD_BUSH);
					break;
				} else if (mat == Material.FLOWER_POT) {
					FlowerPot pot = (FlowerPot) blocks.get(i).getState();
					pot.setContents(new MaterialData(Material.DEAD_BUSH));
					BlockUtil.setBlock(loc, new BlockData(null, pot, Material.FLOWER_POT, null), true);
					break;
				} else if (matString.contains("mushroom")) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (mat == Material.WHEAT) {
					loc.getBlock().setType(Material.DEAD_BUSH);
					break;
				} else if (mat == Material.CACTUS) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (mat == Material.SUGAR_CANE_BLOCK) {
					loc.getBlock().setType(Material.DEAD_BUSH);
					break;
				} else if (mat == Material.PUMPKIN) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (mat == Material.MELON_BLOCK) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (mat == Material.PUMPKIN_STEM) {
					loc.getBlock().setType(Material.DEAD_BUSH);
					break;
				} else if (mat == Material.MELON_STEM) {
					loc.getBlock().setType(Material.DEAD_BUSH);
					break;
				} else if (mat == Material.VINE) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (mat == Material.MYCEL) {
					loc.getBlock().setType(Material.DIRT);
					break;
				} else if (mat == Material.WATER_LILY) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (matString.equals("nether_wart_block")) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (mat == Material.COCOA) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (mat == Material.CARROT) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (mat == Material.POTATO) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (matString.contains("plant")) {
					loc.getBlock().setType(Material.AIR);
					break;
				} else if (matString.equals("beetroot_block")) {
					loc.getBlock().setType(Material.AIR);
					break;
				}
			}
		}
		
		if (Math.random() <= 0.05d) {
			List<Entity> entities = player.getNearbyEntities(3.0d, 3.0d, 3.0d);
			Collections.shuffle(entities);
			
			for (int i = 0; i < entities.size(); i++) {
				if (!(entities.get(i) instanceof Damageable)) {
					continue;
				}
				
				EntityType type = entities.get(i).getType();
				String typeString = type.name().toLowerCase();
				
				if (type == EntityType.BAT
					|| type == EntityType.CHICKEN
					|| typeString.contains("cow")
					|| typeString.equals("donkey")
					|| type == EntityType.HORSE
					|| typeString.contains("llama")
					|| typeString.equals("mule")
					|| type == EntityType.OCELOT
					|| typeString.equals("parrot")
					|| type == EntityType.PIG
					|| type == EntityType.RABBIT
					|| type == EntityType.SHEEP
					|| type == EntityType.SNOWMAN
					|| type == EntityType.SQUID
					|| type == EntityType.WOLF
				) {
					entityUtil.damage((Damageable) entities.get(i), DamageCause.POISON, 0.5d);
					break;
				}
			}
		}
	}
}
