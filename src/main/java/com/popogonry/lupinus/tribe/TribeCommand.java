package com.popogonry.lupinus.tribe;

import com.popogonry.lupinus.Reference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TribeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Reference.prefix_error + Reference.COMMAND_CMD_NOT_PRACTICE_MESSAGE);
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("save") || args[0].equalsIgnoreCase("저장")) {
                if (TribeReference.savePlayerTribeData(player) == true) {
                    player.sendMessage(Reference.prefix_dataSave + "진영 데이터 세이브 완료" + " (" + TribeReference.playerTribeDM.fileName + ")");
                } else {

                }
            } else if (args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("불러오기")) {
                if (TribeReference.loadPlayerTribeData(player) == true) {
                    player.sendMessage(Reference.prefix_dataLoad + "진영 데이터 로드 완료" + " (" + TribeReference.playerTribeDM.fileName + ")");
                } else {
                    player.sendMessage(Reference.prefix_dataError + "진영 데이터 로드 실패" + " (" + TribeReference.playerTribeDM.fileName + ")");
                }
            } else if (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("초기화")) {
                if (TribeReference.onlinePlayerSaveTribeData() == true) {
                    player.sendMessage(Reference.prefix_dataSave + "진영 데이터 세이브 완료" + " (" + TribeReference.playerTribeDM.fileName + ")");
                    TribeReference.playerTribeHashMap.clear();
                    player.sendMessage(Reference.prefix_tribe + "안전을 위해 데이터 세이브 후 초기화 되었습니다.");
                    player.sendMessage(Reference.prefix_tribe + "복구를 원하면 데이터를 로드하세요.");
                } else {

                }
            } else if (args[0].equalsIgnoreCase("showall") || args[0].equalsIgnoreCase("전체목록")) {
                TribeReference.showTribeAll(player);
            }
        }
        else if(args.length == 3) {
            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("설정")) {
                if (args[1].equalsIgnoreCase("tribe") || args[0].equalsIgnoreCase("진영")) {
                    if(TribeReference.setPlayerTribe(player, args[2])) {
                        player.sendMessage(Reference.prefix_tribe + player.getName() + "의 진영이 " + args[2] + "로 설정되었습니다.");
                    }
                    else {
                        player.sendMessage(Reference.prefix_tribe + args[2] + " 진영은 존재하지 않습니다.");
                    }

                }
            }
            else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("제거")) {
                if (args[1].equalsIgnoreCase("region") || args[0].equalsIgnoreCase("지역")) {
                    TribeReference.tribeRegionRemoveProcess(player, args[2]);
                }
            }
        }
        else if(args.length == 4) {
            if (args[1].equalsIgnoreCase("region") || args[0].equalsIgnoreCase("지역")) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("추가")) {
                    TribeReference.tribeRegionAddProcess(player, args[2], args[3]);
                }
                else if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("설정")) {
                    if (TribeReference.tribeRegionRemoveProcess(player, args[2])) {
                        TribeReference.tribeRegionAddProcess(player, args[2], args[3]);
                    }
                }
            }

        }
        return false;
    }
}
