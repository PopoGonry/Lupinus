package com.popogonry.lupinus.item.ItemBan;

import com.popogonry.lupinus.Reference;
import com.popogonry.lupinus.stat.StatReference;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class ItemBanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Reference.prefix_error + Reference.COMMAND_CMD_NOT_PRACTICE_MESSAGE);
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("save")) {
                ItemBanReference.saveBanItemData();
            }
            else if (args[0].equalsIgnoreCase("load")) {
                ItemBanReference.loadBanItemData();
            }
            else if (args[0].equalsIgnoreCase("add")) {
                ItemBanReference.banItemList.add(player.getItemInHand());
            }
            else if (args[0].equalsIgnoreCase("show")) {
                player.sendMessage(ItemBanReference.banItemList.toString());
            }
        }
        return false;
    }
}
