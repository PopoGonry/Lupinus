package com.popogonry.lupinus.player;

import com.popogonry.lupinus.Reference;
import com.popogonry.lupinus.item.rpgitem.RPGItemReference;
import com.popogonry.lupinus.level.LevelReference;
import com.popogonry.lupinus.stat.StatReference;
import com.popogonry.lupinus.tribe.TribeReference;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerEvent implements Listener {
    @EventHandler
    public static void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        StatReference.playerJoinStatSetting(player);
        LevelReference.playerJoinLevelSetting(player);
        TribeReference.loadPlayerTribeData(player);
        Reference.sendDataToClient(player);
    }
    @EventHandler
    public static void playerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        StatReference.saveStatData(player);
        LevelReference.saveLevelData(player);
        TribeReference.savePlayerTribeData(player);
    }
    @EventHandler
    public static void playerRPGItemHeldEvent(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

//        float moveSpeed = (float) PlayerReference.calculationMoveSpeed(player, item);
//        player.setWalkSpeed(moveSpeed);
//        player.setFlySpeed(moveSpeed);

        player.setMaxHealth(PlayerReference.calculationHealthPoints(player, item));
        double moveSpeed = PlayerReference.calculationMoveSpeed(player, item);
        player.setWalkSpeed((float) moveSpeed);
        player.setFlySpeed((float) moveSpeed);
        player.setFoodLevel(19);
    }
    @EventHandler
    public static void playerInventoryCloseEvent(InventoryCloseEvent event) {
        if(event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            ItemStack item = player.getItemInHand();
            player.setMaxHealth(PlayerReference.calculationHealthPoints(player, item));
            double moveSpeed = PlayerReference.calculationMoveSpeed(player, item);
            player.setWalkSpeed((float) moveSpeed);
            player.setFlySpeed((float) moveSpeed);
            player.setFoodLevel(19);
        }
    }
    @EventHandler
    public static void playerFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        if(event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void playerAttackEvent(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity) {
            long start = System.currentTimeMillis();

            LivingEntity attacker = (LivingEntity) event.getDamager();
            LivingEntity victim = (LivingEntity) event.getEntity();


            double damage = PlayerReference.calculationDamage(attacker, victim);
            if(!(attacker instanceof Player)) {
                damage += event.getDamage();
            }
            if (damage < 0) {
                damage = 0;
            }

            event.setDamage(damage);



            // 흡혈
            attacker.setHealth(Math.min(attacker.getHealth() + RPGItemReference.extractValueFromRPGITEM(attacker.getEquipment().getItemInMainHand(), RPGItemReference.prefix_lifeSteal), attacker.getMaxHealth()));
//            attacker.sendMessage(String.valueOf(event.getFinalDamage()));
//            victim.sendMessage(String.valueOf(event.getFinalDamage()));

            long end = System.currentTimeMillis();
            attacker.sendMessage("수행시간: " + (end - start) + " ms");
        }
        else if(event.getEntity() instanceof LivingEntity && !(event.getDamager() instanceof LivingEntity)) {
            LivingEntity victim = (LivingEntity) event.getEntity();
            double damage = event.getDamage() - PlayerReference.calculationDefensivePower(victim);
            if(damage < 0) damage = 0;
            event.setDamage(damage);
        }
    }
}
