package net.aerulion.shop.conversation;

import net.aerulion.shop.utils.Util;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QuestionAnswerConversation extends ValidatingPrompt {

  @Override
  public @NotNull String getPromptText(final @NotNull ConversationContext context) {
    return "Tippe die Antwort in den Chat. Schreibe 'stop' um den Vorgang abzubrechen.";
  }

  @Override
  protected boolean isInputValid(final @NotNull ConversationContext context, final @NotNull String input) {
    return true;
  }

  @Override
  protected Prompt acceptValidatedInput(final @NotNull ConversationContext context, final @NotNull String input) {
    Util.setNewShopQuestionAnswer((Player) context.getForWhom(), input);
    return null;
  }

  @Override
  public String getFailedValidationText(final @NotNull ConversationContext context,
      final @NotNull String invalidInput) {
    return "Fehler: Ungültige Eingabe. Schreibe 'stop' um den Vorgang abzubrechen.";
  }

}