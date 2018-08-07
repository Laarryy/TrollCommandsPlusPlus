package me.egg82.tcpp.reflection.block;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import ninja.egg82.bukkit.core.BlockData;

public interface IFakeBlockHelper {
	//functions
	void queue(Location location, Material type, byte data);
	void queue(BlockData data);
	void deque(Location location);
	
	void sendAll(BlockData data);
	void sendAll(Location location, Material type, byte data);
	
	void sendAllMulti(BlockData[] data);
	void sendAllMulti(Collection<BlockData> data);
	
	void send(Player player, BlockData data);
	void send(Player player, Location location, Material type, byte data);
	
	void sendMulti(Player player, BlockData[] data);
	void sendMulti(Player player, Collection<BlockData> data);
}
