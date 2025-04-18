package com.gmail.markushygedombrowski.mines;
import com.gmail.markushygedombrowski.HLMine;
import com.gmail.markushygedombrowski.cooldown.BlockReplace;
import com.gmail.markushygedombrowski.utils.BlockInfo;
import com.gmail.markushygedombrowski.utils.HLMineUtils;
import com.gmail.markushygedombrowski.utils.RandomChanceCollection;
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
    private final MineManager mineManager;
    private final HLMine plugin;
    private final BlockReplace blockReplace;

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

        Player player = (Player) sender;

        if (!player.hasPermission("createMine")) {
            player.sendMessage("§cDet har du ikke permission til!");
            return true;
        }

        switch (alias.toLowerCase()) {
            case "deletemine":
                handleDeleteMine(player, args);
                break;
            case "addmineblock":
                handleAddMineBlock(player, args);
                break;
            case "reloadmine":
                plugin.reload();
                player.sendMessage("§aMine reloaded!");
                break;
            case "setminepoint":
                handleSetMinePoint(player, args);
                break;
            case "resetmine":
                handleResetMine(player, args);
                break;
            case "replacemine":
                handleReplaceMine(player, args);
                break;
            case "createmine":
                handleCreateMine(player, args);
                break;
            default:
                sendCommandList(player);
                break;
        }
        return true;
    }

    private void handleDeleteMine(Player player, String[] args) {
        if (!validateArgsLength(player, args, 1, "/deletemine <regionname>")) return;

        String mineName = args[0];
        MineInfo mineInfo = getMineInfo(player, mineName);
        if (mineInfo == null) return;

        mineManager.getMineMap().remove(mineName);
        player.sendMessage("§aMine deleted!");
    }

    private void handleAddMineBlock(Player player, String[] args) {
        if (!validateArgsLength(player, args, 1, "/addmineblock <regionname>")) return;

        List<ItemStack> blockList = getBlocksFromPlayerInventory(player);
        if (blockList.isEmpty()) {
            player.sendMessage("§cDu skal have de blokke du vil tilføje i dit inventory!");
            return;
        }

        String mineName = args[0];
        MineInfo mineInfo = getMineInfo(player, mineName);
        if (mineInfo == null) return;

        blockList.forEach(itemStack -> mineInfo.addBlockInfo(new BlockInfo(itemStack, 10)));
        mineManager.save(mineInfo);
        player.sendMessage("§aBlocks added to mine!");
    }

    private void handleSetMinePoint(Player player, String[] args) {
        if (!validateArgsLength(player, args, 1, "/setminepoint <regionname>")) return;

        String mineName = args[0];
        MineInfo mineInfo = getMineInfo(player, mineName);
        if (mineInfo == null) return;

        mineInfo.setPasteLocation(player.getLocation());
        player.sendMessage("§aMine point added!");
    }

    private void handleResetMine(Player player, String[] args) {
        if (!validateArgsLength(player, args, 1, "/resetmine <regionname>")) return;

        String mineName = args[0];
        MineInfo mineInfo = getMineInfo(player, mineName);
        if (mineInfo == null) return;

        blockReplace.replaceBlocks(mineInfo);
        player.sendMessage("§aMine reset!");
    }

    private void handleReplaceMine(Player player, String[] args) {
        if (!validateArgsLength(player, args, 3, "/replacemine <regionname> <MineType> <resettime>")) return;

        String mineName = args[0];
        MineType type = MineType.getMineType(args[1]);
        int resetTime = parseResetTime(player, args[2]);
        if (resetTime == -1) return;

        List<BlockInfo> blockList = getBlockInfoListFromInventory(player);
        if (blockList.isEmpty()) {
            player.sendMessage("§cDu skal have de blokke du vil tilføje i dit inventory!");
            return;
        }

        MineInfo mineInfo = getMineInfo(player, mineName);
        if (mineInfo == null) return;

        mineInfo.setMineBlocks(blockList);
        mineInfo.setType(type);
        mineInfo.setFixedTime(resetTime);
        mineInfo.setTime(resetTime);
        mineInfo.setPasteLocation(player.getLocation());
        mineManager.save(mineInfo);
        player.sendMessage("§aMine Blocks replaced!");
    }

    private void handleCreateMine(Player player, String[] args) {
        if (!validateArgsLength(player, args, 3, "/createmine <regionname> <MineType> <resettime>")) return;

        String mineName = args[0];
        MineType type = MineType.getMineType(args[1]);
        int resetTime = parseResetTime(player, args[2]);
        if (resetTime == -1) return;

        List<BlockInfo> blockList = getBlockInfoListFromInventory(player);
        if (blockList.isEmpty()) {
            player.sendMessage("§cDu skal have de blokke du vil tilføje i dit inventory!");
            return;
        }

        if (mineManager.getMineInfo(mineName) != null) {
            player.sendMessage("§cDen mine findes allerede!");
            return;
        }

        ProtectedRegion region = HLMineUtils.getRegion(mineName, player.getWorld());
        if (region == null) {
            player.sendMessage("§cDen region findes ikke!");
            return;
        }

        MineInfo mineInfo = new MineInfo(mineName, blockList, type, resetTime, resetTime, player.getLocation(), region);
        mineManager.save(mineInfo);
        player.sendMessage("§aMine created!");
    }

    private boolean validateArgsLength(Player player, String[] args, int expectedLength, String usage) {
        if (args.length != expectedLength) {
            player.sendMessage("§cBrug: " + usage);
            return false;
        }
        return true;
    }

    private MineInfo getMineInfo(Player player, String mineName) {
        MineInfo mineInfo = mineManager.getMineInfo(mineName);
        if (mineInfo == null) {
            player.sendMessage("§cDen mine findes ikke!");
        }
        return mineInfo;
    }

    private int parseResetTime(Player player, String resetTimeArg) {
        try {
            return Integer.parseInt(resetTimeArg) * 60 * 20;
        } catch (NumberFormatException e) {
            player.sendMessage("§cReset time skal være et tal!");
            return -1;
        }
    }

    private List<BlockInfo> getBlockInfoListFromInventory(Player player) {
        List<BlockInfo> blockList = new ArrayList<>();
        getBlocksFromPlayerInventory(player).forEach(itemStack -> blockList.add(new BlockInfo(itemStack, 10)));
        return blockList;
    }

    private List<ItemStack> getBlocksFromPlayerInventory(Player player) {
        List<ItemStack> blockList = new ArrayList<>();
        Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
            if (itemStack != null && itemStack.getType().isBlock()) {
                blockList.add(itemStack);
            }
        });
        return blockList;
    }

    private void sendCommandList(Player player) {
        player.sendMessage("/createmine <regionname> <MineType> <resettime>");
        player.sendMessage("/deletemine <regionname>");
        player.sendMessage("/replacemine <regionname> <MineType> <resettime>");
        player.sendMessage("/setminepoint <regionname>");
        player.sendMessage("/resetmine <regionname>");
        player.sendMessage("/addmineblock <regionname>");
        player.sendMessage("/reloadmine");
        player.sendMessage("MineType: TREE/MINE");
    }
}
