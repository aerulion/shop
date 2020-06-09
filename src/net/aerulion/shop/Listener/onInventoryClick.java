package net.aerulion.shop.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.aerulion.shop.Main;
import net.aerulion.shop.Conversations.CommandConversation;
import net.aerulion.shop.Conversations.CooldownConversation;
import net.aerulion.shop.Conversations.NameConversation;
import net.aerulion.shop.Conversations.PermissionConversation;
import net.aerulion.shop.Conversations.PriceConversation;
import net.aerulion.shop.Conversations.QuestionAnswerConversation;
import net.aerulion.shop.Conversations.QuestionConversation;
import net.aerulion.shop.Utils.Lang;
import net.aerulion.shop.Utils.Shop;
import net.aerulion.shop.Utils.Utils;

public class onInventoryClick implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (ChatColor.stripColor(e.getView().getTitle()).startsWith("Shop | ")) {
                if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) || e.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) {
                    e.setCancelled(true);
                    return;
                }
                if (e.getAction().equals(InventoryAction.PLACE_ALL) || e.getAction().equals(InventoryAction.PLACE_ONE) || e.getAction().equals(InventoryAction.PLACE_SOME)) {
                    if ((e.getRawSlot() >= 0 && e.getRawSlot() <= e.getView().getTopInventory().getSize() - 1)) {
                        e.setCancelled(true);
                        return;
                    }

                }
                if (e.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
                    e.setCancelled(true);
                    return;
                }
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    if ((e.getRawSlot() >= 0 && e.getRawSlot() <= e.getView().getTopInventory().getSize() - 1)) {
                        if (!(e.getWhoClicked().hasPermission("shop.admin") && e.getAction().equals(InventoryAction.CLONE_STACK)))
                            e.setCancelled(true);
                        if (e.getCurrentItem().getType().equals(Material.LIME_DYE) && (e.getCurrentItem().getItemMeta().getLore().get(1).equals("§fDie obigen Items kaufen"))) {
                            e.getWhoClicked().closeInventory();
                            Utils.buyItem((Player) e.getWhoClicked());
                        }
                        if ((e.getCurrentItem().getType().equals(Material.BARRIER) && (e.getSlot() == (e.getView().getTopInventory().getSize() - 5)))) {
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.2F, 2.0F);
                        }
                    }
                }
            }
            if (e.getView().getTitle().equals(Lang.AdminInventoryName)) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    if ((e.getRawSlot() >= 0 && e.getRawSlot() <= e.getView().getTopInventory().getSize() - 1)) {
                        e.setCancelled(true);
                        if (e.getCurrentItem().getType().equals(Material.GOLD_INGOT)) {
                            e.getWhoClicked().closeInventory();
                            ConversationFactory cf = new ConversationFactory(Main.plugin);
                            ConversationPrefix cp = prefix -> Lang.CHAT_PREFIX;
                            Conversation c = cf.withFirstPrompt(new PriceConversation()).withEscapeSequence("stop").withModality(false).withLocalEcho(false).withPrefix(cp).buildConversation((Player) e.getWhoClicked());
                            c.begin();
                        }
                        if (e.getCurrentItem().getType().equals(Material.CHEST)) {
                            e.getWhoClicked().closeInventory();
                            Utils.setNewShopItems((Player) e.getWhoClicked());
                        }
                        if (e.getCurrentItem().getType().equals(Material.CLOCK)) {
                            e.getWhoClicked().closeInventory();
                            ConversationFactory cf = new ConversationFactory(Main.plugin);
                            ConversationPrefix cp = prefix -> Lang.CHAT_PREFIX;
                            Conversation c = cf.withFirstPrompt(new CooldownConversation()).withEscapeSequence("stop").withModality(false).withLocalEcho(false).withPrefix(cp).buildConversation((Player) e.getWhoClicked());
                            c.begin();
                        }
                        if (e.getCurrentItem().getType().equals(Material.STRUCTURE_VOID)) {
                            e.getWhoClicked().closeInventory();
                            Utils.resetShopTransactions((Player) e.getWhoClicked());
                        }
                        if (e.getCurrentItem().getType().equals(Material.BARRIER)) {
                            e.getWhoClicked().closeInventory();
                            Utils.deleteShop(Main.AdminPanelUser.get(e.getWhoClicked().getName()));
                            Utils.finishAdminSession(e.getWhoClicked().getName());
                        }
                        if (e.getCurrentItem().getType().equals(Material.TRIPWIRE_HOOK)) {
                            e.getWhoClicked().closeInventory();
                            Utils.updatePosition(Main.AdminPanelUser.get(e.getWhoClicked().getName()), e.getWhoClicked().getLocation());
                            Utils.finishAdminSession(e.getWhoClicked().getName());
                        }
                        if (e.getCurrentItem().getType().equals(Material.ENCHANTED_BOOK)) {
                            e.getWhoClicked().closeInventory();
                            ConversationFactory cf = new ConversationFactory(Main.plugin);
                            ConversationPrefix cp = prefix -> Lang.CHAT_PREFIX;
                            Conversation c = cf.withFirstPrompt(new PermissionConversation()).withEscapeSequence("stop").withModality(false).withLocalEcho(false).withPrefix(cp).buildConversation((Player) e.getWhoClicked());
                            c.begin();
                        }
                        if (e.getCurrentItem().getType().equals(Material.OAK_SIGN)) {
                            e.getWhoClicked().closeInventory();
                            ConversationFactory cf = new ConversationFactory(Main.plugin);
                            ConversationPrefix cp = prefix -> Lang.CHAT_PREFIX;
                            Conversation c = cf.withFirstPrompt(new NameConversation()).withEscapeSequence("stop").withModality(false).withLocalEcho(false).withPrefix(cp).buildConversation((Player) e.getWhoClicked());
                            c.begin();
                        }
                        if (e.getCurrentItem().getType().equals(Material.COMMAND_BLOCK)) {
                            e.getWhoClicked().closeInventory();
                            if (e.getClick().equals(ClickType.DROP)) {
                                Utils.resetShopCommands((Player) e.getWhoClicked());
                            } else if (e.getClick().equals(ClickType.RIGHT)) {
                                Shop shop = Main.LoadedShops.get(Main.AdminPanelUser.get(((Player) e.getWhoClicked()).getName()));
                                String NoPrefixShopName = shop.getShopName();
                                for (String s : Main.LoadedPrefixes.keySet()) {
                                    NoPrefixShopName = NoPrefixShopName.replaceAll(s, "");
                                }
                                for (String cmd : shop.getExecutedCommands()) {
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", ((Player) e.getWhoClicked()).getName()).replaceAll("%shopname%", NoPrefixShopName));
                                }
                                Utils.finishAdminSession(((Player) e.getWhoClicked()).getName());
                            } else {
                                ConversationFactory cf = new ConversationFactory(Main.plugin);
                                ConversationPrefix cp = prefix -> Lang.CHAT_PREFIX;
                                Conversation c = cf.withFirstPrompt(new CommandConversation()).withEscapeSequence("stop").withModality(false).withLocalEcho(false).withPrefix(cp).buildConversation((Player) e.getWhoClicked());
                                c.begin();
                            }
                        }
                        if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                            e.getWhoClicked().closeInventory();
                            if (e.getWhoClicked().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                                e.getWhoClicked().sendMessage(Lang.ErrorNoItemInHand);
                            } else {
                                Utils.updateHead(Main.AdminPanelUser.get(e.getWhoClicked().getName()), e.getWhoClicked().getInventory().getItemInMainHand());
                                e.getWhoClicked().sendMessage(Lang.HeadUpdated);
                            }
                            Utils.finishAdminSession(e.getWhoClicked().getName());
                        }
                        if (e.getCurrentItem().getType().equals(Material.LIME_DYE) || e.getCurrentItem().getType().equals(Material.RED_DYE)) {
                            e.getWhoClicked().closeInventory();
                            Utils.toggleEnabled((Player) e.getWhoClicked());
                        }
                        if (e.getCurrentItem().getType().equals(Material.BOOK)) {
                            e.getWhoClicked().closeInventory();
                            if (e.getClick().equals(ClickType.DROP)) {
                                Utils.resetShopQuestion((Player) e.getWhoClicked());
                            } else if (e.getClick().equals(ClickType.RIGHT)) {
                                ConversationFactory cf = new ConversationFactory(Main.plugin);
                                ConversationPrefix cp = prefix -> Lang.CHAT_PREFIX;
                                Conversation c = cf.withFirstPrompt(new QuestionAnswerConversation()).withEscapeSequence("stop").withModality(false).withLocalEcho(false).withPrefix(cp).buildConversation((Player) e.getWhoClicked());
                                c.begin();
                            } else {
                                ConversationFactory cf = new ConversationFactory(Main.plugin);
                                ConversationPrefix cp = prefix -> Lang.CHAT_PREFIX;
                                Conversation c = cf.withFirstPrompt(new QuestionConversation()).withEscapeSequence("stop").withModality(false).withLocalEcho(false).withPrefix(cp).buildConversation((Player) e.getWhoClicked());
                                c.begin();
                            }
                        }
                    }
                }
            }
        }
    }
}
