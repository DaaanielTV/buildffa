package net.bote.bffa.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.bote.bffa.main.Main;

public class InventoryManager {
	
	private static ArrayList<ItemStack> list = new ArrayList<ItemStack>();
	
	public static void saveInventory(UUID uuid, Player p) {
		String id = uuid.toString();
		
		File file = new File("plugins//BFFA//Inventories//" + id + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException error) {
				p.sendMessage(Main.prefix + "§cEs ist ein Fehler beim erstellen deiner Inventar-Datei aufgetreten! Versuch es erneut.");
				return;
			}
		} else {
			cfg.set("Inventory", null);
			try {
				cfg.save(file);
			} catch (IOException e) {
				p.sendMessage(Main.prefix + "§cEs ist ein Fehler beim editieren deiner Inventar-Datei aufgetreten! Versuch es erneut.");
				return;
			}
		}
			ItemStack[] contents = p.getInventory().getContents();
			for(int i = 0; i < contents.length; i++) {
				ItemStack item = contents[i];
				if(item != null) {
					list.add(item);
				}
			}
			cfg.set("Inventory", list);
			try {
				cfg.save(file);
			} catch (IOException e) {
				if(p != null) {
					p.sendMessage(Main.prefix + "§cEs ist ein Fehler beim editieren deiner Inventar-Datei aufgetreten! Versuch es erneut.");
				}
			}
	}
	
	public static ItemStack[] loadInventory(UUID uuid, Player p) {
		String id = uuid.toString();
		File file = new File("plugins//BFFA//Inventories//" + id + ".yml");
		
		if(file.exists()) {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			ItemStack[] contents = p.getInventory().getContents();
			List<?> list = cfg.getList("Inventory");
			for(int i = 0; i < list.size(); i++) {
				contents[i] = (ItemStack) list.get(i);
			}
			return contents;
		}
		return null;
	}
	
	
	public static void checkOrdner() {
		File file = new File("plugins//BFFA//Inventories//");
		if(!file.exists()) {
			file.mkdir();
		}
	}
	
	public static boolean checkOrdner(UUID uuid) {
		File file;
		try {
			file = new File("plugins//BFFA//Inventories//" + uuid.toString() + ".yml");
		} catch (NullPointerException | IllegalArgumentException e) {
			return false;
		}
		return file.exists();
	}
	
//	public static ItemStack[] getUniqueInventoryContent(UUID uuid) {
//		ArrayList<ItemStack> contents = new ArrayList<ItemStack>();
//		
//		File file = new File("plugins//BFFA//Inventories//" + uuid.toString() + ".yml");
//		
//		YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(file);
//		
//		
//		
//	}
	
	public static void save(UUID uuid, ItemStack[] itemstack) {
		File file = new File("plugins//BFFA//Inventories//" + uuid.toString() + ".yml");
		YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(file);
		
		cfg.set("Inventory", itemstack);
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    @SuppressWarnings("unchecked")
	public static void restore(Player p) {
        YamlConfiguration c = YamlConfiguration.loadConfiguration(new File("plugins//BFFA//Inventories//" + p.getUniqueId().toString() + ".yml"));
        ItemStack[] content = ((List<ItemStack>) c.get("Inventory")).toArray(new ItemStack[0]);
        p.getInventory().setContents(content);
    }

}
