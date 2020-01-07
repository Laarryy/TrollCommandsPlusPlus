package me.egg82.ae.services.material;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.Material;

public class MaterialLookup {
    private static final ConcurrentMap<String, Material> cache = new ConcurrentHashMap<>();

    public static Optional<Material> get(String... search) {
        if (search == null) {
            throw new IllegalArgumentException("search cannot be null.");
        }

        Optional<Material> retVal = Optional.empty();

        for (String s : search) {
            retVal = Optional.ofNullable(cache.computeIfAbsent(s, k -> tryGetMaterial(k)));
            if (retVal.isPresent()) {
                return retVal;
            }
        }

        return retVal;
    }

    private static Material tryGetMaterial(String search) {
        String legacySearch = "LEGACY_" + search;

        for (Material m : Material.values()) {
            if (m.name().equalsIgnoreCase(search) || m.name().equalsIgnoreCase(legacySearch)) {
                return m;
            }
        }
        return Material.matchMaterial(search);
    }
}
