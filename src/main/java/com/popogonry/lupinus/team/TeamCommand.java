package com.popogonry.lupinus.team;

import com.popogonry.lupinus.GUI;
import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Reference.prefix_error + Reference.COMMAND_CMD_NOT_PRACTICE_MESSAGE);
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 0) {
            if(TeamGUI.openTeamGUI(player, 0) == true) {
//                player.sendMessage(Reference.prefix_team + "팀 GUI를 열었습니다.");
            }
            else {
                player.sendMessage(Reference.prefix_error + "팀에 소속되어 있지 않습니다.");
            }
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("save") || args[0].equalsIgnoreCase("저장")) {
                if(player.isOp()) {
                    if(TeamReference.saveTeamData() == true) {
                        player.sendMessage(Reference.prefix_dataSave + "팀 데이터 세이브 완료" + " (" + TeamReference.teamDM.fileName + ")");
                    }
                    else {

                    }
                }
                else {
                    player.sendMessage(Reference.prefix_error + "권한이 부족합니다.");
                }
            }
            else if(args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("불러오기")) {
                if(player.isOp()) {
                    if(TeamReference.loadTeamData() == true) {
                        player.sendMessage(Reference.prefix_dataLoad + "팀 데이터 로드 완료" + " (" + TeamReference.teamDM.fileName + ")");
                    }
                    else {
                        player.sendMessage(Reference.prefix_dataError + "팀 데이터 로드 실패" + " (" + TeamReference.teamDM.fileName + ")");
                    }
                }
                else {
                    player.sendMessage(Reference.prefix_error + "권한이 부족합니다.");
                }
            }
            else if(args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("초기화")) {
                if(player.isOp()) {
                    if(TeamReference.saveTeamData() == true) {
                        player.sendMessage(Reference.prefix_dataSave + "팀 데이터 세이브 완료" + " (" + TeamReference.teamDM.fileName + ")");
                        TeamReference.teamHashMap.clear();
                        player.sendMessage(Reference.prefix_team + "안전을 위해 데이터 세이브 후 초기화 되었습니다.");
                        player.sendMessage(Reference.prefix_team + "복구를 원하면 데이터를 로드하세요.");
                    }
                    else {

                    }
                }
                else {
                    player.sendMessage(Reference.prefix_error + "권한이 부족합니다.");
                }
            }

            else if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("목록")) {
                if(player.isOp()) {
                    TeamReference.showTeamList(player);
                }
                else {
                    player.sendMessage(Reference.prefix_error + "권한이 부족합니다.");
                }
            }
            else if(args[0].equalsIgnoreCase("showall") || args[0].equalsIgnoreCase("전체목록")) {
                if(player.isOp()) {
                    TeamReference.showTeamAll(player);
                }
                else {
                    player.sendMessage(Reference.prefix_error + "권한이 부족합니다.");
                }
            }
            else if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("생성")) {
                TeamReference.teamCreationProcess(player);
            }
            else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("제거")) {
                if(TeamReference.removeTeam(player.getDisplayName())) {
                    player.sendMessage(Reference.prefix_team + "팀이 제거되었습니다.");
                }
                else {
                    player.sendMessage(Reference.prefix_error + "팀이 존재하지 않습니다.");
                }
            }
        }
        else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("생성")) {
                if(player.isOp()) {
                    if(TeamReference.createTeam(args[1])) {
                        player.sendMessage(Reference.prefix_team + args[1] + " 팀이 생성되었습니다.");
                    }
                    else {
                        player.sendMessage(Reference.prefix_error + args[1] + " 팀이 이미 존재합니다.");
                    }
                }
                else {
                    player.sendMessage(Reference.prefix_error + "권한이 부족합니다.");
                }

            }
            else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("제거")) {
                if(player.isOp()) {
                    if(TeamReference.removeTeam(args[1])) {
                        player.sendMessage(Reference.prefix_team + args[1] + " 팀이 제거되었습니다.");
                    }
                    else {
                        player.sendMessage(Reference.prefix_error + args[1] + " 팀이 존재하지 않습니다.");
                    }
                }
                else {
                    player.sendMessage(Reference.prefix_error + "권한이 부족합니다.");
                }
            }

            else if(args[0].equalsIgnoreCase("setmaster") || args[0].equalsIgnoreCase("팀장위임")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                String team = TeamReference.getPlayersTeam(target);
                if(TeamReference.teamHashMap.get(team).get(0).equals(player.getUniqueId()) || player.isOp()) {
                    int returnValue = TeamReference.setTeamMaster(target);
                    if(returnValue == 0) {
                        TeamReference.broadcastTeam(team, Reference.prefix_team + player.getName() + "님이 " + target.getName() + "님에게 팀장 위임을 하였습니다.");
                    }
                    else if(returnValue == 1) {
                        player.sendMessage(Reference.prefix_error + target.getName() + "님이 팀에 없습니다.");
                    }
                    else {
                        player.sendMessage(Reference.prefix_error + target.getName() + "님은 이미 팀장입니다.");
                    }
                }
                else {
                    player.sendMessage(Reference.prefix_error + "권한이 없습니다.");
                }
            }
        }
        else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("rename")) {
                if(player.isOp()) {
                    player.sendMessage(String.valueOf(TeamReference.renameTeam(args[1], args[2])));
                }
                else {
                    player.sendMessage(Reference.prefix_error + "권한이 부족합니다.");
                }
            }
            if(Bukkit.getPlayer(args[2]) instanceof Player) {
                if(args[0].equalsIgnoreCase("add")) {
                    if(player.isOp()) {
                        player.sendMessage(String.valueOf(TeamReference.addMember(args[1], Bukkit.getOfflinePlayer(args[2]))));
                    }
                    else {
                        player.sendMessage(Reference.prefix_error + "권한이 부족합니다.");
                    }
                }
                else if(args[0].equalsIgnoreCase("remove")) {
                    if(player.isOp()) {
                        player.sendMessage(String.valueOf(TeamReference.removeMember(args[1], (Player) Bukkit.getOfflinePlayer(args[2]))));
                    }
                    else {
                        player.sendMessage(Reference.prefix_error + "권한이 부족합니다.");
                    }
                }
            }
        }

        return false;
    }
}
