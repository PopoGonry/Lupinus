package com.popogonry.lupinus.item.rpgitem;

import com.popogonry.lupinus.GUI;
import com.popogonry.lupinus.Reference;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RPGItemEvent implements Listener {

    @EventHandler
    public static void rpgItemGUIClickEvent(InventoryClickEvent event) {
        if (((event.getView().getTitle().contains("RPGItemGUI"))) &&
                (event.getCurrentItem() != null) && (event.getCurrentItem().getType() != Material.AIR)){
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            if((0 <= event.getRawSlot() && event.getRawSlot() <= 53) &&
                    !event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
                if((event.getInventory().getItem(10) != null) && (event.getInventory().getItem(10).getType() != Material.AIR)) {
                    //장비 있을떄
                    if((12 <= event.getRawSlot() && event.getRawSlot() <= 14) || (30 <= event.getRawSlot() && event.getRawSlot() <= 35) || (39 <= event.getRawSlot() && event.getRawSlot() <= 42)) {
                        player.sendMessage(Reference.prefix_opMessage + event.getCurrentItem().getItemMeta().getDisplayName() + "§r의 변경값을 입력해주세요. §8(취소 : C)");
                        Reference.chatModeHashMap.put(player.getUniqueId(), event.getCurrentItem().getItemMeta().getDisplayName());
                        Reference.recentInventoryHashMap.put(player.getUniqueId(), event.getInventory());
                        player.closeInventory();
                    }
                    else if(event.getRawSlot() == 37) {
                        if(event.isLeftClick()) {
                            player.getInventory().addItem(event.getInventory().getItem(10));
                            player.sendMessage(Reference.prefix_opMessage + "아이템이 지급되었습니다.");
                        }
                    }
                    else if(event.getRawSlot() == 10) {
                        RPGItemGUI.openRPGItemGUI(player, new ItemStack(Material.AIR), Arrays.asList());
                    }
                }
                else {
                    //장비 없을때
                    player.sendMessage(Reference.prefix_opMessage + "아이템이 선택되지 않았습니다.");
                }
            }
            else if(54 <= event.getRawSlot() && event.getRawSlot() <= 89) {
                ItemStack item = new ItemStack(event.getCurrentItem());
                String itemName = "";
                if(item.getItemMeta().getDisplayName() == null) {
                    itemName = "§6RPGItem";
                }
                else {
                    itemName = item.getItemMeta().getDisplayName();
                }

                if(event.getCurrentItem().getItemMeta().getLore() == null) {
                    item = RPGItemReference.createRPGItem(item, itemName, "§aRPGItem Lore", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                }

                ItemMeta itemMeta = item.getItemMeta();
                HashMap<String, Integer> valueHashMap = RPGItemReference.extractAllFromRPGItem(item);

                event.getInventory().setItem(10, item);
                RPGItemGUI.setRPGItemWithValue(itemMeta.getDisplayName(), RPGItemReference.extractLoreFromRPGITEM(item),
                        valueHashMap.get(RPGItemReference.prefix_strikingPower),
                        valueHashMap.get(RPGItemReference.prefix_defensivePower),
                        valueHashMap.get(RPGItemReference.prefix_criticalChance),
                        valueHashMap.get(RPGItemReference.prefix_moveSpeed),
                        valueHashMap.get(RPGItemReference.prefix_lifeSteal),
                        valueHashMap.get(RPGItemReference.prefix_defenseIgnore),
                        valueHashMap.get(RPGItemReference.prefix_STR),
                        valueHashMap.get(RPGItemReference.prefix_DEF),
                        valueHashMap.get(RPGItemReference.prefix_DEX),
                        valueHashMap.get(RPGItemReference.prefix_HP),
                        valueHashMap.get(RPGItemReference.prefix_levelLimit),
                        event.getInventory());

                GUI.setGuiItem(RPGItemReference.prefix_accept, 145, 0, 1, Arrays.asList("§6§l변경사항"), 37, event.getInventory());
            }
        }
    }

    @EventHandler
    public static void rpgItemChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(RPGItemReference.prefixList.contains(Reference.chatModeHashMap.get(player.getUniqueId()))) {
            event.setCancelled(true);
            ItemStack item = Reference.recentInventoryHashMap.get(player.getUniqueId()).getItem(10);
            HashMap<String, Integer> valueHashMap = new HashMap<>(RPGItemReference.extractAllFromRPGItem(item));
            ItemStack changeItem = Reference.recentInventoryHashMap.get(player.getUniqueId()).getItem(37);
            List<String> change = changeItem.getItemMeta().getLore();
            change.remove(0);
            if(event.getMessage().equalsIgnoreCase("c")) {
                player.openInventory(Reference.recentInventoryHashMap.get(player.getUniqueId()));
                Reference.chatModeHashMap.remove(player.getUniqueId());
            }

            else if(Reference.chatModeHashMap.get(player.getUniqueId()).equalsIgnoreCase(RPGItemReference.prefix_name)) {
                item = RPGItemReference.createRPGItem(item,
                        event.getMessage(),
                        RPGItemReference.extractLoreFromRPGITEM(item),
                        valueHashMap.get(RPGItemReference.prefix_strikingPower),
                        valueHashMap.get(RPGItemReference.prefix_defensivePower),
                        valueHashMap.get(RPGItemReference.prefix_criticalChance),
                        valueHashMap.get(RPGItemReference.prefix_moveSpeed),
                        valueHashMap.get(RPGItemReference.prefix_lifeSteal),
                        valueHashMap.get(RPGItemReference.prefix_defenseIgnore),
                        valueHashMap.get(RPGItemReference.prefix_STR),
                        valueHashMap.get(RPGItemReference.prefix_DEF),
                        valueHashMap.get(RPGItemReference.prefix_DEX),
                        valueHashMap.get(RPGItemReference.prefix_HP),
                        valueHashMap.get(RPGItemReference.prefix_levelLimit
                        ));
                RPGItemGUI.openRPGItemGUI(player, item, change);
            }
            else if(Reference.chatModeHashMap.get(player.getUniqueId()).equalsIgnoreCase(RPGItemReference.prefix_lore)) {
                item = RPGItemReference.createRPGItem(item,
                        item.getItemMeta().getDisplayName(),
                        event.getMessage(),
                        valueHashMap.get(RPGItemReference.prefix_strikingPower),
                        valueHashMap.get(RPGItemReference.prefix_defensivePower),
                        valueHashMap.get(RPGItemReference.prefix_criticalChance),
                        valueHashMap.get(RPGItemReference.prefix_moveSpeed),
                        valueHashMap.get(RPGItemReference.prefix_lifeSteal),
                        valueHashMap.get(RPGItemReference.prefix_defenseIgnore),
                        valueHashMap.get(RPGItemReference.prefix_STR),
                        valueHashMap.get(RPGItemReference.prefix_DEF),
                        valueHashMap.get(RPGItemReference.prefix_DEX),
                        valueHashMap.get(RPGItemReference.prefix_HP),
                        valueHashMap.get(RPGItemReference.prefix_levelLimit
                        ));
                RPGItemGUI.openRPGItemGUI(player, item, change);
            }
            else {
                boolean isNumeric = StringUtils.isNumeric(event.getMessage());
                if(isNumeric) {
                    change.add(Reference.chatModeHashMap.get(player.getUniqueId()) + " : " + valueHashMap.get(Reference.chatModeHashMap.get(player.getUniqueId())) + " -> " + event.getMessage());

                    valueHashMap.put(Reference.chatModeHashMap.get(player.getUniqueId()), Integer.valueOf(event.getMessage()));
                    item = RPGItemReference.createRPGItem(item,
                            item.getItemMeta().getDisplayName(),
                            RPGItemReference.extractLoreFromRPGITEM(item),
                            valueHashMap.get(RPGItemReference.prefix_strikingPower),
                            valueHashMap.get(RPGItemReference.prefix_defensivePower),
                            valueHashMap.get(RPGItemReference.prefix_criticalChance),
                            valueHashMap.get(RPGItemReference.prefix_moveSpeed),
                            valueHashMap.get(RPGItemReference.prefix_lifeSteal),
                            valueHashMap.get(RPGItemReference.prefix_defenseIgnore),
                            valueHashMap.get(RPGItemReference.prefix_STR),
                            valueHashMap.get(RPGItemReference.prefix_DEF),
                            valueHashMap.get(RPGItemReference.prefix_DEX),
                            valueHashMap.get(RPGItemReference.prefix_HP),
                            valueHashMap.get(RPGItemReference.prefix_levelLimit
                            ));
                    RPGItemGUI.openRPGItemGUI(player, item, change);
                    Reference.chatModeHashMap.remove(player.getUniqueId());
                }
                else {
                    player.sendMessage(Reference.prefix_error + "잘못된 입력입니다. 다시 입력해주세요.");
                }

            }
        }
    }
}
