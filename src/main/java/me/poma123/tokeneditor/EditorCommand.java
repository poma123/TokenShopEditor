package me.poma123.tokeneditor;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditorCommand implements CommandExecutor {

    TokenShopEditor main = TokenShopEditor.getInstance();


    File f = new File(Bukkit.getPluginManager().getPlugin("TokenManager").getDataFolder() + File.separator + "shops.yml");

    public static String generateRandomString(final int length) {
        final boolean useLetters = true;
        final boolean useNumbers = true;
        final String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
        return generatedString;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            if (args.length == 0) {
                if (sender.hasPermission("tokenshopeditor.help")) {

                    for (String s : main.getMsg().getStringList("help")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("\n", System.lineSeparator()));
                    }


                /*    sender.sendMessage("§6 -------     §6HELP       §6------- ");
                    sender.sendMessage("§6Bolt lista:\n§e- §b/tokeneditor shop list");
                    sender.sendMessage("§6Bolt hozzáadás:" +
                            "\n§e- §b/tokeneditor shop add <azonosító> <GUI_sorok_száma> <cím>" +
                            "\n§e- §7A sorok számának §e1 §7és §e6§7 között kell lennie!" +
                            "\n§e- §7A címben használhatsz színkódokat.");
                    sender.sendMessage("§6Bolt frissítése:\n§e- §b/tokeneditor shop update <azonosító> <GUI_sorok_száma> <cím>" +
                            "\n§e- §7A sorok számának §e1 §7és §e6§7 között kell lennie!" +
                            "\n§e- §7A címben használhatsz színkódokat.");
                    sender.sendMessage("§6Bolt törlése:" +
                            "\n§e- §b/tokeneditor shop delete <azonosító>");
                    sender.sendMessage("");
                    sender.sendMessage("§6Item lista lista:\n§e- §b/tokeneditor item list <bolt>");
                    sender.sendMessage("§6Item hozzáadás:" +
                            "\n§e- §b/tokeneditor item add <bolt> <slot> <ár>");
                    sender.sendMessage("§6Item törlése:" +
                            "\n§e- §b/tokeneditor item delete <bolt> <slot>");*/
                } else {
                    //  sender.sendMessage("§cEhhez nincs jogod.");

                    sender.sendMessage(main.getMsgString("no-perm"));

                }

            } else {
                if (args.length > 0) {
                    if (sender.hasPermission("tokenshopeditor.use")) {
                        if (args[0].equalsIgnoreCase("setmsg")) {
                            if (args.length > 1) {
                                List<String> name = new ArrayList<>();
                                for (int i = 1; i < args.length; i++) {
                                    name.add(args[i]);
                                }
                                sender.sendMessage(main.getMsgString("previous-setmsg", "%msg%", ChatColor.translateAlternateColorCodes('§', main.getConfig().getString("buy-message"))));
                                main.getConfig().set("buy-message", String.join(" ", name));
                                main.saveConfig();
                                sender.sendMessage(main.getMsgString("setmsg"));
                            } else {
                                sender.sendMessage(main.getMsgString("usage.setmsg"));
                            }
                        } else if (args[0].equalsIgnoreCase("shop")) {
                            if (args.length > 1) {
                                if (args[1].equalsIgnoreCase("list")) {
                                    FileConfiguration shop = TokenShopEditor.getShops();
                                    if (shop.getConfigurationSection("shops").getKeys(false).size() > 0) {
                                        sender.sendMessage("§7+-     §cShop list");
                                        for (String path : shop.getConfigurationSection("shops").getKeys(false)) {
                                            sender.sendMessage("§7| §b" + path);
                                        }
                                        sender.sendMessage("§7+-");
                                    } else {

                                        //This is impossible, but it can sometimes happen
                                        sender.sendMessage(main.getMsgString("shops-empty"));
                                    }
                                } else if (args[1].equalsIgnoreCase("add")) {
                                    if (args.length > 4) {
                                        FileConfiguration shop = TokenShopEditor.getShops();
                                        if (shop.get("shops." + args[2]) != null) {
                                            sender.sendMessage(main.getMsgString("shop-already", "%shop%", args[2]));
                                        } else {
                                            String path = args[2].toLowerCase();
                                            int rows = 6;
                                            String title;

                                            // Név összegyűjtése
                                            List<String> l = new ArrayList<String>();
                                            for (int i = 4; i < args.length; i++) {
                                                l.add(args[i]);
                                            }
                                            title = ChatColor.translateAlternateColorCodes('&', String.join(" ", l));


                                            // Sorok számmá alakítása
                                            try {
                                                rows = Integer.parseInt(args[3].toLowerCase());
                                            } catch (Exception ex) {
                                                sender.sendMessage(main.getMsgString("wrong-rows"));
                                            }

                                            // Beállítás
                                            shop.set("shops." + path + ".title", title);
                                            shop.set("shops." + path + ".rows", rows);
                                            shop.createSection("shops." + path + ".items");
                                            try {
                                                shop.save(f);
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tokenmanager reload");
                                                sender.sendMessage(main.getMsgString("shop-added", "%shop%", path));
                                            } catch (IOException e) {

                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        sender.sendMessage(main.getMsgString("usage.addshop"));
                                        //sender.sendMessage("§bHasználat: §c/tokeneditor shop add <azonosító> <GUI_sorok_száma> <cím>");
                                        //sender.sendMessage("§bA Gui sorok számának §e1 §bés §e6 között kell lennie! A címben használhatsz színkódokat.");
                                    }


                                } else if (args[1].equalsIgnoreCase("delete")) {
                                    if (args.length > 2) {

                                        FileConfiguration shop = TokenShopEditor.getShops();
                                        if (shop.get("shops." + args[2]) != null) {
                                            if (args.length > 3) {
                                                if (args[3].equalsIgnoreCase("confirm")) {
                                                    args[2] = args[2].toLowerCase();


                                                    shop.set("shops." + args[2], null);
                                                    try {
                                                        shop.save(f);
                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tokenmanager reload");
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    sender.sendMessage(main.getMsgString("shop-deleted", "%shop%", args[2]));
                                                    return true;
                                                }

                                            }
                                            sender.sendMessage(main.getMsgString("shop-confirm", "%shop%", args[2]));
                                        } else {
                                            sender.sendMessage(main.getMsgString("shop-not-exist"));
                                        }

                                    } else {
                                        sender.sendMessage(main.getMsgString("usage.deleteshop"));
                                    }
                                } else if (args[1].equalsIgnoreCase("update")) {
                                    if (args.length > 4) {
                                        FileConfiguration shop = TokenShopEditor.getShops();
                                        if (shop.get("shops." + args[2]) == null) {
                                            sender.sendMessage(main.getMsgString("shop-not-exists"));
                                        } else {
                                            String path = args[2].toLowerCase();
                                            int rows = 6;
                                            String title;

                                            // Név összegyűjtése
                                            List<String> l = new ArrayList<String>();
                                            for (int i = 4; i < args.length; i++) {
                                                l.add(args[i]);
                                            }
                                            title = ChatColor.translateAlternateColorCodes('&', String.join(" ", l));


                                            // Sorok számmá alakítása
                                            try {
                                                rows = Integer.parseInt(args[3].toLowerCase());
                                            } catch (Exception ex) {
                                                sender.sendMessage(main.getMsgString("wrong-rows"));
                                            }

                                            // Beállítás
                                            shop.set("shops." + path + ".title", title);
                                            shop.set("shops." + path + ".rows", rows);
                                            try {
                                                shop.save(f);
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tokenmanager reload");
                                                sender.sendMessage(main.getMsgString("shop-updated"));
                                            } catch (IOException e) {

                                                e.printStackTrace();
                                            }
                                        }

                                    } else {
                                        sender.sendMessage(main.getMsgString("usage.updateshop"));
                                    }
                                } else {
                                     for (String s : main.getMsg().getStringList("help")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("\n", System.lineSeparator()));
                    }

                                }
                            } else {
                                 for (String s : main.getMsg().getStringList("help")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("\n", System.lineSeparator()));
                    }


                            }
                        } else if (args[0].equalsIgnoreCase("item")) {
                            if (args.length > 1) {
                                if (args[1].equalsIgnoreCase("list")) {
                                    if (args.length > 2) {
                                        FileConfiguration shop = TokenShopEditor.getShops();
                                        if (shop.get("shops." + args[2].toLowerCase() + ".items") != null) {
                                            if (shop.getConfigurationSection("shops." + args[2] + ".items").getKeys(false).size() > 0) {
                                                sender.sendMessage("§7+-  §6Item list §7(" + args[2] + " shop)");
                                                for (String path : shop.getConfigurationSection("shops." + args[2] + ".items").getKeys(false)) {
                                                    String material = shop.getString("shops." + args[2] + ".items." + path + ".displayed").split(" ")[0];
                                                    String name = ChatColor.translateAlternateColorCodes('&', shop.getString("shops." + args[2] + ".items." + path + ".displayed").split(" ")[2]);
                                                    int cost = shop.getInt("shops." + args[2] + ".items." + path + ".cost");

                                                    //TODO TEST
                                                   /* String cmds = shop.getString("shops." + args[2] + ".items." + path + ".commands").split(" ")[2].replace("]", "");
                                                    sender.sendMessage(cmds);
                                                    if (main.getConfig().get("test." + cmds) != null) {
                                                        try {
                                                            ItemStack[] itemst = Utils.stacksFromBase64(main.getConfig().getString("test." + cmds));
                                                            sender.sendMessage(itemst[0].getType().toString());
                                                        } catch (IOException e) {
                                                            sender.sendMessage("nop");
                                                            e.printStackTrace();
                                                        }
                                                    }*/
                                                    //TODO Test over

                                                    sender.sendMessage("§7| §b" + path + ". slot, §eprice: " + cost + "§b, §6" + material + "§b, " + name);
                                                }
                                                sender.sendMessage("§7+-");

                                            } else {
                                                sender.sendMessage(main.getMsgString("shop-empty"));
                                            }
                                        } else {
                                            sender.sendMessage(main.getMsgString("shop-not-exists"));
                                        }
                                    } else {
                                        sender.sendMessage(main.getMsgString("usage.itemlist"));
                                    }
                                } else if (args[1].equalsIgnoreCase("add")) {
                                    if (args.length > 4) {
                                        FileConfiguration shop = TokenShopEditor.getShops();
                                        String bolt = args[2];
                                        String slot = args[3].toLowerCase();
                                        int slotint;
                                        int cost;
                                        int rows;

                                        if (shop.get("shops." + bolt + ".items") != null) {
                                            try {
                                                slotint = Integer.parseInt(slot);
                                            } catch (Exception ex) {
                                                sender.sendMessage(main.getMsgString("wrong-slot", "%slot%", args[3]));
                                                return true;
                                            }

                                            try {
                                                cost = Integer.parseInt(args[4]);
                                            } catch (Exception ex) {
                                                sender.sendMessage(main.getMsgString("wrong-price", "%price%", args[4]));
                                                return true;
                                            }

                                            rows = shop.getInt("shops." + bolt + ".rows");

                                            if (slotint < (rows * 9 - 1) && slotint >= 0) {

                                                if (shop.get("shops." + bolt + ".items." + slot) == null) {

                                                    Player p = (Player) sender;
                                                    if (!p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {


                                                        ItemStack item = p.getInventory().getItemInMainHand();
                                                        String displayname = "";
                                                        Material material = item.getType();
                                                        int amount = item.getAmount();
                                                        String lore = ChatColor.translateAlternateColorCodes('§', main.getConfig().getString("lore").replace("_", " "));//"&aÁr:_%price%_token";
                                                        String enchants = "";


                                                        if (item.hasItemMeta()) {
                                                            ItemMeta im = item.getItemMeta();
                                                            if (im.hasDisplayName()) {
                                                                displayname = "name:" + im.getDisplayName();
                                                                displayname = displayname.replace(" ", "_");
                                                            }
                                                            if (im.hasLore()) {
                                                                lore = String.join("|", im.getLore());
                                                                lore = lore.replace(" ", "_");
                                                                lore = lore + "|&c|" + ChatColor.translateAlternateColorCodes('§', main.getConfig().getString("lore").replace("_", " "));
                                                            }
                                                            if (im.hasEnchants()) {
                                                                List<String> ench = new ArrayList<>();
                                                                for (Enchantment en : im.getEnchants().keySet()) {
                                                                    ench.add(en.getName().toUpperCase() + ":" + im.getEnchants().get(en));
                                                                }
                                                                enchants = String.join(" ", ench);
                                                            }
                                                        }
                                                        String rnd = generateRandomString(7);
                                                        if (main.getConfig().get("items." + rnd) == null) {
                                                            if (main.getConfig().getString("data-type").equalsIgnoreCase("config")) {
                                                                main.getConfig().set("items." + rnd, item);
                                                            } else {
                                                                main.getConfig().set("items." + rnd, Utils.toBase64(item));
                                                            }

                                                            main.saveConfig();
                                                        }


                                                        shop.set("shops." + bolt + ".items." + slotint + ".displayed",
                                                                material.toString().toUpperCase() + " " + amount + " " + displayname + " lore:" + lore + " " + enchants);
                                                        shop.set("shops." + bolt + ".items." + slotint + ".cost", cost);
                                                        if (main.getConfig().getString("buy-message") != null) {
                                                            shop.set("shops." + bolt + ".items." + slotint + ".message", main.getConfig().getString("buy-message"));
                                                        } else {
                                                            shop.set("shops." + bolt + ".items." + slotint + ".message", "&bTM &8» &7Thanks for your purchase, %player%! &c-%price% tokens");
                                                        }
                                                        //  shop.set("shops." + bolt + ".items." + slotint + ".message", "&6Beváltó &8» &7Köszönjük a vásárlást, %player%! &c-%price% token");
                                                        shop.set("shops." + bolt + ".items." + slotint + ".commands", Arrays.asList("tgive %player% " + rnd));
                                                        try {
                                                            shop.save(f);
                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tokenmanager reload");
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                        sender.sendMessage(main.getMsgString("item-added"));

                                                    } else {
                                                        sender.sendMessage(main.getMsgString("empty-hand"));
                                                    }
                                                } else {
                                                    sender.sendMessage(main.getMsgString("item-already"));
                                                }

                                            } else {
                                                sender.sendMessage(main.getMsgString("wrong-slot", "%slot%", slot));
                                                sender.sendMessage(main.getMsgString("rows-hint", "%rows_max%", (rows * 9 - 1)+""));
                                            }

                                        } else {
                                            sender.sendMessage(main.getMsgString("shop-not-exists"));
                                        }
                                    } else {
                                        sender.sendMessage(main.getMsgString("usage.itemadd"));
                                    }
                                } else if (args[1].equalsIgnoreCase("delete")) {
                                    if (args.length > 3) {
                                        FileConfiguration shop = TokenShopEditor.getShops();
                                        String bolt = args[2].toLowerCase();
                                        if (shop.get("shops." + bolt + ".items.") != null) {
                                            try {
                                                Integer.parseInt(args[3]);
                                            } catch (Exception ex) {
                                                sender.sendMessage(main.getMsgString("wrong-slot", "%slot%", args[3]));
                                                return true;
                                            }


                                            if (shop.get("shops." + bolt + ".items." + args[3]) != null) {
                                                if (args.length > 4) {
                                                    if (args[4].equalsIgnoreCase("confirm")) {
                                                        shop.set("shops." + bolt + ".items." + args[3], null);
                                                        try {
                                                            shop.save(f);
                                                            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "tokenmanager reload");
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                        sender.sendMessage(main.getMsgString("item-deleted"));
                                                        return true;
                                                    }
                                                }
                                                sender.sendMessage(main.getMsgString("item-confirm", "%shop%", bolt, "%slot%", args[3]));
                                               // sender.sendMessage("§c§lBiztosan törlöd? §r§cÍrd be a §a/tokeneditor item delete " + bolt + " " + args[3] + " §aconfirm §cparancsot a törléshez!");
                                            } else {
                                                sender.sendMessage(main.getMsgString("item-not-exists"));
                                               // sender.sendMessage("§cNincs item ezen a sloton.");
                                            }
                                        } else {
                                            sender.sendMessage(main.getMsgString("shop-not-exists"));
                                        //    sender.sendMessage("§cNincs ilyen bolt.");
                                        }
                                    } else {

                                            sender.sendMessage(main.getMsgString("usage.itemdelete"));

                                    }
                                } else {
                                    for (String s : main.getMsg().getStringList("help")) {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("\n", System.lineSeparator()));
                                    }
                                }
                            } else {
                                 for (String s : main.getMsg().getStringList("help")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("\n", System.lineSeparator()));
                    }
                            }
                        }
                    } else {
                        sender.sendMessage(main.getMsgString("no-perm"));
                    }
                }
            }
        }
        return true;
    }
}
