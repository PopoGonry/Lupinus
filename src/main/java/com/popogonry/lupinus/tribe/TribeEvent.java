package com.popogonry.lupinus.tribe;

import com.popogonry.lupinus.Reference;
import com.popogonry.lupinus.item.itemBan.ItemBanReference;
import com.popogonry.lupinus.region.Region;
import com.popogonry.lupinus.region.RegionReference;
import com.popogonry.lupinus.team.TeamReference;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class TribeEvent implements Listener {
    @EventHandler
    public static void PlayerUseTribeItemEvent(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Player player = event.getPlayer();
                ItemStack item = new ItemStack(event.getPlayer().getItemInHand());

                String[] itemDataArray = new String[2];
                String value;

                for(String tribe : Reference.configTribeItemDataHashMap.keySet()) {
                    value = Reference.configTribeItemDataHashMap.get(tribe);
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

                        TribeReference.playerTribeHashMap.put(player.getUniqueId(), tribe);
                        player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() - 1);
                        player.sendMessage(Reference.prefix_tribe + "『" + TribeReference.changeTribeEnToKo(tribe) + "』에 등록되었습니다.");
                        return;
                    }
            }

        }
    }
    @EventHandler
    public static void playerAttackEvent(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            // 공격자와 대상자의 진영이 같고, 공격자가 진영이 없지 않을때( 즉, 두 사람이 진영이이 없을때 둘다 None 로 같을수 있기 때문에 이를 방지 )
            if(TribeReference.changeTribeKoToEn(TribeReference.playerTribeHashMap.get(event.getEntity().getUniqueId())).equalsIgnoreCase("None")) {
                for(String regionName : TribeReference.tribeRegionHashMap.keySet()) {
                    if(RegionReference.isPlayerInRegion((Player) event.getEntity(), RegionReference.regionHashMap.get(regionName)) && TribeReference.getValueTribe(TribeReference.tribeRegionHashMap.get(regionName)) != 4) {
                        event.setCancelled(true);
                    }
                }
            }
            else {
                for(String regionName : TribeReference.tribeRegionHashMap.keySet()) {
                    if (RegionReference.isPlayerInRegion((Player) event.getEntity(), RegionReference.regionHashMap.get(regionName))) {
                        if (TribeReference.changeTribeKoToEn(TribeReference.tribeRegionHashMap.get(regionName)).equalsIgnoreCase(TribeReference.changeTribeKoToEn(TribeReference.playerTribeHashMap.get(event.getEntity().getUniqueId())))) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
