package net.aerulion.shop.listener;

import net.aerulion.shop.Main;
import net.aerulion.shop.utils.Shop;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;

public class ChunkLoadListener implements Listener {

  @EventHandler
  public void onChunkLoad(@NotNull ChunkLoadEvent event) {
    for (@NotNull Entity entity : event.getChunk().getEntities()) {
      if (entity.getType().equals(EntityType.ARMOR_STAND)) {
        if (Main.loadedShops.containsKey(entity.getCustomName())) {
          Shop shop = Main.loadedShops.get(entity.getCustomName());
          shop.startParticles();
        }
      }
    }
  }

  @EventHandler
  public void onChunkUnload(@NotNull ChunkUnloadEvent event) {
    for (@NotNull Entity entity : event.getChunk().getEntities()) {
      if (entity.getType().equals(EntityType.ARMOR_STAND)) {
        if (Main.loadedShops.containsKey(entity.getCustomName())) {
          Shop shop = Main.loadedShops.get(entity.getCustomName());
          shop.stopParticles();
        }
      }
    }
  }
}