package net.aerulion.shop.listener;

import net.aerulion.shop.Main;
import net.aerulion.shop.utils.Shop;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkLoadListener implements Listener {

  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    for (Entity entity : event.getChunk().getEntities()) {
      if (entity.getType().equals(EntityType.ARMOR_STAND)) {
        if (Main.LoadedShops.containsKey(entity.getCustomName())) {
          Shop shop = Main.LoadedShops.get(entity.getCustomName());
          shop.startParticles();
        }
      }
    }
  }

  @EventHandler
  public void onChunkUnload(ChunkUnloadEvent event) {
    for (Entity entity : event.getChunk().getEntities()) {
      if (entity.getType().equals(EntityType.ARMOR_STAND)) {
        if (Main.LoadedShops.containsKey(entity.getCustomName())) {
          Shop shop = Main.LoadedShops.get(entity.getCustomName());
          shop.stopParticles();
        }
      }
    }
  }
}