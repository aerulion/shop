package net.aerulion.shop.Conversations;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import net.aerulion.shop.Utils.Utils;

public class CommandConversation extends ValidatingPrompt {

	@Override
	public String getPromptText(ConversationContext con) {
		return "Tippe den Befehl den du hinzuf�gen willst in den Chat. Das '/' am Anfang wird automatisch hinzugef�gt. Schreibe 'stop' um den Vorgang abzubrechen. Folgende Variablen sind verf�gbar: %player% %shopname%";
	}

	@Override
	public String getFailedValidationText(ConversationContext con, String InvalidInput) {
		return "Fehler: Ung�ltige Eingabe.";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext con, String input) {
		Utils.setNewShopCommands((Player) con.getForWhom(), input);
		return null;
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		return true;
	}
}
