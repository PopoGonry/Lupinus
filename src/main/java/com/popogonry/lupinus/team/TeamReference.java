package com.popogonry.lupinus.team;

import com.popogonry.lupinus.DataManager;
import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamReference {
    public static DataManager teamDM = new DataManager("team", "/data");

    public static HashMap<String, List<UUID>> teamHashMap = new HashMap<>();
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
                List<UUID> memberList = new ArrayList<>();
                for(String uuid : list) {
                    memberList.add(UUID.fromString(uuid));
                }
                teamHashMap.put(key, memberList);
            }
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataLoad + "팀 데이터 로드 완료" + " (" + teamDM.fileName + ")");
            return true;
        }
    }

    public static boolean createTeam(String teamName) {
        if(teamHashMap.containsKey(teamName)) {
            return false;
        }
        else {
            teamHashMap.put(teamName, Collections.emptyList());
            return true;
        }
    }
    public static boolean removeTeam(String teamName) {
        if(!teamHashMap.containsKey(teamName)) {
            return false;
        }
        else {
            teamHashMap.remove(teamName);
            return true;
        }
    }
    public static boolean renameTeam(String oldTeamName, String newTeamName) {
        if(!teamHashMap.containsKey(oldTeamName) || teamHashMap.containsKey(newTeamName)) {
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
            return false;
        }
        else {
            List<UUID> memberList = new ArrayList<>(teamHashMap.get(teamName));
            memberList.add(member.getUniqueId());
            teamHashMap.put(teamName, memberList);
            return true;
        }
    }

}
