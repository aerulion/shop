package net.aerulion.shop.utils;

public class Lang {

    public static final String INVENTORY_NAME_SHOP = "§8§lShop §9§l| §8";
    public static final String INVENTORY_NAME_ADMIN = "§c§lAdminpanel §8§lShop";
    public static final String CHAT_PREFIX = "§f[§9§lShop§f] §7";

    public static final String ERROR_MAX_STACK_AMOUNT = CHAT_PREFIX + "§cFehler: Du kannst nicht mehr als 27 Stacks pro Shop verkaufen.";
    public static final String ERROR_NOT_ENOUGH_MONEY = CHAT_PREFIX + "§cDu hast nicht genug Geld.";
    public static final String ERROR_INVENTORY_FULL = CHAT_PREFIX + "§cDu hast nicht genug Platz im Inventar";
    public static final String ERROR_TRANSACTION_FAILED = CHAT_PREFIX + "§cEin Fehler ist aufgetreten, die Transaktion wurde abgebrochen.";
    public static final String ERROR_NO_PLAYER = CHAT_PREFIX + "§cDieser Befehl kann nur als Spieler ausgeführt werden.";
    public static final String ERROR_NO_COMMAND_PERMISSION = CHAT_PREFIX + "§cDu hast keine Rechte diesen Befehl zu nutzen.";
    public static final String ERROR_NO_SHOP_PERMISSION = CHAT_PREFIX + "Du hast keine Rechte diesen Shop zu nutzen.";
    public static final String ERROR_INVALID_NUMBER = CHAT_PREFIX + "§cKeine gültige Zahl.";
    public static final String ERROR_NUMBER_TOO_SMALL = CHAT_PREFIX + "§cDie Zahl kann nicht kleiner 1 sein.";
    public static final String ERROR_INVALID_PRICE = CHAT_PREFIX + "§cKein gültiger Preis.";
    public static final String ERROR_INVALID_BOOLEAN = CHAT_PREFIX + "§cKein gültiger boolean.";
    public static final String ERROR_INVALID_LIMIT = CHAT_PREFIX + "§cKein gültiges Limit.";
    public static final String ERROR_SYNTAX = CHAT_PREFIX + "§cSyntaxfehler.";
    public static final String ERROR_NO_ITEM_IN_HAND = CHAT_PREFIX + "§cFehler: Du hälst kein Item in der Hand.";
    public static final String ERROR_SHOP_DISABLED = CHAT_PREFIX + "§cDieser Shop ist deaktiviert.";
    public static final String ERROR_WRONG_QUESTION_ANSWER = CHAT_PREFIX + "§cDas eingegebene Passwort ist nicht korrekt.";
    public static final String ERROR_PLAYER_NOT_FOUND = CHAT_PREFIX + "§cFehler: Spieler nicht gefunden.";
    public static final String ERROR_SHOP_NOT_FOUND = CHAT_PREFIX + "§cFehler: Shop nicht gefunden.";

    public static final String SHOP_ADDED = CHAT_PREFIX + "§aShop erfolgreich hinzugefügt.";
    public static final String NEW_PRICE = CHAT_PREFIX + "Der neue Preis beträgt nun: ";
    public static final String NEW_SHOP_ITEMS = CHAT_PREFIX + "Neue Shop Items wurden festgelegt.";
    public static final String TOGGLED_ENABLED = CHAT_PREFIX + "Der Shop wurde (de-)aktiviert.";
    public static final String NEW_LIMIT = CHAT_PREFIX + "Das neue Limit beträgt nun: ";
    public static final String NEW_PERMISSION = CHAT_PREFIX + "Die neue Permission lautet: §eshop.";
    public static final String NEW_NAME = CHAT_PREFIX + "Die neuer Name lautet: §e";
    public static final String COMMAND_ADDED = CHAT_PREFIX + "Folgender Befehl wurde hinzugefügt: §e/";
    public static final String ALL_COMMANDS_DELETED = CHAT_PREFIX + "Alle Befehle wurden vom Shop gelöscht.";
    public static final String ALL_PLAYERDATA_DELETED = CHAT_PREFIX + "Alle Spielerdaten für diesen Shop wurden zurückgesetzt.";
    public static final String RELOADED = CHAT_PREFIX + "Alle Shopdaten wurden neu geladen.";
    public static final String HEAD_UPDATED = CHAT_PREFIX + "Der Kopf wurde geändert.";
    public static final String NEW_QUESTION = CHAT_PREFIX + "Die neue Frage lautet: ";
    public static final String NEW_QUESTION_ANSWER = CHAT_PREFIX + "Die neue Antwort lautet: ";
    public static final String QUESTION_RESET = CHAT_PREFIX + "Die Passwortabfrage wurde deaktiviert.";
    public static final String ACTION_ESCAPED = CHAT_PREFIX + "Vorgang abgebrochen.";

    public static final String CONSOLE_ENABLING = CHAT_PREFIX + "§bAktiviere Plugin...";
    public static final String CONSOLE_PLUGIN_ENABLED = CHAT_PREFIX + "§bDas Plugin wurde aktiviert.";
    public static final String CONSOLE_DISABLING = CHAT_PREFIX + "§bDeaktiviere Plugin...";
    public static final String CONSOLE_PLUGIN_DISABLED = CHAT_PREFIX + "§bDas Plugin wurde deaktiviert.";
    public static final String CONSOLE_SHOPS_LOADED = "§b Shops wurden geladen. Dauerte §e";
}