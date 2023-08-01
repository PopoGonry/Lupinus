package com.popogonry.lupinus.stat;

import com.popogonry.lupinus.DataManager;
import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class StatReference {

    public static HashMap<UUID, HashMap> statHashMap = new HashMap<>();


    public static DataManager statDM = new DataManager("stat", "/data");
    public static boolean saveStatData(Player player) {
        statDM.getConfig().set("data." + player.getUniqueId() + ".STR", statHashMap.get(player.getUniqueId()).get("STR"));
        statDM.getConfig().set("data." + player.getUniqueId() + ".DEF", statHashMap.get(player.getUniqueId()).get("DEF"));
        statDM.getConfig().set("data." + player.getUniqueId() + ".DEX", statHashMap.get(player.getUniqueId()).get("DEX"));
        statDM.getConfig().set("data." + player.getUniqueId() + ".HP", statHashMap.get(player.getUniqueId()).get("HP"));
        statDM.getConfig().set("data." + player.getUniqueId() + ".STATPOINT", statHashMap.get(player.getUniqueId()).get("STATPOINT"));
        statDM.saveConfig();

        statHashMap.remove(player.getUniqueId());

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + player.getName() + " 스텟 데이터 세이브 완료" + " (" + statDM.fileName + ")");
        return true;
    }

    public static void onlinePlayerSaveStatData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            saveStatData(player);
        }
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + "온라인 플레이어 스텟 데이터 세이브 완료" + " (" + statDM.fileName + ")");
    }


    public static boolean loadStatData(Player player) {
        if(statDM.getConfig().get("data." + player.getUniqueId()) == null) {
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataError + player.getName() + " 스텟 데이터 로드 실패" + " (" + statDM.fileName + ")");
            return false;
        }
        else {
            HashMap<String, Integer> playerStatHashMap = new HashMap<String, Integer>(5){{
                put("STR", (int) statDM.getConfig().get("data." + player.getUniqueId() + ".STR"));
                put("DEF", (int) statDM.getConfig().get("data." + player.getUniqueId() + ".DEF"));
                put("DEX", (int) statDM.getConfig().get("data." + player.getUniqueId() + ".DEX"));
                put("HP", (int) statDM.getConfig().get("data." + player.getUniqueId() + ".HP"));
                put("STATPOINT", (Integer) statDM.getConfig().get("data." + player.getUniqueId() + ".STATPOINT"));
            }};
            statHashMap.remove(player.getUniqueId());
            statHashMap.put(player.getUniqueId(), playerStatHashMap);
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataLoad + player.getName() + " 스텟 데이터 로드 완료" + " (" + statDM.fileName + ")");
            return true;
        }
    }

    public static void onlinePlayerLoadStatData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadStatData(player);
        }
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataLoad + "온라인 플레이어 스텟 데이터 로드 완료" + " (" + statDM.fileName + ")");
    }

    public static void playerJoinStatSetting(Player player) {
        if(!loadStatData(player)) {
            HashMap<String, Integer> playerStatHashMap = new HashMap<String, Integer>(5){{
                put("STR", 0);
                put("DEF", 0);
                put("DEX", 0);
                put("HP", 0);
                put("STATPOINT", 0);
            }};
            statHashMap.put(player.getUniqueId(), playerStatHashMap);
            saveStatData(player);
            loadStatData(player);
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + player.getName() + " 스텟 데이터 세팅 완료" + " (" + statDM.fileName + ")");
        }
    }
    public static boolean investStat(Player player, String stat) {
        HashMap playerStatHashMap = statHashMap.get(player.getUniqueId());
        if((int) playerStatHashMap.get("STATPOINT") >= 1) {
            playerStatHashMap.put("STATPOINT", (int) playerStatHashMap.get("STATPOINT") - 1);
            if(stat.equalsIgnoreCase("HP")) {
                playerStatHashMap.put("HP", (int) playerStatHashMap.get("HP") + 1);
            }
            else if(stat.equalsIgnoreCase("STR")) {
                playerStatHashMap.put("STR", (int) playerStatHashMap.get("STR") + 1);
            }
            else if(stat.equalsIgnoreCase("DEF")) {
                playerStatHashMap.put("DEF", (int) playerStatHashMap.get("DEF") + 1);
            }
            else if(stat.equalsIgnoreCase("DEX")) {
                playerStatHashMap.put("DEX", (int) playerStatHashMap.get("DEX") + 1);
            }
            return true;
        }
        else {
            return false;
        }
    }

    public static int getTotalStat(Player player) {
        int totalStat = 0;
        HashMap<String, Integer> playerStatHashMap = statHashMap.get(player.getUniqueId());
        for(int stat : playerStatHashMap.values()) {
            totalStat += stat;
        }
        return totalStat;
    }

}
