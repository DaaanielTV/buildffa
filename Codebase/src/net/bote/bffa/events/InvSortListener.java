package net.bote.bffa.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.bote.bffa.main.Main;
import net.bote.bffa.utils.InventoryManager;
import net.bote.bffa.utils.InventorySort;
import net.md_5.bungee.api.ChatColor;

public class InvSortListener implements Listener {
    
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if(!(e.getRightClicked() instanceof Villager)) {
            return;
        }

        Player p = e.getPlayer();
        Villager v = (Villager) e.getRightClicked();
        
        if(v.getCustomName() == null || !v.getCustomName().equals("§a§l➜Inventarsortierung")) {
            return;
        }
        
        e.setCancelled(true);
        Inventory inv = Bukkit.createInventory(null, 4*9, "§eInventar");
        p.getInventory().clear();

        if(InventoryManager.checkOrdner(p.getUniqueId())) {
            Inventory openinv = InventorySort.getBank(p.getUniqueId().toString());
            
            for(int i = 0; i < openinv.getSize(); i++) {
                ItemStack itemstack = openinv.getItem(i);
                if(itemstack != null && itemstack.hasItemMeta()) {
                    ItemMeta meta = itemstack.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName()));
                    itemstack.setItemMeta(meta);
                }
            }
            
            p.openInventory(openinv);
        } else {
            inv.setItem(0, createEnchantedItem(Material.GOLDEN_SWORD, 1, "§6« Schwert »", Enchantment.DAMAGE_ALL, 2));
            inv.setItem(1, createEnchantedItem(Material.BOW, 1, "§3« Bogen »", Enchantment.ARROW_INFINITE, 1));
            inv.setItem(2, createEnchantedItem(Material.STICK, 1, "§c« KnockBack Stick »", Enchantment.KNOCKBACK, 2));
            inv.setItem(3, createItem(Material.SANDSTONE, 64, "§e« Blöcke »"));
            inv.setItem(4, createItem(Material.SANDSTONE, 64, "§e« Blöcke »"));
            inv.setItem(5, createItem(Material.SANDSTONE, 64, "§e« Blöcke »"));
            inv.setItem(6, createItem(Material.ENDER_PEARL, 1, "§5« Enderperle »"));
            InventorySort.saveBank(p.getUniqueId().toString(), inv);
            
            for(int i = 0; i < inv.getSize(); i++) {
                ItemStack itemstack = inv.getItem(i);
                if(itemstack != null && itemstack.hasItemMeta()) {
                    ItemMeta meta = itemstack.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName()));
                    itemstack.setItemMeta(meta);
                }
            }
            
            p.openInventory(inv);
        }
    }
    
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(!e.getView().getTitle().equals("§eInventar")) {
            return;
        }

        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        
        // Check if player has items in their inventory
        if(p.getInventory().getContents().length > 0) {
            p.sendMessage(Main.prefix + "§cBitte lege keine Items in dein Inventar.");
            p.sendMessage(Main.prefix + "§4§lDein Inventar wurde nicht gespeichert.");
            
            setDefaultInventory(p);
            InventorySort.saveBank(p.getUniqueId().toString(), inv);
            return;
        }
        
        InventorySort.saveBank(p.getUniqueId().toString(), inv);
        p.getInventory().clear();
        
        // Apply inventory changes after a tick to ensure proper synchronization
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            p.getInventory().setContents(InventorySort.getBank(p.getUniqueId().toString()).getContents());
            
            // Update item display names
            for(int i = 0; i < p.getInventory().getSize(); i++) {
                ItemStack itemstack = p.getInventory().getItem(i);
                if(itemstack != null && itemstack.hasItemMeta()) {
                    ItemMeta meta = itemstack.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName()));
                    itemstack.setItemMeta(meta);
                }
            }
            
            p.updateInventory();
            p.getInventory().setItem(22, createItem(Material.ARROW, 1, "§c« Pfeil »"));
        }, 1);
        
        p.sendMessage(Main.prefix + "§7Du hast dein Inventar §egespeichert");
    }
    
    private void setDefaultInventory(Player p) {
        p.getInventory().setItem(0, createEnchantedItem(Material.GOLDEN_SWORD, 1, "§b·٠§9● §6Schwert", Enchantment.DAMAGE_ALL, 2));
        p.getInventory().setItem(1, createEnchantedItem(Material.BOW, 1, "§b·٠§9● §2Bogen", Enchantment.ARROW_INFINITE, 1));
        p.getInventory().setItem(2, createEnchantedItem(Material.STICK, 1, "§b·٠§9● §cKnockBack Stick", Enchantment.KNOCKBACK, 2));
        p.getInventory().setItem(3, createItem(Material.SANDSTONE, 64, "§b·٠§9● §eBlöcke"));
        p.getInventory().setItem(4, createItem(Material.SANDSTONE, 64, "§b·٠§9● §eBlöcke"));
        p.getInventory().setItem(5, createItem(Material.SANDSTONE, 64, "§b·٠§9● §eBlöcke"));
        p.getInventory().setItem(6, createItem(Material.ENDER_PEARL, 1, "§b·٠§9● §5Enderperle"));
        p.getInventory().setItem(22, createItem(Material.ARROW, 1, "§b·٠§9● Pfeil"));
    }
    
    private static ItemStack createItem(Material mat, int amount, String name) {
        ItemStack item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
    
    private static ItemStack createEnchantedItem(Material mat, int amount, String name, Enchantment ench, int level) {
        ItemStack item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addEnchant(ench, level, true);
        item.setItemMeta(meta);
        return item;
    }
}
