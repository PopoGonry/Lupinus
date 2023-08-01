package com.popogonry.lupinus;

import com.popogonry.lupinus.chatClick.ChatClickEvent;
import com.popogonry.lupinus.item.itemBan.ItemBanCommand;
import com.popogonry.lupinus.item.itemBan.ItemBanEvent;
import com.popogonry.lupinus.item.itemBan.ItemBanReference;
import com.popogonry.lupinus.item.rpgitem.RPGItemCommand;
import com.popogonry.lupinus.item.rpgitem.RPGItemEvent;
import com.popogonry.lupinus.level.LevelCommand;
import com.popogonry.lupinus.level.LevelEvent;
import com.popogonry.lupinus.level.LevelReference;
import com.popogonry.lupinus.player.PlayerCommand;
import com.popogonry.lupinus.player.PlayerEvent;
import com.popogonry.lupinus.region.RegionCommand;
import com.popogonry.lupinus.region.RegionEvent;
import com.popogonry.lupinus.region.RegionReference;
import com.popogonry.lupinus.stat.StatCommand;
import com.popogonry.lupinus.stat.StatEvent;
import com.popogonry.lupinus.stat.StatReference;
import com.popogonry.lupinus.team.TeamCommand;
import com.popogonry.lupinus.team.TeamEvent;
import com.popogonry.lupinus.team.TeamReference;
import com.popogonry.lupinus.tribe.TribeCommand;
import com.popogonry.lupinus.tribe.TribeEvent;
import com.popogonry.lupinus.tribe.TribeReference;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public final class Lupinus extends JavaPlugin implements Listener {

    RPGItemEvent rpgItemEvent = new RPGItemEvent();
    StatEvent statEvent = new StatEvent();
    LupinusEvent lupinusEvent = new LupinusEvent();
    PlayerEvent playerEvent = new PlayerEvent();
    TeamEvent teamEvent = new TeamEvent();
    ItemBanEvent itemBanEvent = new ItemBanEvent();
    ChatClickEvent chatClickEvent = new ChatClickEvent();
    LevelEvent levelEvent = new LevelEvent();
    TribeEvent tribeEvent = new TribeEvent();
    RegionEvent regionEvent = new RegionEvent();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this.rpgItemEvent, this);
        getServer().getPluginManager().registerEvents(this.statEvent, this);
        getServer().getPluginManager().registerEvents(this.lupinusEvent, this);
        getServer().getPluginManager().registerEvents(this.playerEvent, this);
        getServer().getPluginManager().registerEvents(this.teamEvent, this);
        getServer().getPluginManager().registerEvents(this.itemBanEvent, this);
        getServer().getPluginManager().registerEvents(this.chatClickEvent, this);
        getServer().getPluginManager().registerEvents(this.levelEvent, this);
        getServer().getPluginManager().registerEvents(this.tribeEvent, this);
        getServer().getPluginManager().registerEvents(this.regionEvent, this);

        getCommand("lupinus").setExecutor(new LupinusCommand());
        getCommand("RPGItem").setExecutor(new RPGItemCommand());
        getCommand("stat").setExecutor(new StatCommand());
        getCommand("player").setExecutor(new PlayerCommand());
        getCommand("team").setExecutor(new TeamCommand());
        getCommand("itemban").setExecutor(new ItemBanCommand());
        getCommand("level").setExecutor(new LevelCommand());
        getCommand("invload").setExecutor(new InvLoadCommnad());
        getCommand("tribe").setExecutor(new TribeCommand());
        getCommand("region").setExecutor(new RegionCommand());

        File file = new File(this.getDataFolder() + "/");
        if(!file.exists()){
            file.mkdir();
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "플러그인 폴더 생성");
        }

        // DataManager Set Plugin
        StatReference.statDM.setPlugin(this);
        Reference.configDM.setPlugin(this);
        TeamReference.teamDM.setPlugin(this);
        ItemBanReference.itemBanDM.setPlugin(this);
        LevelReference.levelDM.setPlugin(this);
        TribeReference.playerTribeDM.setPlugin(this);
        RegionReference.regionDM.setPlugin(this);
        TribeReference.tribeRegionDM.setPlugin(this);


        // Load Data
        Reference.loadConfigData();
        TeamReference.loadTeamData();
        RegionReference.loadRegionData();
        TribeReference.loadTribeRegionData();
        ItemBanReference.loadBanItemData();
        StatReference.onlinePlayerLoadStatData();
        LevelReference.onlinePlayerLoadLevelData();
        TribeReference.onlinePlayerLoadTribeData();
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "플러그인이 §a활성화§r되었습니다. §r| 포포곤리(PopoGonry)");
    }
    @Override
    public void onDisable() {
        // Save Data
        //Reference.saveConfigLoad();
        TeamReference.saveTeamData();
        RegionReference.saveRegionData();
        TribeReference.saveTribeRegionData();
        ItemBanReference.saveBanItemData();
        StatReference.onlinePlayerSaveStatData();
        LevelReference.onlinePlayerSaveLevelData();
        TribeReference.onlinePlayerSaveTribeData();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "플러그인이 §c비활성화§r되었습니다. §r| 포포곤리(PopoGonry)");
    }

}
