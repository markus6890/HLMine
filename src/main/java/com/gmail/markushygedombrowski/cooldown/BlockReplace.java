package com.gmail.markushygedombrowski.cooldown;

import com.gmail.markushygedombrowski.listener.RegionListener;
import com.gmail.markushygedombrowski.mines.MineInfo;
import com.gmail.markushygedombrowski.mines.MineManager;
import com.gmail.markushygedombrowski.utils.BlockInfo;
import com.gmail.markushygedombrowski.utils.HLMineUtils;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;


public class BlockReplace {
    private MineManager mineManager;

    private RegionListener regionListener;
    private HashMap<Block, BlockInfo> blocklist = new HashMap<>();



    public BlockReplace(MineManager mineManager, RegionListener regionListener) {
        this.mineManager = mineManager;
        this.regionListener = regionListener;
    }

    public void addBlock(Block block, BlockInfo blockInfo) {
        blocklist.put(block, blockInfo);
    }

    public void resetMine() {
        mineManager.getMineMap().forEach((s, mineInfo) -> {
            if (!(mineInfo.getTime() <= 0)) {
                sendResetTimeLeftMessage(mineInfo);
                mineInfo.setTime(mineInfo.getTime() - 20);
                return;
            }
            replaceBlocks(mineInfo);
            mineInfo.setTime(mineInfo.getFixedTime());
            String message = "§7§l[§2§lTræfarm§7§l] §2Træfarmen §7er blevet §aresat!";
            sendMessageToPlayers(mineInfo, message);
        });
    }

    private void sendResetTimeLeftMessage(MineInfo mineInfo) {
        String message;
        if (mineInfo.getSecondsLeft() == 60) {
            message = "§7§l[§2§lTræfarm§7§l] §aDer er §c1 §aminut til at træfarmen resetter!";
            sendMessageToPlayers(mineInfo, message);
        } else if (mineInfo.getSecondsLeft() <= 10) {
            message = "§7§l[§2§lTræfarm§7§l] §aDer er §c" + mineInfo.getSecondsLeft() + " §asekunder til at træfarmen resetter!";
            sendMessageToPlayers(mineInfo, message);
        }
    }

    public void replaceBlocks(MineInfo mineInfo) {
        if (blocklist.isEmpty()) return;

        new HashMap<>(blocklist).forEach((block, info) -> {
            if (HLMineUtils.isLocInRegion(block.getLocation(), mineInfo.getName())) {
                if (!mineInfo.isBlockIn(block.getState().getData().toItemStack())) {
                    ItemStack item = new ItemStack(info.getType(), 1, (short) info.getDura());
                    block.setType(item.getType());
                    block.setData(item.getData().getData());


                    blocklist.remove(block);
                }
            }
        });
    }

    public void sendMessageToPlayers(MineInfo mineInfo, String message) {
        regionListener.getPlayers().entrySet().stream().filter(entry -> {
            return entry.getKey().getId().equalsIgnoreCase(mineInfo.getName());
        }).forEach(entry -> entry.getValue().sendMessage(message));
    }


    public void resetBlocks() {
        if (blocklist.isEmpty()) return;
        blocklist.forEach((block, info) -> {
            mineManager.getMineMap().entrySet().stream().filter(mine -> {
                return HLMineUtils.isLocInRegion(block.getLocation(), mine.getKey());
            }).forEach(mine -> {
                if (!mine.getValue().isBlockIn(block.getState().getData().toItemStack())) {
                    block.setType(block.getType());

                }
            });
        });
    }


}
