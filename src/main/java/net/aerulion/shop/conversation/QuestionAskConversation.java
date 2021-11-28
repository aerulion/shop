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
  public @NotNull String getPromptText(@NotNull ConversationContext con) {
    Shop shop = Main.loadedShops.get(Main.buyingPlayers.get(((Player) con.getForWhom()).getName()));
    return
        "Um diesen Shop nutzen zu können muss zuerst die untenstehende Frage richtig beantwortet werden. Schreibe die Antwort in den Chat. Groß- und Kleinschreibung wird nicht beachtet. Schreibe 'stop' um den Vorgang abzubrechen. §aFrage: "
            + ChatColor.translateAlternateColorCodes('&', shop.getQuestion());
  }

  @Override
  protected boolean isInputValid(ConversationContext context, String input) {
    return true;
  }

  @Override
  protected Prompt acceptValidatedInput(@NotNull ConversationContext con, @NotNull String input) {
    Util.validateQuestion((Player) con.getForWhom(), input);
    return null;
  }

  @Override
  public String getFailedValidationText(ConversationContext con, String invalidInput) {
    return "Fehler: Ungültige Eingabe. Schreibe 'stop' um den Vorgang abzubrechen.";
  }
}