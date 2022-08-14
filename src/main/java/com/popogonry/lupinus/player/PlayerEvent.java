package com.popogonry.lupinus.player;

import com.popogonry.lupinus.item.rpgitem.RPGItemReference;
import com.popogonry.lupinus.stat.StatReference;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerEvent implements Listener {
    @EventHandler
    public static void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        StatReference.PlayerJoinStatSetting(player);
    }
    @EventHandler
    public static void playerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        StatReference.saveStatData(player);
    }
    @EventHandler
    public static void playerRPGItemHeldEvent(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

//        float moveSpeed = (float) PlayerReference.calculationMoveSpeed(player, item);
//        player.setWalkSpeed(moveSpeed);
//        player.setFlySpeed(moveSpeed);

        player.setMaxHealth(PlayerReference.calculationHealthPoints(player, item));
    }
    @EventHandler
    public static void playerInventoryCloseEvent(InventoryCloseEvent event) {
        if(event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            ItemStack item = player.getItemInHand();
            player.setMaxHealth(PlayerReference.calculationHealthPoints(player, item));
        }
    }
    @EventHandler
    public static void playerLevelChangeEvent(PlayerLevelChangeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        player.setMaxHealth(PlayerReference.calculationHealthPoints(player, item));
    }

    @EventHandler
    public static void playerAttackEvent(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getDamager();
            LivingEntity victim = (LivingEntity) event.getEntity();
            event.setDamage(PlayerReference.calculationDamage(attacker, victim));

            // 흡혈
            attacker.setHealth(Math.min(attacker.getHealth() + RPGItemReference.extractValueFromRPGITEM(attacker.getEquipment().getItemInMainHand(), RPGItemReference.prefix_lifeSteal), attacker.getMaxHealth()));

        }
    }
    @EventHandler
    public static void playerDamageEvent(EntityDamageEvent event) {
    }
}
