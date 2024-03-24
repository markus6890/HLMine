package com.gmail.markushygedombrowski.utils;

import com.gmail.markushygedombrowski.HLMine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigMineManager {
    private HLMine plugin = HLMine.getPlugin(HLMine.class);
    public FileConfiguration minecfg;
    public File mineFile;

    public void mineSetup() {
        List<File> configList = new ArrayList<>();
        if(!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();

        }
        mineFile = new File(plugin.getDataFolder(),"mines.yml");
        configList.add(mineFile);
        configList.forEach(file -> {
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "could not create " + file.getName() + "File");
                }
            }
        });

        minecfg = YamlConfiguration.loadConfiguration(mineFile);
    }
    public FileConfiguration getMines() {
        return minecfg;
    }
    public void saveMines() {
        try {
            minecfg.save(mineFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "could not save mines.yml File");
        }
    }
    public void reloadMines() {
        minecfg = YamlConfiguration.loadConfiguration(mineFile);
    }
}
