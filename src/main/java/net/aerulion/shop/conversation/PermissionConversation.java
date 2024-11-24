package net.aerulion.shop.conversation;

import net.aerulion.shop.utils.Util;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PermissionConversation extends ValidatingPrompt {

  @Override
  public @NotNull String getPromptText(final @NotNull ConversationContext context) {
    return "Tippe die neue Permission in den Chat. Das 'shop.' wird automatisch an den Anfang hinzugefügt. Schreibe 'stop' um den Vorgang abzubrechen.";
  }

  @Override
  protected boolean isInputValid(final @NotNull ConversationContext context, final @NotNull String input) {
    return !input.contains(" ");
  }

  @Override
  protected Prompt acceptValidatedInput(final @NotNull ConversationContext context, final @NotNull String input) {
    Util.setNewShopPermission((Player) context.getForWhom(), input);
    return null;
  }

  @Override
  public String getFailedValidationText(final @NotNull ConversationContext context,
      final @NotNull String invalidInput) {
    return "Fehler: Ungültige Eingabe, es dürfen keine Leerzeichen enthalten sein.";
  }

}