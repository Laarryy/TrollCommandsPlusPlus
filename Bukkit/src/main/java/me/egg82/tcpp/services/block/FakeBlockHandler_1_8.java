package me.egg82.ae.services.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import me.egg82.ae.core.FakeBlockData;
import me.egg82.ae.services.CollectionProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FakeBlockHandler_1_8 implements FakeBlockHandler {
    public FakeBlockHandler_1_8() { }

    public void sendFake(Block block, FakeBlockData newBlockData) {
        if (block == null) {
            throw new IllegalArgumentException("block cannot be null.");
        }
        if (newBlockData == null) {
            throw new IllegalArgumentException("newBlockData cannot be null.");
        }

        CollectionProvider.getFakeBlocks().put(block.getLocation(), newBlockData);
        sendSingle(block.getLocation(), newBlockData);
    }

    public void sendReal(Block block) {
        if (block == null) {
            throw new IllegalArgumentException("block cannot be null.");
        }

        CollectionProvider.getFakeBlocks().remove(block.getLocation());
        sendSingle(block.getLocation(), new FakeBlockData(block.getType(), block.getData()));
    }

    public void sendFake(Map<Block, FakeBlockData> blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("block cannot be null.");
        }

        Map<Location, FakeBlockData> addedBlocks = new HashMap<>();

        for (Map.Entry<Block, FakeBlockData> kvp : blocks.entrySet()) {
            Location l = kvp.getKey().getLocation();
            addedBlocks.put(l, kvp.getValue());
            CollectionProvider.getFakeBlocks().put(l, kvp.getValue());
        }

        sendMulti(addedBlocks);
    }

    public void sendReal(Set<Block> blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("block cannot be null.");
        }

        Map<Location, FakeBlockData> removedBlocks = new HashMap<>();

        for (Block block : blocks) {
            Location l = block.getLocation();
            removedBlocks.put(l, new FakeBlockData(block.getType(), block.getData()));
            CollectionProvider.getFakeBlocks().remove(l);
        }

        sendMulti(removedBlocks);
    }

    public void removeAll() {
        Map<Location, FakeBlockData> removedBlocks = new HashMap<>();

        for (Map.Entry<Location, FakeBlockData> kvp : CollectionProvider.getFakeBlocks().entrySet()) {
            Block b = kvp.getKey().getBlock();
            removedBlocks.put(kvp.getKey(), new FakeBlockData(b.getType(), b.getData()));
            CollectionProvider.getFakeBlocks().remove(kvp.getKey());
        }

        sendMulti(removedBlocks);
    }

    private void sendSingle(Location blockLocation, FakeBlockData newBlockData) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendBlockChange(blockLocation, newBlockData.getType(), newBlockData.getData());
        }
    }

    private void sendMulti(Map<Location, FakeBlockData> blocks) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Map.Entry<Location, FakeBlockData> kvp : blocks.entrySet()) {
                player.sendBlockChange(kvp.getKey(), kvp.getValue().getType(), kvp.getValue().getData());
            }
        }
    }
}
