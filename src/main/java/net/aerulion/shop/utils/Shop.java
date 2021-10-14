package net.aerulion.shop.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.aerulion.shop.task.particles.WhiteSpiralTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Shop {

  private final String shopID;
  private final boolean virtual;
  private HashMap<String, String> TransactionDates;
  private List<ItemStack> ItemsForSale;
  private long Cooldown;
  private double Price;
  private WhiteSpiralTask ParticleTask;
  private Location Location;
  private String shopName;
  private String shopPermission;
  private int timesUsed;
  private List<String> executedCommands;
  private String Question;
  private String QuestionAnswer;
  private boolean Enabled;

  public Shop(HashMap<String, String> transactionDates, List<ItemStack> items, double price,
      long cooldown, Location location, String shopID, String shopName, String shopPermission,
      int timesUsed, List<String> commands, boolean enabled, boolean virtual, String question,
      String questionAnswer) {
    this.TransactionDates = transactionDates;
    this.ItemsForSale = items;
    this.Price = price;
    this.Cooldown = cooldown;
    this.Location = location;
    this.shopID = shopID;
    this.shopName = shopName;
    this.shopPermission = shopPermission;
    this.timesUsed = timesUsed;
    this.executedCommands = commands;
    this.Question = question;
    this.QuestionAnswer = questionAnswer;
    this.Enabled = enabled;
    this.virtual = virtual;
  }

  public String getShopName() {
    return this.shopName;
  }

  public String getID() {
    return this.shopID;
  }

  public String getShopPermission() {
    return this.shopPermission;
  }

  public int getTimesUsed() {
    return this.timesUsed;
  }

  public List<String> getExecutedCommands() {
    return this.executedCommands;
  }

  public void addTimesUsed() {
    this.timesUsed++;
  }

  public List<ItemStack> getSoldItems() {
    return this.ItemsForSale;
  }

  public boolean isAllowedToBuy(String UUID) {
      if (this.TransactionDates.containsKey(UUID)) {
          if (this.Cooldown > -1) {
              return (Long.parseLong(Util.splitTransactionDates(this.TransactionDates.get(UUID))[1])
                  + this.Cooldown) < System.currentTimeMillis();
          } else {
              return (Integer.parseInt(Util.splitTransactionDates(this.TransactionDates.get(UUID))[0]))
                  < Math.abs(this.Cooldown);
          }
      } else {
          return true;
      }
  }

  public long getTimeRemaining(String UUID) {
    return ((Long.parseLong(Util.splitTransactionDates(this.TransactionDates.get(UUID))[1]))
        + this.Cooldown) - System.currentTimeMillis();
  }

  public int getRemainingTransactions(String UUID) {
    int max = (int) Math.abs(this.Cooldown);
      if (this.TransactionDates.containsKey(UUID)) {
          return max - (Integer.parseInt(
              Util.splitTransactionDates(this.TransactionDates.get(UUID))[0]));
      } else {
          return max;
      }
  }

  public double getPrice() {
    return this.Price;
  }

  public void setPrice(double price) {
    this.Price = price;
  }

  public void setPermission(String permission) {
    this.shopPermission = permission;
  }

  public void setName(String name) {
    this.shopName = name;
  }

  public long getCooldown() {
    return this.Cooldown;
  }

  public void setCooldown(long cooldown) {
    this.Cooldown = cooldown;
  }

  public void addCommand(String command) {
    this.executedCommands.add(command);
  }

  public void resetCommands() {
    this.executedCommands = new ArrayList<>();
  }

  public void addTransaction(Player player) {
      if (this.TransactionDates.containsKey(player.getUniqueId().toString())) {
          this.TransactionDates.put(player.getUniqueId().toString(), (Integer.parseInt(
              Util.splitTransactionDates(
                  this.TransactionDates.get(player.getUniqueId().toString()))[0]) + 1) + "@@@"
              + System.currentTimeMillis());
      } else {
          this.TransactionDates.put(player.getUniqueId().toString(), "1" + "@@@" + System.currentTimeMillis());
      }

  }

  public void resetTransactions() {
    this.TransactionDates = new HashMap<>();
    this.timesUsed = 0;
  }

  public HashMap<String, String> getTransactionDates() {
    return this.TransactionDates;
  }

  public void startParticles() {
    if (!this.virtual) {
      stopParticles();
      this.ParticleTask = new WhiteSpiralTask(this.Location);
    }

  }

  public void stopParticles() {
    if (this.ParticleTask != null) {
      this.ParticleTask.stop();
      this.ParticleTask = null;
    }
  }

  public Location getShopLocation() {
    return this.Location;
  }

  public void setLocation(Location location) {
    this.Location = location;
  }

  public void setSoldItem(List<ItemStack> items) {
    this.ItemsForSale = items;
  }

  public boolean isEnabled() {
    return this.Enabled;
  }

  public void toggleEnabled() {
    this.Enabled = !this.Enabled;
  }

  public boolean isVirtual() {
    return this.virtual;
  }

  public String getQuestion() {
    return this.Question;
  }

  public void setQuestion(String question) {
    this.Question = question;
  }

  public String getQuestionAnswer() {
    return this.QuestionAnswer;
  }

  public void setQuestionAnswer(String answer) {
    this.QuestionAnswer = answer;
  }

  public void resetQuestion() {
    this.Question = null;
    this.QuestionAnswer = null;
  }
}