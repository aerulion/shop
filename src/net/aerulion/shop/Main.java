package net.aerulion.shop;

import net.aerulion.nucleus.api.console.ConsoleUtils;
import net.aerulion.shop.cmd.CMD_openshop;
import net.aerulion.shop.cmd.CMD_shop;
import net.aerulion.shop.listener.ChunkLoadListener;
import net.aerulion.shop.listener.EntityInteractListener;
import net.aerulion.shop.listener.ShopGUIListener;
import net.aerulion.shop.utils.FileManager;
import net.aerulion.shop.utils.Lang;
import net.aerulion.shop.utils.Shop;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.conversations.Conversation;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static Main plugin;
    public static Economy economy = null;
    private static final Logger log = Logger.getLogger("Minecraft");
    public static HashMap<String, Shop> LoadedShops = new HashMap<>();
    public static HashMap<String, String> BuyingPlayers = new HashMap<>();
    public static HashMap<String, String> AdminPanelUser = new HashMap<>();
    public static HashMap<String, String> LoadedPrefixes = new HashMap<>();
    public static HashMap<String, Conversation> ActiveQuestionConversations = new HashMap<>();

    @Override
    public void onEnable() {
        ConsoleUtils.sendColoredConsoleMessage(Lang.CONSOLE_ENABLING);
        plugin = this;
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(new EntityInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ChunkLoadListener(), this);
        getServer().getPluginManager().registerEvents(new ShopGUIListener(), this);
        getCommand("shop").setExecutor(new CMD_shop());
        getCommand("shop").setTabCompleter(new CMD_shop());
        getCommand("openshop").setExecutor(new CMD_openshop());
        getCommand("openshop").setTabCompleter(new CMD_openshop());
        FileManager.loadAllShopFiles();
        FileManager.copyDefaultPrefix();
        FileManager.loadPrefixes();
        ConsoleUtils.sendColoredConsoleMessage(Lang.CONSOLE_PLUGIN_ENABLED);
    }

    @Override
    public void onDisable() {
        ConsoleUtils.sendColoredConsoleMessage(Lang.CONSOLE_DISABLING);
        ConsoleUtils.sendColoredConsoleMessage(Lang.CONSOLE_PLUGIN_DISABLED);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }
}