package net.aerulion.shop.CMDs;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.aerulion.shop.Main;
import net.aerulion.shop.Utils.Lang;
import net.aerulion.shop.Utils.Shop;
import net.aerulion.shop.Utils.Utils;

public class CMD_OPENSHOP implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission("shop.admin")) {
			sender.sendMessage(Lang.ErrorNoCommandPermission);
			return true;
		}

		if (args.length != 2) {
			sender.sendMessage(Lang.ErrorSyntax);
			return true;
		}

		Player player = Bukkit.getServer().getPlayer(args[0]);
		Shop shop = Main.LoadedShops.get(args[1]);

		Utils.openShopToPlayer(player, shop);

		return true;
	}

}
