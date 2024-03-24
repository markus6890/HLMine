package com.gmail.markushygedombrowski.utils;

import com.gmail.markushygedombrowski.HLMine;
import com.gmail.markushygedombrowski.mines.MineInfo;
import com.gmail.markushygedombrowski.mines.MineManager;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Map;

import static org.bukkit.Bukkit.getServer;


public class HLMineUtils {
    private static HLMine plugin;

    public HLMineUtils(HLMine plugin) {
        HLMineUtils.plugin = plugin;
    }

    public static boolean isLocInRegion(Location loc, String regionName) {
        if (regionName == null) {
            return true;
        }
        ApplicableRegionSet set = getWGSet(loc);
        if (set == null) {
            return false;
        }
        for (ProtectedRegion r : set) {
            if (r.getId().equalsIgnoreCase(regionName)) {
                return true;
            }
        }
        return false;
    }
    public static ProtectedRegion getRegion(String regionName, World world) {
        WorldGuardPlugin wg = getWorldGuard();
        if (wg == null) {
            return null;
        }
        RegionManager rm = wg.getRegionManager(world);
        RegionContainer container = wg.getRegionContainer();
        if (rm == null) {
            return null;
        }
        return rm.getRegion(regionName);
    }



    public static MineInfo getMine(String regionName, MineManager mineManager) {
        Map<String, MineInfo> mineinfo = mineManager.getMineMap();
        return mineinfo.get(regionName);
    }


    public static MineInfo getMine(org.bukkit.Location loc, MineManager mineManager) {
        Map<String, MineInfo> mineinfo = mineManager.getMineMap();
        MineInfo[] mine = new MineInfo[1];
        mineinfo.entrySet().stream().filter(entry -> {
            return isLocInRegion(loc,entry.getKey());
        }).forEach(entry -> mine[0] = entry.getValue());
        return mine[0];
    }

    private static ApplicableRegionSet getWGSet(Location loc) {
        WorldGuardPlugin wg = getWorldGuard();
        if (wg == null) {
            return null;
        }
        RegionManager rm = wg.getRegionManager(loc.getWorld());
        if (rm == null) {
            return null;
        }
        return rm.getApplicableRegions(com.sk89q.worldguard.bukkit.BukkitUtil.toVector(loc));
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
    public static ProtectedPolygonalRegion getPolyRegion(ProtectedRegion region) {
        if (region instanceof ProtectedPolygonalRegion) {
            return (ProtectedPolygonalRegion) region;
        }
        return null;
    }












}
