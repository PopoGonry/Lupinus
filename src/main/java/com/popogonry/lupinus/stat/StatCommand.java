package com.popogonry.lupinus.stat;

import com.popogonry.lupinus.Reference;
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
        }

        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("set")) {
                HashMap<String, Integer> playerStatHashMap = new HashMap<>(StatReference.statHashMap.get(player.getUniqueId()));
                playerStatHashMap.put(args[1].toUpperCase(), Integer.valueOf(args[2]));
                StatReference.statHashMap.put(player.getUniqueId(), playerStatHashMap);
                player.sendMessage(Reference.prefix_normal + args[1].toUpperCase() + "스텟이 " + args[2] + "으로 변경되었습니다.");
            }
        }

        return false;
    }
}