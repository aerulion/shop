package net.aerulion.shop.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.aerulion.shop.Main;

public class ItemBuilder {

	public static ItemStack buildSimpleItem(Material material, String displayName, List<String> loreList) {
		ItemStack SimpleItem = new ItemStack(material);
		ItemMeta mSimpleItem = SimpleItem.getItemMeta();
		mSimpleItem.setDisplayName(displayName);
		mSimpleItem.setLore(loreList);
		SimpleItem.setItemMeta(mSimpleItem);
		return SimpleItem;
	}

	public static ItemStack createDeleteShopBarrier() {
		return buildSimpleItem(Material.BARRIER, "�c�l\u2716 L�schen", Arrays.asList("�7�oL�scht den gesamten Shop"));
	}

	public static ItemStack createResetTransactionsStructureVoid() {
		return buildSimpleItem(Material.STRUCTURE_VOID, "�e�lSpielerdaten zur�cksetzen", Arrays.asList("�7�oErm�glicht es allen Spielern, erneut", "�7�oetwas im Shop kaufen zu k�nnen"));
	}

	public static ItemStack createChangePriceGoldIngot() {
		return buildSimpleItem(Material.GOLD_INGOT, "�e�lPreis �ndern", Arrays.asList("�7�o�ndert den Preis des Shops"));
	}

	public static ItemStack createNewItemSetChest() {
		return buildSimpleItem(Material.CHEST, "�e�lNeue Items", Arrays.asList("�7�oSetzt den Inhalt des aktuellen Inventars", "�7�oals zu kaufende Items des Shops"));
	}

	public static ItemStack createNewCooldownClock() {
		return buildSimpleItem(Material.CLOCK, "�e�lLimit �ndern", Arrays.asList("�7�o�ndert das Limit des Shops"));
	}

	public static ItemStack createUpdatePositionTripwireHook() {
		return buildSimpleItem(Material.TRIPWIRE_HOOK, "�e�lPosition updaten", Arrays.asList("�7�oSucht im Umkreis von 10 Bl�cken", "�7�onach der neuen Armorstand Position"));
	}

	public static ItemStack createNewPermissionEnchantedBook() {
		return buildSimpleItem(Material.ENCHANTED_BOOK, "�e�lPermission �ndern", Arrays.asList("�7�o�ndert die Permission des Shops"));
	}

	public static ItemStack createNewNameSign() {
		return buildSimpleItem(Material.SIGN, "�e�lNamen �ndern", Arrays.asList("�7�o�ndert den Namen des Shops"));
	}

	public static ItemStack createNewCommandCommandBlock() {
		return buildSimpleItem(Material.COMMAND_BLOCK, "�e�lBefehl hinzuf�gen", Arrays.asList("�7�oLinksklick: Befehl hinzuf�gen", "�7�oRechtsklick: Befehle testen/ausf�hren", "�7�oItem Droppen: Alle Befehle l�schen"));
	}

	public static ItemStack createNewHeadPlayerHead() {
		return buildSimpleItem(Material.PLAYER_HEAD, "�e�lKopf �ndern", Arrays.asList("�7�oSetzt das Item in der Main-Hand als Kopf"));
	}

	public static ItemStack createToggleEnabledDye(Shop shop) {
		return buildSimpleItem(shop.isEnabled() ? Material.ROSE_RED : Material.LIME_DYE, shop.isEnabled() ? "�e�lShop deaktivieren" : "�e�lShop aktivieren", Arrays.asList("�7�oAktviert/Deaktiviert den Shop"));
	}

	public static ItemStack createQuestionBook() {
		return buildSimpleItem(Material.BOOK, "�e�lPasswort-Frage �ndern", Arrays.asList("�7�oLinksklick: Frage �ndern", "�7�oRechtsklick: Antwort �ndern", "�7�oItem Droppen: Passwortabfrage deaktivieren"));
	}

	public static ItemStack createStatisticItem(Shop shop) {
		List<String> Lore = new ArrayList<String>();
		Lore.add("�eName: �7" + ChatColor.translateAlternateColorCodes('&', shop.getShopName()));
		Lore.add("�eShop-UUID: �7" + shop.getID());
		Lore.add("�ePermission: �7shop." + shop.getShopPermission());
		Lore.add("�eAktiviert: �7" + (shop.isEnabled() ? "Ja" : "Nein"));
		Lore.add("�ePreis: �7" + shop.getPrice());
		Lore.add("�eLimit: �7" + shop.getCooldown());
		Lore.add("�eWie oft gekauft: �7" + shop.getTimesUsed() + " Mal");
		if (shop.getQuestion() != null) {
			Lore.add("�ePasswortabfrage:");
			Lore.add(" �eF: �7" + shop.getQuestion());
			Lore.add(" �eA: �7" + shop.getQuestionAnswer());
		} else {
			Lore.add("�ePasswortabfrage: �7Deaktiviert");
		}
		if (!shop.getExecutedCommands().isEmpty()) {
			Lore.add("�eBefehle: �7");
			for (String cmd : shop.getExecutedCommands()) {
				boolean first = true;
				for (String s : Utils.WrapString(cmd, 30)) {
					if (first) {
						Lore.add("�e- �7/" + s);
						first = false;
					} else {
						Lore.add("�e   �7" + s);
					}
				}
			}
		}
		return buildSimpleItem(Material.KNOWLEDGE_BOOK, "�a�lInfos & Statistik", Lore);
	}

	public static ItemStack createBuyButton(Shop shop, Player player) {
		if (shop.isAllowedToBuy(player.getUniqueId().toString())) {
			String price = shop.getPrice() + "�7 CT";
			String ItemName = "�a�l\u27A1 Kaufen";
			if (shop.getPrice() == 0) {
				price = "�6�lGRATIS";
				ItemName = "�a�l\u2726 Geschenk abholen";
				for (String s : Main.LoadedPrefixes.keySet()) {
					if (shop.getShopName().contains(s)) {
						ItemName = Main.LoadedPrefixes.get(s);
					}
				}
			}
			return buildSimpleItem(Material.LIME_DYE, ItemName, Arrays.asList("�f�m                              ", "�fDie obigen Items kaufen", "�f�m                              ", "�6�lPreis�8: �6" + price, "�6�lLimit�8: " + Utils.cooldownStringBuilder(shop.getCooldown(), "6", "7") + Utils.remainingTransactionsString(shop, player, "7", "7"), "�f�m                              "));
		} else {
			List<String> reason = Arrays.asList("�7Du kannst diesen Shop nur " + Math.abs(shop.getCooldown()) + "x benutzen");
			if (shop.getCooldown() > 0)
				reason = Arrays.asList("�7Du kannst diesen Shop erst wieder in", Utils.cooldownStringBuilder(shop.getTimeRemaining(player.getUniqueId().toString()), "c", "7"), "�7benutzen");
			return buildSimpleItem(Material.BARRIER, "�c�l\u2716 Bereits genutzt", reason);
		}
	}

}
