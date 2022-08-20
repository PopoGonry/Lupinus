package com.popogonry.lupinus.team;

import com.popogonry.lupinus.item.rpgitem.RPGItemReference;
import com.popogonry.lupinus.player.PlayerReference;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TeamEvent implements Listener {
    @EventHandler
    public static void playerAttackEvent(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if(TeamReference.getPlayersTeam((Player) event.getDamager()) == TeamReference.getPlayersTeam((Player) event.getEntity()) && TeamReference.getPlayersTeam((Player) event.getDamager()) != null) {
                event.setCancelled(true);
            }
        }
    }
}
