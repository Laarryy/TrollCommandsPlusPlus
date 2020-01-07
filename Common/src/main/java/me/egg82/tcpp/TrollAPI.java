package me.egg82.tcpp;

import co.aikar.commands.CommandIssuer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import me.egg82.tcpp.api.Troll;
import me.egg82.tcpp.api.TrollType;

public class TrollAPI {
    private static final TrollAPI api = new TrollAPI();

    private TrollAPI() { }

    public static TrollAPI getInstance() { return api; }

    private final ConcurrentMap<Troll, Boolean> trolls = new ConcurrentHashMap<>();

    public boolean isTrolled(UUID playerID) throws APIException {
        if (playerID == null) {
            throw new APIException(false, "playerID cannot be null.");
        }

        for (Troll t : trolls.keySet()) {
            if (playerID.equals(t.getPlayerID())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTrolled(UUID playerID, TrollType type) throws APIException {
        if (playerID == null) {
            throw new APIException(false, "playerID cannot be null.");
        }
        if (type == null) {
            throw new APIException(false, "type cannot be null.");
        }

        for (Troll t : trolls.keySet()) {
            if (t.getType().equals(type) && playerID.equals(t.getPlayerID())) {
                return true;
            }
        }
        return false;
    }

    public boolean startTroll(Troll troll, CommandIssuer issuer) throws APIException {
        if (troll == null) {
            throw new APIException(false, "troll cannot be null.");
        }
        if (issuer == null) {
            throw new APIException(false, "issuer cannot be null.");
        }

        if (trolls.putIfAbsent(troll, Boolean.TRUE) == null) {
            try {
                troll.start(issuer);
            } catch (Exception ex) {
                throw new APIException(true, "Could not invoke start method for troll " + troll.getClass().getName() + ".", ex);
            }
            return true;
        }
        return false;
    }

    public boolean stopTroll(Troll troll, CommandIssuer issuer) throws APIException {
        if (troll == null) {
            throw new APIException(false, "troll cannot be null.");
        }
        if (issuer == null) {
            throw new APIException(false, "issuer cannot be null.");
        }

        try {
            troll.stop(issuer);
        } catch (Exception ex) {
            throw new APIException(true, "Could not invoke stop method for troll " + troll.getClass().getName() + ".", ex);
        }
        return trolls.remove(troll);
    }

    public boolean stopTroll(UUID playerID, TrollType type, CommandIssuer issuer) throws APIException {
        if (playerID == null) {
            throw new APIException(false, "playerID cannot be null.");
        }
        if (type == null) {
            throw new APIException(false, "type cannot be null.");
        }
        if (issuer == null) {
            throw new APIException(false, "issuer cannot be null.");
        }

        boolean stopped = false;
        for (Troll t : trolls.keySet()) {
            if (t.getType().equals(type) && playerID.equals(t.getPlayerID())) {
                try {
                    t.stop(issuer);
                } catch (Exception ex) {
                    throw new APIException(true, "Could not invoke stop method for troll " + t.getClass().getName() + ".", ex);
                }
                trolls.remove(t);
                stopped = true;
            }
        }
        return stopped;
    }

    public void stopTrolls(UUID playerID, CommandIssuer issuer) throws APIException {
        if (playerID == null) {
            throw new APIException(false, "playerID cannot be null.");
        }
        if (issuer == null) {
            throw new APIException(false, "issuer cannot be null.");
        }

        for (Troll t : trolls.keySet()) {
            if (playerID.equals(t.getPlayerID())) {
                try {
                    t.stop(issuer);
                } catch (Exception ex) {
                    throw new APIException(true, "Could not invoke stop method for troll " + t.getClass().getName() + ".", ex);
                }
                trolls.remove(t);
            }
        }
    }

    public void stopAllTrolls(CommandIssuer issuer) throws APIException {
        if (issuer == null) {
            throw new APIException(false, "issuer cannot be null.");
        }

        for (Troll t : trolls.keySet()) {
            try {
                t.stop(issuer);
            } catch (Exception ex) {
                throw new APIException(true, "Could not invoke stop method for troll " + t.getClass().getName() + ".", ex);
            }
            trolls.remove(t);
        }
    }

    public int getNumRunningTrolls() throws APIException { return trolls.size(); }

    public int getNumRunningTrolls(UUID playerID) throws APIException {
        if (playerID == null) {
            throw new APIException(false, "playerID cannot be null.");
        }

        int retVal = 0;
        for (Troll t : trolls.keySet()) {
            if (playerID.equals(t.getPlayerID())) {
                retVal++;
            }
        }
        return retVal;
    }
}
