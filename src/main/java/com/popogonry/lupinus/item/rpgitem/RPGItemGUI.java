package com.popogonry.lupinus.item.rpgitem;

import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.*;

public class RPGItemGUI {

    public static void setGuiItem(String itemName, int itemId, int data, int stack, List<String> lore, int loc, Inventory inv) {
        ItemStack item = new MaterialData(itemId, (byte)data).toItemStack(stack);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemName);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(itemMeta);
        inv.setItem(loc, item);
    }

    public static void setGuiItemContainItemStack(String itemName, ItemStack itemStack, List<String> lore, int loc, Inventory inv) {
        ItemStack item = new ItemStack(itemStack);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemName);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        inv.setItem(loc, item);
    }

    public static void setGuiItemNoLore(String itemName, int itemId, int data, int stack, int loc, Inventory inv) {
        ItemStack item = new MaterialData(itemId, (byte)data).toItemStack(stack);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemName);
        item.setItemMeta(itemMeta);
        inv.setItem(loc, item);
    }

    public static void setRPGItemWithValue(String name, String lore, int strikingPower, int defensivePower, int criticalChance, int moveSpeed, int lifeSteal, int defenseIgnore, int STR, int DEF, int DEX, int HP, int levelLimit, Inventory inventory) {
        setGuiItem("§6§l● 이름", 421, 0, 1, Collections.singletonList("§6§l현재 이름 : §r" + name), 12, inventory);
        setGuiItem("§6§l● 설명", 340, 0, 1, Collections.singletonList("§6§l현재 설명 : §r" + lore), 13, inventory);
        setGuiItem("§b§l● 공격력", 267, 0, 1, Collections.singletonList("§b§l현재 수치 : " + strikingPower), 30, inventory);
        setGuiItem("§b§l● 방어력", 307, 0, 1, Collections.singletonList("§b§l현재 수치 : " + defensivePower), 31, inventory);
        setGuiItem("§b§l● 치명타확률", 283, 0, 1, Collections.singletonList("§b§l현재 수치 : " + criticalChance), 32, inventory);
        setGuiItem("§b§l● 이동속도", 309, 0, 1, Collections.singletonList("§b§l현재 수치 : " + moveSpeed), 33, inventory);
        setGuiItem("§b§l● 흡혈", 373, 0, 1, Collections.singletonList("§b§l현재 수치 : " + lifeSteal), 34, inventory);
        setGuiItem("§b§l● 방무", 262, 0, 1, Collections.singletonList("§b§l현재 수치 : " + defenseIgnore), 35, inventory);
        setGuiItem("§4§l● STR", 276, 0, 1, Collections.singletonList("§4§l현재 수치 : " + STR), 39, inventory);
        setGuiItem("§7§l● DEF", 311, 0, 1, Collections.singletonList("§7§l현재 수치 : " + DEF), 40, inventory);
        setGuiItem("§e§l● DEX", 313, 0, 1, Collections.singletonList("§e§l현재 수치 : " + DEX), 41, inventory);
        setGuiItem("§2§l● HP", 260, 0, 1, Collections.singletonList("§2§l현재 수치 : " + HP), 42, inventory);
        setGuiItem("§6§l● 레벨제한", 384, 0, 1, Collections.singletonList("§6§l현재 수치 : " + levelLimit), 14, inventory);
    }

    public static void openRPGItemGUI(Player player, ItemStack item, List<String> changeList) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§6§lRPGItemGUI");

        for (int i=0; i < 54; i++) {
            setGuiItemNoLore(" ", Reference.GUI_BACKGROUND_ITEM, 0, 1, i, inventory); //GUI 유리판
        }
        inventory.setItem(10, item); // 칸 비우기
        if(item.getType() == Material.AIR) {
            setRPGItemWithValue("", "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, inventory);
        }
        else {
            HashMap<String, Integer> valueHashMap = new HashMap<>(RPGItemReference.extractAllFromRPGItem(item));
            RPGItemGUI.setRPGItemWithValue(item.getItemMeta().getDisplayName(), RPGItemReference.extractLoreFromRPGITEM(item),
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
                    inventory);
        }

        List<String> list = new ArrayList<>(changeList);
        list.add(0, "§6§l변경사항");
        setGuiItem(RPGItemReference.prefix_accept, 145, 0, 1, list, 37, inventory);

        player.openInventory(inventory);

    }

}
