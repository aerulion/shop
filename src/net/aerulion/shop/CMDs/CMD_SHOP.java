package net.aerulion.shop.CMDs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.aerulion.shop.Utils.FileManager;
import net.aerulion.shop.Utils.Lang;
import net.aerulion.shop.Utils.Utils;

public class CMD_SHOP implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.ErrorNoPlayer);
			return true;
		}

		Player player = (Player) sender;

		if (!player.hasPermission("shop.admin")) {
			player.sendMessage(Lang.ErrorNoCommandPermission);
			return true;
		}

		if (args.length > 0) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("list")) {
					Utils.sendAllShopsMessage(player, 1);
					return true;
				}

				if (args[0].equalsIgnoreCase("reload")) {
					FileManager.reloadAllData();
					player.sendMessage(Lang.Reloaded);
					return true;
				}

				if (args[0].equalsIgnoreCase("help")) {
					Utils.sendHelpMenu(player);
					return true;
				}
			}

			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("list")) {
					try {
						Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						player.sendMessage(Lang.ErrorInvalidNumber);
						return true;
					}
					if (Integer.parseInt(args[1]) < 1) {
						player.sendMessage(Lang.ErrorNumberTooSmall);
						return true;
					}
					Utils.sendAllShopsMessage(player, Integer.parseInt(args[1]));
					return true;
				}
			}

			if (args.length == 5 || args.length == 6) {
				if (args[0].equalsIgnoreCase("create")) {
					try {
						Double.parseDouble(args[2]);
					} catch (NumberFormatException e) {
						player.sendMessage(Lang.ErrorInvalidPrice);
						return true;
					}
					if (Utils.checkCooldownPattern(args[3])) {
						boolean virtual = false;
						if (args.length == 6) {
							if (!(args[5].equalsIgnoreCase("true") || args[5].equalsIgnoreCase("false"))) {
								player.sendMessage(Lang.ErrorInvalidBoolean);
								return true;
							}
							if (args[5].equalsIgnoreCase("true"))
								virtual = true;
						}
						Utils.createNewShop(player, Double.parseDouble(args[2]), Utils.convertCooldownPattern(args[3]), args[1].replaceAll("@", " "), args[4], virtual);
						return true;
					} else {
						player.sendMessage(Lang.ErrorInvalidLimit);
						return true;
					}

				}
			}
		}

		player.sendMessage(Lang.ErrorSyntax);

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2) {
			return Utils.filterForTabcomplete(new ArrayList<String>(Arrays.asList("create", "list", "reload", "help")), args[0]);
		}

		if (args[0].equalsIgnoreCase("create")) {
			if (args.length == 2) {
				return Utils.filterForTabcomplete(new ArrayList<String>(Arrays.asList("<Name>")), args[1]);
			}

			if (args.length == 3) {
				return Utils.filterForTabcomplete(new ArrayList<String>(Arrays.asList("<Preis>")), args[2]);
			}

			if (args.length == 4) {
				return Utils.filterForTabcomplete(new ArrayList<String>(Arrays.asList("<Limit>")), args[3]);
			}

			if (args.length == 5) {
				return Utils.filterForTabcomplete(new ArrayList<String>(Arrays.asList("<Permission>")), args[4]);
			}

			return Arrays.asList();
		}

		if (args[0].equalsIgnoreCase("list")) {
			if (args.length == 2) {
				return Utils.filterForTabcomplete(new ArrayList<String>(Arrays.asList("[Seite]")), args[1]);
			}
		}
		if (args[0].equalsIgnoreCase("reload")) {
			return Arrays.asList();
		}
		if (args[0].equalsIgnoreCase("help")) {
			return Arrays.asList();
		}

		return Arrays.asList();
	}

}
