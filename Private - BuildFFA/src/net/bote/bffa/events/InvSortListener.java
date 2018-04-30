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
		Player p = e.getPlayer();
		
		if(e.getRightClicked().getType() == EntityType.VILLAGER) {
			Villager v = (Villager) e.getRightClicked();
			if(v.getCustomName().equalsIgnoreCase("§a§l➜Inventarsortierung")) {
				e.setCancelled(true);
				Inventory inv = Bukkit.createInventory(null, 4*9, "§eInventar");
				p.getInventory().clear();
				if(InventoryManager.checkOrdner(p.getUniqueId())) {
					
					Inventory openinv = InventorySort.getBank(p.getUniqueId().toString());
					
					for(int i = 0; i < openinv.getSize(); i++) {
						if(openinv.getItem(i) != null) {
							ItemStack itemstack = openinv.getItem(i);
							ItemMeta meta = itemstack.getItemMeta();
							meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemstack.getItemMeta().getDisplayName()));
							itemstack.setItemMeta(meta);
						}
					}
					
					p.openInventory(openinv);
					
				} else {
					
					inv.setItem(0, createEnchantedItem(Material.GOLD_SWORD, 1, "§6« Schwert »", Enchantment.DAMAGE_ALL, 2));
					inv.setItem(1, createEnchantedItem(Material.BOW, 1, "§3« Bogen »", Enchantment.ARROW_INFINITE, 1));
					inv.setItem(2, createEnchantedItem(Material.STICK, 1, "§c« KnockBack Stick »", Enchantment.KNOCKBACK, 2));
					inv.setItem(3, createItem(Material.SANDSTONE, 64, "§e« Blöcke »"));
					inv.setItem(4, createItem(Material.SANDSTONE, 64, "§e« Blöcke »"));
					inv.setItem(5, createItem(Material.SANDSTONE, 64, "§e« Blöcke »"));
					inv.setItem(6, createItem(Material.ENDER_PEARL, 1, "§5« Enderperle »"));
					InventorySort.saveBank(p.getUniqueId().toString(), inv);
					
					for(int i = 0; i < inv.getSize(); i++) {
						if(inv.getItem(i) != null) {
							ItemStack itemstack = inv.getItem(i);
							ItemMeta meta = itemstack.getItemMeta();
							meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemstack.getItemMeta().getDisplayName()));
							itemstack.setItemMeta(meta);
						}
					}
					
					p.openInventory(inv);
				}
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(e.getInventory().getName().equalsIgnoreCase("§eInventar")) {
			
			Inventory inv = e.getInventory();
			boolean bol = false;
			
			for(int i = 0; i < p.getInventory().getSize(); i++) {
				if(p.getInventory().getItem(i) != null) {
					p.sendMessage(Main.prefix + "§cBitte lege keine Items in dein Inventar.");
					p.sendMessage(Main.prefix + "§4§lDein Inventar wurde nicht gespeichert.");
					
					p.getInventory().setItem(0, createEnchantedItem(Material.GOLD_SWORD, 1, "§b·٠§9● §6Schwert", Enchantment.DAMAGE_ALL, 2));
					p.getInventory().setItem(1, createEnchantedItem(Material.BOW, 1, "§b·٠§9● §2Bogen", Enchantment.ARROW_INFINITE, 1));
					p.getInventory().setItem(2, createEnchantedItem(Material.STICK, 1, "§b·٠§9● §cKnockBack Stick", Enchantment.KNOCKBACK, 2));
					p.getInventory().setItem(3, createItem(Material.SANDSTONE, 64, "§b·٠§9● §eBlöcke"));
					p.getInventory().setItem(4, createItem(Material.SANDSTONE, 64, "§b·٠§9● §eBlöcke"));
					p.getInventory().setItem(5, createItem(Material.SANDSTONE, 64, "§b·٠§9● §eBlöcke"));
					p.getInventory().setItem(6, createItem(Material.ENDER_PEARL, 1, "§b·٠§9● §5Enderperle"));
					
					p.getInventory().setItem(22, createItem(Material.ARROW, 1, "§b·٠§9● Pfeil"));
					InventorySort.saveBank(p.getUniqueId().toString(), inv);
					bol = true;
					return;
				}
			}
			
			if(bol == true ) {
				return;
			}
			
			InventorySort.saveBank(p.getUniqueId().toString(), inv);
			
			p.getInventory().clear();
			
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					p.getInventory().setContents(InventorySort.getBank(p.getUniqueId().toString()).getContents());
					
					for(int i = 0; i < p.getInventory().getSize(); i++) {
						if(p.getInventory().getItem(i) != null) {
							ItemStack itemstack = p.getInventory().getItem(i);
							ItemMeta meta = itemstack.getItemMeta();
							meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemstack.getItemMeta().getDisplayName()));
							itemstack.setItemMeta(meta);
						}
					}
					
					p.updateInventory();
					
					p.getInventory().setItem(22, createItem(Material.ARROW, 1, "§c« Pfeil »"));
					
				}
			}, 1);
			
			p.sendMessage(Main.prefix + "§7Du hast dein Inventar §egespeichert");
			
		}
	}
	
	private static ItemStack createItem(Material mat, int anzahl, String name) {
		ItemStack i = new ItemStack(mat, anzahl);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(name);
		i.setItemMeta(m);
		return i;
	}
	
	private static ItemStack createEnchantedItem(Material mat, int anzahl, String name, Enchantment en, int power) {
		ItemStack i = new ItemStack(mat, anzahl);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(name);
		m.addEnchant(en, power, true);
		i.setItemMeta(m);
		return i;
	}
//	
//	public static void saveInventory(Inventory inv, File file, Player p, String name, boolean override) {
//		if (inv == null || file == null)
//			return;
//		if (file.exists() && override)
//			file.delete();
//		FileConfiguration inventory= YamlConfiguration.loadConfiguration(file);
//		inventory.set("Kits." + p.getUniqueId().toString() + "." + name, null);
//		ItemStack[] contents = inv.getContents();
//		for (int i = 0; i < contents.length; i++) {
//			ItemStack item = contents[i];
//			if (item != null)
//				if (item.getType() != Material.AIR)
//					inventory.set("Kits." + p.getUniqueId().toString() + "." + name + ".Slot." + i, item);
//		}
//		try {
//			inventory.save(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static List<ItemStack> getContents(File file, String name, Player p) {
//		ItemStack[] items = new ItemStack[] {};
//		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
//		if (conf.isSet("Kits." + p.getUniqueId().toString() + "." + name + ".Slot")) {
//			int size = conf.getInt("Kits." + p.getUniqueId().toString() + "." + name + ".Slot", 27);
//			items = new ItemStack[size];
//			for (int i = 0; i < size; i++) {
//				if (conf.contains("Kits." + p.getUniqueId().toString() + "." + name + ".Slot." + i))
//					items[i] = conf.getItemStack("Kits." + p.getUniqueId().toString() + "." + name + ".Slot." + i);
//				else
//					items[i] = new ItemStack(Material.AIR);
//			}
//		}
//		List<ItemStack> kit = new ArrayList<ItemStack>();
//		for (ItemStack i : items) {
//			if (i != null) {
//				kit.add(i);
//			}
//		}
//		kit.removeAll(Collections.singleton(null));
//		return kit;
//	}

}
