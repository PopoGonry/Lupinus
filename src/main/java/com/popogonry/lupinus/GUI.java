package com.popogonry.lupinus;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.List;

public class GUI {
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
}
