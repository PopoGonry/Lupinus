package com.popogonry.lupinus.item.rpgitem;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class RPGItemReference {
    // 이속(dex) / 공격(str) / 방어(def) / 체력(hp) / 공격력 / 흡혈 / 방어력무시 / 치명타확률 / 방어력 / 이동속도 / 레벨제한 / 설명
    // 이름(name) / 설명(lore) / 공격력(strikingPower) / 방어력(defensivePower) / 치명타확률(criticalChance) / 이동속도(moveSpeed) / 흡혈(lifeSteal) / 방어력무시(defenseIgnore) / 공격(STR) / 방어(DEF) / 이속(DEX) / 체력(HP) / 레벨제한(levelLimit)
    public static HashMap<UUID, HashMap<String, Integer>> rpgItemCopyDataHashMap = new HashMap<>();
    public static ItemStack createRPGItem(ItemStack item, String name, String lore, int strikingPower, int defensivePower, int criticalChance, int moveSpeed, int lifeSteal, int defenseIgnore, int STR, int DEF, int DEX, int HP, int levelLimit) {
        ItemStack itemStack = new ItemStack(item);
        ItemMeta itemMeta = itemStack.getItemMeta();
        name = name.replace("&", "§");
        name = name.replace("_", " ");
        lore = lore.replace("&", "§");
        lore = lore.replace("_", " ");
        itemMeta.setDisplayName(name);
        List<String> itemLore = Arrays.asList(lore.split("#"));
        itemMeta.setLore(itemLore);
        itemLore = itemMeta.getLore();
        if(strikingPower != 0) itemLore.add(prefix_strikingPower + " : " + strikingPower); // &b
        if(defensivePower != 0) itemLore.add(prefix_defensivePower + " : " + defensivePower); // &b
        if(criticalChance != 0) itemLore.add(prefix_criticalChance + " : " + criticalChance + "%"); // &b
        if(moveSpeed != 0) itemLore.add(prefix_moveSpeed + " : " + moveSpeed); // &b
        if(lifeSteal != 0) itemLore.add(prefix_lifeSteal +  " : " + lifeSteal); // &b
        if(defenseIgnore != 0) itemLore.add(prefix_defenseIgnore + " : " + defenseIgnore); // &b
        if(STR != 0) itemLore.add(prefix_STR + " : " + STR); // &4
        if(DEF != 0) itemLore.add(prefix_DEF + " : " + DEF); // &7
        if(DEX != 0) itemLore.add(prefix_DEX + " : " + DEX); // &e
        if(HP != 0) itemLore.add(prefix_HP + " : " + HP); // &2
        if(levelLimit != 0) itemLore.add(prefix_levelLimit + " : " + levelLimit); // &6
        itemMeta.setLore(itemLore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.setUnbreakable(true);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static String prefix_strikingPower = "§b§l● 공격력";
    public static String prefix_defensivePower = "§b§l● 방어력";
    public static String prefix_criticalChance = "§b§l● 치명타확률";
    public static String prefix_moveSpeed = "§b§l● 이동속도";
    public static String prefix_lifeSteal = "§b§l● 흡혈";
    public static String prefix_defenseIgnore = "§b§l● 방무";
    public static String prefix_STR = "§4§l● STR";
    public static String prefix_DEF = "§7§l● DEF";
    public static String prefix_DEX = "§e§l● DEX";
    public static String prefix_HP = "§2§l● HP";
    public static String prefix_levelLimit = "§6§l● 레벨제한";
    public static String prefix_name = "§6§l● 이름";
    public static String prefix_lore = "§6§l● 설명";
    public static String prefix_accept = "§6§l● 적용하기";

    public static List<String> prefixList = new ArrayList<>(Arrays.asList(prefix_strikingPower,
            prefix_defensivePower,
            prefix_criticalChance,
            prefix_moveSpeed,
            prefix_lifeSteal,
            prefix_defenseIgnore,
            prefix_STR,
            prefix_DEF,
            prefix_DEX,
            prefix_HP,
            prefix_levelLimit,
            prefix_name,
            prefix_lore));



    public static HashMap<String, Integer> extractAllFromRPGItem(ItemStack item) {
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>(11){{
            put(prefix_strikingPower, 0);
            put(prefix_defensivePower, 0);
            put(prefix_criticalChance, 0);
            put(prefix_moveSpeed, 0);
            put(prefix_lifeSteal, 0);
            put(prefix_defenseIgnore, 0);
            put(prefix_STR, 0);
            put(prefix_DEF, 0);
            put(prefix_DEX, 0);
            put(prefix_HP, 0);
            put(prefix_levelLimit, 0);
        }};

        if(item == null) {
            return resultMap;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) {
            return resultMap;
        }

        List<String> itemLore = itemMeta.getLore();
        if(itemLore == null) {
            return resultMap;
        }

        for (String s : itemLore) {
            String[] values = s.split(":");
            if (s.contains(prefix_strikingPower)) {
                resultMap.put(prefix_strikingPower, Integer.parseInt(values[1].replace(" ", "")));
            } else if (s.contains(RPGItemReference.prefix_defensivePower)) {
                resultMap.put(prefix_defensivePower, Integer.parseInt(values[1].replace(" ", "")));
            } else if (s.contains(RPGItemReference.prefix_criticalChance)) {
                values[1] = values[1].replace("%", "");
                resultMap.put(prefix_criticalChance, Integer.parseInt(values[1].replace(" ", "")));
            } else if (s.contains(RPGItemReference.prefix_moveSpeed)) {
                resultMap.put(prefix_moveSpeed, Integer.parseInt(values[1].replace(" ", "")));
            } else if (s.contains(RPGItemReference.prefix_lifeSteal)) {
                resultMap.put(prefix_lifeSteal, Integer.parseInt(values[1].replace(" ", "")));
            } else if (s.contains(RPGItemReference.prefix_defenseIgnore)) {
                resultMap.put(prefix_defenseIgnore, Integer.parseInt(values[1].replace(" ", "")));
            } else if (s.contains(RPGItemReference.prefix_STR)) {
                resultMap.put(prefix_STR, Integer.parseInt(values[1].replace(" ", "")));
            } else if (s.contains(RPGItemReference.prefix_DEF)) {
                resultMap.put(prefix_DEF, Integer.parseInt(values[1].replace(" ", "")));
            } else if (s.contains(RPGItemReference.prefix_DEX)) {
                resultMap.put(prefix_DEX, Integer.parseInt(values[1].replace(" ", "")));
            } else if (s.contains(RPGItemReference.prefix_HP)) {
                resultMap.put(prefix_HP, Integer.parseInt(values[1].replace(" ", "")));
            } else if (s.contains(RPGItemReference.prefix_levelLimit)) {
                resultMap.put(prefix_levelLimit, Integer.parseInt(values[1].replace(" ", "")));
            }
        }

        return resultMap;
    }

    public static int extractValueFromRPGITEM(ItemStack item, String type) {
        if(item == null) {
            return 0;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) {
            return 0;
        }
        List<String> itemLore = itemMeta.getLore();
        if(itemLore == null) {
            return 0;
        }

        for (String s : itemLore) {
            if (s.contains(type)) {
                List<String> values = Arrays.asList(s.split(":"));
                values.set(1, values.get(1).replace("%", ""));
                return Integer.parseInt(values.get(1).replace(" ", ""));
            }
        }
        return 0;
    }
    public static String extractLoreFromRPGITEM(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        List<String> itemLore = itemMeta.getLore();

        StringBuilder lore = new StringBuilder();
        for(int i = 0; (i < itemLore.size()) && (!itemLore.get(i).contains("●")); i++) {
            lore.append(itemLore.get(i));
            lore.append("#");
        }

        return lore.toString();
    }

}
