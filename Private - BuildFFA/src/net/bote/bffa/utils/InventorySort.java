package net.bote.bffa.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventorySort {
	
	public static Map<String, Inventory> banks = new HashMap<String, Inventory>();
	
	
	public static void saveBank(String uuid, Inventory inv) {
		if(!(hasBank(uuid))) {
			try {
				new File("plugins/BFFA/Inventories/" + uuid + ".yml").createNewFile();
			} catch (IOException e) {
				// TODO: handle exception
			}
		}
		
		ItemStack[] contents = inv.getContents();
		List<ItemStack> isl = new ArrayList<ItemStack>();
		
		for(ItemStack iso : contents) {
			
			if(iso != null) {
				ItemStack is = new ItemStack(iso);
				
				ItemMeta im = is.getItemMeta();
				if(im.getDisplayName() != null) {
					im.setDisplayName(im.getDisplayName().replace("§", "&"));
				}
				
				List<String> lore = new ArrayList<String>();
				if(im.getLore() != null) {
					for(String s : im.getLore()) {
						lore.add((s.replace("§", "&")));
					}
					im.setLore(lore);
				}
				
				is.setItemMeta(im);
				
				isl.add(is);
			}
		}
		
		YamlConfiguration cfg = new YamlConfiguration();
		cfg.set("content", isl);
		
		try {
			cfg.save(new File("plugins/BFFA/Inventories/" + uuid + ".yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Inventory loadBank(String uuid) {
		File f = new File("plugins/BFFA/Inventories/" + uuid + ".yml");
		
		if(!(hasBank(uuid))) {
			InventorySort.saveBank(uuid, Bukkit.createInventory(null, 27, "§eInventar"));
		}
		
		Inventory inv = Bukkit.createInventory(null, 27, "§eInventar");
		
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		
		@SuppressWarnings("unchecked")
		ItemStack[] content = ((List<ItemStack>)cfg.get("content")).toArray(new ItemStack[0]);
		
		for(ItemStack iso : content) {
			
			if(iso != null) {
				ItemStack is = new ItemStack(iso);
				
				ItemMeta im = is.getItemMeta();
				if(im.getDisplayName() != null) {
					im.setDisplayName(ChatColor.translateAlternateColorCodes('&', im.getDisplayName()));
				}
				
				List<String> lore = new ArrayList<String>();
				if(im.getLore() != null) {
					for(String s : im.getLore()) {
						lore.add(ChatColor.translateAlternateColorCodes('&', s));
					}
					im.setLore(lore);
				}
				
				is.setItemMeta(im);
			}
		}
		
		inv.setContents(content);
		
		for(int i = 0; i < inv.getSize(); i++) {
			
			if(inv.getItem(i) != null) {
				if(inv.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("&6« Schwert »")) {
					inv.getItem(i).getItemMeta().setDisplayName("§6« Schwert »");
				}
				if(inv.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("&3« Bogen »")) {
					inv.getItem(i).getItemMeta().setDisplayName("§3« Bogen »");
				}
				if(inv.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("&c« KnockBack Stick »")) {
					inv.getItem(i).getItemMeta().setDisplayName("§c« KnockBack Stick »");
				}
				if(inv.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("&e« Blöcke »")) {
					inv.getItem(i).getItemMeta().setDisplayName("§e« Blöcke »");
				}
				if(inv.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("&5« Enderperle »")) {
					inv.getItem(i).getItemMeta().setDisplayName("§5« Enderperle »");
				}
			}
			
		}
		
		return inv;
	}
	
	public static Inventory getBank(String uuid) {
		if(!(banks.containsKey(uuid))) {
			banks.put(uuid, loadBank(uuid));
		}
		
		return banks.get(uuid);
	}


	public static boolean hasBank(String uuid) {
		File f = new File("plugins/BFFA/Inventories/" + uuid + ".yml");
		
		return f.exists();
	}

}
