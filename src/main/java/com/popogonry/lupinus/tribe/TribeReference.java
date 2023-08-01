package com.popogonry.lupinus.tribe;

import com.popogonry.lupinus.DataManager;
import com.popogonry.lupinus.Reference;
import com.popogonry.lupinus.region.Region;
import com.popogonry.lupinus.region.RegionReference;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TribeReference {
    public static HashMap<UUID, String> playerTribeHashMap = new HashMap<>();
    public static HashMap<String, String> tribeRegionHashMap = new HashMap<String, String>();
    public static DataManager playerTribeDM = new DataManager("playerTribe", "/data");
    public static DataManager tribeRegionDM = new DataManager("tribeRegion", "/data");

    public static boolean savePlayerTribeData(Player player) {
        playerTribeDM.getConfig().set("data." + player.getUniqueId() + ".tribe", playerTribeHashMap.get(player.getUniqueId()));

        playerTribeDM.saveConfig();

        playerTribeHashMap.remove(player.getUniqueId());

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + player.getName() + " 진영 데이터 세이브 완료" + " (" + playerTribeDM.fileName + ")");
        return true;
    }

    public static boolean onlinePlayerSaveTribeData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayerTribeData(player);
        }
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + "온라인 플레이어 진영 데이터 세이브 완료" + " (" + playerTribeDM.fileName + ")");
        return true;
    }

