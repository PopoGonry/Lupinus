package com.popogonry.lupinus.team;

import com.popogonry.lupinus.GUI;
import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TeamGUI {
    public static boolean openTeamGUI(Player player, int page) {
        if(TeamReference.getPlayersTeam(player) == null) {
            return false;
        }
        String team = TeamReference.getPlayersTeam(player);
        List<UUID> memberUUIDList = new ArrayList<>(TeamReference.teamHashMap.get(team));
        int teamMemberCount = TeamReference.teamHashMap.get(team).size();
        int maxPage = (int) Math.ceil((double) teamMemberCount/28);
        if(maxPage == 0) maxPage++;

        Inventory inventory = Bukkit.createInventory(null, 54, Reference.prefix_team+ "§e§l" + team + " 팀");

        for(int i=0; i < 54; i++) {
            GUI.setGuiItemNoLore(" ", Reference.GUI_BACKGROUND_ITEM, 0, 1, i, inventory); //GUI 유리판
        }

        for(int j = 1; j <= 4; j++) {
            for(int i = 1; i <= 7; i++) {
                inventory.setItem(i + j*9, new ItemStack(Material.AIR));
            }
        }


        int count = 0;
        for(int j = 1; j <= 4; j++) {
            for(int i = 1; i <= 7 && count < teamMemberCount - 28*page; i++) {
                OfflinePlayer member = Bukkit.getServer().getOfflinePlayer(memberUUIDList.get(count));
                if(count == 0) {
                    GUI.setGuiItemContainItemStack("§6§l" + member.getName(), GUI.getHead(member), Arrays.asList("§b§l ■ 팀장", "§a§l ■ 우클릭 : 더보기"), i + j*9, inventory);
                }
                else {
                    GUI.setGuiItemContainItemStack("§e§l" + member.getName(), GUI.getHead(member), Arrays.asList("§b§l ■ 팀원", "§a§l ■ 우클릭 : 더보기"), i + j*9, inventory);
                }
                count++;
            }
        }

        if(page > 0) GUI.setGuiItemNoLore("§6§l◀ 이전장", 160, 5, 1, 48, inventory);
        else GUI.setGuiItemNoLore("§7이전장이 없습니다.", 160, 7, 1, 48, inventory);

        if(teamMemberCount > (page+1)*45) GUI.setGuiItemNoLore("§6§l▶ 다음장", 160, 5, 1, 50, inventory);
        else GUI.setGuiItemNoLore("§7다음장이 없습니다.", 160, 7, 1, 50, inventory);

        GUI.setGuiItem("§6§l" + (page + 1) + "/" + maxPage, 399, 0, 1, Arrays.asList("§6§l■ 팀인원 : " + teamMemberCount + "명"), 49, inventory);

//        inventory.setItem(10, item); // 칸 비우기

        player.openInventory(inventory);
        return true;
    }

    public static void openPlayerDetailGUI(Player player, OfflinePlayer target) {
        Inventory inventory = Bukkit.createInventory(null, 27, Reference.prefix_team+ "§e§l" + "Detail");
        OfflinePlayer master = Bukkit.getOfflinePlayer(TeamReference.teamHashMap.get(TeamReference.getPlayersTeam(player)).get(0));
        for(int i=0; i < 27; i++) {
            GUI.setGuiItemNoLore(" ", Reference.GUI_BACKGROUND_ITEM, 0, 1, i, inventory); //GUI 유리판
        }
        for(int i = 11; i <= 15; i++) {
            inventory.setItem(i, new ItemStack(Material.AIR));
        }
        GUI.setGuiItemContainItemStack("§6§l" + target.getName(), GUI.getHead(target), Arrays.asList(), 11, inventory);
        GUI.setGuiItem("§a§l정보보기", 340, 0, 1, Arrays.asList(), 12, inventory);
        if(master.equals(player) && !target.equals(master)) {
            GUI.setGuiItem("§b§l팀장위임", 399, 0, 1, Arrays.asList(), 13, inventory);
            GUI.setGuiItem("§c§l팀강퇴", 397, 0, 1, Arrays.asList(), 14, inventory);
        }
        GUI.setGuiItem("§e§l돌아가기", 166, 0, 1, Arrays.asList(), 15, inventory);


        player.openInventory(inventory);
    }

}
