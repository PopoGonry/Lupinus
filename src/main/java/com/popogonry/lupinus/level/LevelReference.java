package com.popogonry.lupinus.level;

import com.popogonry.lupinus.DataManager;
import com.popogonry.lupinus.Reference;
import com.popogonry.lupinus.player.PlayerGUI;
import com.popogonry.lupinus.player.PlayerReference;
import com.popogonry.lupinus.stat.StatReference;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class LevelReference {
    public static HashMap<UUID, Integer> playerLevelHashMap = new HashMap<>();
    public static HashMap<UUID, Integer> playerExpHashMap = new HashMap<>();

    public static DataManager levelDM = new DataManager("level", "/data");

    public static boolean saveLevelData(Player player) {
        levelDM.getConfig().set("data." + player.getUniqueId() + ".LEVEL", playerLevelHashMap.get(player.getUniqueId()));
        levelDM.getConfig().set("data." + player.getUniqueId() + ".EXP", playerExpHashMap.get(player.getUniqueId()));
        levelDM.saveConfig();

        playerLevelHashMap.remove(player.getUniqueId());
        playerExpHashMap.remove(player.getUniqueId());

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + player.getName() + " 레벨 데이터 세이브 완료" + " (" + levelDM.fileName + ")");
        return true;
    }

    public static boolean onlinePlayerSaveLevelData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            saveLevelData(player);
        }
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + "온라인 플레이어 레벨 데이터 세이브 완료" + " (" + levelDM.fileName + ")");
        return true;
    }


    public static boolean loadLevelData(Player player) {
        if(levelDM.getConfig().get("data." + player.getUniqueId()) == null) {
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataError + player.getName() + " 레벨 데이터 로드 실패" + " (" + levelDM.fileName + ")");
            return false;
        }
        else {

            playerLevelHashMap.remove(player.getUniqueId());
            playerExpHashMap.remove(player.getUniqueId());

            playerLevelHashMap.put(player.getUniqueId(), (int) levelDM.getConfig().get("data." + player.getUniqueId() + ".LEVEL"));
            playerExpHashMap.put(player.getUniqueId(), (int) levelDM.getConfig().get("data." + player.getUniqueId() + ".EXP"));

            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataLoad + player.getName() + " 레벨 데이터 로드 완료" + " (" + levelDM.fileName + ")");
            return true;
        }
    }
    public static boolean onlinePlayerLoadLevelData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadLevelData(player);
        }
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + "온라인 플레이어 레벨 데이터 로드 완료" + " (" + levelDM.fileName + ")");
        return true;
    }
    public static void playerJoinLevelSetting(Player player) {
        if(!loadLevelData(player)) {
            playerLevelHashMap.put(player.getUniqueId(), 0);
            playerExpHashMap.put(player.getUniqueId(), 0);
            saveLevelData(player);
            loadLevelData(player);
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + player.getName() + " 레벨 데이터 세팅 완료" + " (" + levelDM.fileName + ")");
        }
    }
    public static void setPlayerLevel(Player player, int level) {
        playerLevelHashMap.put(player.getUniqueId(), level);
        Reference.sendDataToClient(player);
    }
    public static void setPlayerExp(Player player, int exp) {
        playerExpHashMap.put(player.getUniqueId(), exp);
        levelUpProcess(player);
        Reference.sendDataToClient(player);
    }
    public static boolean levelUpProcess(Player player) {
        if (playerLevelHashMap.get(player.getUniqueId()) < Reference.configLevelDataList.size()) {
            HashMap<String, Integer> playerStatHashMap = new HashMap<>(StatReference.statHashMap.get(player.getUniqueId()));
            for(;
                playerLevelHashMap.get(player.getUniqueId()) < Reference.configLevelDataList.size() && playerExpHashMap.get(player.getUniqueId()) >= Reference.configLevelDataList.get(playerLevelHashMap.get(player.getUniqueId()));
                playerExpHashMap.put(player.getUniqueId(), playerExpHashMap.get(player.getUniqueId()) - Reference.configLevelDataList.get(playerLevelHashMap.get(player.getUniqueId()))),
                                playerLevelHashMap.put(player.getUniqueId(), playerLevelHashMap.get(player.getUniqueId()) + 1),
                                playerStatHashMap.put("STATPOINT", (int) (playerStatHashMap.get("STATPOINT") + Reference.configDataHashMap.get("statpoint-per-level")))
            );
            StatReference.statHashMap.put(player.getUniqueId(), playerStatHashMap);

            ItemStack item = player.getItemInHand();
            player.setMaxHealth(PlayerReference.calculationHealthPoints(player, item));

            return true;
        }
        else {
            playerExpHashMap.put(player.getUniqueId(), 0);
            return false;
        }
    }
}
