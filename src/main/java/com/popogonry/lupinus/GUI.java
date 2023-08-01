package com.popogonry.lupinus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
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

    public static ItemStack getHead(OfflinePlayer player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(player.getName());
        skull.setOwner(player.getName());
        item.setItemMeta(skull);
        return item;
    }

    public static void openSecondCheckGUI(Player player, ItemStack itemStack, String title, String sentence) {
        Inventory inventory = Bukkit.createInventory(null, 9, Reference.prefix_normal + title);
        for(int i=0; i < 9; i++) {
            setGuiItemNoLore(" ", Reference.GUI_BACKGROUND_ITEM, 0, 1, i, inventory); //GUI 유리판
        }
        setGuiItemContainItemStack(sentence, itemStack, Arrays.asList(), 4, inventory);

        player.openInventory(inventory);
    }
}
