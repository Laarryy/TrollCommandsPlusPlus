package me.egg82.ae.services;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import me.egg82.ae.core.FakeBlockData;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class CollectionProvider {
    private CollectionProvider() {}

    private static ExpiringMap<UUID, Double> bleeding = ExpiringMap.builder().variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();
    public static ExpiringMap<UUID, Double> getBleeding() { return bleeding; }

    private static ExpiringMap<UUID, Integer> ensnaring = ExpiringMap.builder().variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();
    public static ExpiringMap<UUID, Integer> getEnsnaring() { return ensnaring; }

    private static ExpiringMap<UUID, Double> freezing = ExpiringMap.builder().variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();
    public static ExpiringMap<UUID, Double> getFreezing() { return freezing; }

    private static ExpiringMap<UUID, Integer> stickiness = ExpiringMap.builder().variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();
    public static ExpiringMap<UUID, Integer> getStickiness() { return stickiness; }

    private static ExpiringMap<UUID, Integer> rampage = ExpiringMap.builder().variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();
    public static ExpiringMap<UUID, Integer> getRampage() { return rampage; }

    private static Set<Location> explosive = new HashSet<>();
    public static Set<Location> getExplosive() { return explosive; }

    private static Set<Location> artisan = new HashSet<>();
    public static Set<Location> getArtisan() { return artisan; }

    private static Set<UUID> fiery = new HashSet<>();
    public static Set<UUID> getFiery() { return fiery; }

    private static Set<UUID> multishot = new HashSet<>();
    public static Set<UUID> getMultishot() { return multishot; }

    private static Map<UUID, Integer> markingArrows = new HashMap<>();
    public static Map<UUID, Integer> getMarkingArrows() { return markingArrows; }

    private static ExpiringMap<UUID, Double> marking = ExpiringMap.builder().variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();
    public static ExpiringMap<UUID, Double> getMarking() { return marking; }

    private static Set<UUID> night = new HashSet<>();
    public static Set<UUID> getNight() { return night; }

    private static Map<UUID, List<ItemStack>> soulbound = new HashMap<>();
    public static List<ItemStack> getSoulboundItems(UUID uuid) { return soulbound.computeIfAbsent(uuid, v -> new ArrayList<>()); }
    public static List<ItemStack> getAndClearSoulboundItems(UUID uuid) { return soulbound.remove(uuid); }

    private static ConcurrentMap<Location, FakeBlockData> fakeBlocks = new ConcurrentHashMap<>();
    public static ConcurrentMap<Location, FakeBlockData> getFakeBlocks() { return fakeBlocks; }
}
