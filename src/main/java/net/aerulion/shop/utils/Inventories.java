package net.aerulion.shop.utils;

import net.aerulion.shop.Main;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Inventories {

  public static @NotNull Inventory getAdminPanel(final @NotNull Shop shop) {
    final @NotNull Inventory inv = Bukkit.createInventory(null, 27, Lang.INVENTORY_NAME_ADMIN);
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

  public static @NotNull Inventory getUserPanel(final @NotNull Shop shop, final @NotNull Player player) {
    final int size = Util.calculateInventorySlotSize(shop);
    final int @NotNull [] lineSlots =
        {size - 18, size - 17, size - 16, size - 15, size - 14, size - 13, size - 12, size - 11, size - 10};
    final @NotNull ItemStack line =
        net.aerulion.erenos.utils.item.ItemBuilder.of(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
            .withItemName(LegacyComponentSerializer.legacySection().deserialize("§9§m          ")).asItemStack();
    String shopName = shop.getShopName();
    for (final @NotNull String s : Main.LOADED_PREFIXES.keySet()) {
      shopName = shopName.replaceAll(s, "");
    }
    final @NotNull Inventory inv = Bukkit.createInventory(null, size,
        Lang.INVENTORY_NAME_SHOP + ChatColor.translateAlternateColorCodes('&', shopName));
    for (final int i : lineSlots) {
      inv.setItem(i, line);
    }
    int slot = 0;
    for (final ItemStack is : shop.getSoldItems()) {
      inv.setItem(slot, is);
      slot++;
    }
    inv.setItem(size - 5, ItemBuilder.createBuyButton(shop, player));
    return inv;
  }

}