package com.popogonry.lupinus.team;

import com.popogonry.lupinus.GUI;
import com.popogonry.lupinus.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TeamEvent implements Listener {
    @EventHandler
    public static void playerAttackEvent(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            // 공격자와 대상자의 팀이 같고, 공격자가 팀이 없지 않을때( 즉, 두 사람이 팀이 없을때 둘다 null 로 같을수 있기 때문에 이를 방지 )
            if(TeamReference.getPlayersTeam((Player) event.getDamager()) == TeamReference.getPlayersTeam((Player) event.getEntity()) && TeamReference.getPlayersTeam((Player) event.getDamager()) != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void TeamGUIClickEvent(InventoryClickEvent event) {
        if (((event.getView().getTitle().contains(Reference.prefix_team))) &&
                (event.getCurrentItem() != null) && (event.getCurrentItem().getType() != Material.AIR)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if(event.getView().getTitle().contains("Detail")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(event.getClickedInventory().getItem(11).getItemMeta().getDisplayName().replace("§6§l", ""));
                if(event.getCurrentItem().getItemMeta().getDisplayName().contains("정보보기")) {

                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("팀장위임")) {
                    GUI.openSecondCheckGUI(player, new ItemStack(Material.NETHER_STAR), "§e§l" + target.getName() +" §b§l팀장위임", "§6§l" + target.getName() + "§e§l님에게 §b§l팀장위임§e§l하기");

                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("팀추방")) {
                    GUI.openSecondCheckGUI(player, new ItemStack(Material.SKULL_ITEM), "§e§l" + target.getName() +" §c§l팀추방", "§6§l" + target.getName() + "§e§l님 §c§l팀추방§e§l하기");
                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("돌아가기")) {
                    player.openInventory(Reference.recentInventoryHashMap.get(player.getUniqueId()));
                }
            }
            else {
                String[] pageString = event.getInventory().getItem(49).getItemMeta().getDisplayName().split("/");
                pageString[0] = pageString[0].replace("§6§l", "");
                int page = Integer.parseInt(pageString[0]) - 1;
                if(9 <= event.getRawSlot() && event.getRawSlot() <= 44 && !event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ") && event.isRightClick()) {
                    String targetName = event.getCurrentItem().getItemMeta().getDisplayName().replace("§6§l", "");
                    targetName = targetName.replace("§e§l", "");
                    TeamGUI.openPlayerDetailGUI(player, Bukkit.getServer().getOfflinePlayer(targetName));
                }
                else if (45 <= event.getRawSlot() && event.getRawSlot() <= 53) {
                    if(event.getRawSlot() == 48 && event.getCurrentItem().getItemMeta().getDisplayName().contains("◀ 이전장")) {
                        TeamGUI.openTeamGUI(player, (page - 1));
                    }
                    else if(event.getRawSlot() == 50 && event.getCurrentItem().getItemMeta().getDisplayName().contains("▶ 다음장")) {
                        TeamGUI.openTeamGUI(player, (page + 1));
                    }
                }
            }
        }
    }



}
