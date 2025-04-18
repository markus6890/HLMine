package com.gmail.markushygedombrowski.mines;

import com.gmail.markushygedombrowski.utils.BlockInfo;
import com.gmail.markushygedombrowski.utils.RandomChanceCollection;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MineInfo {
    private String name;
    private List<BlockInfo> mineBlocks;
    private MineType type;
    private int time;
    private int fixedTime;
    private Location pasteLocation;
    private RandomChanceCollection<ItemStack> blocksSpawnChance;


    private ProtectedRegion region;


    public MineInfo(String name, List<BlockInfo> mineBlocks, MineType type, int time, int fixedTime, Location pasteLocation, ProtectedRegion region) {
        this.name = name;
        this.mineBlocks = mineBlocks;
        this.type = type;
        this.time = time;
        this.fixedTime = fixedTime;
        this.pasteLocation = pasteLocation;
        this.region = region;
        if(type == MineType.MINE) {
            blocksSpawnChance = new RandomChanceCollection<>();
            mineBlocks.forEach(blockInfo -> {
                if(blockInfo.getChance() <= 0) {
                    blockInfo.setChance(10);
                }
                blocksSpawnChance.add(blockInfo.getChance(), blockInfo.getItemStack());
            });
        }

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

    public List<BlockInfo> getMineBlocks() {
        return mineBlocks;
    }
    public BlockInfo getBlockInfo(Block block) {
        for (BlockInfo blockInfo : mineBlocks) {
            if (blockInfo.getItemStack().getType() == block.getType() && blockInfo.getItemStack().getDurability() == block.getState().getData().toItemStack().getDurability()) {
                return blockInfo;
            }
        }
        return null;
    }
    public void addBlockInfo(BlockInfo blockInfo) {
        mineBlocks.add(blockInfo);
        if(type == MineType.MINE) {
            blocksSpawnChance.add(blockInfo.getChance(), blockInfo.getItemStack());
        }
    }
    public RandomChanceCollection<ItemStack> getBlocksSpawnChance() {
        return blocksSpawnChance;
    }

    public Location getPasteLocation() {
        return pasteLocation;
    }

    public void setPasteLocation(Location pasteLocation) {
        this.pasteLocation = pasteLocation;
    }

    public void setMineBlocks(List<BlockInfo> mineBlocks) {
        this.mineBlocks = mineBlocks;
    }

    public int getMinutesLeft() {
        return time / 60 / 20;
    }
    public int getSecondsLeft() {
        return time / 20;
    }


    public boolean isBlockIn(Block block) {
        boolean[] found = {false};
        ItemStack blockStack = new ItemStack(block.getType(), 1, block.getState().getData().toItemStack().getDurability());
        mineBlocks.forEach(blockInfo -> {
            if (blockInfo.getItemStack().getType() == blockStack.getType() && blockInfo.getItemStack().getDurability() == blockStack.getDurability()){
                found[0] = true;
                return;
            }
        });
        return found[0];
    }
}
