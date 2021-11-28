package net.aerulion.shop.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.aerulion.nucleus.api.base64.Base64Utils;
import net.aerulion.nucleus.api.console.ConsoleUtils;
import net.aerulion.shop.Main;
import net.aerulion.shop.task.ShopSaveTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {

  public static void loadSpecificShopFromFile(File shopToLoad) {
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(shopToLoad);
    Main.LoadedShops.put(shopToLoad.getName().substring(0, shopToLoad.getName().length() - 4),
        new Shop(deserializeTransactionDates(cfg.getStringList("TransactionDates")),
            Base64Utils.decodeItemStackList(cfg.getStringList("ItemsForSale")),
            cfg.getDouble("Price"), cfg.getLong("Cooldown"), cfg.getBoolean("Virtual") ? null
            : new Location(Bukkit.getWorld(cfg.getString("Location.World")),
                cfg.getDouble("Location.X"), cfg.getDouble("Location.Y"),
                cfg.getDouble("Location.Z")),
            shopToLoad.getName().substring(0, shopToLoad.getName().length() - 4),
            cfg.getString("ShopName"), cfg.getString("ShopPermission"), cfg.getInt("timesUsed"),
            cfg.getStringList("ExecutedCommands"), cfg.getBoolean("Enabled"),
            cfg.getBoolean("Virtual"), cfg.getString("Question"), cfg.getString("QuestionAnswer")));
  }

  public static void saveSpecificShopToFile(String shopID) {
    new ShopSaveTask(shopID);
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
    ConsoleUtils.sendColoredConsoleMessage(
        Lang.CHAT_PREFIX + "§e" + Main.LoadedShops.size() + Lang.CONSOLE_SHOPS_LOADED + (
            System.currentTimeMillis() - start) + "ms");
  }

  public static List<String> serializeTransactionDates(HashMap<String, String> transactionDates) {
    List<String> SerializedData = new ArrayList<>();
    for (String s : transactionDates.keySet()) {
      SerializedData.add(s + "###" + transactionDates.get(s));
    }
    return SerializedData;
  }

  public static HashMap<String, String> deserializeTransactionDates(List<String> serializedList) {
    HashMap<String, String> DeSerializedData = new HashMap<>();
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

  public static void reloadAllData() {
    Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
      for (Shop shop : Main.LoadedShops.values()) {
        shop.stopParticles();
      }
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