package net.aerulion.shop.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.aerulion.nucleus.api.chat.ChatUtils;
import net.aerulion.shop.Main;
import net.aerulion.shop.conversation.QuestionAskConversation;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Util {

  public static void openShopToPlayer(final @NotNull Player player, final @NotNull Shop shop) {
    if (player.hasPermission("shop." + shop.getShopPermission())) {
      if (shop.isEnabled()) {
        if (shop.getQuestion() != null && shop.getQuestionAnswer() != null) {
          if (Main.ACTIVE_QUESTION_CONVERSATIONS.containsKey(player.getUniqueId().toString())) {
            final Conversation conversation = Main.ACTIVE_QUESTION_CONVERSATIONS.get(
                player.getUniqueId().toString());
            conversation.abandon();
          }
          Main.BUYING_PLAYERS.put(player.getName(), shop.getID());
          player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1.3F);
          final @NotNull ConversationFactory conversationFactory = new ConversationFactory(
              Main.plugin);
          final @NotNull ConversationPrefix conversationPrefix = prefix -> Lang.CHAT_PREFIX;
          final @NotNull Conversation conversation = conversationFactory.withFirstPrompt(
                  new QuestionAskConversation()).withModality(false).withLocalEcho(false)
              .withPrefix(conversationPrefix).buildConversation(player);
          conversation.begin();
          Main.ACTIVE_QUESTION_CONVERSATIONS.put(player.getUniqueId().toString(), conversation);
        } else {
          player.openInventory(Inventories.getUserPanel(shop, player));
          Main.BUYING_PLAYERS.put(player.getName(), shop.getID());
          player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 0.5F, 1.3F);
        }
      } else {
        player.sendMessage(Lang.ERROR_SHOP_DISABLED);
        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.2F, 2.0F);
      }
    } else {
      player.sendMessage(Lang.ERROR_NO_SHOP_PERMISSION);
      player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.2F, 2.0F);
    }
  }

  public static void createNewShop(final @NotNull Player player, final double price,
      final long cooldown, final String shopName, final String shopPermission,
      final boolean virtual) {
    final @NotNull List<ItemStack> items = new ArrayList<>();
    for (final ItemStack itemstack : player.getInventory().getStorageContents()) {
      if (itemstack != null) {
        items.add(itemstack.clone());
      }

    }
    if (items.size() > 27) {
      player.sendMessage(Lang.ERROR_MAX_STACK_AMOUNT);
      return;
    }

    final String id = UUID.randomUUID().toString();
    @Nullable Location location = null;
    if (!virtual) {
      location = player.getLocation().subtract(new Vector(0F, 1.37F, 0F));
      final @NotNull ArmorStand armorstand = player.getLocation().getWorld()
          .spawn(location, ArmorStand.class);
      armorstand.setSilent(true);
      armorstand.setSmall(false);
      armorstand.getEquipment().setHelmet(new ItemStack(Material.CHEST));
      armorstand.setGravity(false);
      armorstand.setVisible(false);
      armorstand.setCustomName(id);
      armorstand.setBasePlate(false);
      armorstand.setHeadPose(
          new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0)));
    }
    final @NotNull HashMap<String, String> transactionDates = new HashMap<>();
    Main.LOADED_SHOPS.put(id,
        new Shop(transactionDates, items, price, cooldown, location, id, shopName, shopPermission,
            0, new ArrayList<>(), true, virtual, null, null));
    FileManager.saveSpecificShopToFile(id);
    Main.LOADED_SHOPS.get(id).startParticles();
    player.sendMessage(Lang.SHOP_ADDED);
  }

  public static void giveItemToPlayer(final @NotNull Shop shop, final @NotNull Player player) {
    for (final @NotNull ItemStack is : shop.getSoldItems()) {
      player.getInventory().addItem(is.clone());
    }
  }

  public static boolean hasInventorySpaceToBuy(final @NotNull Player player,
      final @NotNull Shop shop) {
    int usedSlotCount = 0;
    for (final ItemStack is : player.getInventory().getStorageContents()) {
      if (is != null) {
        usedSlotCount++;
      }
    }
    return (36 - usedSlotCount) >= shop.getSoldItems().size();
  }

  public static int calculateInventorySlotSize(final @NotNull Shop shop) {
    return ((int) (Math.ceil(shop.getSoldItems().size() / 9.0))) * 9 + 27;
  }

  public static void finishAdminSession(final String name) {
    Main.ADMIN_PANEL_USER.remove(name);
  }

  public static void finishBuySession(final String name) {
    Main.BUYING_PLAYERS.remove(name);
  }

  public static void finishQuestionSession(final String name) {
    Main.ACTIVE_QUESTION_CONVERSATIONS.remove(name);
  }

  public static void setNewShopPrice(final @NotNull Player player, final double price) {
    final double roundedprice = round(price);
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.setPrice(roundedprice);
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_PRICE + roundedprice);
  }

  public static void setNewShopQuestion(final @NotNull Player player, final String question) {
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.setQuestion(question);
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_QUESTION + question);
  }

  public static void validateQuestion(final @NotNull Player player, final @NotNull String input) {
    if (input.equalsIgnoreCase("stop")) {
      player.sendMessage(Lang.ACTION_ESCAPED);
      finishQuestionSession(player.getName());
      return;
    }
    final Shop shop = Main.LOADED_SHOPS.get(Main.BUYING_PLAYERS.get(player.getName()));
    if (input.equalsIgnoreCase(shop.getQuestionAnswer())) {
      player.openInventory(Inventories.getUserPanel(shop, player));
      player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1.3F);
    } else {
      player.sendMessage(Lang.ERROR_WRONG_QUESTION_ANSWER);
      player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F, 0.8F);
    }
    finishQuestionSession(player.getName());
  }

  public static void setNewShopQuestionAnswer(final @NotNull Player player, final String answer) {
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.setQuestionAnswer(answer);
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_QUESTION_ANSWER + answer);
  }

  public static void toggleEnabled(final @NotNull Player player) {
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.toggleEnabled();
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.TOGGLED_ENABLED);
  }

  public static void setNewShopItems(final @NotNull Player player) {
    final @NotNull List<ItemStack> items = new ArrayList<>();
    for (final ItemStack itemstack : player.getInventory().getStorageContents()) {
      if (itemstack != null) {
        items.add(itemstack.clone());
      }
    }
    if (items.size() > 27) {
      player.sendMessage(Lang.ERROR_MAX_STACK_AMOUNT);
      return;
    }
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.setSoldItem(items);
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_SHOP_ITEMS);
  }

  public static void setNewShopCooldown(final @NotNull Player player,
      final @NotNull String cooldown) {
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.setCooldown(convertCooldownPattern(cooldown));
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(
        Lang.NEW_LIMIT + cooldownStringBuilder(convertCooldownPattern(cooldown), "e", "f"));
  }

  public static void setNewShopPermission(final @NotNull Player player, final String permission) {
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.setPermission(permission);
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_PERMISSION + permission);
  }

  public static void setNewShopName(final @NotNull Player player, final @NotNull String name) {
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.setName(name);
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_NAME + ChatColor.translateAlternateColorCodes('&', name));
  }

  public static void setNewShopCommands(final @NotNull Player player, final String command) {
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.addCommand(command);
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.COMMAND_ADDED + command);
  }

  public static void resetShopCommands(final @NotNull Player player) {
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.resetCommands();
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.ALL_COMMANDS_DELETED);
  }

  public static void resetShopQuestion(final @NotNull Player player) {
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.resetQuestion();
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.QUESTION_RESET);
  }

  public static void resetShopTransactions(final @NotNull Player player) {
    final Shop shop = Main.LOADED_SHOPS.get(Main.ADMIN_PANEL_USER.get(player.getName()));
    shop.resetTransactions();
    FileManager.saveSpecificShopToFile(Main.ADMIN_PANEL_USER.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.ALL_PLAYERDATA_DELETED);
  }

  public static void deleteShop(final String shopID) {
    final Shop shop = Main.LOADED_SHOPS.get(shopID);
    shop.stopParticles();
    final @NotNull Collection<Entity> entities = shop.getShopLocation().getWorld()
        .getNearbyEntities(shop.getShopLocation(), 2, 2, 2);
    for (final @NotNull Entity e : entities) {
      if (e.getType() == EntityType.ARMOR_STAND && e.getName().equals(shopID)) {
        e.remove();
      }
    }
    Main.LOADED_SHOPS.remove(shopID);
    FileManager.deleteShopFile(shopID);
  }

  public static void updateHead(final String shopID, final ItemStack head) {
    final Shop shop = Main.LOADED_SHOPS.get(shopID);
    final @NotNull Collection<Entity> entities = shop.getShopLocation().getWorld()
        .getNearbyEntities(shop.getShopLocation(), 2, 2, 2);
    for (final @NotNull Entity e : entities) {
      if (e.getType() == EntityType.ARMOR_STAND && e.getName().equals(shop.getID())) {
        final @NotNull ArmorStand as = (ArmorStand) e;
        as.getEquipment().setHelmet(head);
      }
    }
  }

  public static void updatePosition(final String shopID, final @NotNull Location playerLocation) {
    final Shop shop = Main.LOADED_SHOPS.get(shopID);
    shop.stopParticles();
    final @NotNull Collection<Entity> entities = playerLocation.getWorld()
        .getNearbyEntities(playerLocation, 10, 10, 10);
    for (final @NotNull Entity e : entities) {
      if (e.getType() == EntityType.ARMOR_STAND && e.getName().equals(shopID)) {
        shop.setLocation(e.getLocation());
      }
    }
    shop.startParticles();
    FileManager.saveSpecificShopToFile(shopID);
  }

  public static void buyItem(final @NotNull Player player) {
    final String shopID = Main.BUYING_PLAYERS.get(player.getName());
    final Shop shop = Main.LOADED_SHOPS.get(shopID);
    if (!hasEnoughMoney(shop, player)) {
      player.sendMessage(Lang.ERROR_NOT_ENOUGH_MONEY);
      finishBuySession(player.getName());
      return;
    }
    if (!hasInventorySpaceToBuy(player, shop)) {
      player.sendMessage(Lang.ERROR_INVENTORY_FULL);
      finishBuySession(player.getName());
      return;
    }
    final EconomyResponse er = Main.economy.withdrawPlayer(player, shop.getPrice());
    if (er.transactionSuccess()) {
      shop.addTransaction(player);
      shop.addTimesUsed();
      giveItemToPlayer(shop, player);
      FileManager.saveSpecificShopToFile(shopID);
      player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.5F);
      String noPrefixShopName = shop.getShopName();
      for (final @NotNull String s : Main.LOADED_PREFIXES.keySet()) {
        noPrefixShopName = noPrefixShopName.replaceAll(s, "");
      }
      for (final @NotNull String cmd : shop.getExecutedCommands()) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
            cmd.replace("%player%", player.getName()).replace("%shopname%", noPrefixShopName));
      }

    } else {
      player.sendMessage(Lang.ERROR_TRANSACTION_FAILED);
    }
    finishBuySession(player.getName());
  }

  private static double round(final double value) {
    @NotNull BigDecimal bd = new BigDecimal(Double.toString(value));
    bd = bd.setScale(2, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  public static String @NotNull [] splitTransactionDates(final @NotNull String input) {
    return input.split("@@@");
  }

  public static void sendAllShopsMessage(final @NotNull Player player, final int page) {
    int currentPage = page;
    int count = 0;
    player.sendMessage(Lang.CHAT_PREFIX + "Eine Liste aller aktuell geladenen Shops:");
    player.sendMessage(
        "§9§m                                                                               ");
    final @NotNull List<String> sortedNames = new ArrayList<>();
    final @NotNull Map<String, TextComponent> messages = new HashMap<>();
    for (final @NotNull Shop shop : Main.LOADED_SHOPS.values()) {
      count++;
      final @NotNull TextComponent message = new TextComponent(" §8§l>> ");
      final @NotNull TextComponent shopName = new TextComponent(
          "§7" + ChatColor.translateAlternateColorCodes('&', shop.getShopName()));
      shopName.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, shop.getID()));
      shopName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
          new ComponentBuilder("§7" + shop.getID()).create()));
      message.addExtra(shopName);
      message.addExtra("§8:   ");
      if (!shop.isVirtual() && shop.getShopLocation().getWorld() != null) {
        final @NotNull TextComponent tp = new TextComponent("§a§l> TP <");
        tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
            "/tppos " + player.getName() + " " + shop.getShopLocation().getX() + " " + (
                shop.getShopLocation().getY() + 2) + " " + shop.getShopLocation().getZ() + " "
                + shop.getShopLocation().getWorld().getName()));
        tp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
            new ComponentBuilder("§eZum Shop teleportieren...").create()));
        message.addExtra(tp);
      }
      if (!sortedNames.contains(shop.getShopName())) {
        sortedNames.add(shop.getShopName());
        messages.put(shop.getShopName(), message);
      } else {
        sortedNames.add(shop.getShopName() + "_1");
        messages.put(shop.getShopName() + "_1", message);
      }
    }
    if (Math.ceil(count / 10D) < page) {
      currentPage = (int) Math.ceil(count / 10D);
    }
    final int from = 10 * currentPage - 10;
    int to = 10 * currentPage;
    if (to > count) {
      to = count;
    }
    sortedNames.sort(String.CASE_INSENSITIVE_ORDER);
    final @NotNull List<String> toBeSent = sortedNames.subList(from, to);
    for (final String msgID : toBeSent) {
      player.spigot().sendMessage(messages.get(msgID));
    }

    if (count > 10) {
      ChatUtils.sendCenteredChatMessage(player, "§9§m                            ");
      ChatUtils.sendCenteredChatMessage(player,
          "§7§lSeite §a§l" + currentPage + "§7/§a§l" + (int) Math.ceil(count / 10D));
      final @NotNull TextComponent pageButtons = new TextComponent("                           ");
      if (currentPage == 1) {
        final @NotNull TextComponent back = new TextComponent("    ");
        pageButtons.addExtra(back);
      } else {
        final @NotNull TextComponent back = new TextComponent("§a<<<");
        back.setClickEvent(
            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/particleshop list " + (currentPage - 1)));
        back.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
            new ComponentBuilder("§e< Vorherige Seite").create()));
        pageButtons.addExtra(back);
      }
      pageButtons.addExtra("                ");
      if (currentPage == (int) Math.ceil(count / 10D)) {
        final @NotNull TextComponent back = new TextComponent("    ");
        pageButtons.addExtra(back);
      } else {
        final @NotNull TextComponent forward = new TextComponent("§a>>>");
        forward.setClickEvent(
            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/particleshop list " + (currentPage + 1)));
        forward.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
            new ComponentBuilder("§e> Nächste Seite").create()));
        pageButtons.addExtra(forward);
      }
      player.spigot().sendMessage(pageButtons);
      ChatUtils.sendCenteredChatMessage(player, "§9§m                            ");
      player.sendMessage("");
    }
    player.sendMessage("");
    ChatUtils.sendCenteredChatMessage(player,
        "§a§l>> §7Aktuell sind §a" + count + "§7 Shops geladen.");
    player.sendMessage(
        "§9§m                                                                               ");
  }

  public static boolean checkCooldownPattern(final @NotNull String toBeChecked) {
    return (toBeChecked.matches("\\d+:\\d+")) || (toBeChecked.matches("-\\d+"));
  }

  public static Long convertCooldownPattern(final @NotNull String pattern) {
    if (pattern.contains("-")) {
      return Long.parseLong(pattern);
    }
    final String @NotNull [] split = pattern.split(":");
    return (((Long.parseLong(split[0]) * 24L) + Long.parseLong(split[1])) * 60L * 60L * 1000L);
  }

  public static @NotNull String remainingTransactionsString(final @NotNull Shop shop,
      final @NotNull Player player, final String primary, final String secondary) {
    @NotNull String output = "";
    final int remaining = shop.getRemainingTransactions(player.getUniqueId().toString());
    if ((shop.getCooldown() < 0) && (remaining < Math.abs(shop.getCooldown()))) {
      output = output + " §" + secondary + "[§" + primary + remaining + "§" + secondary
          + "x verbleibend]";
    }
    return output;
  }

  public static @NotNull String cooldownStringBuilder(final long cooldown, final String primary,
      final String secondary) {
    if (cooldown < 0) {
      return "§" + primary + Math.abs(cooldown) + "§" + secondary + " Mal";
    }
    @NotNull String output = "";
    final int weeks = (int) (cooldown / (1000 * 60 * 60 * 24 * 7));
    final int days = (int) ((cooldown / (1000 * 60 * 60 * 24)) % 7);
    final int hours = (int) ((cooldown / (1000 * 60 * 60)) % 24);
    final int minutes = (int) ((cooldown / (1000 * 60)) % 60);
    final int seconds = (int) ((cooldown / 1000) % 60);

    if (weeks > 0) {
      if (weeks > 1) {
        output = output + "§" + primary + weeks + "§" + secondary + " Wochen ";
      } else {
        output = output + "§" + primary + weeks + "§" + secondary + " Woche ";
      }
    }
    if (days > 0) {
      if (days > 1) {
        output = output + "§" + primary + days + "§" + secondary + " Tage ";
      } else {
        output = output + "§" + primary + days + "§" + secondary + " Tag ";
      }
    }
    if (hours > 0) {
      if (hours > 1) {
        output = output + "§" + primary + hours + "§" + secondary + " Stunden ";
      } else {
        output = output + "§" + primary + hours + "§" + secondary + " Stunde ";
      }
    }
    if (minutes > 0) {
      if (minutes > 1) {
        output = output + "§" + primary + minutes + "§" + secondary + " Minuten ";
      } else {
        output = output + "§" + primary + minutes + "§" + secondary + " Minute ";
      }
    }
    if (seconds > 0) {
      if (seconds > 1) {
        output = output + "§" + primary + seconds + "§" + secondary + " Sekunden ";
      } else {
        output = output + "§" + primary + seconds + "§" + secondary + " Sekunde ";
      }
    }
    return output;
  }

  public static boolean hasEnoughMoney(final @NotNull Shop shop, final Player player) {
    return Main.economy.getBalance(player) >= shop.getPrice();
  }

  public static void sendHelpMenu(final @NotNull Player player) {
    player.sendMessage(Lang.CHAT_PREFIX + "Liste aller Commands:");
    player.sendMessage(
        "§9§m                                                                               ");
    player.sendMessage(" §8§l>> §7/particleshop create <Name> <Preis> <Limit> <Permission>");
    player.sendMessage(" §8§l   §7Eingabeformat Limit: §7§oTage:Stunden §7ODER §c§l§o-§7§oAnzahl");
    player.sendMessage(" §8§l>> §7/particleshop list");
    player.sendMessage(" §8§l>> §7/particleshop reload");
    player.sendMessage(" §8§l>> §7/particleshop help");
    player.sendMessage(
        "§9§m                                                                               ");
  }
}