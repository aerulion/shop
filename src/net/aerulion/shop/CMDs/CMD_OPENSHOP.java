package net.aerulion.shop.CMDs;

import net.aerulion.shop.Main;
import net.aerulion.shop.Utils.Lang;
import net.aerulion.shop.Utils.Shop;
import net.aerulion.shop.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CMD_OPENSHOP implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("shop.admin")) {
            sender.sendMessage(Lang.ERROR_NO_COMMAND_PERMISSION);
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(Lang.ERROR_SYNTAX);
            return true;
        }
        Player player = Bukkit.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(Lang.ERROR_PLAYER_NOT_FOUND);
            return true;
        }
        Shop shop = Main.LoadedShops.get(args[1]);
        Utils.openShopToPlayer(player, shop);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1)
            return null;
        if (args.length == 2)
            return Utils.filterForTabcomplete(new ArrayList<>(Main.LoadedShops.keySet()), args[1]);
        return Collections.emptyList();
    }
}
