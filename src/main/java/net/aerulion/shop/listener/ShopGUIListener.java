package net.aerulion.shop.listener;

import net.aerulion.erenos.menu.input.ErenosInput;
import net.aerulion.shop.Main;
import net.aerulion.shop.utils.Lang;
import net.aerulion.shop.utils.Shop;
import net.aerulion.shop.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class ShopGUIListener implements Listener {

  @EventHandler
  public void onInvClick(final @NotNull InventoryClickEvent e) {
    if (e.getWhoClicked() instanceof Player) {
      if (ChatColor.stripColor(e.getView().getTitle()).startsWith("Shop | ")) {
        if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
            e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
          e.setCancelled(true);
          return;
        }
        if (e.getAction() == InventoryAction.PLACE_ALL || e.getAction() == InventoryAction.PLACE_ONE ||
            e.getAction() == InventoryAction.PLACE_SOME) {
          if ((e.getRawSlot() >= 0 && e.getRawSlot() <= e.getView().getTopInventory().getSize() - 1)) {
            e.setCancelled(true);
            return;
          }

        }
        if (e.getAction() == InventoryAction.HOTBAR_SWAP) {
          e.setCancelled(true);
          return;
        }
        if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
          if ((e.getRawSlot() >= 0 && e.getRawSlot() <= e.getView().getTopInventory().getSize() - 1)) {
            if (!(e.getWhoClicked().hasPermission("shop.admin") && e.getAction() == InventoryAction.CLONE_STACK)) {
              e.setCancelled(true);
            }
            if (e.getCurrentItem().getType() == Material.LIME_DYE &&
                (e.getCurrentItem().getItemMeta().getLore().get(1).equals("§fDie obigen Items kaufen"))) {
              e.getWhoClicked().closeInventory();
              Util.buyItem((Player) e.getWhoClicked());
            }
            if ((e.getCurrentItem().getType() == Material.BARRIER &&
                (e.getSlot() == (e.getView().getTopInventory().getSize() - 5)))) {
              ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(),
                  Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.2F, 2.0F);
            }
          }
        }
      }
      if (e.getView().getTitle().equals(Lang.INVENTORY_NAME_ADMIN)) {
        if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
          if ((e.getRawSlot() >= 0 && e.getRawSlot() <= e.getView().getTopInventory().getSize() - 1)) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.GOLD_INGOT) {
              e.getWhoClicked().closeInventory();
              ErenosInput.menu((Player) e.getWhoClicked(), "Gib den neuen Preis ein:").validate(s -> {
                try {
                  Double.parseDouble(s);
                  return true;
                } catch (final NumberFormatException exception) {
                  return false;
                }
              }).onSubmit((player, s) -> Util.setNewShopPrice(player, Double.parseDouble(s))).request();
            }
            if (e.getCurrentItem().getType() == Material.CHEST) {
              e.getWhoClicked().closeInventory();
              Util.setNewShopItems((Player) e.getWhoClicked());
            }
            if (e.getCurrentItem().getType() == Material.CLOCK) {
              e.getWhoClicked().closeInventory();
              ErenosInput.menu((Player) e.getWhoClicked(),
                      "Gib das neue Limit ein. Nutze das Format Tage:Stunden oder -Anzahl:")
                  .validate(Util::checkCooldownPattern).onSubmit(Util::setNewShopCooldown).request();
            }
            if (e.getCurrentItem().getType() == Material.STRUCTURE_VOID) {
              e.getWhoClicked().closeInventory();
              Util.resetShopTransactions((Player) e.getWhoClicked());
            }
            if (e.getCurrentItem().getType() == Material.BARRIER) {
              e.getWhoClicked().closeInventory();
              Util.deleteShop(Main.ADMIN_PANEL_USER.get(e.getWhoClicked().getName()));
              Util.finishAdminSession(e.getWhoClicked().getName());
            }
            if (e.getCurrentItem().getType() == Material.TRIPWIRE_HOOK) {
              e.getWhoClicked().closeInventory();
              Util.updatePosition(Main.ADMIN_PANEL_USER.get(e.getWhoClicked().getName()),
                  e.getWhoClicked().getLocation());
              Util.finishAdminSession(e.getWhoClicked().getName());
            }
            if (e.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
              e.getWhoClicked().closeInventory();
              ErenosInput.menu((Player) e.getWhoClicked(),
                      "Gib die neue Permission ein. Das 'shop.' wird automatisch an den Anfang hinzugefügt:")
                  .validate(s -> !s.contains(" ")).onSubmit(Util::setNewShopPermission).request();
            }
            if (e.getCurrentItem().getType() == Material.OAK_SIGN) {
              e.getWhoClicked().closeInventory();
              ErenosInput.menu((Player) e.getWhoClicked(), "Gib den neuen Namen ein:").maxLength(256)
                  .onSubmit(Util::setNewShopName).request();
            }
            if (e.getCurrentItem().getType() == Material.COMMAND_BLOCK) {
              e.getWhoClicked().closeInventory();
              if (e.getClick() == ClickType.DROP) {
                Util.resetShopCommands((Player) e.getWhoClicked());
              } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                Util.pasteShopCommands((Player) e.getWhoClicked());
              } else if (e.getClick() == ClickType.SHIFT_RIGHT) {
                Util.copyShopCommands((Player) e.getWhoClicked());
              } else if (e.getClick() == ClickType.RIGHT) {
                final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(e.getWhoClicked().getName()));
                String noPrefixShopName = shop.getShopName();
                for (final @NotNull String s : Main.LOADED_PREFIXES.keySet()) {
                  noPrefixShopName = noPrefixShopName.replaceAll(s, "");
                }
                for (final @NotNull String cmd : shop.getExecutedCommands()) {
                  Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                      cmd.replace("%player%", e.getWhoClicked().getName()).replace("%shopname%", noPrefixShopName));
                }
                Util.finishAdminSession(e.getWhoClicked().getName());
              } else {
                ErenosInput.menu((Player) e.getWhoClicked(),
                        "Gib den zu hinzufügenden Befehl ein. Das / wird automatisch hinzugefügt. Folgende Variablen sind verfügbar: %player% %shopname%:")
                    .onSubmit(Util::setNewShopCommands).request();
              }
            }
            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
              e.getWhoClicked().closeInventory();
              if (e.getWhoClicked().getInventory().getItemInMainHand().getType() == Material.AIR) {
                e.getWhoClicked().sendMessage(Lang.ERROR_NO_ITEM_IN_HAND);
              } else {
                Util.updateHead(Main.ADMIN_PANEL_USER.get(e.getWhoClicked().getName()),
                    e.getWhoClicked().getInventory().getItemInMainHand());
                e.getWhoClicked().sendMessage(Lang.HEAD_UPDATED);
              }
              Util.finishAdminSession(e.getWhoClicked().getName());
            }
            if (e.getCurrentItem().getType() == Material.LIME_DYE || e.getCurrentItem().getType() == Material.RED_DYE) {
              e.getWhoClicked().closeInventory();
              Util.toggleEnabled((Player) e.getWhoClicked());
            }
            if (e.getCurrentItem().getType() == Material.BOOK) {
              e.getWhoClicked().closeInventory();
              if (e.getClick() == ClickType.DROP) {
                Util.resetShopQuestion((Player) e.getWhoClicked());
              } else if (e.getClick() == ClickType.RIGHT) {
                ErenosInput.menu((Player) e.getWhoClicked(), "Gib die neue Antwort ein:").maxLength(512)
                    .onSubmit(Util::setNewShopQuestionAnswer).request();
              } else {
                ErenosInput.menu((Player) e.getWhoClicked(), "Gib die neue Frage ein:").maxLength(512)
                    .onSubmit(Util::setNewShopQuestion).request();
              }
            }
          }
        }
      }
    }
  }

}