package me.egg82.tcpp.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import me.egg82.tcpp.core.FakeBlockData;
import org.bukkit.Location;

public class CollectionProvider {
    private CollectionProvider() {}

    private static ConcurrentMap<Location, FakeBlockData> fakeBlocks = new ConcurrentHashMap<>();
    public static ConcurrentMap<Location, FakeBlockData> getFakeBlocks() { return fakeBlocks; }
}
