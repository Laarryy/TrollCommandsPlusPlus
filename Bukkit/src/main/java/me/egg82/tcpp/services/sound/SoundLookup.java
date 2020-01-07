package me.egg82.ae.services.sound;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.Sound;

public class SoundLookup {
    private static final ConcurrentMap<String, Sound> cache = new ConcurrentHashMap<>();

    public static Optional<Sound> get(String... search) {
        if (search == null) {
            throw new IllegalArgumentException("search cannot be null.");
        }

        Optional<Sound> retVal = Optional.empty();

        for (String s : search) {
            retVal = Optional.ofNullable(cache.computeIfAbsent(s, k -> tryGetSound(k)));
            if (retVal.isPresent()) {
                return retVal;
            }
        }

        return retVal;
    }

    private static Sound tryGetSound(String search) {
        for (Sound s : Sound.values()) {
            if (s.name().equalsIgnoreCase(search)) {
                return s;
            }
        }
        return null;
    }
}
