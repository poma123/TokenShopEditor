package me.poma123.tokeneditor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class GiveCommand implements CommandExecutor {


    TokenShopEditor main = TokenShopEditor.getInstance();

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.hasPermission("tokenshopeditor.admin")) {
            if (args.length > 1) {
                if (Bukkit.getPlayer(args[0]) == null) {
                    sender.sendMessage("§cEz a játékos jelenleg nem elérhető.");
                } else {
                    if (main.getConfig().get("items." + args[1]) != null) {

                        if (main.getConfig().getString("data-type") != null) {
                            if (main.getConfig().getString("data-type").equalsIgnoreCase("config")) {
                                Bukkit.getPlayer(args[0]).getInventory().addItem(main.getConfig().getItemStack("items." + args[1]));

                            } else {

                                try {
                                    ItemStack item;
                                    ItemStack[] itemst = Utils.stacksFromBase64(main.getConfig().getString("items." + args[1]));
                                    item = itemst[0];
                                    if (item != null) {
                                        Bukkit.getPlayer(args[0]).getInventory().addItem(item);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        sender.sendMessage("§cA megadott item nem létezik.");
                    }
                }
            }
        } else {
            sender.sendMessage("§cEhhez nincs jogod!");
        }
        return true;
    }
}
