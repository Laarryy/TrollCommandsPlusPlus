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

    public static final TrollType ALONE = new TrollType("alone", "me.egg82.tcpp.api.trolls.AloneTroll");
    public static final TrollType AMNESIA = new TrollType("amnesia", "me.egg82.tcpp.api.trolls.AmnesiaTroll");
    public static final TrollType ANNOY = new TrollType("annoy", "me.egg82.tcpp.api.trolls.AnnoyTroll");
    public static final TrollType ANVIL = new TrollType("anvil", "me.egg82.tcpp.api.trolls.AnvilTroll");
    public static final TrollType ATTACH = new TrollType("attach", "me.egg82.tcpp.api.trolls.AttachTroll");
    public static final TrollType BANISH = new TrollType("banish", "me.egg82.tcpp.api.trolls.BanishTroll");
    public static final TrollType CONTROL = new TrollType("control", "me.egg82.tcpp.api.trolls.ControlTroll");
    public static final TrollType FREEZE = new TrollType("freeze", "me.egg82.tcpp.api.trolls.FreezeTroll");
    public static final TrollType GARBLE = new TrollType("garble", "me.egg82.tcpp.api.trolls.GarbleTroll");
    public static final TrollType LIFT = new TrollType("lift", "me.egg82.tcpp.api.trolls.LiftTroll");
    public static final TrollType SNOWBALLFIGHT = new TrollType("snowballfight", "me.egg82.tcpp.api.trolls.SnowballFightTroll");

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
    private final String className;
    private final int hc;
    private TrollType(String name, String className) {
        if (ConfigUtil.getDebugOrFalse()) {
            logger.info("Adding new troll type " + name);
        }
        this.name = name;
        this.className = className;
        this.hc = Objects.hash(name);
        allTypes.add(this);
    }

    // Used for ExternalAPI
    private TrollType() {
        this.name = null;
        this.className = null;
        this.hc = -1;
    }

    public String getName() { return name; }

    public String getClassName() { return className; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrollType)) return false;
        TrollType trollType = (TrollType) o;
        return name.equals(trollType.name);
    }

    public int hashCode() { return hc; }
}
