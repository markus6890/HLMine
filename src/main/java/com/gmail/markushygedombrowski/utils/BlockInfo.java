package com.gmail.markushygedombrowski.utils;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockInfo implements Cloneable, ConfigurationSerializable {
    private final ItemStack itemStack;
    private double chance;

    public BlockInfo(ItemStack itemStack, double chance) {
        this.itemStack = itemStack;
        this.chance = chance;
    }


    public ItemStack getItemStack() {
        return itemStack;
    }

    public double getChance() {
        return chance;
    }
    public void setChance(double chance) {
        this.chance = chance;
    }
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("chance", this.chance);

        // Convert the ItemStack to a serializable format
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", this.itemStack);
        result.put("item", config.saveToString());

        return result;
    }
    @Override
    public BlockInfo clone() {
        try {
            return (BlockInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }


    public static BlockInfo deserialize(Map<String, Object> args) {
        double chance = (double) args.get("chance");

        // Convert the serialized ItemStack back to an ItemStack
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString((String) args.get("item"));
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        ItemStack item = config.getItemStack("item");

        return new BlockInfo(item, chance);
    }
}
