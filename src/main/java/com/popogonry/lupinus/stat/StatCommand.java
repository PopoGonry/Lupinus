package com.popogonry.lupinus.stat;

import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class StatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Reference.prefix_error + Reference.COMMAND_CMD_NOT_PRACTICE_MESSAGE);
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("show")) {
                player.sendMessage(String.valueOf(StatReference.statHashMap.get(player.getUniqueId())));
            }
            else if(args[0].equalsIgnoreCase("save")) {
                player.sendMessage(String.valueOf(StatReference.saveStatData(player)));
            }
            else if(args[0].equalsIgnoreCase("load")) {
                player.sendMessage(String.valueOf(StatReference.loadStatData(player)));
            }
            else if(args[0].equalsIgnoreCase("showall")) {
                player.sendMessage(StatReference.statHashMap.toString());
            }
            else if(args[0].contains("inv")) {
                if(args[0].equalsIgnoreCase("invhp")) {
                    StatReference.investStat(player, "HP");
                }
                else if(args[0].equalsIgnoreCase("invstr")) {
                    StatReference.investStat(player, "STR");
                }
                else if(args[0].equalsIgnoreCase("invdef")) {
                    StatReference.investStat(player, "DEF");
                }
                else if(args[0].equalsIgnoreCase("invdex")) {
                    StatReference.investStat(player, "DEX");
                }
                Reference.sendDataToClient(player);
            }
        }
        if(args[0].equalsIgnoreCase("set")) {
            Player commandPlayer;
            if (args.length == 3) {
                HashMap<String, Integer> playerStatHashMap = new HashMap<>(StatReference.statHashMap.get(player.getUniqueId()));
                playerStatHashMap.put(args[1].toUpperCase(), Integer.valueOf(args[2]));
                StatReference.statHashMap.put(player.getUniqueId(), playerStatHashMap);
                player.sendMessage(Reference.prefix_normal + player.getName() + "의 " + args[1].toUpperCase() + "스텟이 " + args[2] + "으로 변경되었습니다.");
            } else if (args.length == 4) {
                commandPlayer = Bukkit.getServer().getPlayer(args[1]);
                HashMap<String, Integer> playerStatHashMap = new HashMap<>(StatReference.statHashMap.get(commandPlayer.getUniqueId()));
                playerStatHashMap.put(args[2].toUpperCase(), Integer.valueOf(args[3]));
                StatReference.statHashMap.put(commandPlayer.getUniqueId(), playerStatHashMap);
                player.sendMessage(Reference.prefix_normal + commandPlayer.getName() + "의 " + args[2].toUpperCase() + "스텟이 " + args[3] + "으로 변경되었습니다.");
            }
        }
        return false;
    }
}
