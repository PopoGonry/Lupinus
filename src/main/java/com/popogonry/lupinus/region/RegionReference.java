package com.popogonry.lupinus.region;

import com.popogonry.lupinus.DataManager;
import com.popogonry.lupinus.Reference;
import com.popogonry.lupinus.level.LevelReference;
import com.popogonry.lupinus.player.PlayerReference;
import com.popogonry.lupinus.stat.StatReference;
import com.popogonry.lupinus.tribe.TribeReference;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class RegionReference {
    public static HashMap<String, Region> regionHashMap = new HashMap<String, Region>();

    public static DataManager regionDM = new DataManager("region", "/data");

    public static HashMap<UUID, List<Integer>> playerPosition1HashMap = new HashMap<>();
    public static HashMap<UUID, List<Integer>> playerPosition2HashMap = new HashMap<>();


    public static boolean saveRegionData() {
        List<String> list = new ArrayList<>();

        for(String region : regionHashMap.keySet()) {
            list.add(region);
            regionDM.getConfig().set("data." + region + ".world", regionHashMap.get(region).world.getName());
            regionDM.getConfig().set("data." + region + ".position1", regionHashMap.get(region).position1);
            regionDM.getConfig().set("data." + region + ".position2", regionHashMap.get(region).position2);
        }
        regionDM.getConfig().set("dataList.regions", list);

        regionDM.saveConfig();

        regionHashMap.clear();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + "지역 데이터 세이브 완료" + " (" + regionDM.fileName + ")");
        return true;
    }

    public static boolean loadRegionData() {
        if(regionDM.getConfig().get("dataList.regions") == null || regionDM.getConfig().get("data") == null) {
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataError + "지역 데이터 로드 실패" + " (" + regionDM.fileName + ")");
            return false;
        }
        else {
            regionHashMap.clear();
            List<String> regionList = regionDM.getConfig().getStringList("dataList.regions");
            for(String region : regionList) {
                for(World world : Bukkit.getWorlds()) {
                    if(world.getName().equals(regionDM.getConfig().get("data." + region + ".world"))) {
                        List<Integer> position1 = new ArrayList<>();
                        for(String string : regionDM.getConfig().getStringList("data." + region + ".position1")) {
                            position1.add(Integer.valueOf(string));
                        }
                        List<Integer> position2 = new ArrayList<>();
                        for(String string : regionDM.getConfig().getStringList("data." + region + ".position2")) {
                            position2.add(Integer.valueOf(string));
                        }
                        regionHashMap.put(region, new Region(
                                region,
                                world,
                                position1,
                                position2
                        ));
                    }
                }

            }
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataLoad + "지역 데이터 로드 완료" + " (" + regionDM.fileName + ")");
            return true;
        }
    }

    public static boolean isPlayerInRegion(Player player, Region region) {
        int minX = Math.min(region.position1.get(0), region.position2.get(0));
        int minY = Math.min(region.position1.get(1), region.position2.get(1));
        int minZ = Math.min(region.position1.get(2), region.position2.get(2));

        int maxX = Math.max(region.position1.get(0), region.position2.get(0)) + 1;
        int maxY = Math.max(region.position1.get(1), region.position2.get(1)) + 1;
        int maxZ = Math.max(region.position1.get(2), region.position2.get(2)) + 1;

        double playerX = player.getLocation().getX();
        double playerY = player.getLocation().getY();
        double playerZ = player.getLocation().getZ();

//        player.sendMessage(minX + " " + playerX + " " + maxX);
//        player.sendMessage(minY + " " + playerY + " " + maxY);
//        player.sendMessage(minZ + " " + playerZ + " " + maxZ);


        if((minX <= playerX && playerX <= maxX) && (minY <= playerY && playerY <= maxY) && (minZ <= playerZ && playerZ <= maxZ)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean regionAddProcess(Player player, String regionName) {
        if(playerPosition1HashMap.containsKey(player.getUniqueId()) && playerPosition2HashMap.containsKey(player.getUniqueId())) {
            if(addRegion(new Region(regionName, player.getWorld(), RegionReference.playerPosition1HashMap.get(player.getUniqueId()), RegionReference.playerPosition2HashMap.get(player.getUniqueId())))) {
                player.sendMessage(Reference.prefix_region + player.getWorld().getName() + RegionReference.playerPosition1HashMap.get(player.getUniqueId()) + " " + RegionReference.playerPosition2HashMap.get(player.getUniqueId()) + "가 " + regionName + " 지역으로 설정되었습니다.");
                return true;
            }
            else {
                player.sendMessage(Reference.prefix_error + "같은 이름의 지역이 존재합니다.");
                return false;
            }
        }
        else {
            player.sendMessage(Reference.prefix_error + "좌표가 설정되지 않았습니다.");
            return false;
        }
    }
    public static boolean addRegion(Region region) {
        if(regionHashMap.containsKey(region.name)) {
            // 팀이 있으면,
            return false;
        }
        else {
            regionHashMap.put(region.name, region);
            return true;
        }
    }
    public static boolean regionRemoveProcess(Player player, String regionName) {
        if(regionHashMap.containsKey(regionName)) {
            if(removeRegion(regionHashMap.get(regionName))) {
                if(TribeReference.tribeRegionHashMap.containsKey(regionName)) {
                    TribeReference.removeTribeRegion(regionName);
                }
                player.sendMessage(Reference.prefix_team + regionName + " 지역이 제거되었습니다.");
                return true;
            }
            else {
                player.sendMessage(Reference.prefix_error + regionName + "지역은 존재하지 않습니다.");
                return false;
            }
        }
        else {
            player.sendMessage(Reference.prefix_error + regionName + "지역은 존재하지 않습니다.");
            return false;
        }
    }

    public static boolean removeRegion(Region region) {
        if(!regionHashMap.containsKey(region.name)) {
            // 팀이 없으면,
            return false;
        }
        else {
            regionHashMap.remove(region.name);
            return true;
        }
    }
    public static boolean renameRegion(String oldRegionName, String newRegionName) {
        if(!regionHashMap.containsKey(oldRegionName) || regionHashMap.containsKey(newRegionName)) {
            //oldRegionName을 가진 팀이 존재하고, newRegionName을 가진 팀이 존재하지않으면,
            return false;
        }
        else {
            Region region = regionHashMap.get(oldRegionName);
            region.name = newRegionName;

            regionHashMap.put(newRegionName, region);
            regionHashMap.remove(oldRegionName);
            return true;
        }
    }

    public static void showRegionList(Player player) {
        player.sendMessage("");
        player.sendMessage(Reference.prefix_normal + "지역 목록 : " + regionHashMap.keySet());
        player.sendMessage("");
    }


    public static void showRegionAll(Player player) {
        player.sendMessage("");
        player.sendMessage(" " + Reference.prefix_normal);
        for(Region region : regionHashMap.values()) {
            String message = String.format("지역 이름 : %s, 월드 : %s, 좌표 : %s %s",
                    region.name,
                    region.world.toString(),
                    region.position1.toString(),
                    region.position2.toString()
            );
            player.sendMessage(message);
        }
        player.sendMessage("");
    }


    public static boolean teleportPlayerToRegion(Player player, String regionName) {
        if(!regionHashMap.containsKey(regionName)) {
            // 팀이 없으면,
            return false;
        }
        else {
            player.teleport(regionHashMap.get(regionName).returnLocation());
            return true;
        }
    }

}
