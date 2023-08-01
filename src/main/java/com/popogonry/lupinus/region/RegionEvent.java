package com.popogonry.lupinus.region;

import com.popogonry.lupinus.Reference;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class RegionEvent implements Listener {
    @EventHandler
    public static void PlayerUseTribeItemEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = new ItemStack(event.getPlayer().getItemInHand());

            String[] itemDataArray = new String[2];
            String value;

            value = Reference.configRegionItem;
            if(value.contains(":")) {
                itemDataArray = value.split(":");
            }
            else {
                itemDataArray[0] = value;
                itemDataArray[1] = "0";
            }
            if(item.getTypeId() == Integer.parseInt(itemDataArray[0])
                    && item.getData().getData() == Byte.valueOf(itemDataArray[1])) {
                event.setCancelled(true);
                if (!Reference.actionDelay.containsKey(player.getUniqueId()) || System.currentTimeMillis() - Reference.actionDelay.get(player.getUniqueId()) < 0)  Reference.actionDelay.put(player.getUniqueId(), System.currentTimeMillis() - 100);
                if(System.currentTimeMillis() - Reference.actionDelay.get(player.getUniqueId()) >= 100) {
                    if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                        RegionReference.playerPosition1HashMap.put(player.getUniqueId(), Arrays.asList(event.getClickedBlock().getLocation().getBlockX(), event.getClickedBlock().getLocation().getBlockY(), event.getClickedBlock().getLocation().getBlockZ()));
                        player.sendMessage(Reference.prefix_region + "1번째 위치가 설정되었습니다 (" + RegionReference.playerPosition1HashMap.get(player.getUniqueId()) + ")");
                    }
                    else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        RegionReference.playerPosition2HashMap.put(player.getUniqueId(), Arrays.asList(event.getClickedBlock().getLocation().getBlockX(), event.getClickedBlock().getLocation().getBlockY(), event.getClickedBlock().getLocation().getBlockZ()));
                        player.sendMessage(Reference.prefix_region + "2번째 위치가 설정되었습니다 (" + RegionReference.playerPosition2HashMap.get(player.getUniqueId()) + ")");
                    }
                    Reference.actionDelay.put(player.getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }
}
