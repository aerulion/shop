package net.aerulion.shop.conversation;

import net.aerulion.shop.utils.Util;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CooldownConversation extends ValidatingPrompt {

  @Override
  public @NotNull String getPromptText(final @NotNull ConversationContext context) {
    return "Tippe das neue Limit in den Chat. Nutze das Format Tage:Stunden oder -Anzahl. Schreibe 'stop' um den Vorgang abzubrechen.";
  }

  @Override
  protected boolean isInputValid(final @NotNull ConversationContext context, final @NotNull String input) {
    return Util.checkCooldownPattern(input);
  }

  @Override
  protected Prompt acceptValidatedInput(final @NotNull ConversationContext context, final @NotNull String input) {
    Util.setNewShopCooldown((Player) context.getForWhom(), input);
    return null;
  }

  @Override
  public String getFailedValidationText(final @NotNull ConversationContext context,
      final @NotNull String invalidInput) {
    return "Fehler: Ungültige Eingabe, bitte nutze das Format Tage:Stunden oder -Anzahl. Schreibe 'stop' um den Vorgang abzubrechen.";
  }

}