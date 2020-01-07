package me.egg82.ae.hooks;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.AsynchronousManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import me.egg82.ae.core.ChunkData;
import me.egg82.ae.core.FakeBlockData;
import me.egg82.ae.services.CollectionProvider;
import me.egg82.ae.services.block.FakeBlockHandler;
import me.egg82.ae.utils.ConfigUtil;
import ninja.egg82.service.ServiceLocator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolLibHook implements PluginHook, FakeBlockHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    private AsynchronousManager asyncManager = manager.getAsynchronousManager();

    private Plugin plugin;

    public ProtocolLibHook(Plugin plugin) {
        this.plugin = plugin;

        // Fake block events

        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Server.BLOCK_CHANGE) {
            public void onPacketSending(PacketEvent event) {
                if (event.isCancelled()) {
                    return;
                }

                WrapperPlayServerBlockChange packet = new WrapperPlayServerBlockChange(event.getPacket());

                Location location = packet.getBukkitLocation(event.getPlayer().getWorld());
                WrappedBlockData data = packet.getBlockData();
                FakeBlockData sentData = new FakeBlockData(data.getType(), (byte) data.getData());
                FakeBlockData fakeData = CollectionProvider.getFakeBlocks().get(location);
                if (fakeData != null && !fakeData.equals(sentData)) {
                    if (ConfigUtil.getDebugOrFalse()) {
                        logger.info("Replacing block packet with fake data at " + location);
                    }
                    packet.setBlockData(WrappedBlockData.createData(fakeData.getType(), fakeData.getData()));
                }
            }
        });

        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Server.MULTI_BLOCK_CHANGE) {
            public void onPacketSending(PacketEvent event) {
                if (event.isCancelled()) {
                    return;
                }

                WrapperPlayServerMultiBlockChange packet = new WrapperPlayServerMultiBlockChange(event.getPacket());

                for (MultiBlockChangeInfo record : packet.getRecords()) {
                    Location location = record.getLocation(event.getPlayer().getWorld());
                    WrappedBlockData data = record.getData();
                    FakeBlockData sentData = new FakeBlockData(data.getType(), (byte) data.getData());
                    FakeBlockData fakeData = CollectionProvider.getFakeBlocks().get(location);
                    if (fakeData != null && !fakeData.equals(sentData)) {
                        if (ConfigUtil.getDebugOrFalse()) {
                            logger.info("Replacing block packet with fake data at " + location);
                        }
                        record.setData(WrappedBlockData.createData(fakeData.getType(), fakeData.getData()));
                    }
                }
            }
        });
    }

    public static void setGlowing(ItemStack item) {
        if (item == null) {
            return;
        }

        NbtCompound compound = NbtFactory.asCompound(NbtFactory.fromItemTag(item));
        compound.put("ench", NbtFactory.ofList(""));
        NbtFactory.setItemTag(item, compound);
    }

    public static void removeGlowing(ItemStack item) {
        if (item == null) {
            return;
        }

        NbtCompound compound = NbtFactory.asCompound(NbtFactory.fromItemTag(item));
        compound.remove("ench");
        NbtFactory.setItemTag(item, compound);
    }

    public void cancel() {
        Set<? extends FakeBlockHandler> handlers = ServiceLocator.remove(FakeBlockHandler.class);
        for (FakeBlockHandler h : handlers) {
            h.removeAll();
        }

        asyncManager.unregisterAsyncHandlers(plugin);
        manager.removePacketListeners(plugin);
    }

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
        WrapperPlayServerBlockChange packet = new WrapperPlayServerBlockChange();
        packet.setLocation(new BlockPosition(blockLocation.getBlockX(), blockLocation.getBlockY(), blockLocation.getBlockZ()));
        packet.setBlockData(WrappedBlockData.createData(newBlockData.getType(), newBlockData.getData()));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(blockLocation.getWorld())) {
                try {
                    manager.sendServerPacket(player, packet.getHandle());
                } catch (InvocationTargetException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }

    private void sendMulti(Map<Location, FakeBlockData> blocks) {
        Map<ChunkData, List<MultiBlockChangeInfo>> chunkInfo = new HashMap<>();

        for (Map.Entry<Location, FakeBlockData> kvp : blocks.entrySet()) {
            ChunkData chunkData = new ChunkData(kvp.getKey().getWorld(), kvp.getKey().getBlockX() >> 4, kvp.getKey().getBlockZ() >> 4);
            List<MultiBlockChangeInfo> info = chunkInfo.computeIfAbsent(chunkData, k -> new ArrayList<>());
            info.add(new MultiBlockChangeInfo(kvp.getKey(), WrappedBlockData.createData(kvp.getValue().getType(), kvp.getValue().getData())));
        }

        for (Map.Entry<ChunkData, List<MultiBlockChangeInfo>> kvp : chunkInfo.entrySet()) {
            WrapperPlayServerMultiBlockChange packet = new WrapperPlayServerMultiBlockChange();
            packet.setChunk(new ChunkCoordIntPair(kvp.getKey().getChunkX(), kvp.getKey().getChunkZ()));
            packet.setRecords(kvp.getValue().toArray(new MultiBlockChangeInfo[0]));

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getWorld().equals(kvp.getKey().getWorld())) {
                    try {
                        manager.sendServerPacket(player, packet.getHandle());
                    } catch (InvocationTargetException ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        }
    }
}
