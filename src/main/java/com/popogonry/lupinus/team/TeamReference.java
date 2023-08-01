package com.popogonry.lupinus.team;

import com.popogonry.lupinus.DataManager;
import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TeamReference {
    public static DataManager teamDM = new DataManager("team", "/data");

    public static HashMap<String, List<UUID>> teamHashMap = new HashMap<>();
    public static boolean saveTeamData() {
        for(String key : teamHashMap.keySet()) {
            List<String> list = new ArrayList<>();
            for(UUID member : teamHashMap.get(key)) {
                list.add(member.toString());
            }
            teamDM.getConfig().set("data." + key, list);
        }
        List<String> teamList = new ArrayList<>(teamHashMap.keySet());
        teamDM.getConfig().set("teamList", teamList);
        teamDM.saveConfig();
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + "팀 데이터 세이브 완료" + " (" + teamDM.fileName + ")");
        return true;
    }
    public static boolean loadTeamData() {
        if(teamDM.getConfig().get("teamList") == null && teamDM.getConfig().get("data") == null) {
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataError + "팀 데이터 로드 실패" + " (" + teamDM.fileName + ")");
            return false;
        }
        else {
            teamHashMap.clear();
            List<String> teamList = new ArrayList<>(teamDM.getConfig().getStringList("teamList"));
            for(String key : teamList) {
                List<String> list = new ArrayList<>(teamDM.getConfig().getStringList("data." + key));
                List<UUID> uuidList = new ArrayList<>();
                for(String uuid : list) {
                    uuidList.add(UUID.fromString(uuid));
                }
                teamHashMap.put(key, uuidList);
            }
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataLoad + "팀 데이터 로드 완료" + " (" + teamDM.fileName + ")");
            return true;
        }
    }

    public static boolean createTeam(String teamName) {
        if(teamHashMap.containsKey(teamName)) {
            // 팀이 있으면,
            return false;
        }
        else {
            teamHashMap.put(teamName, Collections.emptyList());
            return true;
        }
    }

    public static boolean teamCreationProcess(OfflinePlayer player) {
        if(getPlayersTeam(player) == null) {
            if(createTeam(player.getName())) {
                int returnValue = addMember(player.getName(), player);
                if(returnValue == 0) {
                    player.getPlayer().sendMessage(Reference.prefix_team + "팀이 생성되었습니다.");
                    return true;
                }
                else {
                    if(returnValue == 1) {
                        player.getPlayer().sendMessage(Reference.prefix_error + player.getName() + "님은 이미 팀이 존재합니다.");
                    }
                    else if(returnValue == 2) {
                        player.getPlayer().sendMessage(Reference.prefix_error + "팀이 존재하지 않습니다.");
                    }
                    else if(returnValue == 3) {
                        player.getPlayer().sendMessage(Reference.prefix_error + player.getName() + "님이 이미 소속되어 있습니다.");
                    }
                    removeTeam(player.getName());
                    return false;
                }
            }
            else {
                player.getPlayer().sendMessage(Reference.prefix_error + "팀이 이미 존재합니다.");
                return false;
            }
        }
        else {
            player.getPlayer().sendMessage(Reference.prefix_error + "팀에 이미 소속되어 있습니다.");
            return false;
        }
    }
    public static boolean removeTeam(String teamName) {
        if(!teamHashMap.containsKey(teamName)) {
            // 팀이 없으면,
            return false;
        }
        else {
            teamHashMap.remove(teamName);
            return true;
        }
    }
    public static boolean renameTeam(String oldTeamName, String newTeamName) {
        if(!teamHashMap.containsKey(oldTeamName) || teamHashMap.containsKey(newTeamName)) {
            //oldTeamName을 가진 팀이 존재하고, newTeamName을 가진 팀이 존재하지않으면,
            return false;
        }
        else {
            teamHashMap.put(newTeamName, teamHashMap.get(oldTeamName));
            teamHashMap.remove(oldTeamName);
            return true;
        }
    }
    public static int addMember(String teamName, OfflinePlayer member) {
        if(getPlayersTeam(member.getPlayer()) != null) {
            //player + 님은 이미 팀이 존재합니다.
            return 1;
        }
        else {
            if(!teamHashMap.containsKey(teamName)) {
                //team + 팀이 존재하지 않습니다.
                return 2;
            }
            List<UUID> memberList = new ArrayList<>(teamHashMap.get(teamName));
            if(memberList.contains(member.getUniqueId())) {
                // 팀에 + player + 님이 이미 소속되어 있습니다.
                return 3;
            }
            else {
                // 팀에 + player + 님이 추가되었습니다.
                memberList.add(member.getUniqueId());
                teamHashMap.put(teamName, memberList);
                return 0;
            }
        }
    }
    public static int removeMember(String teamName, OfflinePlayer member) {
        if(getPlayersTeam(member.getPlayer()) == null) {
            //팀에 소속되어 있지 않을때,
            //player + 님은 팀이 존재하지 않습니다.
            return 1;
        }
        else {
            if(!teamHashMap.containsKey(teamName)) {
                //team + 팀이 존재하지 않습니다.
                return 2;
            }
            List<UUID> memberList = new ArrayList<>(teamHashMap.get(teamName));
            if(!memberList.contains(member.getUniqueId())) {
                // 팀에 + player + 님이 소속되어 있지 않습니다.
                return 3;
            }
            else {
                // 팀에 + player + 님이 제거되었습니다.
                memberList.remove(member.getUniqueId());
                teamHashMap.put(teamName, memberList);
                return 0;
            }
        }
    }
    public static boolean checkSameTeam(Player attacker, Player victim) {
        for(String team : teamHashMap.keySet()) {
            if(new HashSet<>(teamHashMap.get(team)).containsAll(Arrays.asList(attacker.getUniqueId(), victim.getUniqueId()))) {
               // attacker와 victim이 같은 팀에 있으면,
               return true;
            }
        }
        return false;
    }

    public static String getPlayersTeam(OfflinePlayer player) {
        for(String team : teamHashMap.keySet()) {
            if(teamHashMap.get(team).contains(player.getUniqueId())) {
                return team;
            }
        }
        return null;
    }

    public static void showTeamList(Player player) {
        player.sendMessage("");
        player.sendMessage(Reference.prefix_normal + "팀 목록 : " + teamHashMap.keySet());
        player.sendMessage("");
    }


    public static void showTeamAll(Player player) {
        player.sendMessage("");
        player.sendMessage(" " + Reference.prefix_normal);
        for(String team : teamHashMap.keySet()) {
            StringBuilder string = new StringBuilder();
            List<UUID> memberList = new ArrayList<>(teamHashMap.get(team));
            string.append("  - " + "§f" + team + " : ");
            for(int i = 0; i < memberList.size(); i++) {
                string.append(Bukkit.getServer().getOfflinePlayer(memberList.get(i)).getName());
                if(i + 1 < memberList.size()) string.append(", ");
            }
            player.sendMessage(String.valueOf(string));
        }
        player.sendMessage("");
    }
    public static int setTeamMaster(OfflinePlayer player) {
        String team = getPlayersTeam(player);
        if(!teamHashMap.get(team).contains(player.getUniqueId())) {
            // member가 팀에 없으면,
            return 1;
        }
        else if(teamHashMap.get(team).get(0).equals(player.getUniqueId())) {
            // member가 이미 팀장이면,
            return 2;
        }
        else {
            renameTeam(Bukkit.getOfflinePlayer(teamHashMap.get(team).get(0)).getName(), player.getName());
            teamHashMap.get(team).remove(player.getUniqueId());
            teamHashMap.get(team).add(0, player.getUniqueId());
            return 0;
        }
    }
    public static boolean broadcastTeam(String teamName, String message) {
        if(!teamHashMap.containsKey(teamName)) {
            //teamName을 가진 팀이 존재하지않으면,
            return false;
        }
        else {
            for(UUID memberUUID : teamHashMap.get(teamName)) {
                OfflinePlayer member = Bukkit.getOfflinePlayer(memberUUID);
                if(member.isOnline()) {
                    Player onlineMember = member.getPlayer();
                    onlineMember.sendMessage(message);
                }
            }
            return true;
        }
    }

    public static boolean reloadTeamGUI(String teamName) {
        if(!teamHashMap.containsKey(teamName)) {
            //teamName을 가진 팀이 존재하지않으면,
            return false;
        }
        else {
            for(UUID memberUUID : teamHashMap.get(teamName)) {
                OfflinePlayer member = Bukkit.getOfflinePlayer(memberUUID);
                if(member.isOnline()) {
                    Player onlineMember = member.getPlayer();
                    InventoryView inventory = onlineMember.getOpenInventory();
                    if(inventory.getTitle().contains("팀")) {
                        String[] pageString = inventory.getItem(49).getItemMeta().getDisplayName().split("/");
                        pageString[0] = pageString[0].replace("§6§l", "");
                        int page = Integer.parseInt(pageString[0]) - 1;
                        TeamGUI.openTeamGUI(onlineMember, page);
                        onlineMember.sendMessage(Reference.prefix_team + "팀정보가 변경되었습니다.");
                    }
                }
            }
            return true;
        }
    }





    // 팀생성 명령어, 팀초대, 팀강퇴, 팀제거, 팀탈퇴, 팀참가신청

    // 팀장 : 팀초대, 팀강퇴, 팀제거
    // 팀원 : 팀탈퇴


}
