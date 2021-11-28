package net.aerulion.shop.conversation;

import net.aerulion.shop.utils.Util;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PermissionConversation extends ValidatingPrompt {

  @Override
  public @NotNull String getPromptText(ConversationContext con) {
    return "Tippe die neue Permission in den Chat. Das 'shop.' wird automatisch an den Anfang hinzugefügt. Schreibe 'stop' um den Vorgang abzubrechen.";
  }

  @Override
  protected boolean isInputValid(ConversationContext context, @NotNull String input) {
    return !input.contains(" ");
  }

  @Override
  protected Prompt acceptValidatedInput(@NotNull ConversationContext con, String input) {
    Util.setNewShopPermission((Player) con.getForWhom(), input);
    return null;
  }

  @Override
  public String getFailedValidationText(ConversationContext con, String invalidInput) {
    return "Fehler: Ungültige Eingabe, es dürfen keine Leerzeichen enthalten sein.";
  }
}