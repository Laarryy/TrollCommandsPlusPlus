package me.egg82.tcpp.api;

import com.google.common.collect.ImmutableSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import me.egg82.tcpp.utils.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrollType {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final Set<TrollType> allTypes = new HashSet<>();

    public static final TrollType ALONE = new TrollType("alone");

    public static Set<TrollType> values() { return ImmutableSet.copyOf(allTypes); }

    public static Optional<TrollType> getByName(String name) {
        for (TrollType t : values()) {
            if (t.name.equalsIgnoreCase(name)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    private final String name;
    private final int hc;
    private TrollType(String name) {
        if (ConfigUtil.getDebugOrFalse()) {
            logger.info("Adding new troll type " + name);
        }
        this.name = name;
        this.hc = Objects.hash(name);
        allTypes.add(this);
    }

    // Used for ExternalAPI
    private TrollType() {
        this.name = null;
        this.hc = -1;
    }

    public static TrollType register(String name) {
        Optional<TrollType> retVal = getByName(name);
        return retVal.orElseGet(() -> new TrollType(name));
    }

    public String getName() { return name; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrollType)) return false;
        TrollType trollType = (TrollType) o;
        return name.equals(trollType.name);
    }

    public int hashCode() { return hc; }
}
