package net.aerulion.shop.listener;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import net.aerulion.shop.Main;
import net.aerulion.shop.utils.Shop;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class EntityLoadListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR)
  public void listen(final @NotNull EntityAddToWorldEvent event) {
    if (event.getEntity().getType() == EntityType.ARMOR_STAND && Main.LOADED_SHOPS.containsKey(
        event.getEntity().getCustomName())) {
      final Shop shop = Main.LOADED_SHOPS.get(event.getEntity().getCustomName());
      shop.startParticles();
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void listen(final @NotNull EntityRemoveFromWorldEvent event) {
    if (event.getEntity().getType() == EntityType.ARMOR_STAND && Main.LOADED_SHOPS.containsKey(
        event.getEntity().getCustomName())) {
      final Shop shop = Main.LOADED_SHOPS.get(event.getEntity().getCustomName());
      shop.stopParticles();
    }
  }

}