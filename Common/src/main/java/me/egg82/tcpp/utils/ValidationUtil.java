package me.egg82.tcpp.utils;

import java.util.regex.Pattern;

public class ValidationUtil {
    /**
     * UUID_PATTERN_6 compiled and benchmarked from
     * https://github.com/tinnet/java-uuid-validation-benchmark
     * Results on my machine, 06/22/18: https://pastebin.com/hWs62pV2
     * Update: modified for less-than-great UUIDs
     */
    private static final Pattern uuidValidator = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private ValidationUtil() {}

    public static boolean isValidUuid(String uuid) {
        if (uuid == null) {
            return false;
        }
        return uuidValidator.matcher(uuid).matches();
    }
}
