package me.egg82.tcpp.utils;

import de.slikey.effectlib.Effect;
import org.bukkit.entity.Entity;

public class EffectUtil {
    public static void start(Effect effect, Entity entity) {
        effect.setEntity(entity);
        effect.disappearWithOriginEntity = true;
        effect.start();
    }

    public static void start(Effect effect, Entity from, Entity to) {
        effect.setEntity(from);
        effect.disappearWithOriginEntity = true;
        effect.setTargetEntity(to);
        effect.disappearWithTargetEntity = true;
        effect.start();
    }
}
