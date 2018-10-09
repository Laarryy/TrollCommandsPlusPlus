package me.egg82.tcpp.reflection.entity;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.LongUnaryOperator;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import com.comphenix.packetwrapper.WrapperPlayServerAnimation;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityHeadRotation;
import com.comphenix.packetwrapper.WrapperPlayServerEntityLook;
import com.comphenix.packetwrapper.WrapperPlayServerEntityStatus;
import com.comphenix.packetwrapper.WrapperPlayServerEntityTeleport;
import com.comphenix.packetwrapper.WrapperPlayServerRelEntityMove;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.google.common.util.concurrent.AtomicDouble;

import ninja.egg82.analytics.exceptions.IExceptionHandler;
import ninja.egg82.bukkit.BasePlugin;
import ninja.egg82.bukkit.reflection.entity.IEntityHelper;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.concurrent.DynamicConcurrentSet;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.tuples.pair.Pair;
import ninja.egg82.utils.ThreadUtil;

public class ProtocolLibFakeLivingEntity implements IFakeLivingEntity {
    //vars
    private ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    private IEntityHelper entityHelper = ServiceLocator.getService(IEntityHelper.class);

    private int id = getNextEntityId();
    private static AtomicInteger currentEntityId = new AtomicInteger(Integer.MAX_VALUE);
    private UUID uuid = UUID.randomUUID();

    private IConcurrentSet<UUID> players = new DynamicConcurrentSet<UUID>();

    private AtomicDouble health = new AtomicDouble(20.0d);
    private AtomicLong lastAttackTime = new AtomicLong(-1L);
    private AtomicLong lastAnimationTime = new AtomicLong(-1L);

    private AtomicReference<Location> currentLocation = new AtomicReference<Location>(null);
    private EntityType type = null;

    private AtomicBoolean dead = new AtomicBoolean(false);

    private boolean is18 = false;

    //constructor
    public ProtocolLibFakeLivingEntity(Location spawnLocation, EntityType type) {
        if (spawnLocation == null) {
            throw new IllegalArgumentException("spawnLocation cannot be null.");
        }
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }

        this.currentLocation.set(spawnLocation);
        this.type = type;

