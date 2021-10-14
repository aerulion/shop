package net.aerulion.shop.conversation;

import net.aerulion.shop.utils.Util;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

public class CooldownConversation extends ValidatingPrompt {

  @Override
  public String getPromptText(ConversationContext con) {
    return "Tippe das neue Limit in den Chat. Nutze das Format Tage:Stunden oder -Anzahl. Schreibe 'stop' um den Vorgang abzubrechen.";
  }

  @Override
  protected boolean isInputValid(ConversationContext context, String input) {
    return Util.checkCooldownPattern(input);
  }

  @Override
  protected Prompt acceptValidatedInput(ConversationContext con, String input) {
    Util.setNewShopCooldown((Player) con.getForWhom(), input);
    return null;
  }

  @Override
  public String getFailedValidationText(ConversationContext con, String InvalidInput) {
    return "Fehler: Ungültige Eingabe, bitte nutze das Format Tage:Stunden oder -Anzahl. Schreibe 'stop' um den Vorgang abzubrechen.";
  }
}