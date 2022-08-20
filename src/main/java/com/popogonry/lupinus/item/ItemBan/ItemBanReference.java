package com.popogonry.lupinus.item.ItemBan;

import com.popogonry.lupinus.DataManager;
import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class ItemBanReference {
    public static DataManager itemBanDM = new DataManager("itemBan", "/data");
    public static List<ItemStack> banItemList = new ArrayList<>();

    public static boolean loadBanItemData() {
        if(itemBanDM.getConfig().get("data") == null) {
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataError + "아이템밴 데이터 로드 실패" + " (" + itemBanDM.fileName + ")");
            return false;
        }
        else {
            banItemList.clear();
            List<String> list = itemBanDM.getConfig().getStringList("data");
            for(String item : list) {
                String[] data = item.split(":");
                ItemStack resultItem = new MaterialData(Integer.parseInt(data[0]), Byte.parseByte(data[1])).toItemStack(1);
                banItemList.add(resultItem);
            }
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataLoad + "아이템밴 데이터 로드 완료" + " (" + itemBanDM.fileName + ")");
            return true;
        }
    }
    public static boolean saveBanItemData() {
        List<String> list = new ArrayList<>();
        for(ItemStack itemStack : banItemList) {
            String item = itemStack.getTypeId() + ":" + itemStack.getData().getData();
            list.add(item);
        }
        itemBanDM.getConfig().set("data", list);
        itemBanDM.saveConfig();
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataSave + "아이템밴 데이터 세이브 완료" + " (" + itemBanDM.fileName + ")");
        return true;
    }






}
