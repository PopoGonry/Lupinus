package com.popogonry.lupinus.item.ItemBan;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemBanEvent implements Listener {

    @EventHandler
    public static void PlayerUseBanItemEvent(PlayerInteractEvent event) {
        if(!event.getPlayer().isOp()) {
            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(ItemBanReference.banItemList.contains(new ItemStack(event.getPlayer().getItemInHand().getType()))) {
                    event.setCancelled(true);
                }
            }
        }

    }






    @EventHandler
    public static void ItemBanGUIClickEvent(InventoryClickEvent event) {
        if (((event.getView().getTitle().contains("BanItemGUI"))) &&
                (event.getCurrentItem() != null) && (event.getCurrentItem().getType() != Material.AIR)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            String[] pageString = event.getInventory().getItem(49).getItemMeta().getDisplayName().split("/");
            pageString[0] = pageString[0].replace("§6§l", "");
            int page = Integer.parseInt(pageString[0]) - 1;
            if(0 <= event.getRawSlot() && event.getRawSlot() <= 44) {
                ItemBanReference.removeBanItem(event.getCurrentItem());
                ItemBanGUI.openBanItemGUI(player, page);
            }
            else if (45 <= event.getRawSlot() && event.getRawSlot() <= 53) {
                if(event.getRawSlot() == 48 && event.getCurrentItem().getItemMeta().getDisplayName().contains("◀ 이전장")) {
                    ItemBanGUI.openBanItemGUI(player, (page - 1));
                }
                else if(event.getRawSlot() == 50 && event.getCurrentItem().getItemMeta().getDisplayName().contains("▶ 다음장")) {
                    ItemBanGUI.openBanItemGUI(player, (1));
                }
            }
            else if(54 <= event.getRawSlot() && event.getRawSlot() <= 89) {
                ItemBanReference.addBanItem(event.getCurrentItem());
                ItemBanGUI.openBanItemGUI(player, page);
            }
        }
    }
}