//    public static boolean saveAllTribeData() {
//
//    }
    public static boolean saveTribeRegionData() {
        List<String> list = new ArrayList<>();

        for(String regionName : tribeRegionHashMap.keySet()) {
            list.add(regionName);
            tribeRegionDM.getConfig().set("data." + regionName + ".tribe", tribeRegionHashMap.get(regionName));
        }
        tribeRegionDM.getConfig().set("dataList.tribeRegions", list);

        tribeRegionDM.saveConfig();

        tribeRegionHashMap.clear();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + "진영지역 데이터 세이브 완료" + " (" + tribeRegionDM.fileName + ")");
        return true;
    }
    public static boolean loadTribeRegionData() {
        if(tribeRegionDM.getConfig().get("dataList.tribeRegions") == null || tribeRegionDM.getConfig().get("data") == null) {
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataError + "진영지역 데이터 로드 실패" + " (" + tribeRegionDM.fileName + ")");
            return false;
        }
        else {
            tribeRegionHashMap.clear();
            List<String> regionList = tribeRegionDM.getConfig().getStringList("dataList.tribeRegions");
            for(String regionName : regionList) {
                tribeRegionHashMap.put(regionName, (String) tribeRegionDM.getConfig().get("data." + regionName + ".tribe"));
            }
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataLoad + "진영지역 데이터 로드 완료" + " (" + tribeRegionDM.fileName + ")");
            return true;
        }
    }


    public static boolean loadPlayerTribeData(Player player) {
        if(playerTribeDM.getConfig().get("data." + player.getUniqueId() + ".tribe") == null) {
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataError + player.getName() + " 진영 데이터 로드 실패" + " (" + playerTribeDM.fileName + ")");
            playerTribeHashMap.put(player.getUniqueId(), "None");
            return false;
        }
        else {
            playerTribeHashMap.remove(player.getUniqueId());

            playerTribeHashMap.put(player.getUniqueId(), (String) playerTribeDM.getConfig().get("data." + player.getUniqueId() + ".tribe"));

            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataLoad + player.getName() + " 진영 데이터 로드 완료" + " (" + playerTribeDM.fileName + ")");
            return true;
        }
    }
    public static boolean onlinePlayerLoadTribeData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayerTribeData(player);
        }
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + "온라인 플레이어 진영 데이터 로드 완료" + " (" + playerTribeDM.fileName + ")");
        return true;
    }

    public static boolean setPlayerTribe(Player player, String tribe) {
        if(getValueTribe(changeTribeKoToEn(tribe)) == -1) {
            return false;
        }
        else {
            playerTribeHashMap.put(player.getUniqueId(), changeTribeKoToEn(tribe));
            return true;
        }
    }

    public static boolean addTribeRegion(String regionName, String tribe) {
        if(tribeRegionHashMap.containsKey(regionName)) {
            // 팀이 있으면,
            return false;
        }
        else {
            tribeRegionHashMap.put(regionName, tribe);
            return true;
        }
    }

    public static boolean tribeRegionAddProcess(Player player, String regionName, String tribe) {

        if(RegionReference.regionHashMap.containsKey(regionName)) {
            if(!tribeRegionHashMap.containsKey(regionName)) {
                if(getValueTribe(changeTribeKoToEn(tribe)) != -1) {
                    addTribeRegion(regionName, tribe);
                    player.sendMessage(Reference.prefix_tribe + regionName + " 지역이 " + changeTribeEnToKo(tribe) + "의 진영으로 등록되었습니다.");
                    return true;
                }
                else {
                    player.sendMessage(Reference.prefix_error + tribe + " 진영은 존재하지 않습니다.");
                    return false;

                }
            }
            else {
                player.sendMessage(Reference.prefix_error + regionName + " 지역은 이미 진영지역 목록에 존재합니다.");
                return false;
            }

        }
        else {
            player.sendMessage(Reference.prefix_error + regionName + " 지역은 존재하지 않습니다.");
            return false;
        }
    }

    public static boolean removeTribeRegion(String regionName) {
        if(!tribeRegionHashMap.containsKey(regionName)) {
            // 팀이 없으면,
            return false;
        }
        else {
            tribeRegionHashMap.remove(regionName);
            return true;
        }
    }

    public static boolean tribeRegionRemoveProcess(Player player, String regionName) {
        if(tribeRegionHashMap.containsKey(regionName)) {
            player.sendMessage(Reference.prefix_tribe + regionName + " 지역이 " + changeTribeEnToKo(tribeRegionHashMap.get(regionName)) + "의 진영으로 등록해제 되었습니다.");
            removeTribeRegion(regionName);
            return true;
        }
        else {
            player.sendMessage(Reference.prefix_error + regionName + " 지역은 진영지역 목록에 존재하지 않습니다.");
            return false;
        }
    }

    public static void showTribeAll(Player player) {
        player.sendMessage("");
        player.sendMessage(" " + Reference.prefix_normal);
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String message = String.format("플레이어 이름 : %s, 진영 : %s",
                    onlinePlayer.getName(),
                    changeTribeEnToKo(playerTribeHashMap.get(onlinePlayer.getUniqueId()))

            );
            player.sendMessage(message);
        }
        player.sendMessage("");
        for(String regionName : tribeRegionHashMap.keySet()) {
            Region region = RegionReference.regionHashMap.get(regionName);
            String message = String.format("지역 이름 : %s, 월드 : %s, 좌표 : %s %s, 진영 : %s",
                    region.name,
                    region.world.toString(),
                    region.position1.toString(),
                    region.position2.toString(),
                    changeTribeEnToKo(tribeRegionHashMap.get(regionName))
            );
            player.sendMessage(message);
        }
        player.sendMessage("");
    }

    public static int getValueTribe(String tribe) {
        switch (changeTribeKoToEn(tribe)) {
            case "None":
                return 0;
            case "Douglas":
                return 1;
            case "Florence":
                return 2;
            case "Justitia":
                return 3;
            case "Hiro":
                return 4;
            default:
                return -1;
        }
    }

    public static String changeTribeKoToEn(String tribe) {
        switch (tribe) {
            case "더글라스":
                return "Douglas";
            case "플로렌스":
                return "Florence";
            case "유스티치아":
                return "Justitia";
            case "타라토스":
                return "Hiro";
            case "무소속":
                return "None";
            default:
                return tribe;
        }
    }
    public static String changeTribeEnToKo(String tribe) {
        switch (tribe) {
            case "Douglas":
                return "더글라스";
            case "Florence":
                return "플로렌스";
            case "Justitia":
                return "유스티치아";
            case "Hiro":
                return "타라토스";
            case "None":
                return "무소속";
            default:
                return tribe;
        }
    }



}
