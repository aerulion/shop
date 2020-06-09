package net.aerulion.shop.Listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import net.aerulion.shop.Main;
import net.aerulion.shop.Utils.Shop;

public class onChunkUnload implements Listener {

    @EventHandler
    public void onChunkUnloading(ChunkUnloadEvent e) {
        for (Entity entity : e.getChunk().getEntities()) {
            if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                if (Main.LoadedShops.keySet().contains(entity.getCustomName())) {
                    Shop shop = Main.LoadedShops.get(entity.getCustomName());
                    shop.stopParticles();
                }
            }
        }
    }
}
