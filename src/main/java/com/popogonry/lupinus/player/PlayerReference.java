package com.popogonry.lupinus.player;

import com.popogonry.lupinus.Reference;
import com.popogonry.lupinus.item.rpgitem.RPGItemReference;
import com.popogonry.lupinus.level.LevelReference;
import com.popogonry.lupinus.stat.StatReference;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerReference {

    // 이름(name) / 설명(lore) / 공격력(strikingPower) / 방어력(defensivePower) / 치명타확률(criticalChance) / 이동속도(moveSpeed) / 흡혈(lifeSteal) /
    // 방어력무시(defenseIgnore) / 공격(STR) / 방어(DEF) / 이속(DEX) / 체력(HP) / 레벨제한(levelLimit)
    //(공격력 + STR*스텟계수) * 치명타계수 - (방어력 + DEF*스텟계수)
    public static double calculationDamage(LivingEntity attacker, LivingEntity victim) {

        List<ItemStack> attackerItemList = new ArrayList<>(Arrays.asList(
                attacker.getEquipment().getItemInMainHand(),
                attacker.getEquipment().getHelmet(),
                attacker.getEquipment().getChestplate(),
                attacker.getEquipment().getLeggings(),
                attacker.getEquipment().getBoots()
        ));

        List<ItemStack> victimItemList = new ArrayList<>(Arrays.asList(
                victim.getEquipment().getItemInMainHand(),
                victim.getEquipment().getHelmet(),
                victim.getEquipment().getChestplate(),
                victim.getEquipment().getLeggings(),
                victim.getEquipment().getBoots()
        ));


        // attacker
        double strikingPower = 0;
        double criticalChance = 0;
        double criticalDamageCoefficient = 1;
        double defenseIgnore = 0;
        int STR = 0;
        int DEX = 0;

        // victim
        double defensivePower = 0;
        int DEF = 0;

        if(attacker instanceof Player) {
            // attacker
            STR = (int) StatReference.statHashMap.get(attacker.getUniqueId()).get("STR");
            DEX = (int) StatReference.statHashMap.get(attacker.getUniqueId()).get("DEX");
            // victim
        }
        if(victim instanceof Player) {
            DEF = (int) StatReference.statHashMap.get(victim.getUniqueId()).get("DEF");
        }


        // 공격력 치명타확률 흡혈 방무 STR DEX 레벨제한
        for(ItemStack item : attackerItemList) {
            HashMap<String, Integer> valueMap = RPGItemReference.extractAllFromRPGItem(item);
            if(attacker instanceof Player) {
                Player player = (Player) attacker;
                if(valueMap.get(RPGItemReference.prefix_levelLimit) > player.getLevel()) {
                    //player.sendMessage(item.toString() + " 레벨부족");
                    continue;
                }
            }
            strikingPower += valueMap.get(RPGItemReference.prefix_strikingPower);
            criticalChance += valueMap.get(RPGItemReference.prefix_criticalChance);
            defenseIgnore += valueMap.get(RPGItemReference.prefix_defenseIgnore);
            STR += valueMap.get(RPGItemReference.prefix_STR);
            DEX += valueMap.get(RPGItemReference.prefix_DEX);
        }

        // 방어력 DEF 레벨제한
        for(ItemStack item : victimItemList) {
            HashMap<String, Integer> valueMap = RPGItemReference.extractAllFromRPGItem(item);
            if(victim instanceof Player) {
                Player player = (Player) victim;
                if(valueMap.get(RPGItemReference.prefix_levelLimit) > player.getLevel()) {
                    //player.sendMessage(item.toString() + " 레벨부족");
                    continue;
                }
            }
            defensivePower += valueMap.get(RPGItemReference.prefix_defensivePower);
            DEF += valueMap.get(RPGItemReference.prefix_DEF);
        }

        //(공격력 + STR*스텟계수) * 치명타계수 - (방어력 + DEF*스텟계수 - 방어력무시)

        Random random = new Random();
        if(random.nextInt(100000) < (criticalChance + (DEX * Reference.configDataHashMap.get("DEX-criticalChance-coefficient")))*1000) {
            criticalDamageCoefficient = Reference.configDataHashMap.get("criticalDamage-coefficient");
            attacker.sendMessage(Reference.prefix_normal + "크리티컬!");
        }

        double resultDamage = ((strikingPower + (STR * Reference.configDataHashMap.get("STR-strikingPower-coefficient")))
                * criticalDamageCoefficient);
        resultDamage -= (defensivePower + (DEF * Reference.configDataHashMap.get("DEF-defensivePower-coefficient")) - defenseIgnore);

//        attacker.sendMessage(String.valueOf(strikingPower + (STR * Reference.configDataHashMap.get("STR-strikingPower-coefficient"))));
//        attacker.sendMessage(String.valueOf(criticalDamageCoefficient));
//        attacker.sendMessage(String.valueOf(defensivePower + (DEF * Reference.configDataHashMap.get("DEF-defensivePower-coefficient")) - defenseIgnore));

//        attacker.sendMessage(String.valueOf(resultDamage));
        return resultDamage;
    }
    public static double calculationAttackPower(Player player) {
        List<ItemStack> playerItemList = new ArrayList<>(Arrays.asList(
                player.getEquipment().getHelmet(),
                player.getEquipment().getChestplate(),
                player.getEquipment().getLeggings(),
                player.getEquipment().getBoots()
        ));

        // attacker
        double strikingPower = 0;
        int STR = (int) StatReference.statHashMap.get(player.getUniqueId()).get("STR");

        // 공격력 치명타확률 흡혈 방무 STR DEX 레벨제한
        for(ItemStack item : playerItemList) {
            int levelLimit = RPGItemReference.extractValueFromRPGITEM(item, RPGItemReference.prefix_levelLimit);
            if(levelLimit > player.getLevel()) {
                //player.sendMessage(item.toString() + " 레벨부족");
                continue;
            }
            strikingPower += RPGItemReference.extractValueFromRPGITEM(item, RPGItemReference.prefix_strikingPower);
            STR += RPGItemReference.extractValueFromRPGITEM(item, RPGItemReference.prefix_STR);
        }

        double result =
                ((strikingPower + (STR * Reference.configDataHashMap.get("STR-strikingPower-coefficient"))));

        return result;
    }
    public static double calculationDefensivePower(LivingEntity entity) {
        List<ItemStack> victimItemList = new ArrayList<>(Arrays.asList(
                entity.getEquipment().getItemInMainHand(),
                entity.getEquipment().getHelmet(),
                entity.getEquipment().getChestplate(),
                entity.getEquipment().getLeggings(),
                entity.getEquipment().getBoots()
        ));

        // victim
        double defensivePower = 0;
        int DEF = 0;

        if(entity instanceof Player) {
            DEF = (int) StatReference.statHashMap.get(entity.getUniqueId()).get("DEF");
        }

        // 방어력 DEF 레벨제한
        for(ItemStack item : victimItemList) {
            HashMap<String, Integer> valueMap = RPGItemReference.extractAllFromRPGItem(item);
            if(entity instanceof Player) {
                Player player = (Player) entity;
                if(valueMap.get(RPGItemReference.prefix_levelLimit) > player.getLevel()) {
                    //player.sendMessage(item.toString() + " 레벨부족");
                    continue;
                }
            }
            defensivePower += valueMap.get(RPGItemReference.prefix_defensivePower);
            DEF += valueMap.get(RPGItemReference.prefix_DEF);
        }

        double result = defensivePower + (DEF * Reference.configDataHashMap.get("DEF-defensivePower-coefficient"));


        return result;
    }
    public static double calculationCriticalChance(Player player) {
        List<ItemStack> playerItemList = new ArrayList<>(Arrays.asList(
                player.getEquipment().getHelmet(),
                player.getEquipment().getChestplate(),
                player.getEquipment().getLeggings(),
                player.getEquipment().getBoots()
        ));

        // attacker
        double criticalChance = 0;
        int DEX = (int) StatReference.statHashMap.get(player.getUniqueId()).get("DEX");

        // 공격력 치명타확률 흡혈 방무 STR DEX 레벨제한
        for(ItemStack item : playerItemList) {
            int levelLimit = RPGItemReference.extractValueFromRPGITEM(item, RPGItemReference.prefix_levelLimit);
            if(levelLimit > player.getLevel()) {
                //player.sendMessage(item.toString() + " 레벨부족");
                continue;
            }
            criticalChance += RPGItemReference.extractValueFromRPGITEM(item, RPGItemReference.prefix_criticalChance);
            DEX += RPGItemReference.extractValueFromRPGITEM(item, RPGItemReference.prefix_DEX);
        }

        double result =
                ((criticalChance + (DEX * Reference.configDataHashMap.get("DEX-criticalChance-coefficient"))));

        return result;
    }
    public static double calculationMoveSpeed(Player player) {
        List<ItemStack> attackerItemList = new ArrayList<>(Arrays.asList(
                player.getEquipment().getHelmet(),
                player.getEquipment().getChestplate(),
                player.getEquipment().getLeggings(),
                player.getEquipment().getBoots()
        ));

        // attacker
        double moveSpeed = 0;
        int DEX = (int) StatReference.statHashMap.get(player.getUniqueId()).get("DEX");

        // 공격력 치명타확률 흡혈 방무 STR DEX 레벨제한
        for(ItemStack item : attackerItemList) {
            int levelLimit = RPGItemReference.extractValueFromRPGITEM(item, RPGItemReference.prefix_levelLimit);
            if(levelLimit > player.getLevel()) {
                //player.sendMessage(item.toString() + " 레벨부족");
                continue;
            }
            moveSpeed += RPGItemReference.extractValueFromRPGITEM(item, RPGItemReference.prefix_moveSpeed);
            DEX += RPGItemReference.extractValueFromRPGITEM(item, RPGItemReference.prefix_DEX);
        }

        double result =
                ((moveSpeed + (DEX * Reference.configDataHashMap.get("DEX-moveSpeed-coefficient"))));

        return result;
    }


    public static double calculationMoveSpeed(Player player, ItemStack ItemInHand) {

        List<ItemStack> playerItemList = new ArrayList<>(Arrays.asList(
                ItemInHand,
                player.getEquipment().getHelmet(),
                player.getEquipment().getChestplate(),
                player.getEquipment().getLeggings(),
                player.getEquipment().getBoots()
        ));

        // player
        double moveSpeed = 0.2 * 1000;
        int DEX = (int) StatReference.statHashMap.get(player.getUniqueId()).get("DEX");


        for (ItemStack item : playerItemList) {
            HashMap<String, Integer> valueMap = RPGItemReference.extractAllFromRPGItem(item);
            if (valueMap.get(RPGItemReference.prefix_levelLimit) > player.getLevel()) {
                //player.sendMessage(item.toString() + " 레벨부족");
                continue;
            }
            moveSpeed += valueMap.get(RPGItemReference.prefix_moveSpeed);
            DEX += valueMap.get(RPGItemReference.prefix_DEX);
        }

        double resultMoveSpeed = (moveSpeed/1000) + (DEX * Reference.configDataHashMap.get("DEX-moveSpeed-coefficient"));
        if(resultMoveSpeed > 1) {
            resultMoveSpeed = 1;
        }
        //player.sendMessage(String.valueOf(resultMoveSpeed));
        return resultMoveSpeed;
    }

    public static double calculationHealthPoints(Player player, ItemStack ItemInHand) {

        List<ItemStack> playerItemList = new ArrayList<>(Arrays.asList(
                ItemInHand,
                player.getEquipment().getHelmet(),
                player.getEquipment().getChestplate(),
                player.getEquipment().getLeggings(),
                player.getEquipment().getBoots()
        ));

        // player
        double healthPoints = Reference.configDataHashMap.get("default-healthPoints")
                + (Reference.configDataHashMap.get("healthPoints-per-level") * LevelReference.playerLevelHashMap.get(player.getUniqueId()));
        int HP = (int) StatReference.statHashMap.get(player.getUniqueId()).get("HP");

        for (ItemStack item : playerItemList) {
            HashMap<String, Integer> valueMap = RPGItemReference.extractAllFromRPGItem(item);
            if (valueMap.get(RPGItemReference.prefix_levelLimit) > player.getLevel()) {
                //player.sendMessage(item.toString() + " 레벨부족");
                continue;
            }
            HP += valueMap.get(RPGItemReference.prefix_HP);
        }

        double resultHealthPoints = healthPoints + (HP * Reference.configDataHashMap.get("HP-healthPoints-coefficient"));
//        player.sendMessage(String.valueOf(resultHealthPoints));
        return resultHealthPoints;
    }

}
