package com.popogonry.lupinus;

import com.popogonry.lupinus.item.rpgitem.RPGItemCommand;
import com.popogonry.lupinus.item.rpgitem.RPGItemEvent;
import com.popogonry.lupinus.player.PlayerCommand;
import com.popogonry.lupinus.player.PlayerEvent;
import com.popogonry.lupinus.stat.StatCommand;
import com.popogonry.lupinus.stat.StatEvent;
import com.popogonry.lupinus.stat.StatReference;
import com.popogonry.lupinus.team.TeamCommand;
import com.popogonry.lupinus.team.TeamEvent;
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
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this.rpgItemEvent, this);
        getServer().getPluginManager().registerEvents(this.statEvent, this);
        getServer().getPluginManager().registerEvents(this.lupinusEvent, this);
        getServer().getPluginManager().registerEvents(this.playerEvent, this);
        getServer().getPluginManager().registerEvents(this.teamEvent, this);
        getCommand("lupinus").setExecutor(new LupinusCommand());
        getCommand("RPGItem").setExecutor(new RPGItemCommand());
        getCommand("stat").setExecutor(new StatCommand());
        getCommand("player").setExecutor(new PlayerCommand());
        getCommand("team").setExecutor(new TeamCommand());

        File file = new File(this.getDataFolder() + "/");
        if(!file.exists()){
            file.mkdir();
            Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "플러그인 폴더 생성");
        }


        StatReference.statDM.setPlugin(this);
        Reference.configDM.setPlugin(this);

        Reference.loadConfigLoad();
        StatReference.onlinePlayerLoadStatData();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "플러그인이 §a활성화§r되었습니다. §r| 포포곤리(PopoGonry)");
    }
    @Override
    public void onDisable() {
        //Reference.saveConfigLoad();
        StatReference.onlinePlayerSaveStatData();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "플러그인이 §c비활성화§r되었습니다. §r| 포포곤리(PopoGonry)");
    }

}
