package com.popogonry.lupinus;

import com.popogonry.lupinus.level.LevelReference;
import com.popogonry.lupinus.player.PlayerReference;
import com.popogonry.lupinus.stat.StatReference;
import com.popogonry.lupinus.tribe.TribeReference;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Reference {
    public static String prefix_normal = "§r§f「§6§lLupinus§r§f」§7 §r";
    public static String prefix_opMessage = "§r§f「§6§lLupinus §f- §bOP Message§r§f」§7 §r";
    public static String prefix_error = "§r§c「§lLupinus」 §7";
    public static String prefix_dataError = "§r§c「§lLupinus - Data Error」 §7";
    public static String prefix_dataLoad = "§r§f「§6§lLupinus §f- §b§lData Load§r§f」§7 §r";
    public static String prefix_dataSave = "§r§f「§6§lLupinus §f- §b§lData Save§r§f」§7 §r";
    public static String prefix_team = "§r§f「§6§lLupinus §f- §b§lTeam§r§f」§7 §r";
    public static String prefix_level = "§r§f「§6§lLupinus §f- §e§lLevel§r§f」§7 §r";
    public static String prefix_tribe = "§r§f「§6§lLupinus §f- §a§lTribe§r§f」§7 §r";
    public static String prefix_region = "§r§f「§6§lLupinus §f- §6§lRegion§r§f」§7 §r";
    public static String COMMAND_CMD_NOT_PRACTICE_MESSAGE = "버킷창에서는 실행되지 않으니, 게임에서 명령어를 실행해주시기 바랍니다";


    public static int GUI_BACKGROUND_ITEM = 160;


    public static HashMap<UUID, String> chatModeHashMap = new HashMap<>();

    public static HashMap<UUID, Inventory> recentInventoryHashMap = new HashMap<>();
    public static HashMap<UUID, Long> actionDelay = new HashMap<>();

    public static DataManager configDM = new DataManager("config", "");
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
    public static List<Integer> configLevelDataList = new ArrayList<>();
    public static HashMap<String, String> configTribeItemDataHashMap = new HashMap<String, String>(4){{
        put("Douglas", "0");
        put("Florence", "0");
        put("Justitia", "0");
        put("Hiro", "0");
    }};
    public static String configRegionItem = "0";

    public static boolean isInteger(String strValue) {
        try {
            Integer.parseInt(strValue);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean saveConfigData() {
        for(String key : configDataHashMap.keySet()) {
            configDM.getConfig().set(key, configDataHashMap.get(key));
        }

        configDM.getConfig().set("maxExp-to-levelUp", configLevelDataList);

        for(String tribe : configTribeItemDataHashMap.keySet()) {
            configDM.getConfig().set(tribe, configTribeItemDataHashMap.get(tribe));
        }

        configDM.getConfig().set("regionWand", configRegionItem);

        configDM.saveConfig();
        return true;
    }

    public static void loadConfigData() {
        configDataHashMap.replaceAll((k, v) -> Double.valueOf(String.valueOf(configDM.getConfig().get(k))));

        configLevelDataList = (List<Integer>) configDM.getConfig().get("maxExp-to-levelUp");

        for(String tribe : configTribeItemDataHashMap.keySet()) {
            configTribeItemDataHashMap.put(tribe, (String) configDM.getConfig().get(tribe));
        }

        configRegionItem = (String) configDM.getConfig().get("regionWand");

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_dataLoad + "플러그인 config 로드 완료" + " (" + configDM.fileName + ")");
    }


    public static void sendDataToClient(Player player) {
        int maxExp;
        String tribe;

        try {
            maxExp = Reference.configLevelDataList.get(LevelReference.playerLevelHashMap.get(player.getUniqueId()));
        }
        catch (Exception e) {
            maxExp = 0;
        }
        if (TribeReference.playerTribeHashMap.get(player.getUniqueId()) != null) {
            tribe = TribeReference.playerTribeHashMap.get(player.getUniqueId());
        }
        else {
            tribe = "None";
        }

        String command = String.format("loadstat %d %d %d %.1f%% %d %d %d %d %d %d %d %s %d %d %d %d %s",
                (int) PlayerReference.calculationHealthPoints(player, player.getItemInHand()),
                (int) PlayerReference.calculationAttackPower(player),
                (int) PlayerReference.calculationDefensivePower(player),
                PlayerReference.calculationCriticalChance(player),
                (int) (PlayerReference.calculationMoveSpeed(player) * 1000),
                (int) StatReference.statHashMap.get(player.getUniqueId()).get("STATPOINT"),
                (int) StatReference.getTotalStat(player),
                (int) StatReference.statHashMap.get(player.getUniqueId()).get("HP"),
                (int) StatReference.statHashMap.get(player.getUniqueId()).get("STR"),
                (int) StatReference.statHashMap.get(player.getUniqueId()).get("DEF"),
                (int) StatReference.statHashMap.get(player.getUniqueId()).get("DEX"),
                TribeReference.changeTribeEnToKo(tribe),
                TribeReference.getValueTribe(tribe),
                LevelReference.playerLevelHashMap.get(player.getUniqueId()),
                LevelReference.playerExpHashMap.get(player.getUniqueId()),
                maxExp,
                player.getName()
        );

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
//        player.performCommand(command);
//        player.sendMessage(command);

    }
}


















