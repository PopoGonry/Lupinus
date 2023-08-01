package com.popogonry.lupinus;

import com.popogonry.lupinus.team.TeamGUI;
import com.popogonry.lupinus.team.TeamReference;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public class LupinusEvent implements Listener {
//    @EventHandler
//    public static void getRawClickSlot(InventoryClickEvent event) {
//        Player player = (Player) event.getWhoClicked();
//        player.sendMessage(String.valueOf(event.getRawSlot()));
//    }
    @EventHandler
    public static void playerInventoryCloseEvent(InventoryCloseEvent event) {
        Reference.recentInventoryHashMap.put(event.getPlayer().getUniqueId(), event.getInventory());
    }

    @EventHandler
    public static void playerSecondCheckGUIClickEvent(InventoryClickEvent event) {
        if(((event.getView().getTitle().contains(Reference.prefix_normal))) &&
                (event.getCurrentItem() != null) && (event.getCurrentItem().getType() != Material.AIR)) {
            if(!event.getView().getTitle().contains("Detail")) {
                Player player = (Player) event.getWhoClicked();
                if(event.getView().getTitle().contains("팀장위임")) {
                    event.setCancelled(true);
                    if(event.getCurrentItem().getItemMeta().getDisplayName().contains("팀장위임")) {
                        String teamName = TeamReference.getPlayersTeam(player);
                        String targetString = event.getCurrentItem().getItemMeta().getDisplayName();
                        targetString = targetString.replace("§6§l", "");
                        targetString = targetString.replace("§e§l님에게 §b§l팀장위임§e§l하기", "");
                        OfflinePlayer target = Bukkit.getOfflinePlayer(targetString);

                        int returnValue = TeamReference.setTeamMaster(target);
                        if(returnValue == 0) {
                            TeamReference.broadcastTeam(teamName, Reference.prefix_team + player.getName() + "님이 " + target.getName() + "님에게 팀장 위임을 하였습니다.");
                            TeamGUI.openTeamGUI(player, 0);
                            TeamReference.reloadTeamGUI(teamName);
                        }
                        else {
                            if(returnValue == 1) {
                                player.sendMessage(Reference.prefix_error + target.getName() + "님이 팀에 없습니다.");
                                player.openInventory(Reference.recentInventoryHashMap.get(player.getUniqueId()));
                            }
                            else {
                                player.sendMessage(Reference.prefix_error + target.getName() + "님은 이미 팀장입니다.");
                                player.openInventory(Reference.recentInventoryHashMap.get(player.getUniqueId()));
                            }
                        }

                    }
                }
                else if(event.getView().getTitle().contains("팀강퇴")) {
                    event.setCancelled(true);
                    if(event.getCurrentItem().getItemMeta().getDisplayName().contains("팀강퇴")) {
                        String teamName = TeamReference.getPlayersTeam(player);
                        String targetString = event.getCurrentItem().getItemMeta().getDisplayName();
                        targetString = targetString.replace("§6§l", "");
                        targetString = targetString.replace("§e§l님 §c§l팀강퇴§e§l하기", "");
                        OfflinePlayer target = Bukkit.getOfflinePlayer(targetString);
                        int returnValue = TeamReference.removeMember(teamName, target);
                        if(returnValue == 0) {
                            TeamReference.broadcastTeam(teamName, Reference.prefix_team + player.getName() + "님이 " + target.getName() + "님을 팀강퇴를 하였습니다. ");
                            if(target.isOnline()) {
                                Player onlineTarget = target.getPlayer();
                                onlineTarget.sendMessage(Reference.prefix_team + "팀에서 강퇴되었습니다.");
                                if(onlineTarget.getOpenInventory().getTitle().contains(teamName)) {
                                    onlineTarget.closeInventory();
                                }
                            }
                            TeamGUI.openTeamGUI(player, 0);
                            TeamReference.reloadTeamGUI(teamName);
                        }
                        else {
                            if(returnValue == 1) {
                                player.sendMessage(Reference.prefix_error + target.getName() + "님은 팀이 존재하지 않습니다.");
                            }
                            else if(returnValue == 2) {
                                player.sendMessage(Reference.prefix_error + teamName + "팀이 존재하지 않습니다.");
                            }
                            else if(returnValue == 3) {
                                player.sendMessage(Reference.prefix_error + "팀에" + target.getName() + "님이 소속되어 있지 않습니다.");
                            }
                            player.openInventory(Reference.recentInventoryHashMap.get(player.getUniqueId()));
                        }
                    }
                }
            }
        }
    }
}
