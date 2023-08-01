package com.popogonry.lupinus.region;

import com.popogonry.lupinus.Reference;
import com.popogonry.lupinus.stat.StatReference;
import com.popogonry.lupinus.team.TeamReference;
import com.popogonry.lupinus.tribe.TribeReference;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class RegionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Reference.prefix_error + Reference.COMMAND_CMD_NOT_PRACTICE_MESSAGE);
            return true;
        }
        Player player = (Player) sender;
        if(player.isOp()) {
            if(args.length == 1) {
                if (args[0].equalsIgnoreCase("save") || args[0].equalsIgnoreCase("저장")) {
                    if (RegionReference.saveRegionData() == true) {
                        player.sendMessage(Reference.prefix_dataSave + "지역 데이터 세이브 완료" + " (" + RegionReference.regionDM.fileName + ")");
                    } else {

                    }
                } else if (args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("불러오기")) {
                    if (RegionReference.loadRegionData() == true) {
                        player.sendMessage(Reference.prefix_dataLoad + "지역 데이터 로드 완료" + " (" + RegionReference.regionDM.fileName + ")");
                    } else {
                        player.sendMessage(Reference.prefix_dataError + "지역 데이터 로드 실패" + " (" + RegionReference.regionDM.fileName + ")");
                    }
                } else if (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("초기화")) {
                    if (RegionReference.saveRegionData() == true) {
                        player.sendMessage(Reference.prefix_dataSave + "지역 데이터 세이브 완료" + " (" + RegionReference.regionDM.fileName + ")");
                        RegionReference.regionHashMap.clear();
                        player.sendMessage(Reference.prefix_tribe + "안전을 위해 데이터 세이브 후 초기화 되었습니다.");
                        player.sendMessage(Reference.prefix_tribe + "복구를 원하면 데이터를 로드하세요.");
                    } else {

                    }
                } else if (args[0].equalsIgnoreCase("showall") || args[0].equalsIgnoreCase("전체목록")) {
                    RegionReference.showRegionAll(player);
                }
            }
            else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("추가")) {
                    RegionReference.regionAddProcess(player, args[1]);
                }
                else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("제거")) {
                    RegionReference.regionRemoveProcess(player, args[1]);
                }
                else if (args[0].equalsIgnoreCase("test") || args[0].equalsIgnoreCase("test")) {
                    player.sendMessage(String.valueOf(RegionReference.isPlayerInRegion(player, RegionReference.regionHashMap.get(args[1]))));
                }
                else if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) {
                    player.sendMessage(String.valueOf(RegionReference.teleportPlayerToRegion(player, args[1])));
                }

            }
            else if(args.length == 3) {
                if(args[0].equalsIgnoreCase("rename") || args[0].equalsIgnoreCase("이름변경")) {
                    if(RegionReference.renameRegion(args[1], args[2])) {
                        player.sendMessage(Reference.prefix_region + args[1] + " 지역 이름이 " + args[2] + " 로 변경되었습니다.");
                    }
                    else {
                        player.sendMessage(Reference.prefix_error + args[1] + " 지역이 없거나, " + args[2] + " 지역이 존재합니다.");
                    }
                }
            }
        }
        else {
            player.sendMessage(Reference.prefix_error + "권한이 부족합니다.");
        }

        return false;
    }
}
