package net.aerulion.shop.Conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import net.aerulion.shop.Main;
import net.aerulion.shop.Utils.Shop;
import net.aerulion.shop.Utils.Utils;

public class QuestionAskConversation extends ValidatingPrompt {

	@Override
	public String getPromptText(ConversationContext con) {
		Shop shop = Main.LoadedShops.get(Main.BuyingPlayers.get(((Player) con.getForWhom()).getName()));
		return "Um diesen Shop nutzen zu können muss zuerst die untenstehende Frage richtig beantwortet werden. Schreibe die Antwort in den Chat. Groß- und Kleinschreibung wird nicht beachtet. Schreibe 'stop' um den Vorgang abzubrechen. §aFrage: " + ChatColor.translateAlternateColorCodes('&', shop.getQuestion());
	}

	@Override
	public String getFailedValidationText(ConversationContext con, String InvalidInput) {
		return "Fehler: Ungültige Eingabe. Schreibe 'stop' um den Vorgang abzubrechen.";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext con, String input) {
		Utils.validateQuestion((Player) con.getForWhom(), input);
		return null;
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		return true;
	}
}
