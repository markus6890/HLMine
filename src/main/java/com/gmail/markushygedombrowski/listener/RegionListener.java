package com.gmail.markushygedombrowski.listener;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionEnteredEvent;
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionListener implements Listener {
    private Map<ProtectedRegion,Player> players = new HashMap<>();

    public Map<ProtectedRegion,Player> getPlayers() {
        return players;
    }

    @EventHandler
    public void onRegionEnter(RegionEnterEvent e) {
        players.put(e.getRegion(),e.getPlayer());

    }
    @EventHandler
    public void onRegionLeave(RegionLeaveEvent e) {
        players.remove(e.getRegion());
    }


}
