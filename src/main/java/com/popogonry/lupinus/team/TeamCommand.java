package com.popogonry.lupinus.team;

import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Reference.prefix_error + Reference.COMMAND_CMD_NOT_PRACTICE_MESSAGE);
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("save")) {
                player.sendMessage(String.valueOf(TeamReference.saveTeamData()));
            }
            else if(args[0].equalsIgnoreCase("load")) {
                player.sendMessage(String.valueOf(TeamReference.loadTeamData()));
            }
            else if(args[0].equalsIgnoreCase("show")) {
                player.sendMessage(String.valueOf(TeamReference.teamHashMap));
            }
            else if(args[0].equalsIgnoreCase("reset")) {
                TeamReference.teamHashMap.clear();
            }
            else if(args[0].equalsIgnoreCase("list")) {
                TeamReference.showTeamList(player);
            }
            else if(args[0].equalsIgnoreCase("showall")) {
                TeamReference.showTeamAll(player);
            }
            else if(args[0].equalsIgnoreCase("head")) {
                player.getInventory().addItem(TeamReference.getHead(player));
            }
            else if(args[0].equalsIgnoreCase("gui")) {
                TeamGUI.openTeamGUI(player, 1);
            }
        }
        else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("create")) {
                player.sendMessage(String.valueOf(TeamReference.createTeam(args[1])));
            }
            else if(args[0].equalsIgnoreCase("remove")) {
                player.sendMessage(String.valueOf(TeamReference.removeTeam(args[1])));
            }
        }
        else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("rename")) {
                player.sendMessage(String.valueOf(TeamReference.renameTeam(args[1], args[2])));
            }
            if(Bukkit.getPlayer(args[2]) instanceof Player) {
                if(args[0].equalsIgnoreCase("add")) {
                    player.sendMessage(String.valueOf(TeamReference.addMember(args[1], Bukkit.getPlayer(args[2]))));
                }
                else if(args[0].equalsIgnoreCase("remove")) {
                    player.sendMessage(String.valueOf(TeamReference.removeMember(args[1], Bukkit.getPlayer(args[2]))));
                }
            }
        }

        return false;
    }
}
