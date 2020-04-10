package net.aerulion.shop.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.aerulion.shop.Main;

public class Inventories {

	public static Inventory AdminPanel(Shop shop) {
		Inventory inv = Bukkit.createInventory(null, 27, Lang.AdminInventoryName);
		inv.addItem(ItemBuilder.createChangePriceGoldIngot());
		inv.addItem(ItemBuilder.createNewItemSetChest());
		inv.addItem(ItemBuilder.createNewCooldownClock());
		inv.addItem(ItemBuilder.createResetTransactionsStructureVoid());
		inv.addItem(ItemBuilder.createDeleteShopBarrier());
		inv.addItem(ItemBuilder.createUpdatePositionTripwireHook());
		inv.addItem(ItemBuilder.createNewPermissionEnchantedBook());
		inv.addItem(ItemBuilder.createNewNameSign());
		inv.addItem(ItemBuilder.createNewCommandCommandBlock());
		inv.addItem(ItemBuilder.createNewHeadPlayerHead());
		inv.addItem(ItemBuilder.createToggleEnabledDye(shop));
		inv.addItem(ItemBuilder.createQuestionBook());
		inv.setItem(22, ItemBuilder.createStatisticItem(shop));
		return inv;
	}
	
	public static Inventory UserPanel(Shop shop, Player player) {
		int size = Utils.calculateInventorySlotSize(shop);
		int[] LineSlots = { size - 18, size - 17, size - 16, size - 15, size - 14, size - 13, size - 12, size - 11, size - 10 };
		ItemStack line = ItemBuilder.buildSimpleItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "�9�m          ", null);
		String shopName = shop.getShopName();
		for (String s : Main.LoadedPrefixes.keySet()) {
			shopName = shopName.replaceAll(s, "");
		}
		Inventory inv = Bukkit.createInventory(null, size, Lang.ShopInventoryName + ChatColor.translateAlternateColorCodes('&', shopName));
		for (int i : LineSlots) {
			inv.setItem(i, line);
		}
		int slot = 0;
		for (ItemStack is : shop.getSoldItems()) {
			inv.setItem(slot, is);
			slot++;
		}
		inv.setItem(size - 5, ItemBuilder.createBuyButton(shop, player));
		return inv;
	}

}
