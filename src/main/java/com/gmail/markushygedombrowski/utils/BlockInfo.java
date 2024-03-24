package com.gmail.markushygedombrowski.utils;

import org.bukkit.Material;

public class BlockInfo {
    private int dura;
    private Material material;

    public BlockInfo(int dura, Material material) {
        this.dura = dura;
        this.material = material;
    }

    public Material getType() {
        return material;
    }
    public int getDura() {
        return dura;
    }
}
