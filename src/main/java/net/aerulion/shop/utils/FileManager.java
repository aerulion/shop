package net.aerulion.shop.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import net.aerulion.erenos.utils.base64.Base64Utils;
import net.aerulion.shop.Main;
import net.aerulion.shop.task.ShopSaveTask;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileManager {

  public static void loadSpecificShopFromFile(final @NotNull File shopToLoad) {
    final @NotNull FileConfiguration cfg = YamlConfiguration.loadConfiguration(shopToLoad);
    Main.LOADED_SHOPS.put(shopToLoad.getName().substring(0, shopToLoad.getName().length() - 4),
        new Shop(deserializeTransactionDates(cfg.getStringList("TransactionDates")),
            Base64Utils.decodeItemStacks(cfg.getStringList("ItemsForSale")), cfg.getDouble("Price"),
            cfg.getLong("Cooldown"), cfg.getBoolean("Virtual") ? null :
            new Location(Bukkit.getWorld(cfg.getString("Location.World")), cfg.getDouble("Location.X"),
                cfg.getDouble("Location.Y"), cfg.getDouble("Location.Z")),
            shopToLoad.getName().substring(0, shopToLoad.getName().length() - 4), cfg.getString("ShopName"),
            cfg.getString("ShopPermission"), cfg.getInt("timesUsed"), cfg.getStringList("ExecutedCommands"),
            cfg.getBoolean("Enabled"), cfg.getBoolean("Virtual"), cfg.getString("Question"),
            cfg.getString("QuestionAnswer")));
  }

  public static void saveSpecificShopToFile(final String shopID) {
    new ShopSaveTask(shopID);
  }

  public static void loadAllShopFiles() {
    final long start = System.currentTimeMillis();
    final @NotNull File folder = new File(Main.plugin.getDataFolder().getPath() + "/Shops");
    final File @Nullable [] listOfFiles = folder.listFiles();
    if (listOfFiles != null) {
      for (final @NotNull File file : listOfFiles) {
        if (file.isFile()) {
          try {
            loadSpecificShopFromFile(file);
          } catch (final @NotNull Exception exception) {
            Main.plugin.getLogger().log(Level.SEVERE, "Failed to load shop from " + file.getName(), exception);
          }
        }
      }
    }
    Main.plugin.getComponentLogger().info(LegacyComponentSerializer.legacySection().deserialize(
        Lang.CHAT_PREFIX + "§e" + Main.LOADED_SHOPS.size() + Lang.CONSOLE_SHOPS_LOADED +
            (System.currentTimeMillis() - start) + "ms"));
  }

  public static @NotNull List<String> serializeTransactionDates(final @NotNull Map<String, String> transactionDates) {
    final @NotNull List<String> serializedData = new ArrayList<>();
    for (final Entry<String, String> entry : transactionDates.entrySet()) {
      serializedData.add(entry.getKey() + "###" + entry.getValue());
    }
    return serializedData;
  }

  public static @NotNull Map<String, String> deserializeTransactionDates(final @NotNull List<String> serializedList) {
    final @NotNull HashMap<String, String> deSerializedData = new HashMap<>();
    for (final @NotNull String s : serializedList) {
      final String @NotNull [] split = s.split("###");
      deSerializedData.put(split[0], split[1]);
    }
    return deSerializedData;
  }

  public static void deleteShopFile(final String shopID) {
    final @NotNull File newShop = new File(Main.plugin.getDataFolder().getPath() + "/Shops", shopID + ".yml");
    newShop.delete();
  }

  public static void reloadAllData() {
    Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
      for (final @NotNull Shop shop : Main.LOADED_SHOPS.values()) {
        shop.stopParticles();
      }
      Main.LOADED_SHOPS.clear();
      Main.ADMIN_PANEL_USER.clear();
      Main.BUYING_PLAYERS.clear();
      Main.LOADED_PREFIXES.clear();
      loadPrefixes();
      loadAllShopFiles();
    });
  }

  public static void copyDefaultPrefix() {
    final @NotNull File file = new File(Main.plugin.getDataFolder(), "prefix.yml");
    final @NotNull FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    cfg.options().copyDefaults(true);
    cfg.addDefault("%easteregg%", "§a§l\u2726 Easteregg abholen");
    cfg.addDefault("%halloween%", "§f\u2620 §6§lHalloween-Easteregg abholen");
    cfg.addDefault("%ak%", "§f\u2603 §2§lAdventskalender Türchen öffnen");
    try {
      cfg.save(file);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  public static void loadPrefixes() {
    final @NotNull File file = new File(Main.plugin.getDataFolder(), "prefix.yml");
    final @NotNull FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    for (final @NotNull String s : cfg.getKeys(false)) {
      Main.LOADED_PREFIXES.put(s, cfg.getString(s));
    }
  }

}