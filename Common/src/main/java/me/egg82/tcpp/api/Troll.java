package me.egg82.tcpp.api;

import co.aikar.commands.CommandIssuer;
import java.util.Objects;
import java.util.UUID;
import me.egg82.tcpp.TrollAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Troll {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final TrollAPI api = TrollAPI.getInstance();

    protected final UUID playerID;
    protected final TrollType type;

    private final int hc;

    public Troll(UUID playerID, TrollType type) {
        this.playerID = playerID;
        this.type = type;

        this.hc = Objects.hash(playerID, type);
    }

    public UUID getPlayerID() { return playerID; }

    public TrollType getType() { return type; }

    public abstract void start(CommandIssuer issuer) throws Exception;

    public abstract void stop(CommandIssuer issuer) throws Exception;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Troll)) return false;
        Troll troll = (Troll) o;
        return playerID.equals(troll.playerID) &&
                type.equals(troll.type);
    }

    public int hashCode() { return hc; }
}
