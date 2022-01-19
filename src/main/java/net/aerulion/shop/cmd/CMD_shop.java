package net.aerulion.shop.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.aerulion.nucleus.api.command.CommandUtils;
import net.aerulion.shop.utils.FileManager;
import net.aerulion.shop.utils.Lang;
import net.aerulion.shop.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMD_shop implements CommandExecutor, TabCompleter {

  @Override
  public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command,
      final @NotNull String label, final String @NotNull [] args) {

    if (!(sender instanceof final @NotNull Player player)) {
      sender.sendMessage(Lang.ERROR_NO_PLAYER);
      return true;
    }

    if (!player.hasPermission("shop.admin")) {
      player.sendMessage(Lang.ERROR_NO_COMMAND_PERMISSION);
      return true;
    }

    if (args.length > 0) {
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("list")) {
          Util.sendAllShopsMessage(player, 1);
          return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
          FileManager.reloadAllData();
          player.sendMessage(Lang.RELOADED);
          return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
          Util.sendHelpMenu(player);
          return true;
        }
      }

      if (args.length == 2) {
        if (args[0].equalsIgnoreCase("list")) {
          try {
            Integer.parseInt(args[1]);
          } catch (final NumberFormatException e) {
            player.sendMessage(Lang.ERROR_INVALID_NUMBER);
            return true;
          }
          if (Integer.parseInt(args[1]) < 1) {
            player.sendMessage(Lang.ERROR_NUMBER_TOO_SMALL);
            return true;
          }
          Util.sendAllShopsMessage(player, Integer.parseInt(args[1]));
          return true;
        }
      }

      if (args.length == 5 || args.length == 6) {
        if (args[0].equalsIgnoreCase("create")) {
          try {
            Double.parseDouble(args[2]);
          } catch (final NumberFormatException e) {
            player.sendMessage(Lang.ERROR_INVALID_PRICE);
            return true;
          }
          if (Util.checkCooldownPattern(args[3])) {
            boolean virtual = false;
            if (args.length == 6) {
              if (!(args[5].equalsIgnoreCase("true") || args[5].equalsIgnoreCase("false"))) {
                player.sendMessage(Lang.ERROR_INVALID_BOOLEAN);
                return true;
              }
              if (args[5].equalsIgnoreCase("true")) {
                virtual = true;
              }
            }
            Util.createNewShop(player, Double.parseDouble(args[2]),
                Util.convertCooldownPattern(args[3]), args[1].replaceAll("@", " "), args[4],
                virtual);
          } else {
            player.sendMessage(Lang.ERROR_INVALID_LIMIT);
          }
          return true;
        }
      }
    }

    player.sendMessage(Lang.ERROR_SYNTAX);

    return true;
  }

  @Override
  public List<String> onTabComplete(final @NotNull CommandSender sender,
      final @NotNull Command command, final @NotNull String alias, final String @NotNull [] args) {
    if (args.length < 2) {
      return CommandUtils.filterForTabCompleter(
          new ArrayList<>(Arrays.asList("create", "list", "reload", "help")), args[0]);
    }
    if (args[0].equalsIgnoreCase("create")) {
      if (args.length == 2) {
        return CommandUtils.filterForTabCompleter(
            new ArrayList<>(Collections.singletonList("<Name>")), args[1]);
      }
      if (args.length == 3) {
        return CommandUtils.filterForTabCompleter(
            new ArrayList<>(Collections.singletonList("<Preis>")), args[2]);
      }
      if (args.length == 4) {
        return CommandUtils.filterForTabCompleter(
            new ArrayList<>(Collections.singletonList("<Limit>")), args[3]);
      }
      if (args.length == 5) {
        return CommandUtils.filterForTabCompleter(
            new ArrayList<>(Collections.singletonList("<Permission>")), args[4]);
      }
      if (args.length == 6) {
        return CommandUtils.filterForTabCompleter(
            new ArrayList<>(Arrays.asList("[Virtuell]", "true", "false")), args[5]);
      }
      return Collections.emptyList();
    }
    if (args[0].equalsIgnoreCase("list")) {
      if (args.length == 2) {
        return CommandUtils.filterForTabCompleter(
            new ArrayList<>(Collections.singletonList("[Seite]")), args[1]);
      }
    }
    return Collections.emptyList();
  }
}