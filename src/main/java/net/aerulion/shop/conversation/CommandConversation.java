package net.aerulion.shop.conversation;

import net.aerulion.shop.utils.Util;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandConversation extends ValidatingPrompt {

  @Override
  public @NotNull String getPromptText(final @NotNull ConversationContext context) {
    return "Tippe den Befehl den du hinzufügen willst in den Chat. Das '/' am Anfang wird automatisch hinzugefügt. Schreibe 'stop' um den Vorgang abzubrechen. Folgende Variablen sind verfügbar: %player% %shopname%";
  }

  @Override
  protected boolean isInputValid(final @NotNull ConversationContext context, final @NotNull String input) {
    return true;
  }

  @Override
  protected Prompt acceptValidatedInput(final @NotNull ConversationContext context, final @NotNull String input) {
    Util.setNewShopCommands((Player) context.getForWhom(), input);
    return null;
  }

  @Override
  public String getFailedValidationText(final @NotNull ConversationContext context, final @NotNull String invalidInput) {
    return "Fehler: Ungültige Eingabe.";
  }
}