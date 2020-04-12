package net.aerulion.shop.Tasks;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import net.aerulion.shop.Main;
import net.aerulion.shop.Utils.Base64Utils;
import net.aerulion.shop.Utils.FileManager;
import net.aerulion.shop.Utils.Shop;

public class SaveTask extends BukkitRunnable {

	private String uuid;

	public SaveTask(String shopID) {
		this.uuid = shopID;
		this.runTaskAsynchronously(Main.plugin);
	}

	@Override
	public void run() {
		Shop shop = Main.LoadedShops.get(uuid);
		File shopFile = new File("plugins/Shop/Shops", uuid + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(shopFile);
		cfg.set("ShopName", shop.getShopName());
		cfg.set("ShopPermission", shop.getShopPermission());
		if (!shop.isVirtual()) {
			cfg.set("Location.X", shop.getShopLocation().getX());
			cfg.set("Location.Y", shop.getShopLocation().getY());
			cfg.set("Location.Z", shop.getShopLocation().getZ());
			cfg.set("Location.World", shop.getShopLocation().getWorld().getName());
		}
		cfg.set("TransactionDates", FileManager.serializeTransactionDates(shop.getTransactionDates()));
		cfg.set("Cooldown", shop.getCooldown());
		cfg.set("Price", shop.getPrice());
		cfg.set("ItemsForSale", Base64Utils.EncodeItems(shop.getSoldItems()));
		cfg.set("timesUsed", shop.getTimesUsed());
		cfg.set("Enabled", shop.isEnabled());
		cfg.set("Virtual", shop.isVirtual());
		cfg.set("Question", shop.getQuestion());
		cfg.set("QuestionAnswer", shop.getQuestionAnswer());
		cfg.set("ExecutedCommands", shop.getExecutedCommands());
		try {
			cfg.save(shopFile);
		} catch (IOException e) {
		}
	}
}
