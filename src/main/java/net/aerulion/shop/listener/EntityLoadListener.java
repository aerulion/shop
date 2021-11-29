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
  public void listen(@NotNull EntityAddToWorldEvent event) {
    if (event.getEntity().getType().equals(EntityType.ARMOR_STAND) && Main.loadedShops.containsKey(
        event.getEntity().getCustomName())) {
      Shop shop = Main.loadedShops.get(event.getEntity().getCustomName());
      shop.startParticles();
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void listen(@NotNull EntityRemoveFromWorldEvent event) {
    if (event.getEntity().getType().equals(EntityType.ARMOR_STAND) && Main.loadedShops.containsKey(
        event.getEntity().getCustomName())) {
      Shop shop = Main.loadedShops.get(event.getEntity().getCustomName());
      shop.stopParticles();
    }
  }

}