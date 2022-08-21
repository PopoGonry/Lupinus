package com.popogonry.lupinus.item.ItemBan;

import com.popogonry.lupinus.GUI;
import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class ItemBanGUI {
    public static void openBanItemGUI(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(null, 54, Reference.prefix_normal + "§e§l" + "BanItemGUI");
        int itemCount = ItemBanReference.banItemList.size();
        int maxPage = (int) Math.ceil((double) itemCount/45);
        if(maxPage == 0) maxPage++;

        for(int i = 0; i < 45 && i < itemCount - 45*page; i++) {
            inventory.setItem(i, ItemBanReference.banItemList.get(i + page*45));
        }
        for(int i = 0; i < 9; i++) {
            GUI.setGuiItemNoLore(" ", Reference.GUI_BACKGROUND_ITEM, 0, 1, i + 45, inventory); //GUI 유리판
        }

        if(page > 0) GUI.setGuiItemNoLore("§6§l◀ 이전장", 160, 5, 1, 48, inventory);
        else GUI.setGuiItemNoLore("§7이전장이 없습니다.", 160, 7, 1, 48, inventory);

        if(itemCount > (page+1)*45) GUI.setGuiItemNoLore("§6§l▶ 다음장", 160, 5, 1, 50, inventory);
        else GUI.setGuiItemNoLore("§7다음장이 없습니다.", 160, 7, 1, 50, inventory);

        GUI.setGuiItem("§6§l" + (page + 1) + "/" + maxPage, 399, 0, 1, Arrays.asList("§6§l■ 아이템 갯수 : " + itemCount), 49, inventory);
        player.openInventory(inventory);
    }
}