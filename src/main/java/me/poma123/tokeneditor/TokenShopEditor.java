package me.poma123.tokeneditor;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.logging.Level;

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
        saveDefaultConfig();
        saveDefaultMsg();
        getCommand("tgive").setExecutor(new GiveCommand());
        getCommand("tokenshopeditor").setExecutor(new EditorCommand());



        if (getConfig().getString("data-type").equalsIgnoreCase("config")) {
            if (getConfig().isConfigurationSection("items") && getConfig().getConfigurationSection("items").getKeys(false).size() > 0) {
                for (String path : getConfig().getConfigurationSection("items").getKeys(false)) {
                    try {
                        ItemStack stack = getConfig().getItemStack("items." + path);
                        stack.getType();
                    } catch (Exception ex) {
                        try {
                            ItemStack[] stacks = Utils.stacksFromBase64(getConfig().getString("items." + path));
                            ItemStack stack = stacks[0];
                            getConfig().set("items." + path, stack);
                            saveConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (getConfig().isConfigurationSection("items") && getConfig().getConfigurationSection("items").getKeys(false).size() > 0) {
                for (String path : getConfig().getConfigurationSection("items").getKeys(false)) {
                    try {
                        ItemStack[] stacks = Utils.stacksFromBase64(getConfig().getString("items." + path));
                        ItemStack stack = stacks[0];

                    } catch (Exception ex) {

                        ItemStack stack = getConfig().getItemStack("items." + path);
                        stack.getType();
                        getConfig().set("items." + path, stack);
                        saveConfig();

                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    public FileConfiguration getMsg() {
        if (msg == null) {
            reloadMsg();
        }
        return msg;
    }

    
    public void saveMsg() {
        if (msg == null || messages == null) {
            return;
        }
        try {
            getMsg().save(messages);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save messages.yml to " + messages, ex);
        }
    }
    public void reloadMsg() {
        if (messages == null) {
            messages = new File(getDataFolder(), "messages.yml");
        }
        msg = YamlConfiguration.loadConfiguration(messages);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            msg.setDefaults(defConfig);
        }
    }


    public void saveDefaultMsg() {
        if (messages == null) {
            messages = new File(getDataFolder(), "messages.yml");
        }
        if (!messages.exists()) {
            saveResource("messages.yml", false);
        }
    }

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
