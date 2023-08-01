package com.popogonry.lupinus.item.rpgitem;

import com.popogonry.lupinus.Reference;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

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
            else if(args[0].equalsIgnoreCase("copy")) {
                RPGItemReference.rpgItemCopyDataHashMap.put(player.getUniqueId(), RPGItemReference.extractAllFromRPGItem(player.getItemInHand()));
            }
            else if(args[0].equalsIgnoreCase("paste")) {
                HashMap<String, Integer> dataHashMap = RPGItemReference.rpgItemCopyDataHashMap.get(player.getUniqueId());

                player.getInventory().setItemInMainHand(RPGItemReference.createRPGItem(player.getItemInHand(), "Copied Item", "lore",
                        dataHashMap.get(RPGItemReference.prefix_strikingPower),
                        dataHashMap.get(RPGItemReference.prefix_defensivePower),
                        dataHashMap.get(RPGItemReference.prefix_criticalChance),
                        dataHashMap.get(RPGItemReference.prefix_moveSpeed),
                        dataHashMap.get(RPGItemReference.prefix_lifeSteal),
                        dataHashMap.get(RPGItemReference.prefix_defenseIgnore),
                        dataHashMap.get(RPGItemReference.prefix_STR),
                        dataHashMap.get(RPGItemReference.prefix_DEF),
                        dataHashMap.get(RPGItemReference.prefix_DEX),
                        dataHashMap.get(RPGItemReference.prefix_HP),
                        dataHashMap.get(RPGItemReference.prefix_levelLimit)
                ));
            }
        }
        return false;
    }
}
