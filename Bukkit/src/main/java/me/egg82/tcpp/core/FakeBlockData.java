package me.egg82.ae.core;

import java.util.Objects;
import org.bukkit.Material;

public class FakeBlockData {
    private final Material newType;
    private final byte newData;

    private final int hash;

    public FakeBlockData(Material newType) { this(newType, (byte) 0x00); }

    public FakeBlockData(Material newType, byte newData) {
        this.newType = newType;
        this.newData = newData;

        this.hash = Objects.hash(newType, newData);
    }

    public Material getType() { return newType; }

    public byte getData() { return newData; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FakeBlockData)) return false;
        FakeBlockData that = (FakeBlockData) o;
        return newData == that.newData &&
                newType == that.newType;
    }

    public int hashCode() { return hash; }
}
