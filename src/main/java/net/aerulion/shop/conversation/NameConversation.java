package net.aerulion.shop.conversation;

import net.aerulion.shop.utils.Util;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NameConversation extends ValidatingPrompt {

  @Override
  public @NotNull String getPromptText(ConversationContext con) {
    return "Tippe den neuen Namen in den Chat. Schreibe 'stop' um den Vorgang abzubrechen.";
  }

  @Override
  protected boolean isInputValid(ConversationContext context, String input) {
    return true;
  }

  @Override
  protected Prompt acceptValidatedInput(@NotNull ConversationContext con, @NotNull String input) {
    Util.setNewShopName((Player) con.getForWhom(), input);
    return null;
  }

  @Override
  public String getFailedValidationText(ConversationContext con, String invalidInput) {
    return "Fehler: Ungültige Eingabe, es dürfen keine Leerzeichen enthalten sein.";
  }
}