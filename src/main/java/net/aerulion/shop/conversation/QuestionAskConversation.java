package net.aerulion.shop.conversation;

import net.aerulion.shop.Main;
import net.aerulion.shop.utils.Shop;
import net.aerulion.shop.utils.Util;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QuestionAskConversation extends ValidatingPrompt {

  @Override
  public @NotNull String getPromptText(final @NotNull ConversationContext context) {
    final Shop shop = Main.LOADED_SHOPS.get(Main.BUYING_PLAYERS.get(((Player) context.getForWhom()).getName()));
    return
        "Um diesen Shop nutzen zu können muss zuerst die untenstehende Frage richtig beantwortet werden. Schreibe die Antwort in den Chat. Groß- und Kleinschreibung wird nicht beachtet. Schreibe 'ende' um den Vorgang abzubrechen. §aFrage: " +
            ChatColor.translateAlternateColorCodes('&', shop.getQuestion());
  }

  @Override
  protected boolean isInputValid(final @NotNull ConversationContext context, final @NotNull String input) {
    return true;
  }

  @Override
  protected Prompt acceptValidatedInput(final @NotNull ConversationContext context, final @NotNull String input) {
    Util.validateQuestion((Player) context.getForWhom(), input);
    return null;
  }

  @Override
  public String getFailedValidationText(final @NotNull ConversationContext context,
      final @NotNull String invalidInput) {
    return "Fehler: Ungültige Eingabe. Schreibe 'ende' um den Vorgang abzubrechen.";
  }

}