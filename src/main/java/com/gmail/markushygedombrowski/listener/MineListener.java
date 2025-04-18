package com.gmail.markushygedombrowski.listener;

import com.gmail.markushygedombrowski.cooldown.BlockReplace;
import com.gmail.markushygedombrowski.mines.MineInfo;
import com.gmail.markushygedombrowski.mines.MineManager;
import com.gmail.markushygedombrowski.shards.Shards;
import com.gmail.markushygedombrowski.utils.BlockInfo;
import com.gmail.markushygedombrowski.utils.HLMineUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

public class MineListener implements Listener {
    private MineManager mineManager;
    private BlockReplace blockReplace;
    private Shards shards;

    public MineListener(MineManager mineManager, BlockReplace blockReplace, Shards shards) {
        this.mineManager = mineManager;
        this.blockReplace = blockReplace;
        this.shards = shards;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlock();
        if (p.hasPermission("bygger")) return;
        if (HLMineUtils.isLocInRegion(event.getBlock().getLocation(), "vagtshards")) {
            if (!p.hasPermission("vagtshards")) {
                event.setCancelled(true);
                return;
            }
            shards.mineShards(p, block);
            event.setCancelled(true);
            return;
        }
        Map<String, MineInfo> mineinfo = mineManager.getMineMap();
        mineinfo.entrySet().stream().filter(mineInfo -> checkBlockLoc(block, mineInfo.getKey())).findFirst().ifPresent(mineInfo ->
        {
            if (!mineInfo.getValue().isBlockIn(block)) {
                event.setCancelled(true);
                return;
            }

            blockReplace.addBlock(block,mineInfo.getValue().getBlockInfo(block));
        });



    }

    public boolean checkBlockLoc(Block block, String entry) {
        return HLMineUtils.isLocInRegion(block.getLocation(), entry);
    }
}
