package com.gmail.markushygedombrowski.mines;

import com.gmail.markushygedombrowski.utils.BlockInfo;
import com.gmail.markushygedombrowski.utils.ConfigMineManager;
import com.gmail.markushygedombrowski.utils.HLMineUtils;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineManager {
    private ConfigMineManager configM;
    private Map<String, MineInfo> mineMap = new HashMap<>();

    public MineManager(ConfigMineManager configM) {
        this.configM = configM;
    }

    public void load() {
        FileConfiguration config = configM.getMines();
        mineMap.clear();
        if (config.getConfigurationSection("mines") == null) {
            return;
        }
        config.getConfigurationSection("mines").getKeys(false).forEach(entry -> {
            String path = "mines." + entry + ".";
            String name = config.getString(path + "name");
            String tag = config.getString(path + "tag");
            int time = config.getInt(path + "time");
            List<BlockInfo> blocks = ((List<BlockInfo>) config.get(path + "blocks"));
            Location pasteLocation = (Location) config.get(path + "pasteLocation");
            ProtectedRegion region = HLMineUtils.getRegion(name, pasteLocation.getWorld());

            MineInfo mineInfo = new MineInfo(name, blocks, MineType.getMineType(tag), time, time, pasteLocation, region);
            mineMap.put(name, mineInfo);
        });


    }

    public void save(MineInfo mineInfo) {
        FileConfiguration config = configM.getMines();
        String mine = "mines." + mineInfo.getName();
        config.set(mine + ".name", mineInfo.getName());
        config.set(mine + ".tag", mineInfo.getType().name());
        config.set(mine + ".time", mineInfo.getTime());
        config.set(mine + ".blocks", mineInfo.getMineBlocks());
        config.set(mine + ".pasteLocation", mineInfo.getPasteLocation());
        configM.saveMines();
        mineMap.put(mineInfo.getName(), mineInfo);
    }

    public MineInfo getMineInfo(String mine) {
        return mineMap.get(mine);
    }

    public Map<String, MineInfo> getMineMap() {
        return mineMap;
    }
}
