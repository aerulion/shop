package net.aerulion.shop.Conversations;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import net.aerulion.shop.Utils.Utils;

public class CooldownConversation extends ValidatingPrompt {

	@Override
	public String getPromptText(ConversationContext con) {
		return "Tippe das neue Limit in den Chat. Nutze das Format Tage:Stunden oder -Anzahl. Schreibe 'stop' um den Vorgang abzubrechen.";
	}

	@Override
	public String getFailedValidationText(ConversationContext con, String InvalidInput) {
		return "Fehler: Ungültige Eingabe, bitte nutze das Format Tage:Stunden oder -Anzahl. Schreibe 'stop' um den Vorgang abzubrechen.";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext con, String input) {
		Utils.setNewShopCooldown((Player) con.getForWhom(), input);
		return null;
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		if (Utils.checkCooldownPattern(input))
			return true;
		return false;
	}
}
