package com.popogonry.lupinus.level;

import com.popogonry.lupinus.Reference;
import com.popogonry.lupinus.team.TeamReference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Reference.prefix_error + Reference.COMMAND_CMD_NOT_PRACTICE_MESSAGE);
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("save") || args[0].equalsIgnoreCase("저장")) {
                if (LevelReference.saveLevelData(player) == true) {
                    player.sendMessage(Reference.prefix_dataSave + "레벨 데이터 세이브 완료" + " (" + LevelReference.levelDM.fileName + ")");
                } else {

                }
            } else if (args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("불러오기")) {
                if (LevelReference.loadLevelData(player) == true) {
                    player.sendMessage(Reference.prefix_dataLoad + "레벨 데이터 로드 완료" + " (" + LevelReference.levelDM.fileName + ")");
                } else {
                    player.sendMessage(Reference.prefix_dataError + "레벨 데이터 로드 실패" + " (" + LevelReference.levelDM.fileName + ")");
                }
            } else if (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("초기화")) {
                if (LevelReference.onlinePlayerSaveLevelData() == true) {
                    player.sendMessage(Reference.prefix_dataSave + "레벨 데이터 세이브 완료" + " (" + LevelReference.levelDM.fileName + ")");
                    LevelReference.playerLevelHashMap.clear();
                    LevelReference.playerExpHashMap.clear();
                    player.sendMessage(Reference.prefix_level + "안전을 위해 데이터 세이브 후 초기화 되었습니다.");
                    player.sendMessage(Reference.prefix_level + "복구를 원하면 데이터를 로드하세요.");
                } else {

                }
            } else if (args[0].equalsIgnoreCase("showall") || args[0].equalsIgnoreCase("전체목록")) {
                player.sendMessage(String.valueOf(LevelReference.playerLevelHashMap));
                player.sendMessage(String.valueOf(LevelReference.playerExpHashMap));
                player.sendMessage(Reference.configLevelDataList.toString());
            } else if (args[0].equalsIgnoreCase("test")) {
                player.sendMessage(String.valueOf(Reference.configLevelDataList.size()));
            }
        }
        else if(args.length == 3) {
            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("설정")) {
                if(Reference.isInteger(args[2])) {
                    if(args[1].equalsIgnoreCase("level")) {
                        LevelReference.setPlayerLevel(player, Integer.parseInt(args[2]));
                        player.sendMessage(Reference.prefix_level + player.getName() + "의 레벨이 " + args[2] + "로 설정되었습니다.");
                    }
                    else if(args[1].equalsIgnoreCase("exp")) {
                        LevelReference.setPlayerExp(player, Integer.parseInt(args[2]));
                        player.sendMessage(Reference.prefix_level + player.getName() + "의 경험치가 " + args[2] + "로 설정되었습니다.");
                    }
                    else {
                        player.sendMessage(Reference.prefix_error + "잘못 입력");
                    }
                }
                else {
                    player.sendMessage(Reference.prefix_error + "정수형이 아님");
                }
            }
        }
        return false;
    }
}