        String gameVersion = ServiceLocator.getService(BasePlugin.class).getGameVersion();
        this.is18 = (gameVersion.equals("1.8") || gameVersion.equals("1.8.1") || gameVersion.equals("1.8.3") || gameVersion.equals("1.8.8")) ? true : false;
    }

    //public
    public int getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isVisibleTo(Player player) {
        if (player == null) {
            return false;
        }

        return players.contains(player.getUniqueId());
    }

    public void addVisibilityToPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("player cannot be null.");
        }

        Location l = currentLocation.get();

        if (players.add(player.getUniqueId())) {
            sendPacket(player, getSpawnPacket(l), false);
            // 1.8 hack- spawn requires teleport
            if (is18) {
                sendPacket(player, getTeleportPacket(l), false);
            }
        }
    }

    public void removeVisibilityFromPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("player cannot be null.");
        }

        if (players.remove(player.getUniqueId())) {
            sendPacket(player, getDestroyPacket(), false);
        }
    }

    public double getHealth() {
        return health.get();
    }

    public void setHealth(double health) {
        health = Math.max(0.0d, health);

        double oldHealth = this.health.getAndSet(health);
        if (oldHealth == health) {
            return;
        }

        if (health == 0) {
            kill();
        } else {
            if (oldHealth > health) {
                PacketContainer hurt = getHurtPacket();
                for (UUID uuid : players) {
                    sendPacket(CommandUtil.getPlayerByUuid(uuid), hurt, false);
                }
            }
        }
    }

    public void lookToward(Location targetLocation) {
        if (targetLocation == null) {
            throw new IllegalArgumentException("targetLocation cannot be null.");
        }

        Location l = currentLocation.get();

        double dX = l.getX() - targetLocation.getX();
        double dY = (l.getY() + 1.0d) - targetLocation.getY();
        double dZ = l.getZ() - targetLocation.getZ();
        float yaw = (float) (Math.toDegrees(Math.atan2(dZ, dX)) + 90.0d);
        float pitch = (float) (((Math.atan2(fastSqrt(dZ * dZ + dX * dX), dY) / Math.PI) - 0.5d) * -90.0d);

        l.setPitch(pitch);
        l.setYaw(yaw);

        PacketContainer look = getLookPacket(l);
        PacketContainer headRotation = getHeadRotationPacket(l);
        for (UUID uuid : players) {
            sendPacket(CommandUtil.getPlayerByUuid(uuid), look, false);
            sendPacket(CommandUtil.getPlayerByUuid(uuid), headRotation, false);
        }
    }

    public void moveToward(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("location cannot be null.");
        }

        Pair<PacketContainer, Location> move = getMovePacket(this.currentLocation.get(), location);

        Location oldLocation = this.currentLocation.getAndSet(move.getRight());
        if (oldLocation.equals(move.getRight())) {
            return;
        }

        for (UUID uuid : players) {
            sendPacket(CommandUtil.getPlayerByUuid(uuid), move.getLeft(), false);
        }
    }

    public void teleportTo(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("location cannot be null.");
        }

        Location oldLocation = this.currentLocation.getAndSet(location);
        if (oldLocation.equals(location)) {
            return;
        }

        PacketContainer teleport = getTeleportPacket(location);
        for (UUID uuid : players) {
            sendPacket(CommandUtil.getPlayerByUuid(uuid), teleport, false);
        }
    }

    public boolean requiresTeleport(Location newLocation) {
        if (newLocation == null) {
            throw new IllegalArgumentException("newLocation cannot be null.");
        }

        Location l = currentLocation.get();

        if (!newLocation.getWorld().equals(l.getWorld())) {
            return true;
        }

        return ((is18 && l.distanceSquared(newLocation) > 16.0d) || (!is18 && l.distanceSquared(newLocation) > 64.0d)) ? true : false; // 4 blocks, or 8 blocks
    }

    public Location getLocation() {
        return currentLocation.get().clone();
    }

    public void animate(AnimationType type) {
        if (System.currentTimeMillis() - lastAnimationTime.getAndUpdate(updateAnimation) < 1000L) {
            return;
        }

        PacketContainer animation = getAnimationPacket(type.getAnimationId());
        for (UUID uuid : players) {
            sendPacket(CommandUtil.getPlayerByUuid(uuid), animation, false);
        }
    }

    public void attack(Damageable entity, double damage) {
        if (entity == null) {
            throw new IllegalArgumentException("entity cannot be null.");
        }
        if (damage <= 0.0d) {
            return;
        }

        if (System.currentTimeMillis() - lastAttackTime.getAndUpdate(updateAttack) < 1000L) {
            return;
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                return;
            }
        }

        entityHelper.damage(entity, DamageCause.ENTITY_ATTACK, damage);
        Vector v = entity.getLocation().toVector().subtract(currentLocation.get().toVector()).normalize().setY(0.5d).multiply(0.5d);
        if (LocationUtil.isFinite(v)) {
            entity.setVelocity(v);
        }
    }

    public void kill() {
        if (dead.getAndSet(true)) {
            System.out.println("Previously dead");
            return;
        }

        PacketContainer death = getDeathPacket();
        PacketContainer destroy = getDestroyPacket();
        for (UUID uuid : players) {
            sendPacket(CommandUtil.getPlayerByUuid(uuid), death, true);
            ThreadUtil.schedule(new Runnable() {
                public void run() {
                    sendPacket(CommandUtil.getPlayerByUuid(uuid), destroy, true);
                }
            }, 3000L);
        }
    }

    public boolean isDead() {
        return dead.get();
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof IFakeLivingEntity)) {
            return false;
        }

        IFakeLivingEntity o = (IFakeLivingEntity) other;
        return uuid.equals(o.getUuid());
    }

    public int hashCode() {
        return uuid.hashCode();
    }

    //private
    private PacketContainer getSpawnPacket(Location l) {
        WrapperPlayServerSpawnEntityLiving packet = new WrapperPlayServerSpawnEntityLiving();
        packet.setEntityID(id);
        packet.setUniqueId(uuid);
        packet.setType(type);
        packet.setX(l.getX());
        packet.setY(l.getY());
        packet.setZ(l.getZ());
        packet.setPitch(l.getPitch());
        packet.setHeadPitch(l.getPitch());
        packet.setYaw(l.getYaw());
        return packet.getHandle();
    }

    private PacketContainer getDestroyPacket() {
        WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
        packet.setEntityIds(new int[]{id});
        return packet.getHandle();
    }

    private PacketContainer getHurtPacket() {
        WrapperPlayServerEntityStatus packet = new WrapperPlayServerEntityStatus();
        packet.setEntityID(id);
        packet.setEntityStatus((byte) 2);
        return packet.getHandle();
    }

    private PacketContainer getDeathPacket() {
        WrapperPlayServerEntityStatus packet = new WrapperPlayServerEntityStatus();
        packet.setEntityID(id);
        packet.setEntityStatus((byte) 3);
        return packet.getHandle();
    }

    private PacketContainer getAnimationPacket(int animationId) {
        WrapperPlayServerAnimation packet = new WrapperPlayServerAnimation();
        packet.setEntityID(id);
        packet.setAnimation(animationId);
        return packet.getHandle();
    }

    private PacketContainer getLookPacket(Location l) {
        WrapperPlayServerEntityLook packet = new WrapperPlayServerEntityLook();
        packet.setEntityID(id);
        packet.setOnGround(!isFlying(l));
        packet.setPitch(l.getPitch());
        packet.setYaw(l.getYaw());
        return packet.getHandle();
    }

    private PacketContainer getHeadRotationPacket(Location l) {
        WrapperPlayServerEntityHeadRotation packet = new WrapperPlayServerEntityHeadRotation();
        packet.setEntityID(id);
        packet.setHeadYaw((byte) ((l.getYaw() / 360.0f) * 255.0f));
        return packet.getHandle();
    }

    private Pair<PacketContainer, Location> getMovePacket(Location from, Location to) {
        if (!from.getWorld().equals(to.getWorld())) {
            return new Pair<PacketContainer, Location>(null, from);
        }

        if ((is18 && from.distanceSquared(to) > 16.0d) || (!is18 && from.distanceSquared(to) > 64.0d)) { // 4 blocks, or 8 blocks
            double distance = (is18) ? 4.0d : 8.0d;
            double angle = from.toVector().angle(to.toVector());

            double sin = Math.sin(angle);
            to = new Location(from.getWorld(), from.getX() + distance * Math.cos(angle), from.getY() + distance * sin * sin, from.getZ() + distance * sin);
        }

        WrapperPlayServerRelEntityMove packet = new WrapperPlayServerRelEntityMove();
        packet.setEntityID(id);
        if (is18) {
            packet.setDx((int) Math.floor((to.getX() - from.getX()) / 32.0d));
            packet.setDy((int) Math.floor((to.getY() - from.getY()) / 32.0d));
            packet.setDz((int) Math.floor((to.getZ() - from.getZ()) / 32.0d));
        } else {
            packet.setDx((int) Math.floor((to.getX() - from.getX()) * 4096.0d));
            packet.setDy((int) Math.floor((to.getY() - from.getY()) * 4096.0d));
            packet.setDz((int) Math.floor((to.getZ() - from.getZ()) * 4096.0d));
        }

        packet.setOnGround(!isFlying(to));
        return new Pair<PacketContainer, Location>(packet.getHandle(), to);
    }

    private PacketContainer getTeleportPacket(Location l) {
        WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport();
        packet.setEntityID(id);
        packet.setX(l.getX());
        packet.setY(l.getY());
        packet.setZ(l.getZ());
        packet.setOnGround(!isFlying(l));
        packet.setPitch(l.getPitch());
        packet.setYaw(l.getYaw());
        return packet.getHandle();
    }

    private void sendPacket(Player player, PacketContainer packet, boolean ignoreDead) {
        if (player == null || packet == null) {
            return;
        }

        if (!ignoreDead && dead.get()) {
            return;
        }

        try {
            manager.sendServerPacket(player, packet);
        } catch (Exception ex) {
            IExceptionHandler handler = ServiceLocator.getService(IExceptionHandler.class);
            if (handler != null) {
                handler.sendException(ex);
            }
            ex.printStackTrace();
        }
    }

    private LongUnaryOperator updateAnimation = new LongUnaryOperator() {
        public long applyAsLong(long operand) {
            long time = System.currentTimeMillis();
            if (time - operand >= 1000L) {
                return time;
            }
            return operand;
        }
    };
    private LongUnaryOperator updateAttack = new LongUnaryOperator() {
        public long applyAsLong(long operand) {
            long time = System.currentTimeMillis();
            if (time - operand >= 1000L) {
                return time;
            }
            return operand;
        }
    };

    private static int getNextEntityId() {
        int newId = currentEntityId.getAndDecrement();

        if (newId <= ((Number) Accessors.getFieldAccessor(MinecraftReflection.getEntityClass(), "entityCount", true).get(null)).intValue()) {
            currentEntityId.set(Integer.MAX_VALUE);
            newId = currentEntityId.getAndDecrement();
        }

        return newId;
    }

    private double fastSqrt(double in) {
        // Fast but inaccurate square root
        double retVal = Double.longBitsToDouble(((Double.doubleToLongBits(in) - (1L << 52)) >> 1) + (1L << 61));

        // Newton's method for improving accuracy at the cost of speed. 2 iterations will be slower than Math.sqrt()
        // So we only use 1 iteration
        retVal = (retVal + in / retVal) / 2.0d;

        return retVal;
    }

    private boolean isFlying(Location location) {
        return (BlockUtil.getHighestSolidBlock(location).add(0.0d, 1.0d, 0.0d).getY() == location.getY()) ? false : true;
    }
}
