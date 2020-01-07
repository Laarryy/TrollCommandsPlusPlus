package me.egg82.ae.core;

import java.util.Objects;
import org.bukkit.World;

public class ChunkData {
    private final World world;
    private final int chunkX;
    private final int chunkZ;

    public ChunkData(World world, int chunkX, int chunkZ) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public World getWorld() { return world; }

    public int getChunkX() { return chunkX; }

    public int getChunkZ() { return chunkZ; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkData chunkData = (ChunkData) o;
        return chunkX == chunkData.chunkX &&
                chunkZ == chunkData.chunkZ &&
                Objects.equals(world, chunkData.world);
    }

    public int hashCode() {
        return Objects.hash(world, chunkX, chunkZ);
    }
}
