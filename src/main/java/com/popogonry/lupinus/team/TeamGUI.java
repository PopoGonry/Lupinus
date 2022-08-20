package com.popogonry.lupinus.team;

import com.popogonry.lupinus.GUI;
import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;

public class TeamGUI {
    public static void openTeamGUI(Player player, int page) {
        String team = TeamReference.getPlayersTeam(player);
        Inventory inventory = Bukkit.createInventory(null, 54, Reference.prefix_team+ "§e§l" + team);


        for (int i=0; i < 54; i++) {
            GUI.setGuiItemNoLore(" ", Reference.GUI_BACKGROUND_ITEM, 0, 1, i, inventory); //GUI 유리판
        }



//        inventory.setItem(10, item); // 칸 비우기












        player.openInventory(inventory);
    }
}
