package net.aerulion.shop.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
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

import net.aerulion.shop.Main;
import net.aerulion.shop.Conversations.QuestionAskConversation;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;

public class Utils {

	public static void openShopToPlayer(Player player, Shop shop) {
		if (player.hasPermission("shop." + shop.getShopPermission())) {
			if (shop.isEnabled()) {
				if (shop.getQuestion() != null && shop.getQuestionAnswer() != null) {
					if (Main.ActiveQuestionConversations.containsKey(player.getUniqueId().toString())) {
						Conversation conversation = Main.ActiveQuestionConversations.get(player.getUniqueId().toString());
						conversation.abandon();
					}
					Main.BuyingPlayers.put(player.getName(), shop.getID());
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1.3F);
					ConversationFactory cf = new ConversationFactory(Main.plugin);
					ConversationPrefix cp = prefix -> Lang.ChatPluginPrefix;
					Conversation c = cf.withFirstPrompt(new QuestionAskConversation()).withModality(false).withLocalEcho(false).withPrefix(cp).buildConversation(player);
					c.begin();
					Main.ActiveQuestionConversations.put(player.getUniqueId().toString(), c);

				} else {
					player.openInventory(Inventories.UserPanel(shop, player));
					Main.BuyingPlayers.put(player.getName(), shop.getID());
					player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 0.5F, 1.3F);
					return;
				}
			} else {
				player.sendMessage(Lang.ErrorShopDisabled);
				player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.2F, 2.0F);
				return;
			}
		} else {
			player.sendMessage(Lang.ErrorNoShopPermission);
			player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.2F, 2.0F);
			return;
		}
	}

	public static void createNewShop(Player player, double price, long cooldown, String shopName, String shopPermission, boolean virtual) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (ItemStack itemstack : player.getInventory().getStorageContents()) {
			if (itemstack != null) {
				items.add(itemstack.clone());
			}

		}
		if (items.size() > 27) {
			player.sendMessage(Lang.ErrorMaxStackAmount);
			return;
		}

		final String ID = UUID.randomUUID().toString();
		Location location = null;
		if (!virtual) {
			location = player.getLocation().subtract(new Vector(0F, 1.37F, 0F));
			ArmorStand armorstand = player.getLocation().getWorld().spawn(location, ArmorStand.class);
			armorstand.setSilent(true);
			armorstand.setSmall(false);
			armorstand.setHelmet(new ItemStack(Material.CHEST));
			armorstand.setGravity(false);
			armorstand.setVisible(false);
			armorstand.setCustomName(ID);
			armorstand.setBasePlate(false);
			armorstand.setHeadPose(new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0)));
		}
		HashMap<String, String> transactionDates = new HashMap<String, String>();
		Main.LoadedShops.put(ID, new Shop(transactionDates, items, price, cooldown, location, ID, shopName, shopPermission, 0, new ArrayList<String>(), true, virtual, null, null));
		FileManager.addNewShopFile(ID, shopName, shopPermission, location, transactionDates, cooldown, price, items, virtual);
		player.sendMessage(Lang.ShopAdded);
	}

	public static void giveItemToPlayer(Shop shop, Player player) {
		for (ItemStack is : shop.getSoldItems()) {
			player.getInventory().addItem(is);
		}
	}

	public static boolean hasInventorySpaceToBuy(Player player, Shop shop) {
		int UsedSlotCount = 0;
		for (ItemStack is : player.getInventory().getStorageContents()) {
			if (is != null)
				UsedSlotCount++;
		}
		if ((36 - UsedSlotCount) >= shop.getSoldItems().size())
			return true;
		return false;
	}

	public static int calculateInventorySlotSize(Shop shop) {
		return ((int) (Math.ceil(shop.getSoldItems().size() / 9.0))) * 9 + 27;

	}

	public static void finishAdminSession(String name) {
		Main.AdminPanelUser.remove(name);
	}

	public static void finishBuySession(String name) {
		Main.BuyingPlayers.remove(name);
	}

	public static void finishQuestionSession(String name) {
		Main.ActiveQuestionConversations.remove(name);
	}

	public static void setNewShopPrice(Player player, double price) {
		double roundedprice = round(price);
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.setPrice(roundedprice);
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.NewPrice + roundedprice);
	}

	public static void setNewShopQuestion(Player player, String question) {
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.setQuestion(question);
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.NewQuestion + question);
	}

	public static void validateQuestion(Player player, String input) {
		if (input.equalsIgnoreCase("stop")) {
			player.sendMessage(Lang.ActionEscaped);
			finishQuestionSession(player.getName());
			return;
		}
		Shop shop = Main.LoadedShops.get(Main.BuyingPlayers.get(player.getName()));
		if (input.equalsIgnoreCase(shop.getQuestionAnswer())) {
			player.openInventory(Inventories.UserPanel(shop, player));
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1.3F);
			finishQuestionSession(player.getName());
		} else {
			player.sendMessage(Lang.ErrorWrongQuestionAnswer);
			player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F, 0.8F);
			finishQuestionSession(player.getName());
		}
	}

	public static void setNewShopQuestionAnswer(Player player, String answer) {
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.setQuestionAnswer(answer);
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.NewQuestionAnswer + answer);
	}

	public static void toggleEnabled(Player player) {
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.toggleEnabled();
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.ToggledEnabled);
	}

	public static void setNewShopItems(Player player) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (ItemStack itemstack : player.getInventory().getStorageContents()) {
			if (itemstack != null) {
				items.add(itemstack.clone());
			}
		}
		if (items.size() > 27) {
			player.sendMessage(Lang.ErrorMaxStackAmount);
			return;
		}
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.setSoldItem(items);
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.NewShopItems);
	}

	public static void setNewShopCooldown(Player player, String cooldown) {
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.setCooldown(convertCooldownPattern(cooldown));
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.NewLimit + cooldownStringBuilder(convertCooldownPattern(cooldown), "e", "f"));
	}

	public static void setNewShopPermission(Player player, String permission) {
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.setPermission(permission);
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.NewPermission + permission);
	}

	public static void setNewShopName(Player player, String Name) {
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.setName(Name);
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.NewName + ChatColor.translateAlternateColorCodes('&', Name));
	}

	public static void setNewShopCommands(Player player, String Command) {
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.addCommand(Command);
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.CommandAdded + Command);
	}

	public static void resetShopCommands(Player player) {
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.resetCommands();
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.AllCommandsDeleted);
	}

	public static void resetShopQuestion(Player player) {
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.resetQuestion();
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.QuestionReset);
	}

	public static void resetShopTransactions(Player player) {
		Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(player.getName()));
		shop.resetTransactions();
		FileManager.saveSpecificShop(Main.AdminPanelUser.get(player.getName()));
		finishAdminSession(player.getName());
		player.sendMessage(Lang.AllPlayerdataDeleted);
	}

	public static void deleteShop(String shopID) {
		Shop shop = Main.LoadedShops.get(shopID);
		shop.stopParticles();
		Collection<Entity> entitys = shop.getShopLocation().getWorld().getNearbyEntities(shop.getShopLocation(), 2, 2, 2);
		for (Entity e : entitys) {
			if (e.getType().equals(EntityType.ARMOR_STAND)) {
				if (e.getCustomName().equals(shopID)) {
					e.remove();
				}
			}
		}
		Main.LoadedShops.remove(shopID);
		FileManager.deleteShopFile(shopID);

	}

	public static void updateHead(String shopID, ItemStack head) {
		Shop shop = Main.LoadedShops.get(shopID);
		Collection<Entity> entitys = shop.getShopLocation().getWorld().getNearbyEntities(shop.getShopLocation(), 2, 2, 2);
		for (Entity e : entitys) {
			if (e.getType().equals(EntityType.ARMOR_STAND)) {
				if (e.getName().equals(shop.getID())) {
					ArmorStand as = (ArmorStand) e;
					as.setHelmet(head);
				}
			}
		}
	}

	public static void updatePosition(String shopID, Location playerLocation) {
		Shop shop = Main.LoadedShops.get(shopID);
		shop.stopParticles();
		Collection<Entity> entitys = playerLocation.getWorld().getNearbyEntities(playerLocation, 10, 10, 10);
		for (Entity e : entitys) {
			if (e.getType().equals(EntityType.ARMOR_STAND)) {
				if (e.getName().equals(shopID)) {
					shop.setLocation(e.getLocation());
				}
			}
		}
		shop.startParticles();
		FileManager.saveSpecificShop(shopID);
	}

	public static void buyItem(Player player) {
		String shopID = Main.BuyingPlayers.get(player.getName());
		Shop shop = Main.LoadedShops.get(shopID);
		if (!hasEnoughMoney(shop, player)) {
			player.sendMessage(Lang.ErrorNotEnoughMoney);
			finishBuySession(player.getName());
			return;
		}
		if (!hasInventorySpaceToBuy(player, shop)) {
			player.sendMessage(Lang.ErrorInventoryFull);
			finishBuySession(player.getName());
			return;
		}
		EconomyResponse er = Main.economy.withdrawPlayer(player, shop.getPrice());
		if (er.transactionSuccess()) {
			shop.addTransaction(player, shop);
			shop.addTimesUsed();
			giveItemToPlayer(shop, player);
			FileManager.saveSpecificShop(shopID);
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.5F);
			String NoPrefixShopName = shop.getShopName();
			for (String s : Main.LoadedPrefixes.keySet()) {
				NoPrefixShopName = NoPrefixShopName.replaceAll(s, "");
			}
			for (String cmd : shop.getExecutedCommands()) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", player.getName()).replaceAll("%shopname%", NoPrefixShopName));
			}
			finishBuySession(player.getName());

		} else {
			player.sendMessage(Lang.ErrorTransactionFailed);
			finishBuySession(player.getName());
			return;
		}

	}

	private static double round(double value) {
		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String[] splitTransactionDates(String input) {
		return input.split("@@@");
	}

	private final static int CENTER_PX = 154;

	public static void sendCenteredMessage(Player player, String message) {
		if (message == null || message.equals(""))
			player.sendMessage("");
		message = ChatColor.translateAlternateColorCodes('&', message);

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '§') {
				previousCode = true;
				continue;
			} else if (previousCode == true) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				} else
					isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while (compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}
		player.sendMessage(sb.toString() + message);
	}

	public static void sendAllShopsMessage(Player player, int page) {
		int Page = page;
		int count = 0;
		player.sendMessage(Lang.ChatPluginPrefix + "Eine Liste aller aktuell geladenen Shops:");
		player.sendMessage("§9§m                                                                               ");
		List<String> SortedNames = new ArrayList<String>();
		HashMap<String, TextComponent> Messages = new HashMap<String, TextComponent>();
		for (Shop shop : Main.LoadedShops.values()) {
			count++;
			TextComponent message = new TextComponent(" §8§l>> ");
			TextComponent ShopName = new TextComponent("§7" + ChatColor.translateAlternateColorCodes('&', shop.getShopName()));
			ShopName.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, shop.getID()));
			ShopName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7" + shop.getID()).create()));
			message.addExtra(ShopName);
			message.addExtra("§8:   ");
			if (!shop.isVirtual()) {
				TextComponent tp = new TextComponent("§a§l> TP <");
				tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tppos " + player.getName() + " " + shop.getShopLocation().getX() + " " + (shop.getShopLocation().getY() + 2) + " " + shop.getShopLocation().getZ() + " " + shop.getShopLocation().getWorld().getName()));
				tp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eZum Shop teleportieren...").create()));
				message.addExtra(tp);
			}
			if (!SortedNames.contains(shop.getShopName())) {
				SortedNames.add(shop.getShopName());
				Messages.put(shop.getShopName(), message);
			} else {
				SortedNames.add(shop.getShopName().concat("_1"));
				Messages.put(shop.getShopName().concat("_1"), message);
			}
		}
		if (Math.ceil(count / 10D) < page)
			Page = (int) Math.ceil(count / 10D);
		int from = 10 * Page - 10;
		int to = 10 * Page;
		if (to > count)
			to = count;
		SortedNames.sort(String.CASE_INSENSITIVE_ORDER);
		List<String> toBeSent = SortedNames.subList(from, to);
		for (String msgID : toBeSent)
			player.spigot().sendMessage(Messages.get(msgID));

		if (count > 10) {
			sendCenteredMessage(player, "§9§m                            ");
			sendCenteredMessage(player, "§7§lSeite §a§l" + Page + "§7/§a§l" + (int) Math.ceil(count / 10D));
			TextComponent PageButtons = new TextComponent("                           ");
			if (Page == 1) {
				TextComponent Back = new TextComponent("    ");
				PageButtons.addExtra(Back);
			} else {
				TextComponent Back = new TextComponent("§a<<<");
				Back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/shop:shop list " + (Page - 1)));
				Back.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§e< Vorherige Seite").create()));
				PageButtons.addExtra(Back);
			}
			PageButtons.addExtra("                ");
			if (Page == Math.ceil(count / 10D)) {
				TextComponent Back = new TextComponent("    ");
				PageButtons.addExtra(Back);
			} else {
				TextComponent Forward = new TextComponent("§a>>>");
				Forward.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/shop:shop list " + (Page + 1)));
				Forward.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§e> Nächste Seite").create()));
				PageButtons.addExtra(Forward);
			}
			player.spigot().sendMessage(PageButtons);
			sendCenteredMessage(player, "§9§m                            ");
			player.sendMessage("");

		}

		player.sendMessage("");
		sendCenteredMessage(player, "§a§l>> §7Aktuell sind §a" + count + "§7 Shops geladen.");
		player.sendMessage("§9§m                                                                               ");
	}

	public static boolean checkCooldownPattern(String toBeChecked) {
		if ((toBeChecked.matches("\\d+:\\d+")) || (toBeChecked.matches("-\\d+"))) {
			return true;
		}
		return false;
	}

	public static Long convertCooldownPattern(String Pattern) {
		if (Pattern.contains("-")) {
			return Long.parseLong(Pattern);
		}
		String[] Splitted = Pattern.split(":");
		return (((Long.parseLong(Splitted[0]) * 24L) + Long.parseLong(Splitted[1])) * 60L * 60L * 1000L);
	}

	public static String remainingTransactionsString(Shop shop, Player player, String primary, String secondary) {
		String output = "";
		int remaining = shop.getRemainingTransactions(player.getUniqueId().toString());
		if ((shop.getCooldown() < 0) && (remaining < Math.abs(shop.getCooldown()))) {
			output = output.concat(" §" + secondary + "[§" + primary + remaining + "§" + secondary + "x verbleibend]");
		}
		return output;
	}

	public static String cooldownStringBuilder(long Cooldown, String Primary, String Secondary) {
		if (Cooldown < 0) {
			return "§" + Primary + Math.abs(Cooldown) + "§" + Secondary + " Mal";
		}
		String output = "";
		int weeks = (int) (Cooldown / (1000 * 60 * 60 * 24 * 7));
		int days = (int) ((Cooldown / (1000 * 60 * 60 * 24)) % 7);
		int hours = (int) ((Cooldown / (1000 * 60 * 60)) % 24);
		int minutes = (int) ((Cooldown / (1000 * 60)) % 60);
		int seconds = (int) ((Cooldown / 1000) % 60);

		if (weeks > 0) {
			if (weeks > 1)
				output = output.concat("§" + Primary + weeks + "§" + Secondary + " Wochen ");
			else
				output = output.concat("§" + Primary + weeks + "§" + Secondary + " Woche ");
		}
		if (days > 0) {
			if (days > 1)
				output = output.concat("§" + Primary + days + "§" + Secondary + " Tage ");
			else
				output = output.concat("§" + Primary + days + "§" + Secondary + " Tag ");
		}
		if (hours > 0) {
			if (hours > 1)
				output = output.concat("§" + Primary + hours + "§" + Secondary + " Stunden ");
			else
				output = output.concat("§" + Primary + hours + "§" + Secondary + " Stunde ");
		}
		if (minutes > 0) {
			if (minutes > 1)
				output = output.concat("§" + Primary + minutes + "§" + Secondary + " Minuten ");
			else
				output = output.concat("§" + Primary + minutes + "§" + Secondary + " Minute ");
		}
		if (seconds > 0) {
			if (seconds > 1)
				output = output.concat("§" + Primary + seconds + "§" + Secondary + " Sekunden ");
			else
				output = output.concat("§" + Primary + seconds + "§" + Secondary + " Sekunde ");
		}
		return output;
	}

	public static boolean hasEnoughMoney(Shop shop, Player player) {
		if (Main.economy.getBalance(player) >= shop.getPrice()) {
			return true;
		}
		return false;
	}

	public static void sendHelpMenu(Player player) {
		player.sendMessage(Lang.ChatPluginPrefix + "Liste aller Commands:");
		player.sendMessage("§9§m                                                                               ");
		player.sendMessage(" §8§l>> §7/shop create <Name> <Preis> <Limit> <Permission>");
		player.sendMessage(" §8§l   §7Eingabeformat Limit: §7§oTage:Stunden §7ODER §c§l§o-§7§oAnzahl");
		player.sendMessage(" §8§l>> §7/shop list");
		player.sendMessage(" §8§l>> §7/shop reload");
		player.sendMessage(" §8§l>> §7/shop help");
		player.sendMessage("§9§m                                                                               ");
	}

	public static ArrayList<String> filterForTabcomplete(ArrayList<String> Input, String Filter) {
		if (Filter != null) {
			for (Iterator<String> iterator = Input.iterator(); iterator.hasNext();) {
				String value = iterator.next();
				if (!value.toLowerCase().startsWith(Filter.toLowerCase())) {
					{
						iterator.remove();
					}
				}
			}
		}
		return Input;
	}

	public static List<String> WrapString(String Comment, int width) {
		List<String> WrappedString = new ArrayList<String>();
		String wrapped = WordUtils.wrap(Comment, width, "\n", true);
		for (String s : wrapped.split("\n")) {
			WrappedString.add(s);
		}
		return WrappedString;
	}
}
