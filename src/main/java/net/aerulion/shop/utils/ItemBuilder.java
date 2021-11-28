package net.aerulion.shop.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.aerulion.nucleus.api.item.ItemUtils;
import net.aerulion.nucleus.api.string.StringUtils;
import net.aerulion.shop.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemBuilder {

  public static @NotNull ItemStack createDeleteShopBarrier() {
    return ItemUtils.buildItemStack(Material.BARRIER, "§c§l\u2716 Löschen",
        Collections.singletonList("§7§oLöscht den gesamten Shop"), false);
  }

  public static @NotNull ItemStack createResetTransactionsStructureVoid() {
    return ItemUtils.buildItemStack(Material.STRUCTURE_VOID, "§e§lSpielerdaten zurücksetzen",
        Arrays.asList("§7§oErmöglicht es allen Spielern, erneut",
            "§7§oetwas im Shop kaufen zu können"), false);
  }

  public static @NotNull ItemStack createChangePriceGoldIngot() {
    return ItemUtils.buildItemStack(Material.GOLD_INGOT, "§e§lPreis ändern",
        Collections.singletonList("§7§oÄndert den Preis des Shops"), false);
  }

  public static @NotNull ItemStack createNewItemSetChest() {
    return ItemUtils.buildItemStack(Material.CHEST, "§e§lNeue Items",
        Arrays.asList("§7§oSetzt den Inhalt des aktuellen Inventars",
            "§7§oals zu kaufende Items des Shops"), false);
  }

  public static @NotNull ItemStack createNewCooldownClock() {
    return ItemUtils.buildItemStack(Material.CLOCK, "§e§lLimit ändern",
        Collections.singletonList("§7§oändert das Limit des Shops"), false);
  }

  public static @NotNull ItemStack createUpdatePositionTripwireHook() {
    return ItemUtils.buildItemStack(Material.TRIPWIRE_HOOK, "§e§lPosition updaten",
        Arrays.asList("§7§oSucht im Umkreis von 10 Blöcken",
            "§7§onach der neuen Armorstand Position"), false);
  }

  public static @NotNull ItemStack createNewPermissionEnchantedBook() {
    return ItemUtils.buildItemStack(Material.ENCHANTED_BOOK, "§e§lPermission ändern",
        Collections.singletonList("§7§oÄndert die Permission des Shops"), false);
  }

  public static @NotNull ItemStack createNewNameSign() {
    return ItemUtils.buildItemStack(Material.OAK_SIGN, "§e§lNamen ändern",
        Collections.singletonList("§7§oÄndert den Namen des Shops"), false);
  }

  public static @NotNull ItemStack createNewCommandCommandBlock() {
    return ItemUtils.buildItemStack(Material.COMMAND_BLOCK, "§e§lBefehl hinzufügen",
        Arrays.asList("§7§oLinksklick: Befehl hinzufügen",
            "§7§oRechtsklick: Befehle testen/ausführen", "§7§oItem Droppen: Alle Befehle löschen"),
        false);
  }

  public static @NotNull ItemStack createNewHeadPlayerHead() {
    return ItemUtils.buildItemStack(Material.PLAYER_HEAD, "§e§lKopf ändern",
        Collections.singletonList("§7§oSetzt das Item in der Main-Hand als Kopf"), false);
  }

  public static @NotNull ItemStack createToggleEnabledDye(@NotNull Shop shop) {
    return ItemUtils.buildItemStack(shop.isEnabled() ? Material.RED_DYE : Material.LIME_DYE,
        shop.isEnabled() ? "§e§lShop deaktivieren" : "§e§lShop aktivieren",
        Collections.singletonList("§7§oAktviert/Deaktiviert den Shop"), false);
  }

  public static @NotNull ItemStack createQuestionBook() {
    return ItemUtils.buildItemStack(Material.BOOK, "§e§lPasswort-Frage ändern",
        Arrays.asList("§7§oLinksklick: Frage ändern", "§7§oRechtsklick: Antwort ändern",
            "§7§oItem Droppen: Passwortabfrage deaktivieren"), false);
  }

  public static @NotNull ItemStack createStatisticItem(@NotNull Shop shop) {
    @NotNull List<String> Lore = new ArrayList<>();
    Lore.add("§eName: §7" + ChatColor.translateAlternateColorCodes('&', shop.getShopName()));
    Lore.add("§eShop-UUID: §7" + shop.getID());
    Lore.add("§ePermission: §7shop." + shop.getShopPermission());
    Lore.add("§eAktiviert: §7" + (shop.isEnabled() ? "Ja" : "Nein"));
    Lore.add("§ePreis: §7" + shop.getPrice());
    Lore.add("§eLimit: §7" + shop.getCooldown());
    Lore.add("§eWie oft gekauft: §7" + shop.getTimesUsed() + " Mal");
    if (shop.getQuestion() != null) {
      Lore.add("§ePasswortabfrage:");
      Lore.add(" §eF: §7" + shop.getQuestion());
      Lore.add(" §eA: §7" + shop.getQuestionAnswer());
    } else {
      Lore.add("§ePasswortabfrage: §7Deaktiviert");
    }
    if (!shop.getExecutedCommands().isEmpty()) {
      Lore.add("§eBefehle: §7");
      for (String cmd : shop.getExecutedCommands()) {
        boolean first = true;
        for (String s : StringUtils.wrapString(cmd, 30)) {
          if (first) {
            Lore.add("§e- §7/" + s);
            first = false;
          } else {
            Lore.add("§e   §7" + s);
          }
        }
      }
    }
    return ItemUtils.buildItemStack(Material.KNOWLEDGE_BOOK, "§a§lInfos & Statistik", Lore, false);
  }

  public static @NotNull ItemStack createBuyButton(@NotNull Shop shop, @NotNull Player player) {
    if (shop.isAllowedToBuy(player.getUniqueId().toString())) {
      @NotNull String price = shop.getPrice() + "§7 CT";
      String ItemName = "§a§l\u27A1 Kaufen";
      if (shop.getPrice() == 0) {
        price = "§6§lGRATIS";
        ItemName = "§a§l\u2726 Geschenk abholen";
        for (@NotNull String s : Main.loadedPrefixes.keySet()) {
          if (shop.getShopName().contains(s)) {
            ItemName = Main.loadedPrefixes.get(s);
          }
        }
      }
      return ItemUtils.buildItemStack(Material.LIME_DYE, ItemName,
          Arrays.asList("§f§m                              ", "§fDie obigen Items kaufen",
              "§f§m                              ", "§6§lPreis§8: §6" + price,
              "§6§lLimit§8: " + Util.cooldownStringBuilder(shop.getCooldown(), "6", "7")
                  + Util.remainingTransactionsString(shop, player, "7", "7"),
              "§f§m                              "), false);
    } else {
      @NotNull List<String> reason = Collections.singletonList(
          "§7Du kannst diesen Shop nur " + Math.abs(shop.getCooldown()) + "x benutzen");
      if (shop.getCooldown() > 0) {
        reason = Arrays.asList("§7Du kannst diesen Shop erst wieder in",
            Util.cooldownStringBuilder(shop.getTimeRemaining(player.getUniqueId().toString()), "c",
                "7"), "§7benutzen");
      }
      return ItemUtils.buildItemStack(Material.BARRIER, "§c§l\u2716 Bereits genutzt", reason,
          false);
    }
  }
}