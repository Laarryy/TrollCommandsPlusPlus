package me.egg82.tcpp.reflection.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.packetwrapper.WrapperPlayServerBlockChange;
import com.comphenix.packetwrapper.WrapperPlayServerMultiBlockChange;
import com.comphenix.protocol.AsynchronousManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
import com.comphenix.protocol.wrappers.WrappedBlockData;

import me.egg82.tcpp.lists.FakeBlockSet;
import ninja.egg82.analytics.exceptions.IExceptionHandler;
import ninja.egg82.bukkit.core.BlockData;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.tuples.pair.IntIntPair;

public class ProtocolLibFakeBlockHelper implements IFakeBlockHelper {
    //vars
    private IConcurrentSet<BlockData> fakeBlockSet = ServiceLocator.getService(FakeBlockSet.class);

    private ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    private AsynchronousManager asyncManager = ProtocolLibrary.getProtocolManager().getAsynchronousManager();

    //constructor
    public ProtocolLibFakeBlockHelper() {
        asyncManager.registerAsyncHandler(new PacketAdapter(ServiceLocator.getService(Plugin.class), ListenerPriority.HIGH, PacketType.Play.Client.ENTITY_ACTION) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.isCancelled()) {
                    return;
                }

                BlockPosition position = event.getPacket().getBlockPositionModifier().readSafely(0);
                if (position == null) {
                    return;
                }

                Location l = position.toLocation(event.getPlayer().getWorld());

                for (Iterator<BlockData> i = fakeBlockSet.iterator(); i.hasNext(); ) {
                    BlockData data = i.next();
                    if (data.getLocation().equals(l)) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }).start();
    }

    //public
    public void queue(Location location, Material type, byte data) {
        queue(new BlockData(location, type, data, null));
    }

    public void queue(BlockData data) {
        fakeBlockSet.add(data);
        sendAll(data);
    }

    @SuppressWarnings("deprecation")
    public void deque(Location location) {
        for (Iterator<BlockData> i = fakeBlockSet.iterator(); i.hasNext(); ) {
            BlockData data = i.next();
            if (data.getLocation().equals(location)) {
                i.remove();
            }
        }

        TaskUtil.runAsync(new Runnable() {
            public void run() {
                sendAll(new BlockData(location, location.getBlock().getType(), location.getBlock().getData(), null));
            }
        }, 2L);
    }

    public void sendAll(BlockData data) {
        sendAll(data.getLocation(), data.getType(), data.getBlockData());
    }

    public void sendAll(Location location, Material type, byte data) {
        WrapperPlayServerBlockChange packet = new WrapperPlayServerBlockChange();
        packet.setLocation(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        packet.setBlockData(WrappedBlockData.createData(type, data));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(location.getWorld())) {
                try {
                    manager.sendServerPacket(player, packet.getHandle());
                } catch (Exception ex) {
                    IExceptionHandler handler = ServiceLocator.getService(IExceptionHandler.class);
                    if (handler != null) {
                        handler.sendException(ex);
                    }
                    ex.printStackTrace();
                }
            }
        }
    }

    public void sendAllMulti(BlockData[] data) {
        sendAllMulti(Arrays.asList(data));
    }

    public void sendAllMulti(Collection<BlockData> data) {
        List<BlockData> newData = new ArrayList<BlockData>(data);

        while (newData.size() > 0) {
            BlockData first = newData.get(0);
            World world = first.getLocation().getWorld();
            IntIntPair coords = new IntIntPair(first.getLocation().getBlockX() >> 4, first.getLocation().getBlockZ() >> 4);
            List<MultiBlockChangeInfo> info = new ArrayList<MultiBlockChangeInfo>();

            WrapperPlayServerMultiBlockChange packet = new WrapperPlayServerMultiBlockChange();
            packet.setChunk(new ChunkCoordIntPair(coords.getLeft(), coords.getRight()));

            for (Iterator<BlockData> i = newData.iterator(); i.hasNext(); ) {
                BlockData d = i.next();
                if (!d.getLocation().getWorld().equals(world)) {
                    continue;
                }
                if (d.getLocation().getBlockX() >> 4 != coords.getLeft()) {
                    continue;
                }
                if (d.getLocation().getBlockZ() >> 4 != coords.getRight()) {
                    continue;
                }

                info.add(new MultiBlockChangeInfo(d.getLocation(), WrappedBlockData.createData(d.getType(), d.getBlockData())));

                i.remove();
            }

            packet.setRecords(info.toArray(new MultiBlockChangeInfo[0]));

            for (Player player : Bukkit.getOnlinePlayers()) {
                sendMultiInternal(player, world, packet.getHandle());
            }
        }
    }

    public void send(Player player, BlockData data) {
        send(player, data.getLocation(), data.getType(), data.getBlockData());
    }

    public void send(Player player, Location location, Material type, byte data) {
        if (!player.getWorld().equals(location.getWorld())) {
            return;
        }

        WrapperPlayServerBlockChange packet = new WrapperPlayServerBlockChange();
        packet.setLocation(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        packet.setBlockData(WrappedBlockData.createData(type, data));

        try {
            manager.sendServerPacket(player, packet.getHandle());
        } catch (Exception ex) {
            IExceptionHandler handler = ServiceLocator.getService(IExceptionHandler.class);
            if (handler != null) {
                handler.sendException(ex);
            }
            ex.printStackTrace();
        }
    }

    public void sendMulti(Player player, BlockData[] data) {
        sendMulti(player, Arrays.asList(data));
    }

    public void sendMulti(Player player, Collection<BlockData> data) {
        List<BlockData> newData = new ArrayList<BlockData>(data);

        while (newData.size() > 0) {
            BlockData first = newData.get(0);
            World world = first.getLocation().getWorld();
            IntIntPair coords = new IntIntPair(first.getLocation().getBlockX() >> 4, first.getLocation().getBlockZ() >> 4);
            List<MultiBlockChangeInfo> info = new ArrayList<MultiBlockChangeInfo>();

            WrapperPlayServerMultiBlockChange packet = new WrapperPlayServerMultiBlockChange();
            packet.setChunk(new ChunkCoordIntPair(coords.getLeft(), coords.getRight()));

            for (Iterator<BlockData> i = newData.iterator(); i.hasNext(); ) {
                BlockData d = i.next();
                if (!d.getLocation().getWorld().equals(world)) {
                    continue;
                }
                if (d.getLocation().getBlockX() >> 4 != coords.getLeft()) {
                    continue;
                }
                if (d.getLocation().getBlockZ() >> 4 != coords.getRight()) {
                    continue;
                }

                info.add(new MultiBlockChangeInfo(d.getLocation(), WrappedBlockData.createData(d.getType(), d.getBlockData())));

                i.remove();
            }

            packet.setRecords(info.toArray(new MultiBlockChangeInfo[0]));

            sendMultiInternal(player, world, packet.getHandle());
        }
    }

    //private
    private void sendMultiInternal(Player player, World world, PacketContainer packet) {
        if (player.getWorld().equals(world)) {
            try {
                manager.sendServerPacket(player, packet);
            } catch (Exception ex) {
                IExceptionHandler handler = ServiceLocator.getService(IExceptionHandler.class);
                if (handler != null) {
                    handler.sendException(ex);
                }
                ex.printStackTrace();
            }
        }
    }
}
