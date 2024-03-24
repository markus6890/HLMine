package com.gmail.markushygedombrowski.mines;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MineInfo {
    private String name;
    private List<ItemStack> mineBlocks;
    private MineType type;
    private int time;
    private int fixedTime;
    private Location pasteLocation;


    private ProtectedRegion region;


    public MineInfo(String name, List<ItemStack> mineBlocks, MineType type, int time, int fixedTime, Location pasteLocation, ProtectedRegion region) {
        this.name = name;
        this.mineBlocks = mineBlocks;
        this.type = type;
        this.time = time;
        this.fixedTime = fixedTime;
        this.pasteLocation = pasteLocation;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public ProtectedRegion getRegion() {
        return region;
    }

    public void setRegion(ProtectedRegion region) {
        this.region = region;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(MineType type) {
        this.type = type;
    }

    public int getFixedTime() {
        return fixedTime;
    }

    public void setFixedTime(int fixedTime) {
        this.fixedTime = fixedTime;
    }

    public MineType getType() {
        return type;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public List<ItemStack> getMineBlocks() {
        return mineBlocks;
    }

    public Location getPasteLocation() {
        return pasteLocation;
    }

    public void setPasteLocation(Location pasteLocation) {
        this.pasteLocation = pasteLocation;
    }

    public void setMineBlocks(List<ItemStack> mineBlocks) {
        this.mineBlocks = mineBlocks;
    }

    public int getMinutesLeft() {
        return time / 60 / 20;
    }
    public int getSecondsLeft() {
        return time / 20;
    }


    public boolean isBlockIn(ItemStack block) {
        for (ItemStack st : mineBlocks) {
            if (st == null) return false;
            if (st.getType() == block.getType() && st.getDurability() == block.getDurability()) {
                return true;
            }
        }
        return false;
    }
}
