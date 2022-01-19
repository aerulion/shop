package net.aerulion.shop.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.aerulion.nucleus.api.command.CommandUtils;
import net.aerulion.shop.Main;
import net.aerulion.shop.utils.Lang;
import net.aerulion.shop.utils.Shop;
import net.aerulion.shop.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CMD_openshop implements CommandExecutor, TabCompleter {

  @Override
  public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label,
      final String @NotNull [] args) {
    if (!sender.hasPermission("shop.admin")) {
      sender.sendMessage(Lang.ERROR_NO_COMMAND_PERMISSION);
      return true;
    }
    if (args.length != 2) {
      sender.sendMessage(Lang.ERROR_SYNTAX);
      return true;
    }
    final @Nullable Player player = Bukkit.getServer().getPlayer(args[0]);
    if (player == null) {
      sender.sendMessage(Lang.ERROR_PLAYER_NOT_FOUND);
      return true;
    }
    if (!Main.LOADED_SHOPS.containsKey(args[1])) {
      sender.sendMessage(Lang.ERROR_SHOP_NOT_FOUND);
      return true;
    }
    final Shop shop = Main.LOADED_SHOPS.get(args[1]);
    Util.openShopToPlayer(player, shop);
    return true;
  }

  @Override
  public List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String alias,
      final String @NotNull [] args) {
    if (args.length == 1) {
      return null;
    }
    if (args.length == 2) {
      return CommandUtils.filterForTabCompleter(new ArrayList<>(Main.LOADED_SHOPS.keySet()),
          args[1]);
    }
    return Collections.emptyList();
  }
}