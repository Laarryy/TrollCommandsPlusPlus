package me.egg82.tcpp.core;

import org.bukkit.Material;

public class FakeBlockData {
    private final Material newType;
    private final byte newData;

    public FakeBlockData(Material newType) { this(newType, (byte) 0x00); }

    public FakeBlockData(Material newType, byte newData) {
        this.newType = newType;
        this.newData = newData;
    }

    public Material getType() { return newType; }

    public byte getData() { return newData; }
}
