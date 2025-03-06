package com.gmail.markushygedombrowski.shards;

import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class Shards {
    private HashMap<Block,Integer> cooldown = new HashMap<>();
    private PlayerProfiles playerProfiles;

    public Shards(PlayerProfiles playerProfiles) {
        this.playerProfiles = playerProfiles;
    }

    public void mineShards(Player p, Block block) {
        if(block.getType() == Material.EMERALD_ORE) {
            p.sendMessage("§aDu har fundet en shard!");
            PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
            ItemStack shard = new ItemStack(Material.PRISMARINE_CRYSTALS,profile.castPropertyToInt(profile.getProperty("shardsrate")));
            ItemMeta meta = shard.getItemMeta();
            meta.setDisplayName("§aShard");
            shard.setItemMeta(meta);
            p.getInventory().addItem(shard);
            block.setType(Material.BEDROCK);
            cooldown.put(block, 240);

        }
    }

    public void cooldown() {
        if(cooldown.isEmpty()) return;
        for(Block block : cooldown.keySet()) {
            int time = cooldown.get(block);
            if(time == 0) {
                cooldown.remove(block);
                block.setType(Material.EMERALD_ORE);
            } else {
                cooldown.put(block, time - 1);
            }
        }
    }

}
