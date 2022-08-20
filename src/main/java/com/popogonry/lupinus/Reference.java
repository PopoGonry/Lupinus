package com.popogonry.lupinus;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

public class Reference {
    public static String prefix_normal = "§r§f「§6§lLupinus§r§f」§7 §r";
    public static String prefix_opMessage = "§r§f「§6§lLupinus §f- §bOP Message§r§f」§7 §r";
    public static String prefix_error = "§r§c「§lLupinus」 §7";
    public static String prefix_dataError = "§r§c「§lLupinus - Data Error」 §7";
    public static String prefix_dataLoad = "§r§f「§6§lLupinus §f- §b§lData Load§r§f」§7 §r";
    public static String prefix_dataSave = "§r§f「§6§lLupinus §f- §b§lData Save§r§f」§7 §r";
    public static String prefix_team = "§r§f「§6§lLupinus §f- §b§lTeam§r§f」§7 §r";
    public static String COMMAND_CMD_NOT_PRACTICE_MESSAGE = "버킷창에서는 실행되지 않으니, 게임에서 명령어를 실행해주시기 바랍니다";


    public static int GUI_BACKGROUND_ITEM = 160;


    public static HashMap<UUID, String> chatModeHashMap = new HashMap<>();

    public static HashMap<UUID, Inventory> recentInventoryHashMap = new HashMap<>();

    public static DataManager configDM = new DataManager("config", "");


    public static boolean saveConfigLoad() {
        for(String key : configDataHashMap.keySet()) {
            configDM.getConfig().set(key, configDataHashMap.get(key));
        }
        configDM.saveConfig();
        return true;
    }

    public static void loadConfigLoad() {
        configDataHashMap.replaceAll((k, v) -> Double.valueOf(String.valueOf(configDM.getConfig().get(k))));
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataLoad + "플러그인 config 로드 완료" + " (" + configDM.fileName + ")");
    }

    public static HashMap<String, Double> configDataHashMap = new HashMap<String, Double>(){{
        put("STR-strikingPower-coefficient", (double) 0);
        put("DEF-defensivePower-coefficient", (double) 0);
        put("DEX-moveSpeed-coefficient", (double) 0);
        put("DEX-criticalChance-coefficient", (double) 0);
        put("HP-healthPoints-coefficient", (double) 0);
        put("statpoint-per-level", (double) 0);
        put("criticalDamage-coefficient", (double) 0);
        put("default-healthPoints", (double) 0);
        put("healthPoints-per-level", (double) 0);
    }};

    public static String uuidToName(String uuid) {
        return new String();
    }
    public static String nameToUUID(String name) {

        return new String();
    }

}
