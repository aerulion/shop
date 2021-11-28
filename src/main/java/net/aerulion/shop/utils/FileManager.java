package net.aerulion.shop.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.aerulion.nucleus.api.base64.Base64Utils;
import net.aerulion.nucleus.api.console.ConsoleUtils;
import net.aerulion.shop.Main;
import net.aerulion.shop.task.ShopSaveTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileManager {

  public static void loadSpecificShopFromFile(@NotNull File shopToLoad) {
    @NotNull FileConfiguration cfg = YamlConfiguration.loadConfiguration(shopToLoad);
    Main.loadedShops.put(shopToLoad.getName().substring(0, shopToLoad.getName().length() - 4),
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
    @NotNull File folder = new File("plugins/Shop/Shops");
    File @Nullable [] listOfFiles = folder.listFiles();
    if (listOfFiles != null) {
      for (@NotNull File file : listOfFiles) {
        if (file.isFile()) {
          loadSpecificShopFromFile(file);
        }
      }
    }
    ConsoleUtils.sendColoredConsoleMessage(
        Lang.CHAT_PREFIX + "§e" + Main.loadedShops.size() + Lang.CONSOLE_SHOPS_LOADED + (
            System.currentTimeMillis() - start) + "ms");
  }

  public static @NotNull List<String> serializeTransactionDates(
      @NotNull Map<String, String> transactionDates) {
    @NotNull List<String> serializedData = new ArrayList<>();
    for (String s : transactionDates.keySet()) {
      serializedData.add(s + "###" + transactionDates.get(s));
    }
    return serializedData;
  }

  public static @NotNull Map<String, String> deserializeTransactionDates(
      @NotNull List<String> serializedList) {
    @NotNull HashMap<String, String> deSerializedData = new HashMap<>();
    for (@NotNull String s : serializedList) {
      String @NotNull [] split = s.split("###");
      deSerializedData.put(split[0], split[1]);
    }
    return deSerializedData;
  }

  public static void deleteShopFile(String shopID) {
    @NotNull File newShop = new File("plugins/Shop/Shops", shopID + ".yml");
    newShop.delete();
  }

  public static void reloadAllData() {
    Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
      for (@NotNull Shop shop : Main.loadedShops.values()) {
        shop.stopParticles();
      }
      Main.loadedShops.clear();
      Main.adminPanelUser.clear();
      Main.buyingPlayers.clear();
      Main.activeQuestionConversations.clear();
      Main.loadedPrefixes.clear();
      loadPrefixes();
      loadAllShopFiles();
    });
  }

  public static void copyDefaultPrefix() {
    @NotNull File file = new File("plugins/Shop", "prefix.yml");
    @NotNull FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
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
    @NotNull File file = new File("plugins/Shop", "prefix.yml");
    @NotNull FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    for (@NotNull String s : cfg.getKeys(false)) {
      Main.loadedPrefixes.put(s, cfg.getString(s));
    }
  }
}