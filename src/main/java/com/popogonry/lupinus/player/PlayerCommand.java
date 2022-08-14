package com.popogonry.lupinus.player;

import com.popogonry.lupinus.Reference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Reference.prefix_error + Reference.COMMAND_CMD_NOT_PRACTICE_MESSAGE);
            return true;
        }

        Player player = (Player) sender;
//        if(args[0].equalsIgnoreCase("test")) {
//            for(String key : Reference.configDataHashMap.keySet()) {
//                player.sendMessage(String.valueOf(Reference.configDataHashMap.get(key)));
//            }
//        }
        return false;
    }
}
