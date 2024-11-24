package net.aerulion.shop;

import java.util.HashMap;
import java.util.Map;
import net.aerulion.erenos.utils.console.ConsoleUtils;
import net.aerulion.shop.cmd.CMD_openshop;
import net.aerulion.shop.cmd.CMD_particleshop;
import net.aerulion.shop.listener.EntityInteractListener;
import net.aerulion.shop.listener.EntityLoadListener;
import net.aerulion.shop.listener.ShopGUIListener;
import net.aerulion.shop.utils.FileManager;
import net.aerulion.shop.utils.Lang;
import net.aerulion.shop.utils.Shop;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.conversations.Conversation;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Main extends JavaPlugin {

  public static final @NotNull Map<String, Shop> LOADED_SHOPS = new HashMap<>();
  public static final @NotNull Map<String, String> BUYING_PLAYERS = new HashMap<>();
  public static final @NotNull Map<String, String> ADMIN_PANEL_USER = new HashMap<>();
  public static final @NotNull Map<String, String> LOADED_PREFIXES = new HashMap<>();
  public static final @NotNull Map<String, Conversation> ACTIVE_QUESTION_CONVERSATIONS = new HashMap<>();
  public static Main plugin;

  @Override
  public void onDisable() {
    ConsoleUtils.sendColoredConsoleMessage(
        LegacyComponentSerializer.legacySection().deserialize(Lang.CONSOLE_DISABLING));
    ConsoleUtils.sendColoredConsoleMessage(
        LegacyComponentSerializer.legacySection().deserialize(Lang.CONSOLE_PLUGIN_DISABLED));
  }

  @Override
  public void onEnable() {
    ConsoleUtils.sendColoredConsoleMessage(
        LegacyComponentSerializer.legacySection().deserialize(Lang.CONSOLE_ENABLING));
    plugin = this;
    getServer().getPluginManager().registerEvents(new EntityInteractListener(), this);
    getServer().getPluginManager().registerEvents(new EntityLoadListener(), this);
    getServer().getPluginManager().registerEvents(new ShopGUIListener(), this);
    getCommand("particleshop").setExecutor(new CMD_particleshop());
    getCommand("particleshop").setTabCompleter(new CMD_particleshop());
    getCommand("openshop").setExecutor(new CMD_openshop());
    getCommand("openshop").setTabCompleter(new CMD_openshop());
    FileManager.loadAllShopFiles();
    FileManager.copyDefaultPrefix();
    FileManager.loadPrefixes();
    ConsoleUtils.sendColoredConsoleMessage(
        LegacyComponentSerializer.legacySection().deserialize(Lang.CONSOLE_PLUGIN_ENABLED));
  }

}