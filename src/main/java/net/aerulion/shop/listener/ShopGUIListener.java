package net.aerulion.shop.listener;

import net.aerulion.shop.Main;
import net.aerulion.shop.conversation.CommandConversation;
import net.aerulion.shop.conversation.CooldownConversation;
import net.aerulion.shop.conversation.NameConversation;
import net.aerulion.shop.conversation.PermissionConversation;
import net.aerulion.shop.conversation.PriceConversation;
import net.aerulion.shop.conversation.QuestionAnswerConversation;
import net.aerulion.shop.conversation.QuestionConversation;
import net.aerulion.shop.utils.Lang;
import net.aerulion.shop.utils.Shop;
import net.aerulion.shop.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
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
        if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY
            || e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
          e.setCancelled(true);
          return;
        }
        if (e.getAction() == InventoryAction.PLACE_ALL || e.getAction() == InventoryAction.PLACE_ONE
            || e.getAction() == InventoryAction.PLACE_SOME) {
          if ((e.getRawSlot() >= 0
              && e.getRawSlot() <= e.getView().getTopInventory().getSize() - 1)) {
            e.setCancelled(true);
            return;
          }

        }
        if (e.getAction() == InventoryAction.HOTBAR_SWAP) {
          e.setCancelled(true);
          return;
        }
        if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
          if ((e.getRawSlot() >= 0
              && e.getRawSlot() <= e.getView().getTopInventory().getSize() - 1)) {
            if (!(e.getWhoClicked().hasPermission("shop.admin")
                && e.getAction() == InventoryAction.CLONE_STACK)) {
              e.setCancelled(true);
            }
            if (e.getCurrentItem().getType() == Material.LIME_DYE && (e.getCurrentItem()
                .getItemMeta().getLore().get(1).equals("§fDie obigen Items kaufen"))) {
              e.getWhoClicked().closeInventory();
              Util.buyItem((Player) e.getWhoClicked());
            }
            if ((e.getCurrentItem().getType() == Material.BARRIER && (e.getSlot() == (
                e.getView().getTopInventory().getSize() - 5)))) {
              ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(),
                  Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.2F, 2.0F);
            }
          }
        }
      }
      if (e.getView().getTitle().equals(Lang.INVENTORY_NAME_ADMIN)) {
        if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
          if ((e.getRawSlot() >= 0
              && e.getRawSlot() <= e.getView().getTopInventory().getSize() - 1)) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.GOLD_INGOT) {
              e.getWhoClicked().closeInventory();
              final @NotNull ConversationFactory conversationFactory = new ConversationFactory(
                  Main.plugin);
              final @NotNull ConversationPrefix conversationPrefix = prefix -> Lang.CHAT_PREFIX;
              final @NotNull Conversation conversation = conversationFactory.withFirstPrompt(
                      new PriceConversation()).withEscapeSequence("stop").withModality(false)
                  .withLocalEcho(false).withPrefix(conversationPrefix)
                  .buildConversation((Player) e.getWhoClicked());
              conversation.begin();
            }
            if (e.getCurrentItem().getType() == Material.CHEST) {
              e.getWhoClicked().closeInventory();
              Util.setNewShopItems((Player) e.getWhoClicked());
            }
            if (e.getCurrentItem().getType() == Material.CLOCK) {
              e.getWhoClicked().closeInventory();
              final @NotNull ConversationFactory conversationFactory = new ConversationFactory(
                  Main.plugin);
              final @NotNull ConversationPrefix conversationPrefix = prefix -> Lang.CHAT_PREFIX;
              final @NotNull Conversation conversation = conversationFactory.withFirstPrompt(
                      new CooldownConversation()).withEscapeSequence("stop").withModality(false)
                  .withLocalEcho(false).withPrefix(conversationPrefix)
                  .buildConversation((Player) e.getWhoClicked());
              conversation.begin();
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
              final @NotNull ConversationFactory conversationFactory = new ConversationFactory(
                  Main.plugin);
              final @NotNull ConversationPrefix conversationPrefix = prefix -> Lang.CHAT_PREFIX;
              final @NotNull Conversation conversation = conversationFactory.withFirstPrompt(
                      new PermissionConversation()).withEscapeSequence("stop").withModality(false)
                  .withLocalEcho(false).withPrefix(conversationPrefix)
                  .buildConversation((Player) e.getWhoClicked());
              conversation.begin();
            }
            if (e.getCurrentItem().getType() == Material.OAK_SIGN) {
              e.getWhoClicked().closeInventory();
              final @NotNull ConversationFactory conversationFactory = new ConversationFactory(
                  Main.plugin);
              final @NotNull ConversationPrefix conversationPrefix = prefix -> Lang.CHAT_PREFIX;
              final @NotNull Conversation conversation = conversationFactory.withFirstPrompt(
                      new NameConversation()).withEscapeSequence("stop").withModality(false)
                  .withLocalEcho(false).withPrefix(conversationPrefix)
                  .buildConversation((Player) e.getWhoClicked());
              conversation.begin();
            }
            if (e.getCurrentItem().getType() == Material.COMMAND_BLOCK) {
              e.getWhoClicked().closeInventory();
              if (e.getClick() == ClickType.DROP) {
                Util.resetShopCommands((Player) e.getWhoClicked());
              } else if (e.getClick() == ClickType.RIGHT) {
                final Shop shop = Main.LOADED_SHOPS.get(
                    Main.ADMIN_PANEL_USER.get(e.getWhoClicked().getName()));
                String noPrefixShopName = shop.getShopName();
                for (final @NotNull String s : Main.LOADED_PREFIXES.keySet()) {
                  noPrefixShopName = noPrefixShopName.replaceAll(s, "");
                }
                for (final @NotNull String cmd : shop.getExecutedCommands()) {
                  Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                      cmd.replace("%player%", e.getWhoClicked().getName())
                          .replace("%shopname%", noPrefixShopName));
                }
                Util.finishAdminSession(e.getWhoClicked().getName());
              } else {
                final @NotNull ConversationFactory conversationFactory = new ConversationFactory(
                    Main.plugin);
                final @NotNull ConversationPrefix conversationPrefix = prefix -> Lang.CHAT_PREFIX;
                final @NotNull Conversation conversation = conversationFactory.withFirstPrompt(
                        new CommandConversation()).withEscapeSequence("stop").withModality(false)
                    .withLocalEcho(false).withPrefix(conversationPrefix)
                    .buildConversation((Player) e.getWhoClicked());
                conversation.begin();
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
            if (e.getCurrentItem().getType() == Material.LIME_DYE
                || e.getCurrentItem().getType() == Material.RED_DYE) {
              e.getWhoClicked().closeInventory();
              Util.toggleEnabled((Player) e.getWhoClicked());
            }
            if (e.getCurrentItem().getType() == Material.BOOK) {
              e.getWhoClicked().closeInventory();
              if (e.getClick() == ClickType.DROP) {
                Util.resetShopQuestion((Player) e.getWhoClicked());
              } else if (e.getClick() == ClickType.RIGHT) {
                final @NotNull ConversationFactory conversationFactory = new ConversationFactory(
                    Main.plugin);
                final @NotNull ConversationPrefix conversationPrefix = prefix -> Lang.CHAT_PREFIX;
                final @NotNull Conversation conversation = conversationFactory.withFirstPrompt(
                        new QuestionAnswerConversation()).withEscapeSequence("stop").withModality(false)
                    .withLocalEcho(false).withPrefix(conversationPrefix)
                    .buildConversation((Player) e.getWhoClicked());
                conversation.begin();
              } else {
                final @NotNull ConversationFactory conversationFactory = new ConversationFactory(
                    Main.plugin);
                final @NotNull ConversationPrefix conversationPrefix = prefix -> Lang.CHAT_PREFIX;
                final @NotNull Conversation conversation = conversationFactory.withFirstPrompt(
                        new QuestionConversation()).withEscapeSequence("stop").withModality(false)
                    .withLocalEcho(false).withPrefix(conversationPrefix)
                    .buildConversation((Player) e.getWhoClicked());
                conversation.begin();
              }
            }
          }
        }
      }
    }
  }
}