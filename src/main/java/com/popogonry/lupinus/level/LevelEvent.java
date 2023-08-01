package com.popogonry.lupinus.level;

import com.popogonry.lupinus.Reference;
import com.popogonry.lupinus.item.rpgitem.RPGItemReference;
import com.popogonry.lupinus.player.PlayerReference;
import com.popogonry.lupinus.stat.StatReference;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class LevelEvent implements Listener {
    @EventHandler
    public static void playerExpChangeEvent(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        LevelReference.playerExpHashMap.put(player.getUniqueId(), LevelReference.playerExpHashMap.get(player.getUniqueId()) + event.getAmount());
        LevelReference.levelUpProcess(player);
        Reference.sendDataToClient(player);
    }
}
