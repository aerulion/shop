package net.aerulion.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.aerulion.shop.CMDs.CMD_OPENSHOP;
import net.aerulion.shop.CMDs.CMD_SHOP;
import net.aerulion.shop.Listener.onChunkLoad;
import net.aerulion.shop.Listener.onChunkUnload;
import net.aerulion.shop.Listener.onEntityClick;
import net.aerulion.shop.Listener.onInventoryClick;
import net.aerulion.shop.Utils.FileManager;
import net.aerulion.shop.Utils.Shop;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	public static Main plugin;
	public static Economy economy = null;
	private static final Logger log = Logger.getLogger("Minecraft");
	public static HashMap<String, Shop> LoadedShops = new HashMap<String, Shop>();
	public static HashMap<String, String> BuyingPlayers = new HashMap<String, String>();
	public static HashMap<String, String> AdminPanelUser = new HashMap<String, String>();
	public static HashMap<String, String> LoadedPrefixes = new HashMap<String, String>();
	public static List<String> ActiveQuestionConversations = new ArrayList<String>();

	@Override
	public void onEnable() {
		plugin = this;
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getServer().getPluginManager().registerEvents(new onEntityClick(), this);
		getServer().getPluginManager().registerEvents(new onChunkLoad(), this);
		getServer().getPluginManager().registerEvents(new onChunkUnload(), this);
		getServer().getPluginManager().registerEvents(new onInventoryClick(), this);
		getCommand("shop").setExecutor(new CMD_SHOP());
		getCommand("shop").setTabCompleter(new CMD_SHOP());
		getCommand("openshop").setExecutor(new CMD_OPENSHOP());
		FileManager.loadAllShopFiles();
		FileManager.copyDefaultPrefix();
		FileManager.loadPrefixes();

	}

	@Override
	public void onDisable() {

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
		return economy != null;
	}
}
