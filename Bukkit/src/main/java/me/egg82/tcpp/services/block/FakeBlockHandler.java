package me.egg82.tcpp.services.block;

import java.util.Map;
import java.util.Set;
import me.egg82.tcpp.core.FakeBlockData;
import org.bukkit.block.Block;

public interface FakeBlockHandler {
    void sendFake(Block block, FakeBlockData newBlockData);
    void sendReal(Block block);

    void sendFake(Map<Block, FakeBlockData> blocks);
    void sendReal(Set<Block> blocks);

    void removeAll();
}
