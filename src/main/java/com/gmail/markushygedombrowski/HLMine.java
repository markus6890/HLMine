package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.cooldown.BlockReplace;
import com.gmail.markushygedombrowski.listener.MineListener;
import com.gmail.markushygedombrowski.listener.RegionListener;
import com.gmail.markushygedombrowski.mines.MineCommands;
import com.gmail.markushygedombrowski.mines.MineManager;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.shards.Shards;
import com.gmail.markushygedombrowski.utils.BlockInfo;
import com.gmail.markushygedombrowski.utils.ConfigMineManager;
import com.gmail.markushygedombrowski.utils.HLMineUtils;



import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class HLMine extends JavaPlugin {
    private BlockReplace blockReplace;
    private MineManager mineManager;
    private ConfigMineManager configM;

    @Override
    public void onEnable() {
        PlayerProfiles playerProfiles = VagtProfiler.getInstance().getPlayerProfiles();
        ConfigurationSerialization.registerClass(BlockInfo.class);
        loadConfigManager();
        HLMineUtils utils = new HLMineUtils(this);

        Shards shards = new Shards(playerProfiles);
        RegionListener regionListener = new RegionListener();
        getServer().getPluginManager().registerEvents(regionListener, this);
        blockReplace = new BlockReplace(mineManager, regionListener);
        MineCommands createMine = new MineCommands(mineManager, this, blockReplace);
        getCommand("createmine").setExecutor(createMine);
        MineListener mineListener = new MineListener(mineManager, blockReplace, shards);
        getServer().getPluginManager().registerEvents(mineListener, this);
        System.out.println("-----------------------------");
        System.out.println("HLMine enabled");
        System.out.println("-----------------------------");

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                shards.cooldown();
                blockReplace.resetMine();
            }
        }, 20L, 20L);
    }

    @Override
    public void onDisable() {
        blockReplace.resetBlocks();
        System.out.println("-----------------------------");
        System.out.println("HLMine disabled");
        System.out.println("-----------------------------");

    }

    public void loadConfigManager() {
        configM = new ConfigMineManager();
        configM.mineSetup();
        configM.saveMines();
        configM.reloadMines();
        mineManager = new MineManager(configM);
        mineManager.load();
    }

    public void reload() {
        reloadConfig();
        configM.reloadMines();
        loadConfigManager();
    }
}
