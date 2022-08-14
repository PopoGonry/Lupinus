package com.popogonry.lupinus;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    private Plugin plugin;
    public String fileName;
    private String path;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(String fileName, String path) {
        this.fileName = fileName + ".yml";
        this.path = path;
    }

    public void reloadConfig() {
        if(this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder() + path, fileName);
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource(fileName);
        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if(this.dataConfig == null) reloadConfig();

        return this.dataConfig;
    }

    public void saveConfig() {
        if(this.dataConfig == null || this.configFile == null) return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, fileName + " 파일을 저장할 수 없습니다!", e);
        }
    }
    public void saveDefaultConfig() {

        if(this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder() + path, fileName);
        }
        if(!this.configFile.exists() && !fileName.equalsIgnoreCase("config.yml")) {
            try {
                File folder = new File(this.plugin.getDataFolder() + path);
                if(!folder.exists()) {
                    folder.mkdir();
                    Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + path + " 디렉토리 생성");
                }
                this.configFile.createNewFile();
                Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + path + "/" + fileName + " 파일 생성");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!this.configFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}



