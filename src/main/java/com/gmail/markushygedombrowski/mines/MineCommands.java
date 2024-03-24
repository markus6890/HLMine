package com.gmail.markushygedombrowski.mines;
import com.gmail.markushygedombrowski.HLMine;
import com.gmail.markushygedombrowski.cooldown.BlockReplace;
import com.gmail.markushygedombrowski.utils.HLMineUtils;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MineCommands implements CommandExecutor {
    private MineManager mineManager;
    private HLMine plugin;
    private BlockReplace blockReplace;
    public MineCommands(MineManager mineManager, HLMine plugin, BlockReplace blockReplace) {
        this.mineManager = mineManager;
        this.plugin = plugin;
        this.blockReplace = blockReplace;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("§cKan kun bruges InGame!");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("createMine")) {
            p.sendMessage("§cDet har du ikke permission til!");
            return true;
        }
        if(alias.equalsIgnoreCase("reloadmine")) {
            plugin.reload();
            p.sendMessage("§aMine reloaded!");
            return true;
        } else if(alias.equalsIgnoreCase("setminepoint")) {
            String mine = args[0];
            MineInfo mineInfo = mineManager.getMineInfo(mine);
            if(mineInfo == null) {
                p.sendMessage("§cDen mine findes ikke!");
                return true;
            }
            mineInfo.setPasteLocation(p.getLocation());
            p.sendMessage("§aMine point added!");
            return true;
        } else if(alias.equalsIgnoreCase("resetmine")) {
            if (resetMine(args, p)) return true;
            return true;
        }
        if (args.length != 3) {
            commandList(p);

            return true;
        }
        String mine = args[0];
        MineType type = MineType.getMineType(args[1]);
        int resetTime = Integer.parseInt(args[2]);
        resetTime = resetTime * 60 * 20;

        List<ItemStack> blockList = getBlocksFromPlayerInventory(p);


        if(alias.equalsIgnoreCase("replacemine")) {
            replaceMine(p,mine,type,resetTime,blockList);
            return true;
        }
         createMine(p, mine, type, resetTime, blockList);


        return true;
    }

    private void commandList(Player p) {
        p.sendMessage("/createmine <regionname> <MineType> <resettime>");
        p.sendMessage("/replacemine <regionname> <MineType> <resettime>");
        p.sendMessage("/setminepoint <regionname>");
        p.sendMessage("/resetmine <regionname>");
        p.sendMessage("/reloadmine");
        p.sendMessage("MineType: TREE/MINE");
    }

    private boolean resetMine(String[] args, Player p) {
        String mine = args[0];
        MineInfo mineInfo = mineManager.getMineInfo(mine);
        if(mineInfo == null) {
            p.sendMessage("§cDen mine findes ikke!");
            return true;
        }
        blockReplace.replaceBlocks(mineInfo);
        p.sendMessage("§aMine reset!");
        return false;
    }

    private void createMine(Player p, String mine, MineType type, int resetTime, List<ItemStack> blockList) {
        ProtectedRegion region = HLMineUtils.getRegion(mine, p.getWorld());
        if(region == null) {
            p.sendMessage("§cDen region findes ikke!");
            return;
        }
        MineInfo mineInfo = new MineInfo(mine, blockList, type, resetTime, resetTime, p.getLocation(), region);
        mineManager.save(mineInfo);
        p.sendMessage("§aMine saved!");
        p.sendMessage("§aMine: " + mine);
        p.sendMessage("§aType: " + type);
        p.sendMessage("§aResetTime: " + resetTime);
        p.sendMessage("§aBlocks: " + blockList.toString());

    }

    private void replaceMine(Player p, String mine, MineType type, int resetTime, List<ItemStack> blockList) {
        MineInfo mineInfo = mineManager.getMineInfo(mine);
        if(mineInfo == null) {
            p.sendMessage("§cDen mine findes ikke!");
            return;
        }
        mineInfo.setMineBlocks(blockList);
        mineInfo.setType(type);
        mineInfo.setFixedTime(resetTime);
        mineInfo.setTime(resetTime);
        mineInfo.setPasteLocation(p.getLocation());
        mineManager.save(mineInfo);
        p.sendMessage("§aMine Blocks replaced!");

    }
    private List<ItemStack> getBlocksFromPlayerInventory(Player p) {
        List<ItemStack> blockList = new ArrayList<>();
        Arrays.stream(p.getInventory().getContents()).forEach(itemStack -> {
            if (itemStack != null) {
                if(itemStack.getType().isBlock()) {
                    blockList.add(itemStack);
                }
            }
        });
        return blockList;
    }
}
