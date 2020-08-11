package net.aerulion.shop.listener;

import net.aerulion.shop.Main;
import net.aerulion.shop.utils.Inventories;
import net.aerulion.shop.utils.Shop;
import net.aerulion.shop.utils.Util;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class EntityInteractListener implements Listener {
    @EventHandler
    public void onEntityInteraction(PlayerInteractAtEntityEvent e) {
        if (Main.LoadedShops.containsKey(e.getRightClicked().getCustomName())) {
            e.setCancelled(true);
            Shop shop = Main.LoadedShops.get(e.getRightClicked().getCustomName());
            Player player = e.getPlayer();
            if (player.isSneaking() && player.hasPermission("shop.admin")) {
                Main.AdminPanelUser.put(player.getName(), shop.getID());
                player.openInventory(Inventories.AdminPanel(shop));
                player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 0.5F, 1.3F);
                return;
            }
            Util.openShopToPlayer(player, shop);
        }
    }
}