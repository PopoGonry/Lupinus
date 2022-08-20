package com.popogonry.lupinus.team;

import com.popogonry.lupinus.DataManager;
import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class TeamReference {
    public static DataManager teamDM = new DataManager("team", "/data");

    public static HashMap<String, List<String>> teamHashMap = new HashMap<>();
    public static boolean saveTeamData() {
        for(String key : teamHashMap.keySet()) {
            teamDM.getConfig().set("data." + key, teamHashMap.get(key));
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
                teamHashMap.put(key, list);
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
    public static boolean addMember(String teamName, Player member) {
        if(!teamHashMap.containsKey(teamName)) {
            // 팀이 없으면,
            return false;
        }
        List<String> memberList = new ArrayList<>(teamHashMap.get(teamName));
        if(memberList.contains(member.getUniqueId().toString())) {
            // 이미 member가 팀에 있으면,
            return false;
        }
        else {
            memberList.add(member.getUniqueId().toString());
            teamHashMap.put(teamName, memberList);
            return true;
        }
    }
    public static boolean removeMember(String teamName, Player member) {
        if(!teamHashMap.containsKey(teamName)) {
            // 팀이 없으면,
            return false;
        }
        List<String> memberList = new ArrayList<>(teamHashMap.get(teamName));
        if(!memberList.contains(member.getUniqueId().toString())) {
            // member가 팀에 없으면,
            return false;
        }
        else {
            memberList.remove(member.getUniqueId().toString());
            teamHashMap.put(teamName, memberList);
            return true;
        }
    }
    public static boolean checkSameTeam(Player attacker, Player victim) {
        for(String team : teamHashMap.keySet()) {
            if(new HashSet<>(teamHashMap.get(team)).containsAll(Arrays.asList(attacker.getUniqueId().toString(), victim.getUniqueId().toString()))) {
               // attacker와 victim이 같은 팀에 있으면,
               return true;
            }
        }
        return false;
    }

    public static String getPlayersTeam(Player player) {
        for(String team : teamHashMap.keySet()) {
            if(teamHashMap.get(team).contains(player.getUniqueId().toString())) {
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
            List<String> memberList = new ArrayList<>(teamHashMap.get(team));
            string.append("  - " + team + " : ");
            for(int i = 0; i < memberList.size(); i++) {
                string.append(Bukkit.getServer().getOfflinePlayer(UUID.fromString(memberList.get(i))).getName());
                if(i + 1 < memberList.size()) string.append(", ");
            }
            player.sendMessage(String.valueOf(string));
        }
        player.sendMessage("");
    }

    // 팀생성 명령어, 팀초대, 팀강퇴, 팀제거, 팀탈퇴, 파티참가신청

    // 파티장 : 팀초대, 팀강퇴, 팀제거
    // 파티원 : 팀탈퇴

    public static ItemStack getHead(Player player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(player.getName());
        skull.setOwner(player.getName());
        item.setItemMeta(skull);
        return item;
    }


}
