package net.aerulion.shop.task;

import java.io.File;
import java.io.IOException;
import net.aerulion.nucleus.api.base64.Base64Utils;
import net.aerulion.shop.Main;
import net.aerulion.shop.utils.FileManager;
import net.aerulion.shop.utils.Shop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class ShopSaveTask extends BukkitRunnable {

  private final String uuid;

  public ShopSaveTask(final String shopID) {
    this.uuid = shopID;
    this.runTaskAsynchronously(Main.plugin);
  }

  @Override
  public void run() {
    final Shop shop = Main.LOADED_SHOPS.get(uuid);
    final @NotNull File shopFile = new File("plugins/Shop/Shops", uuid + ".yml");
    final @NotNull FileConfiguration cfg = YamlConfiguration.loadConfiguration(shopFile);
    cfg.set("ShopName", shop.getShopName());
    cfg.set("ShopPermission", shop.getShopPermission());
    if (!shop.isVirtual()) {
      cfg.set("Location.X", shop.getShopLocation().getX());
      cfg.set("Location.Y", shop.getShopLocation().getY());
      cfg.set("Location.Z", shop.getShopLocation().getZ());
      cfg.set("Location.World", shop.getShopLocation().getWorld().getName());
    }
    cfg.set("TransactionDates", FileManager.serializeTransactionDates(shop.getTransactionDates()));
    cfg.set("Cooldown", shop.getCooldown());
    cfg.set("Price", shop.getPrice());
    cfg.set("ItemsForSale", Base64Utils.encodeItemStackList(shop.getSoldItems()));
    cfg.set("timesUsed", shop.getTimesUsed());
    cfg.set("Enabled", shop.isEnabled());
    cfg.set("Virtual", shop.isVirtual());
    cfg.set("Question", shop.getQuestion());
    cfg.set("QuestionAnswer", shop.getQuestionAnswer());
    cfg.set("ExecutedCommands", shop.getExecutedCommands());
    try {
      cfg.save(shopFile);
    } catch (final IOException ignored) {
    }
  }
}