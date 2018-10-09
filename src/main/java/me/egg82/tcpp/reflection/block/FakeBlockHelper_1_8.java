package me.egg82.tcpp.reflection.block;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.egg82.tcpp.lists.FakeBlockSet;
import ninja.egg82.bukkit.core.BlockData;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;

public class FakeBlockHelper_1_8 implements IFakeBlockHelper {
    //vars
    private IConcurrentSet<BlockData> fakeBlockSet = ServiceLocator.getService(FakeBlockSet.class);

    //constructor
    public FakeBlockHelper_1_8() {

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
        for (Player player : Bukkit.getOnlinePlayers()) {
            send(player, location, type, data);
        }
    }

    public void sendAllMulti(BlockData[] data) {
        sendAllMulti(Arrays.asList(data));
    }

    public void sendAllMulti(Collection<BlockData> data) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendMulti(player, data);
        }
    }

    public void send(Player player, BlockData data) {
        send(player, data.getLocation(), data.getType(), data.getBlockData());
    }

    @SuppressWarnings("deprecation")
    public void send(Player player, Location location, Material type, byte data) {
        player.sendBlockChange(location, type, data);
    }

    public void sendMulti(Player player, BlockData[] data) {
        sendMulti(player, Arrays.asList(data));
    }

    public void sendMulti(Player player, Collection<BlockData> data) {
        for (BlockData d : data) {
            send(player, d);
        }
    }

    //private

}
