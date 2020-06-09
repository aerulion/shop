package net.aerulion.shop.Conversations;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import net.aerulion.shop.Utils.Utils;

public class PermissionConversation extends ValidatingPrompt {

    @Override
    public String getPromptText(ConversationContext con) {
        return "Tippe die neue Permission in den Chat. Das 'shop.' wird automatisch an den Anfang hinzugefügt. Schreibe 'stop' um den Vorgang abzubrechen.";
    }

    @Override
    public String getFailedValidationText(ConversationContext con, String InvalidInput) {
        return "Fehler: Ungültige Eingabe, es dürfen keine Leerzeichen enthalten sein.";
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext con, String input) {
        Utils.setNewShopPermission((Player) con.getForWhom(), input);
        return null;
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return !input.contains(" ");
    }
}
