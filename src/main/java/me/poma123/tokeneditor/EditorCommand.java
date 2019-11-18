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
import sun.tools.jstat.Token;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EditorCommand implements CommandExecutor {

    TokenShopEditor main = TokenShopEditor.getInstance();


    File f = new File (Bukkit.getPluginManager().getPlugin("TokenManager").getDataFolder() + File.separator + "shops.yml");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            if (args.length == 0) {
                if (sender.hasPermission("tokenshopeditor.help")) {



                    FileConfiguration msg = main.getMsg();
                    if (msg.get("help") != null) {
                        for (String s : msg.getStringList("help")) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("\n", System.lineSeparator()));
                        }
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
                    FileConfiguration msg = main.getMsg();
                    if (msg.get("noperm") != null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.getString("noperm")).replace("\n", System.lineSeparator()));
                    }
                }

            } else {
                if (args.length > 0) {
                    if (sender.hasPermission("tokenshopeditor.use")) {
                        if (args[0].equalsIgnoreCase("shop")) {
                            if (args.length > 1) {
                                if (args[1].equalsIgnoreCase("list")) {
                                    FileConfiguration shop = TokenShopEditor.getShops();
                                    if (shop.getConfigurationSection("shops").getKeys(false).size() > 0) {
                                        sender.sendMessage("§7+-     §cBoltok listája");
                                        for (String path : shop.getConfigurationSection("shops").getKeys(false)) {
                                            sender.sendMessage("§7| §b" + path);
                                        }
                                        sender.sendMessage("§7+-");
                                    } else {
                                        //Ez elvileg lehetetlen, de azért hátha megtörténik :)
                                        sender.sendMessage("§cNincs bolt beállítva.");
                                    }
                                } else if (args[1].equalsIgnoreCase("add")) {
                                    if (args.length > 4) {
                                        FileConfiguration shop = TokenShopEditor.getShops();
                                        if (shop.get("shops." + args[2]) != null) {
                                            sender.sendMessage("§cEz a bolt már létezik, az eltávolításhoz írd be:");
                                            sender.sendMessage("§e/tokeneditor shop delete " + args[2]);
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
                                                sender.sendMessage("§cRossz GUI sorok száma értéket adtál meg, alapértelmezett szám beállítva. (6)");
                                            }

                                            // Beállítás
                                            shop.set("shops." + path + ".title", title);
                                            shop.set("shops." + path + ".rows", rows);
                                            shop.createSection("shops." + path + ".items");
                                            try {
                                                shop.save(f);
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tokenmanager reload");
                                                sender.sendMessage("§aSikeresen hozzáadtad a(z) " + path + "§a boltot!");
                                                sender.sendMessage("§aHasználd a §7/tokeneditor item §aparancsot a szerkesztéshez!");
                                            } catch (IOException e) {

                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        sender.sendMessage("§bHasználat: §c/tokeneditor shop add <azonosító> <GUI_sorok_száma> <cím>");
                                        sender.sendMessage("§bA Gui sorok számának §e1 §bés §e6 között kell lennie! A címben használhatsz színkódokat.");
                                    }


                                }
                                if (args[1].equalsIgnoreCase("delete")) {
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
                                                    sender.sendMessage("§aSikeresen törölted a(z) §7" + args[2] + "§a boltot!");
                                                    return true;
                                                }

                                            }
                                            sender.sendMessage("§c§lBiztosan törlöd? §r§cÍrd be a §a/tokeneditor shop delete " + args[2] + " §aconfirm §cparancsot a törléshez!");
                                        } else {
                                            sender.sendMessage("§cNincs ilyen bolt.");
                                        }

                                    } else {
                                        sender.sendMessage("§bHasználat: §c/tokeneditor shop delete <azonosító>");
                                    }
                                } else if (args[1].equalsIgnoreCase("update")) {
                                    if (args.length > 4) {
                                        FileConfiguration shop = TokenShopEditor.getShops();
                                        if (shop.get("shops." + args[2]) == null) {
                                            sender.sendMessage("§cEz a bolt nem létezik.");
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
                                                sender.sendMessage("§cRossz GUI sorok száma értéket adtál meg, alapértelmezett szám beállítva. (6)");
                                            }

                                            // Beállítás
                                            shop.set("shops." + path + ".title", title);
                                            shop.set("shops." + path + ".rows", rows);
                                            try {
                                                shop.save(f);
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tokenmanager reload");
                                                sender.sendMessage("§aSikeresen frissítetted a(z) " + path + "§a boltot!");
                                                sender.sendMessage("§aHasználd a §7/tokeneditor item §aparancsot a szerkesztéshez!");
                                            } catch (IOException e) {

                                                e.printStackTrace();
                                            }
                                        }

                                    } else {
                                        sender.sendMessage("§bHasználat: §c/tokeneditor shop update <azonosító> <GUI_sorok_száma> <cím>");
                                        sender.sendMessage("§bA Gui sorok számának §e1 §bés §e6 között kell lennie! A címben használhatsz színkódokat.");
                                    }
                                }
                            } else {
                                sender.sendMessage("§6 -------     §6HELP       §6------- ");
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


                            }
                        } else if (args[0].equalsIgnoreCase("item")) {
                            if (args.length > 1) {
                                if (args[1].equalsIgnoreCase("list")) {
                                    if (args.length > 2) {
                                        FileConfiguration shop = TokenShopEditor.getShops();
                                        if (shop.get("shops." + args[2].toLowerCase() + ".items") != null) {
                                            if (shop.getConfigurationSection("shops." + args[2] + ".items").getKeys(false).size() > 0) {
                                                sender.sendMessage("§7+-  §6Item lista §7(" + args[2] + " bolt)");
                                                for (String path : shop.getConfigurationSection("shops." + args[2] + ".items").getKeys(false)) {
                                                    String material = shop.getString("shops." + args[2] + ".items." + path + ".displayed").split(" ")[0];
                                                    String name = ChatColor.translateAlternateColorCodes('&', shop.getString("shops." + args[2] + ".items." + path + ".displayed").split(" ")[2]);
                                                    int cost = shop.getInt("shops." + args[2] + ".items." + path + ".cost");

                                                    sender.sendMessage("§7| §b" + path + ". slot, §eár: " + cost + "§b, §6" + material + "§b, " + name);
                                                }
                                                sender.sendMessage("§7+-");

                                            } else {
                                                sender.sendMessage("§bEbben a boltban nincsenek még itemek.");
                                            }
                                        } else {
                                            sender.sendMessage("§cNincs ilyen bolt.");
                                        }
                                    } else {
                                        sender.sendMessage("§bHasználat: §c/tokeneditor item list <bolt>");
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
                                                sender.sendMessage("§cHibás slot szám: " + args[3]);
                                                return true;
                                            }

                                            try {
                                                cost = Integer.parseInt(args[4]);
                                            } catch (Exception ex) {
                                                sender.sendMessage("§cHibás ár: " + args[4]);
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
                                                            String lore = "&aÁr:_%price%_token";
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
                                                                    lore = lore + "|&c|&aÁr:_%price%_token";
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
                                                                main.getConfig().set("items." + rnd, item);
                                                                main.saveConfig();
                                                            }


                                                            shop.set("shops." + bolt + ".items." + slotint + ".displayed",
                                                                    material.toString().toUpperCase() + " " + amount + " " + displayname + " lore:" + lore + " " + enchants);
                                                            shop.set("shops." + bolt + ".items." + slotint + ".cost", cost);
                                                            shop.set("shops." + bolt + ".items." + slotint + ".message", "&6Beváltó &8» &7Köszönjük a vásárlást, %player%! &c-%price% token");
                                                            shop.set("shops." + bolt + ".items." + slotint + ".commands", Arrays.asList("tgive %player% " + rnd));
                                                            try {
                                                                shop.save(f);
                                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tokenmanager reload");
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            sender.sendMessage("§aSikeresen hozzáadtad az itemet!");

                                                    } else {
                                                        sender.sendMessage("§cNincs semmi a kezedben.");
                                                    }
                                                } else {
                                                    sender.sendMessage("§cEzen a sloton már létezik egy másik item.");
                                                    sender.sendMessage("§cVálassz egy másik slotot, vagy töröld az jelenlegi itemet (/tse item delete <bolt> <slot>).");
                                                }

                                            } else {
                                                sender.sendMessage("§cHibás slot szám: " + slot);
                                                sender.sendMessage("§cA számnak 0 és " + (rows * 9 - 1) + " közé kell esnie.");
                                            }

                                        } else {
                                            sender.sendMessage("§cNincs ilyen bolt.");
                                        }
                                    } else {
                                        sender.sendMessage("§bHasználat: §c/tokeneditor item add <bolt> <slot> <ár>");
                                    }
                                } else if (args[1].equalsIgnoreCase("delete")) {
                                    if (args.length > 3) {
                                        FileConfiguration shop = TokenShopEditor.getShops();
                                        String bolt = args[2].toLowerCase();
                                        if (shop.get("shops." + bolt + ".items.") != null) {
                                            try {
                                                Integer.parseInt(args[3]);
                                            } catch (Exception ex) {
                                                sender.sendMessage("§cA megadott slot szám hibás: " + args[3]);
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
                                                        sender.sendMessage("§aSikeresen törölted az itemet!");
                                                        return true;
                                                    }
                                                }
                                                sender.sendMessage("§c§lBiztosan törlöd? §r§cÍrd be a §a/tokeneditor item delete " + bolt + " " + args[3] + " §aconfirm §cparancsot a törléshez!");
                                            } else {
                                                sender.sendMessage("§cNincs item ezen a sloton.");
                                            }
                                        } else {
                                            sender.sendMessage("§cNincs ilyen bolt.");
                                        }
                                    }
                                } else {
                                    sender.sendMessage("§bHasználat: §c/tokeneditor item delete <bolt> <slot>");
                                }
                            } else {
                                sender.sendMessage("§6 -------     §6HELP       §6------- ");
                                sender.sendMessage("§6Item lista lista:\n§e- §b/tokeneditor item list <bolt>");
                                sender.sendMessage("§6Item hozzáadás:" +
                                        "\n§e- §b/tokeneditor item add <bolt> <slot> <ár>");
                                sender.sendMessage("§6Item törlése:" +
                                        "\n§e- §b/tokeneditor item delete <bolt> <slot>");
                            }
                        }
                    } else {
                        sender.sendMessage("§cEhhez nincs jogod.");
                    }
                }
            }
        }
        return true;
    }

    public static String generateRandomString(final int length) {
        final boolean useLetters = true;
        final boolean useNumbers = true;
        final String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
        return generatedString;
    }
}
