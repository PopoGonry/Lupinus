package com.popogonry.lupinus.item.rpgitem;

import com.popogonry.lupinus.Reference;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class RPGItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Reference.prefix_error + Reference.COMMAND_CMD_NOT_PRACTICE_MESSAGE);
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("gui")) {
                RPGItemGUI.openRPGItemGUI(player, new ItemStack(Material.AIR), Arrays.asList());
            }
        }
        return false;
    }
}
