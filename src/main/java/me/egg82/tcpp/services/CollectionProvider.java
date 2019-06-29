package me.egg82.tcpp.services;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import me.egg82.tcpp.core.FakeBlockData;
import org.bukkit.Location;

public class CollectionProvider {
    private CollectionProvider() {}

    private static ConcurrentMap<String, Set<UUID>> setCache = new ConcurrentHashMap<>();
    public static Set<UUID> getSet(String setName) { return setCache.computeIfAbsent(setName, k -> new HashSet<>()); }
    public static ConcurrentMap<String, Set<UUID>> getSetCache() { return setCache; }

    private static ConcurrentMap<Location, FakeBlockData> fakeBlocks = new ConcurrentHashMap<>();
    public static ConcurrentMap<Location, FakeBlockData> getFakeBlocks() { return fakeBlocks; }
}
