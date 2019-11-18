package me.poma123.tokeneditor;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class TokenShopEditor extends JavaPlugin {


    private static TokenShopEditor instance;
    public TokenShopEditor() {
        TokenShopEditor.instance = this;
    }

    public static TokenShopEditor getInstance() {
        return TokenShopEditor.instance;
    }

    File messages = new File(getDataFolder() + File.separator + "messages.yml");
    FileConfiguration msg;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("tgive").setExecutor(new GiveCommand());
        getCommand("tokenshopeditor").setExecutor(new EditorCommand());
        saveDefaultConfig();

        msg = YamlConfiguration.loadConfiguration(messages);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    public FileConfiguration getMsg() {return msg;}

    public void saveMsg() throws IOException {msg.save(messages);}

    public void reloadMsg() {msg = YamlConfiguration.loadConfiguration(messages);}

    @Override
    public FileConfiguration getConfig() {
        return super.getConfig();
    }

    @Override
    public void saveConfig() {
        super.saveConfig();
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
    }

    public static FileConfiguration getShops() {
        File f = new File (Bukkit.getPluginManager().getPlugin("TokenManager").getDataFolder() + File.separator + "shops.yml");
        return YamlConfiguration.loadConfiguration(f);
    }



}
