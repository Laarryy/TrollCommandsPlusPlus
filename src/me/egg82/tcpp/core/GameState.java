package me.egg82.tcpp.core;

import java.util.HashSet;

import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.core.EntityData;

public class GameState {
	//vars
	private EntityData[] entities = null;
	private BlockData[] blocks = null;
	private long timestamp = System.currentTimeMillis();
	
	//constructor
	public GameState() {
		
	}
	public GameState(HashSet<EntityData> entities, HashSet<BlockData> blocks) {
		this.entities = entities.toArray(new EntityData[0]);
		this.blocks = blocks.toArray(new BlockData[0]);
	}
	
	//public
	public EntityData[] getEntities() {
		return entities.clone();
	}
	public BlockData[] getBlocks() {
		return blocks.clone();
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	//private
	
}
