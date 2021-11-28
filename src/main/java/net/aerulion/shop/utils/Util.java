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

  public static void openShopToPlayer(@NotNull Player player, @NotNull Shop shop) {
    if (player.hasPermission("shop." + shop.getShopPermission())) {
      if (shop.isEnabled()) {
        if (shop.getQuestion() != null && shop.getQuestionAnswer() != null) {
          if (Main.activeQuestionConversations.containsKey(player.getUniqueId().toString())) {
            Conversation conversation = Main.activeQuestionConversations.get(
                player.getUniqueId().toString());
            conversation.abandon();
          }
          Main.buyingPlayers.put(player.getName(), shop.getID());
          player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1.3F);
          @NotNull ConversationFactory cf = new ConversationFactory(Main.plugin);
          @NotNull ConversationPrefix cp = prefix -> Lang.CHAT_PREFIX;
          @NotNull Conversation c = cf.withFirstPrompt(new QuestionAskConversation())
              .withModality(false).withLocalEcho(false).withPrefix(cp).buildConversation(player);
          c.begin();
          Main.activeQuestionConversations.put(player.getUniqueId().toString(), c);
        } else {
          player.openInventory(Inventories.getUserPanel(shop, player));
          Main.buyingPlayers.put(player.getName(), shop.getID());
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

  public static void createNewShop(@NotNull Player player, double price, long cooldown,
      String shopName, String shopPermission, boolean virtual) {
    @NotNull List<ItemStack> items = new ArrayList<>();
    for (@Nullable ItemStack itemstack : player.getInventory().getStorageContents()) {
      if (itemstack != null) {
        items.add(itemstack.clone());
      }

    }
    if (items.size() > 27) {
      player.sendMessage(Lang.ERROR_MAX_STACK_AMOUNT);
      return;
    }

    final String ID = UUID.randomUUID().toString();
    @Nullable Location location = null;
    if (!virtual) {
      location = player.getLocation().subtract(new Vector(0F, 1.37F, 0F));
      @NotNull ArmorStand armorstand = player.getLocation().getWorld()
          .spawn(location, ArmorStand.class);
      armorstand.setSilent(true);
      armorstand.setSmall(false);
      armorstand.getEquipment().setHelmet(new ItemStack(Material.CHEST));
      armorstand.setGravity(false);
      armorstand.setVisible(false);
      armorstand.setCustomName(ID);
      armorstand.setBasePlate(false);
      armorstand.setHeadPose(
          new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0)));
    }
    @NotNull HashMap<String, String> transactionDates = new HashMap<>();
    Main.loadedShops.put(ID,
        new Shop(transactionDates, items, price, cooldown, location, ID, shopName, shopPermission,
            0, new ArrayList<>(), true, virtual, null, null));
    FileManager.saveSpecificShopToFile(ID);
    Main.loadedShops.get(ID).startParticles();
    player.sendMessage(Lang.SHOP_ADDED);
  }

  public static void giveItemToPlayer(@NotNull Shop shop, @NotNull Player player) {
    for (@NotNull ItemStack is : shop.getSoldItems()) {
      player.getInventory().addItem(is.clone());
    }
  }

  public static boolean hasInventorySpaceToBuy(@NotNull Player player, @NotNull Shop shop) {
    int usedSlotCount = 0;
    for (@Nullable ItemStack is : player.getInventory().getStorageContents()) {
      if (is != null) {
        usedSlotCount++;
      }
    }
    return (36 - usedSlotCount) >= shop.getSoldItems().size();
  }

  public static int calculateInventorySlotSize(@NotNull Shop shop) {
    return ((int) (Math.ceil(shop.getSoldItems().size() / 9.0))) * 9 + 27;
  }

  public static void finishAdminSession(String name) {
    Main.adminPanelUser.remove(name);
  }

  public static void finishBuySession(String name) {
    Main.buyingPlayers.remove(name);
  }

  public static void finishQuestionSession(String name) {
    Main.activeQuestionConversations.remove(name);
  }

  public static void setNewShopPrice(@NotNull Player player, double price) {
    double roundedprice = round(price);
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.setPrice(roundedprice);
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_PRICE + roundedprice);
  }

  public static void setNewShopQuestion(@NotNull Player player, String question) {
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.setQuestion(question);
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_QUESTION + question);
  }

  public static void validateQuestion(@NotNull Player player, @NotNull String input) {
    if (input.equalsIgnoreCase("stop")) {
      player.sendMessage(Lang.ACTION_ESCAPED);
      finishQuestionSession(player.getName());
      return;
    }
    Shop shop = Main.loadedShops.get(Main.buyingPlayers.get(player.getName()));
    if (input.equalsIgnoreCase(shop.getQuestionAnswer())) {
      player.openInventory(Inventories.getUserPanel(shop, player));
      player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1.3F);
    } else {
      player.sendMessage(Lang.ERROR_WRONG_QUESTION_ANSWER);
      player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F, 0.8F);
    }
    finishQuestionSession(player.getName());
  }

  public static void setNewShopQuestionAnswer(@NotNull Player player, String answer) {
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.setQuestionAnswer(answer);
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_QUESTION_ANSWER + answer);
  }

  public static void toggleEnabled(@NotNull Player player) {
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.toggleEnabled();
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.TOGGLED_ENABLED);
  }

  public static void setNewShopItems(@NotNull Player player) {
    @NotNull List<ItemStack> items = new ArrayList<>();
    for (@Nullable ItemStack itemstack : player.getInventory().getStorageContents()) {
      if (itemstack != null) {
        items.add(itemstack.clone());
      }
    }
    if (items.size() > 27) {
      player.sendMessage(Lang.ERROR_MAX_STACK_AMOUNT);
      return;
    }
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.setSoldItem(items);
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_SHOP_ITEMS);
  }

  public static void setNewShopCooldown(@NotNull Player player, @NotNull String cooldown) {
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.setCooldown(convertCooldownPattern(cooldown));
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(
        Lang.NEW_LIMIT + cooldownStringBuilder(convertCooldownPattern(cooldown), "e", "f"));
  }

  public static void setNewShopPermission(@NotNull Player player, String permission) {
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.setPermission(permission);
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_PERMISSION + permission);
  }

  public static void setNewShopName(@NotNull Player player, @NotNull String name) {
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.setName(name);
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.NEW_NAME + ChatColor.translateAlternateColorCodes('&', name));
  }

  public static void setNewShopCommands(@NotNull Player player, String command) {
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.addCommand(command);
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.COMMAND_ADDED + command);
  }

  public static void resetShopCommands(@NotNull Player player) {
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.resetCommands();
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.ALL_COMMANDS_DELETED);
  }

  public static void resetShopQuestion(@NotNull Player player) {
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.resetQuestion();
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.QUESTION_RESET);
  }

  public static void resetShopTransactions(@NotNull Player player) {
    Shop shop = Main.loadedShops.get(Main.adminPanelUser.get(player.getName()));
    shop.resetTransactions();
    FileManager.saveSpecificShopToFile(Main.adminPanelUser.get(player.getName()));
    finishAdminSession(player.getName());
    player.sendMessage(Lang.ALL_PLAYERDATA_DELETED);
  }

  public static void deleteShop(String shopID) {
    Shop shop = Main.loadedShops.get(shopID);
    shop.stopParticles();
    @NotNull Collection<Entity> entities = shop.getShopLocation().getWorld()
        .getNearbyEntities(shop.getShopLocation(), 2, 2, 2);
    for (@NotNull Entity e : entities) {
      if (e.getType().equals(EntityType.ARMOR_STAND) && e.getName().equals(shopID)) {
        e.remove();
      }
    }
    Main.loadedShops.remove(shopID);
    FileManager.deleteShopFile(shopID);
  }

  public static void updateHead(String shopID, ItemStack head) {
    Shop shop = Main.loadedShops.get(shopID);
    @NotNull Collection<Entity> entities = shop.getShopLocation().getWorld()
        .getNearbyEntities(shop.getShopLocation(), 2, 2, 2);
    for (@NotNull Entity e : entities) {
      if (e.getType().equals(EntityType.ARMOR_STAND) && e.getName().equals(shop.getID())) {
        @NotNull ArmorStand as = (ArmorStand) e;
        as.getEquipment().setHelmet(head);
      }
    }
  }

  public static void updatePosition(String shopID, @NotNull Location playerLocation) {
    Shop shop = Main.loadedShops.get(shopID);
    shop.stopParticles();
    @NotNull Collection<Entity> entities = playerLocation.getWorld()
        .getNearbyEntities(playerLocation, 10, 10, 10);
    for (@NotNull Entity e : entities) {
      if (e.getType().equals(EntityType.ARMOR_STAND) && e.getName().equals(shopID)) {
        shop.setLocation(e.getLocation());
      }
    }
    shop.startParticles();
    FileManager.saveSpecificShopToFile(shopID);
  }

  public static void buyItem(@NotNull Player player) {
    String shopID = Main.buyingPlayers.get(player.getName());
    Shop shop = Main.loadedShops.get(shopID);
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
    EconomyResponse er = Main.economy.withdrawPlayer(player, shop.getPrice());
    if (er.transactionSuccess()) {
      shop.addTransaction(player);
      shop.addTimesUsed();
      giveItemToPlayer(shop, player);
      FileManager.saveSpecificShopToFile(shopID);
      player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.5F);
      String noPrefixShopName = shop.getShopName();
      for (@NotNull String s : Main.loadedPrefixes.keySet()) {
        noPrefixShopName = noPrefixShopName.replaceAll(s, "");
      }
      for (@NotNull String cmd : shop.getExecutedCommands()) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
            cmd.replace("%player%", player.getName()).replace("%shopname%", noPrefixShopName));
      }

    } else {
      player.sendMessage(Lang.ERROR_TRANSACTION_FAILED);
    }
    finishBuySession(player.getName());
  }

  private static double round(double value) {
    @NotNull BigDecimal bd = new BigDecimal(Double.toString(value));
    bd = bd.setScale(2, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  public static String @NotNull [] splitTransactionDates(@NotNull String input) {
    return input.split("@@@");
  }

  public static void sendAllShopsMessage(@NotNull Player player, int page) {
    int currentPage = page;
    int count = 0;
    player.sendMessage(Lang.CHAT_PREFIX + "Eine Liste aller aktuell geladenen Shops:");
    player.sendMessage(
        "§9§m                                                                               ");
    @NotNull List<String> sortedNames = new ArrayList<>();
    @NotNull Map<String, TextComponent> messages = new HashMap<>();
    for (@NotNull Shop shop : Main.loadedShops.values()) {
      count++;
      @NotNull TextComponent message = new TextComponent(" §8§l>> ");
      @NotNull TextComponent shopName = new TextComponent(
          "§7" + ChatColor.translateAlternateColorCodes('&', shop.getShopName()));
      shopName.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, shop.getID()));
      shopName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
          new ComponentBuilder("§7" + shop.getID()).create()));
      message.addExtra(shopName);
      message.addExtra("§8:   ");
      if (!shop.isVirtual() && shop.getShopLocation().getWorld() != null) {
        @NotNull TextComponent tp = new TextComponent("§a§l> TP <");
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
        sortedNames.add(shop.getShopName().concat("_1"));
        messages.put(shop.getShopName().concat("_1"), message);
      }
    }
    if (Math.ceil(count / 10D) < page) {
      currentPage = (int) Math.ceil(count / 10D);
    }
    int from = 10 * currentPage - 10;
    int to = 10 * currentPage;
    if (to > count) {
      to = count;
    }
    sortedNames.sort(String.CASE_INSENSITIVE_ORDER);
    @NotNull List<String> toBeSent = sortedNames.subList(from, to);
    for (String msgID : toBeSent) {
      player.spigot().sendMessage(messages.get(msgID));
    }

    if (count > 10) {
      ChatUtils.sendCenteredChatMessage(player, "§9§m                            ");
      ChatUtils.sendCenteredChatMessage(player,
          "§7§lSeite §a§l" + currentPage + "§7/§a§l" + (int) Math.ceil(count / 10D));
      @NotNull TextComponent pageButtons = new TextComponent("                           ");
      if (currentPage == 1) {
        @NotNull TextComponent back = new TextComponent("    ");
        pageButtons.addExtra(back);
      } else {
        @NotNull TextComponent back = new TextComponent("§a<<<");
        back.setClickEvent(
            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/shop:shop list " + (currentPage - 1)));
        back.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
            new ComponentBuilder("§e< Vorherige Seite").create()));
        pageButtons.addExtra(back);
      }
      pageButtons.addExtra("                ");
      if (currentPage == Math.ceil(count / 10D)) {
        @NotNull TextComponent back = new TextComponent("    ");
        pageButtons.addExtra(back);
      } else {
        @NotNull TextComponent forward = new TextComponent("§a>>>");
        forward.setClickEvent(
            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/shop:shop list " + (currentPage + 1)));
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

  public static boolean checkCooldownPattern(@NotNull String toBeChecked) {
    return (toBeChecked.matches("\\d+:\\d+")) || (toBeChecked.matches("-\\d+"));
  }

  public static Long convertCooldownPattern(@NotNull String pattern) {
    if (pattern.contains("-")) {
      return Long.parseLong(pattern);
    }
    String @NotNull [] split = pattern.split(":");
    return (((Long.parseLong(split[0]) * 24L) + Long.parseLong(split[1])) * 60L * 60L * 1000L);
  }

  public static @NotNull String remainingTransactionsString(@NotNull Shop shop,
      @NotNull Player player, String primary, String secondary) {
    @NotNull String output = "";
    int remaining = shop.getRemainingTransactions(player.getUniqueId().toString());
    if ((shop.getCooldown() < 0) && (remaining < Math.abs(shop.getCooldown()))) {
      output = output.concat(
          " §" + secondary + "[§" + primary + remaining + "§" + secondary + "x verbleibend]");
    }
    return output;
  }

  public static @NotNull String cooldownStringBuilder(long cooldown, String primary,
      String secondary) {
    if (cooldown < 0) {
      return "§" + primary + Math.abs(cooldown) + "§" + secondary + " Mal";
    }
    @NotNull String output = "";
    int weeks = (int) (cooldown / (1000 * 60 * 60 * 24 * 7));
    int days = (int) ((cooldown / (1000 * 60 * 60 * 24)) % 7);
    int hours = (int) ((cooldown / (1000 * 60 * 60)) % 24);
    int minutes = (int) ((cooldown / (1000 * 60)) % 60);
    int seconds = (int) ((cooldown / 1000) % 60);

    if (weeks > 0) {
      if (weeks > 1) {
        output = output.concat("§" + primary + weeks + "§" + secondary + " Wochen ");
      } else {
        output = output.concat("§" + primary + weeks + "§" + secondary + " Woche ");
      }
    }
    if (days > 0) {
      if (days > 1) {
        output = output.concat("§" + primary + days + "§" + secondary + " Tage ");
      } else {
        output = output.concat("§" + primary + days + "§" + secondary + " Tag ");
      }
    }
    if (hours > 0) {
      if (hours > 1) {
        output = output.concat("§" + primary + hours + "§" + secondary + " Stunden ");
      } else {
        output = output.concat("§" + primary + hours + "§" + secondary + " Stunde ");
      }
    }
    if (minutes > 0) {
      if (minutes > 1) {
        output = output.concat("§" + primary + minutes + "§" + secondary + " Minuten ");
      } else {
        output = output.concat("§" + primary + minutes + "§" + secondary + " Minute ");
      }
    }
    if (seconds > 0) {
      if (seconds > 1) {
        output = output.concat("§" + primary + seconds + "§" + secondary + " Sekunden ");
      } else {
        output = output.concat("§" + primary + seconds + "§" + secondary + " Sekunde ");
      }
    }
    return output;
  }

  public static boolean hasEnoughMoney(@NotNull Shop shop, Player player) {
    return Main.economy.getBalance(player) >= shop.getPrice();
  }

  public static void sendHelpMenu(@NotNull Player player) {
    player.sendMessage(Lang.CHAT_PREFIX + "Liste aller Commands:");
    player.sendMessage(
        "§9§m                                                                               ");
    player.sendMessage(" §8§l>> §7/shop create <Name> <Preis> <Limit> <Permission>");
    player.sendMessage(" §8§l   §7Eingabeformat Limit: §7§oTage:Stunden §7ODER §c§l§o-§7§oAnzahl");
    player.sendMessage(" §8§l>> §7/shop list");
    player.sendMessage(" §8§l>> §7/shop reload");
    player.sendMessage(" §8§l>> §7/shop help");
    player.sendMessage(
        "§9§m                                                                               ");
  }
}