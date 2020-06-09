package net.aerulion.shop.Conversations;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import net.aerulion.shop.Utils.Utils;

public class QuestionConversation extends ValidatingPrompt {

    @Override
    public String getPromptText(ConversationContext con) {
        return "Tippe die Frage in den Chat. Schreibe 'stop' um den Vorgang abzubrechen.";
    }

    @Override
    public String getFailedValidationText(ConversationContext con, String InvalidInput) {
        return "Fehler: Ungültige Eingabe. Schreibe 'stop' um den Vorgang abzubrechen.";
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext con, String input) {
        Utils.setNewShopQuestion((Player) con.getForWhom(), input);
        return null;
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return true;
    }
}
