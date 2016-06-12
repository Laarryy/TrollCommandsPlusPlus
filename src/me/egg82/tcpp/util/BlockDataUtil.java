package me.egg82.tcpp.util;

import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Beacon;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.EndGateway;
import org.bukkit.block.FlowerPot;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.block.Jukebox;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;

public class BlockDataUtil {
	//vars
	
	//constructor
	public BlockDataUtil() {
		
	}
	
	//public
	public static void setBlockData(BlockState block, BlockState data) {
		Material type = block.getType();
		
		block.setData(data.getData());
		
		if (type == Material.STANDING_BANNER || type == Material.WALL_BANNER) {
			Banner b1 = (Banner) block;
			Banner b2 = (Banner) data;
			b1.setBaseColor(b2.getBaseColor());
			b1.setPatterns(b2.getPatterns());
		} else if (type == Material.BEACON) {
			((Beacon) block).getInventory().setContents(((Beacon) data).getInventory().getContents());
		} else if (type == Material.BREWING_STAND) {
			BrewingStand b1 = (BrewingStand) block;
			BrewingStand b2 = (BrewingStand) data;
			b1.setBrewingTime(b2.getBrewingTime());
			b1.setFuelLevel(b2.getFuelLevel());
		} else if (type == Material.CHEST) {
			((Chest) block).getBlockInventory().setContents(((Chest) data).getBlockInventory().getContents());
		} else if (type == Material.COMMAND) {
			CommandBlock b1 = (CommandBlock) block;
			CommandBlock b2 = (CommandBlock) data;
			b1.setName(b2.getName());
			b1.setCommand(b2.getCommand());
		} else if (type == Material.FURNACE || type == Material.BURNING_FURNACE) {
			Furnace b1 = (Furnace) block;
			Furnace b2 = (Furnace) data;
			b1.setBurnTime(b2.getBurnTime());
			b1.setCookTime(b2.getCookTime());
			b1.getInventory().setContents(b2.getInventory().getContents());
		} else if (type == Material.MOB_SPAWNER) {
			CreatureSpawner b1 = (CreatureSpawner) block;
			CreatureSpawner b2 = (CreatureSpawner) data;
			b1.setCreatureTypeByName(b2.getCreatureTypeName());
			b1.setDelay(b2.getDelay());
			b1.setSpawnedType(b2.getSpawnedType());
		} else if (type == Material.DISPENSER) {
			((Dispenser) block).getInventory().setContents(((Dispenser) data).getInventory().getContents());
		} else if (type == Material.DROPPER) {
			((Dropper) block).getInventory().setContents(((Dropper) data).getInventory().getContents());
		} else if (type == Material.END_GATEWAY) {
			EndGateway b1 = (EndGateway) block;
			EndGateway b2 = (EndGateway) data;
			b1.setExactTeleport(b2.isExactTeleport());
			b1.setExitLocation(b2.getExitLocation());
		} else if (type == Material.FLOWER_POT) {
			((FlowerPot) block).setContents(((FlowerPot) data).getContents());
		} else if (type == Material.HOPPER) {
			((Hopper) block).getInventory().setContents(((Hopper) data).getInventory().getContents());
		} else if (type == Material.JUKEBOX) {
			((Jukebox) block).setPlaying(((Jukebox) data).getPlaying());
		} else if (type == Material.NOTE_BLOCK) {
			((NoteBlock) block).setNote(((NoteBlock) data).getNote());
		} else if (type == Material.SIGN_POST || type == Material.WALL_SIGN) {
			Sign b1 = (Sign) block;
			String[] lines = ((Sign) data).getLines();
			for (int j = 0; j < lines.length; j++) {
				b1.setLine(j, lines[j]);
			}
		} else if (type == Material.SKULL) {
			Skull b1 = (Skull) block;
			Skull b2 = (Skull) data;
			b1.setOwningPlayer(b2.getOwningPlayer());
			b1.setRotation(b2.getRotation());
			b1.setSkullType(b2.getSkullType());
		}
	}
	
	//private
	
}
