package net.aerulion.shop.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import net.aerulion.shop.Main;

public class FileManager {

	public static void addNewShopFile(String shopID, String shopName, String shopPermission, Location location, HashMap<String, String> transactionDates, long cooldown, double price, List<ItemStack> soldItems, boolean virtual) {
		File newShop = new File("plugins/Shop/Shops", shopID + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(newShop);
		cfg.options().copyDefaults(true);
		cfg.addDefault("ShopName", shopName);
		cfg.addDefault("ShopPermission", shopPermission);
		if (!virtual) {
			cfg.addDefault("Location.X", location.getX());
			cfg.addDefault("Location.Y", location.getY());
			cfg.addDefault("Location.Z", location.getZ());
			cfg.addDefault("Location.World", location.getWorld().getName());
		}
		cfg.addDefault("TransactionDates", serializeTransactionDates(transactionDates));
		cfg.addDefault("Cooldown", cooldown);
		cfg.addDefault("Price", price);
		cfg.addDefault("ItemsForSale", Base64Utils.EncodeItems(soldItems));
		cfg.addDefault("timesUsed", 0);
		cfg.addDefault("Enabled", true);
		cfg.addDefault("Virtual", virtual);
		cfg.addDefault("Question", null);
		cfg.addDefault("QuestionAnswer", null);
		cfg.addDefault("ExecutedCommands", new ArrayList<>());

		try {
			cfg.save(newShop);
		} catch (IOException e) {
		}
	}

	public static void loadSpecificShopFromFile(File shopToLoad) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(shopToLoad);
		Main.LoadedShops.put(shopToLoad.getName().substring(0, shopToLoad.getName().length() - 4), new Shop(deserializeTransactionDates(cfg.getStringList("TransactionDates")), Base64Utils.DecodeItems(cfg.getStringList("ItemsForSale")), cfg.getDouble("Price"), cfg.getLong("Cooldown"), cfg.getBoolean("Virtual") ? null : new Location(Bukkit.getWorld(cfg.getString("Location.World")), cfg.getDouble("Location.X"), cfg.getDouble("Location.Y"), cfg.getDouble("Location.Z")), shopToLoad.getName().substring(0, shopToLoad.getName().length() - 4), cfg.getString("ShopName"), cfg.getString("ShopPermission"), cfg.getInt("timesUsed"), cfg.getStringList("ExecutedCommands"), cfg.getBoolean("Enabled"), cfg.getBoolean("Virtual"), cfg.getString("Question"), cfg.getString("QuestionAnswer")));
	}

	public static void loadAllShopFiles() {
		long start = System.currentTimeMillis();
		File folder = new File("plugins/Shop/Shops");
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles != null) {
			for (File file : listOfFiles) {
				if (file.isFile()) {
					loadSpecificShopFromFile(file);
				}
			}
		}
		Utils.sendColoredConsoleMessage(Lang.CHAT_PREFIX + "§e" + Main.LoadedShops.size() + Lang.CONSOLE_SHOPS_LOADED + (System.currentTimeMillis() - start) + "ms");
	}

	public static List<String> serializeTransactionDates(HashMap<String, String> transactionDates) {
		List<String> SerializedData = new ArrayList<String>();
		for (String s : transactionDates.keySet()) {
			SerializedData.add(s + "###" + transactionDates.get(s));
		}
		return SerializedData;
	}

	public static HashMap<String, String> deserializeTransactionDates(List<String> serializedList) {
		HashMap<String, String> DeSerializedData = new HashMap<String, String>();
		for (String s : serializedList) {
			String[] SplittedString = s.split("###");
			DeSerializedData.put(SplittedString[0], SplittedString[1]);
		}
		return DeSerializedData;
	}

	public static void deleteShopFile(String shopID) {
		File newShop = new File("plugins/Shop/Shops", shopID + ".yml");
		newShop.delete();
	}

	public static void saveSpecificShop(String shopID) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			Shop shop = Main.LoadedShops.get(shopID);
			File shopFile = new File("plugins/Shop/Shops", shopID + ".yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(shopFile);
			cfg.set("ShopName", shop.getShopName());
			cfg.set("ShopPermission", shop.getShopPermission());
			if (!shop.isVirtual()) {
				cfg.set("Location.X", shop.getShopLocation().getX());
				cfg.set("Location.Y", shop.getShopLocation().getY());
				cfg.set("Location.Z", shop.getShopLocation().getZ());
				cfg.set("Location.World", shop.getShopLocation().getWorld().getName());
			}
			cfg.set("TransactionDates", serializeTransactionDates(shop.getTransactionDates()));
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
		});
	}

	public static void saveAllShops() {
		for (String ID : Main.LoadedShops.keySet()) {
			saveSpecificShop(ID);
		}
	}

	public static void reloadAllData() {
		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			for (Shop shop : Main.LoadedShops.values())
				shop.stopParticles();
			Main.LoadedShops.clear();
			Main.AdminPanelUser.clear();
			Main.BuyingPlayers.clear();
			Main.ActiveQuestionConversations.clear();
			Main.LoadedPrefixes.clear();
			loadPrefixes();
			loadAllShopFiles();
		});
	}

	public static void copyDefaultPrefix() {
		File file = new File("plugins/Shop", "prefix.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.options().copyDefaults(true);
		cfg.addDefault("%easteregg%", "§a§l\u2726 Easteregg abholen");
		cfg.addDefault("%halloween%", "§f\u2620 §6§lHalloween-Easteregg abholen");
		cfg.addDefault("%ak%", "§f\u2603 §2§lAdventskalender Türchen öffnen");
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadPrefixes() {
		File file = new File("plugins/Shop", "prefix.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		for (String s : cfg.getKeys(false)) {
			Main.LoadedPrefixes.put(s, cfg.getString(s));
		}
	}
}
