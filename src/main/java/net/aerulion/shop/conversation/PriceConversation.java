package net.aerulion.shop.conversation;

import net.aerulion.shop.utils.Util;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PriceConversation extends ValidatingPrompt {

  @Override
  public @NotNull String getPromptText(ConversationContext con) {
    return "Tippe den neuen Preis in den Chat. Schreibe 'stop' um den Vorgang abzubrechen.";
  }

  @Override
  protected boolean isInputValid(ConversationContext context, @NotNull String input) {
    try {
      Double.parseDouble(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  @Override
  protected Prompt acceptValidatedInput(@NotNull ConversationContext con, @NotNull String input) {
    Util.setNewShopPrice((Player) con.getForWhom(), Double.parseDouble(input));
    return null;
  }

  @Override
  public String getFailedValidationText(ConversationContext con, String invalidInput) {
    return "Fehler: Ungültige Eingabe. Schreibe 'stop' um den Vorgang abzubrechen.";
  }
}