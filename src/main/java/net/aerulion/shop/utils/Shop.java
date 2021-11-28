package net.aerulion.shop.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.aerulion.shop.task.particles.WhiteSpiralTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Shop {

  private final String shopID;
  private final boolean virtual;
  private Map<String, String> transactionDates;
  private List<ItemStack> itemsForSale;
  private long cooldown;
  private double price;
  private @Nullable WhiteSpiralTask particleTask;
  private Location location;
  private String shopName;
  private String shopPermission;
  private int timesUsed;
  private List<String> executedCommands;
  private @Nullable String question;
  private @Nullable String questionAnswer;
  private boolean enabled;

  public Shop(Map<String, String> transactionDates, List<ItemStack> items, double price,
      long cooldown, Location location, String shopID, String shopName, String shopPermission,
      int timesUsed, List<String> commands, boolean enabled, boolean virtual, String question,
      String questionAnswer) {
    this.transactionDates = transactionDates;
    this.itemsForSale = items;
    this.price = price;
    this.cooldown = cooldown;
    this.location = location;
    this.shopID = shopID;
    this.shopName = shopName;
    this.shopPermission = shopPermission;
    this.timesUsed = timesUsed;
    this.executedCommands = commands;
    this.question = question;
    this.questionAnswer = questionAnswer;
    this.enabled = enabled;
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
    return this.itemsForSale;
  }

  public boolean isAllowedToBuy(String uuid) {
    if (this.transactionDates.containsKey(uuid)) {
      if (this.cooldown > -1) {
        return (Long.parseLong(Util.splitTransactionDates(this.transactionDates.get(uuid))[1])
            + this.cooldown) < System.currentTimeMillis();
      } else {
        return (Integer.parseInt(Util.splitTransactionDates(this.transactionDates.get(uuid))[0]))
            < Math.abs(this.cooldown);
      }
    } else {
      return true;
    }
  }

  public long getTimeRemaining(String uuid) {
    return ((Long.parseLong(Util.splitTransactionDates(this.transactionDates.get(uuid))[1]))
        + this.cooldown) - System.currentTimeMillis();
  }

  public int getRemainingTransactions(String uuid) {
    int max = (int) Math.abs(this.cooldown);
    if (this.transactionDates.containsKey(uuid)) {
      return max - (Integer.parseInt(
          Util.splitTransactionDates(this.transactionDates.get(uuid))[0]));
    } else {
      return max;
    }
  }

  public double getPrice() {
    return this.price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public void setPermission(String permission) {
    this.shopPermission = permission;
  }

  public void setName(String name) {
    this.shopName = name;
  }

  public long getCooldown() {
    return this.cooldown;
  }

  public void setCooldown(long cooldown) {
    this.cooldown = cooldown;
  }

  public void addCommand(String command) {
    this.executedCommands.add(command);
  }

  public void resetCommands() {
    this.executedCommands = new ArrayList<>();
  }

  public void addTransaction(@NotNull Player player) {
    if (this.transactionDates.containsKey(player.getUniqueId().toString())) {
      this.transactionDates.put(player.getUniqueId().toString(), (Integer.parseInt(
          Util.splitTransactionDates(this.transactionDates.get(player.getUniqueId().toString()))[0])
          + 1) + "@@@" + System.currentTimeMillis());
    } else {
      this.transactionDates.put(player.getUniqueId().toString(),
          "1" + "@@@" + System.currentTimeMillis());
    }

  }

  public void resetTransactions() {
    this.transactionDates = new HashMap<>();
    this.timesUsed = 0;
  }

  public Map<String, String> getTransactionDates() {
    return this.transactionDates;
  }

  public void startParticles() {
    if (!this.virtual) {
      stopParticles();
      this.particleTask = new WhiteSpiralTask(this.location);
    }

  }

  public void stopParticles() {
    if (this.particleTask != null) {
      this.particleTask.stop();
      this.particleTask = null;
    }
  }

  public Location getShopLocation() {
    return this.location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public void setSoldItem(List<ItemStack> items) {
    this.itemsForSale = items;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public void toggleEnabled() {
    this.enabled = !this.enabled;
  }

  public boolean isVirtual() {
    return this.virtual;
  }

  public @Nullable String getQuestion() {
    return this.question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public @Nullable String getQuestionAnswer() {
    return this.questionAnswer;
  }

  public void setQuestionAnswer(String answer) {
    this.questionAnswer = answer;
  }

  public void resetQuestion() {
    this.question = null;
    this.questionAnswer = null;
  }
}