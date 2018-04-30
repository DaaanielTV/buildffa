package net.bote.bffa.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.bote.bffa.main.Main;
import net.bote.bffa.stats.ConfigStats;
import net.bote.bffa.stats.Stats;

public class Utils {
	
	public static boolean villager = false;
	
	public static ArrayList<Player> build = new ArrayList<Player>();
	
	private static int count = 0;
	
	private static String[] animation = new String[] {
			"§e·٠§6● §l§eBuildFFA", "§e·٠§6● §l§e§6B§l§euildFFA", "§e·٠§6● §l§eB§6u§l§eildFFA", "§e·٠§6● §l§eBu§l§6i§eldFFA", "§e·٠§6● §l§eBui§6l§l§edFFA", "§e·٠§6● §l§eBuil§6d§l§eFFA", "§e·٠§6● §l§eBuild§6§lFFA", "§e·٠§6● §l§eBuild§6§lFFA", "§e·٠§6● §l§eBuildFFA"
	};
	
	public static void setJoinInv(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		
		p.setLevel(0);
		p.setHealth(20);
		p.setFoodLevel(20);
		
		p.getInventory().setItem(0, createEnchantedItem(Material.GOLD_SWORD, 1, "§b·٠§9● §6Schwert", Enchantment.DAMAGE_ALL, 2));
		p.getInventory().setItem(1, createEnchantedItem(Material.BOW, 1, "§b·٠§9● §2Bogen", Enchantment.ARROW_INFINITE, 1));
		p.getInventory().setItem(2, createEnchantedItem(Material.STICK, 1, "§b·٠§9● §cKnockBack Stick", Enchantment.KNOCKBACK, 2));
		p.getInventory().setItem(3, createItem(Material.SANDSTONE, 64, "§b·٠§9● §eBlöcke"));
		p.getInventory().setItem(4, createItem(Material.SANDSTONE, 64, "§b·٠§9● §eBlöcke"));
		p.getInventory().setItem(5, createItem(Material.SANDSTONE, 64, "§b·٠§9● §eBlöcke"));
		p.getInventory().setItem(6, createItem(Material.ENDER_PEARL, 1, "§b·٠§9● §5Enderperle"));
		InventorySort.saveBank(p.getUniqueId().toString(), p.getInventory());
		
		if(InventorySort.getBank(p.getUniqueId().toString()) != null) {
			p.getInventory().clear();
			p.getInventory().setContents(InventorySort.getBank(p.getUniqueId().toString()).getContents());
			
			for(int i = 0; i < p.getInventory().getSize(); i++) {
				if(p.getInventory().getItem(i) != null) {
					ItemStack itemstack = p.getInventory().getItem(i);
					ItemMeta meta = itemstack.getItemMeta();
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemstack.getItemMeta().getDisplayName()));
					itemstack.setItemMeta(meta);
				}
			}
			
		}
		
		p.getInventory().setHelmet(createItem(Material.LEATHER_HELMET, 1, "§b·٠§9● Helm"));
		p.getInventory().setChestplate(createItem(Material.LEATHER_CHESTPLATE, 1, "§b·٠§9● Brustplatte"));
		p.getInventory().setLeggings(createItem(Material.LEATHER_LEGGINGS, 1, "§b·٠§9● Hose"));
		p.getInventory().setBoots(createItem(Material.LEATHER_BOOTS, 1, "§b·٠§9● Schuhe"));
			
		p.getInventory().setItem(22, createItem(Material.ARROW, 1, "§b·٠§9● Pfeil"));
		
//		if(InventoryManager.checkOrdner(p.getUniqueId()) == false) {
//			p.getInventory().setItem(0, createEnchantedItem(Material.GOLD_SWORD, 1, "§6« Schwert »", Enchantment.DAMAGE_ALL, 2));
//			p.getInventory().setItem(1, createEnchantedItem(Material.BOW, 1, "§3« Bogen »", Enchantment.ARROW_INFINITE, 1));
//			p.getInventory().setItem(2, createEnchantedItem(Material.STICK, 1, "§c« KnockBack Stick »", Enchantment.KNOCKBACK, 2));
//			p.getInventory().setItem(3, createItem(Material.SANDSTONE, 64, "§e« Blöcke »"));
//			p.getInventory().setItem(4, createItem(Material.SANDSTONE, 64, "§e« Blöcke »"));
//			p.getInventory().setItem(5, createItem(Material.SANDSTONE, 64, "§e« Blöcke »"));
//			p.getInventory().setItem(6, createItem(Material.ENDER_PEARL, 1, "§d« Enderperle »"));
//			// InventoryManager.save(p.getUniqueId(), p.getInventory().getContents());
//			InventoryManager.saveInventory(p.getUniqueId(), p);
//		} else {
//			// InventoryManager.restore(p);
//			InventoryManager.loadInventory(p.getUniqueId(), p);
//		}
		
		
	}
	
	public void startAnimation() {
		Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				Bukkit.getOnlinePlayers().forEach(current -> {
					
					if(current.getScoreboard() == null) {
						updateScoreboard(current);
					}
					current.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(animation[count]);
					
				});
				
				count++;
				
				if(count == animation.length) {
					count = 0;
				}
				
			}
		}, 0, 20);
	}
	
	public static void updateScoreboard(Player p) {
		
		ScoreboardManager sm = Bukkit.getScoreboardManager();
		final Scoreboard board = (Scoreboard) sm.getNewScoreboard();
		final Objective o = ((org.bukkit.scoreboard.Scoreboard) board).registerNewObjective("aaa", "dummy");
		
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.setDisplayName(animation[count]);
		
		o.getScore("§f§lMap:").setScore(10);
		if(Main.cfg.getString("Config.Mapname") != null) {
			o.getScore("§c➜ §c" + Main.cfg.getString("Config.Mapname")).setScore(9);
		} else {
			o.getScore("§c➜ §7Unbekannt").setScore(9);
		}
		o.getScore("§7 ").setScore(8);
		o.getScore("§f§lKills:").setScore(7);
		if(Main.isMySQLEnabled()) {
			o.getScore("§c➜ §c" + Stats.getKills(p.getUniqueId().toString())).setScore(6);
		} else {
			o.getScore("§c➜ §c" + ConfigStats.getKills(p.getUniqueId().toString())).setScore(6);
		}
		o.getScore("§c ").setScore(5);
		o.getScore("§f§lSpieler:").setScore(4);
		o.getScore("§c➜ §c" + Bukkit.getOnlinePlayers().size()).setScore(3);
		o.getScore("§6 ").setScore(2);
		o.getScore("§f§lSpielvariante:").setScore(1);
		if(Main.cfg.getBoolean("Config.Teaming") == true) {
			o.getScore("§c➜ §aTeaming erlaubt").setScore(0);
		} else {
			o.getScore("§c➜ §4Teaming verboten").setScore(0);
		}
		
		p.setScoreboard(board);
	}
	
	public static void updateScoreboard(Player p, int players) {
		
	ScoreboardManager sm = Bukkit.getScoreboardManager();
	final Scoreboard board = (Scoreboard) sm.getNewScoreboard();
	final Objective o = ((org.bukkit.scoreboard.Scoreboard) board).registerNewObjective("aaa", "dummy");
	
	o.setDisplaySlot(DisplaySlot.SIDEBAR);
	o.setDisplayName(animation[count]);
	
	o.getScore("§f§lMap:").setScore(10);
	if(Main.cfg.getString("Config.Mapname") != null) {
		o.getScore("§c➜ §c" + Main.cfg.getString("Config.Mapname")).setScore(9);
	} else {
		o.getScore("§c➜ §7Unbekannt").setScore(9);
	}
	o.getScore("§7 ").setScore(8);
	o.getScore("§f§lKills:").setScore(7);
	if(Main.isMySQLEnabled()) {
		o.getScore("§c➜ §c" + Stats.getKills(p.getUniqueId().toString())).setScore(6);
	} else {
		o.getScore("§c➜ §c" + ConfigStats.getKills(p.getUniqueId().toString())).setScore(6);
	}
	o.getScore("§c ").setScore(5);
	o.getScore("§f§lSpieler:").setScore(4);
	o.getScore("§c➜ §c" + players).setScore(3);
	o.getScore("§6 ").setScore(2);
	o.getScore("§f§lSpielvariante:").setScore(1);
	if(Main.cfg.getBoolean("Config.Teaming") == true) {
		o.getScore("§c➜ §aTeaming erlaubt").setScore(0);
	} else {
		o.getScore("§c➜ §4Teaming verboten").setScore(0);
	}
	
	p.setScoreboard(board);
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
	
	public static boolean hasPlayerPrefix(Player p) {
		return p.getDisplayName().startsWith("§");
	}
//	
//	private static void saveInvConfig() {
//		try {
//			Main.invs.save(Main.file);
//		} catch (IOException e) {
//			System.err.println(Main.error);
//			e.printStackTrace();
//		}
//	}
//	
//	public static void saveInventory(Inventory inv, String uuid) {
//		HashMap<Integer, ItemStack> contents = new HashMap<Integer, ItemStack>();
//		for(int i = 0; i < Integer.valueOf(inv.getContents().length).intValue(); i++) {
//			contents.put(i, inv.getItem(i));
//		}
//		Main.invs.set(uuid, contents);
//		saveInvConfig();
//	}
//	
//	public static Inventory getPlayerInventory(String uuid) {
//		Inventory inv = Bukkit.createInventory(null, 9*2);
//		HashMap<?, ?> contents = (HashMap<?, ?>) Main.invs.getMapList(uuid);
//		
//		for(int i = 0; i < contents.size(); i++) {
//			inv.setItem(i, (ItemStack) contents.get(i));
//		}
//		return inv;
//		
//	}
	
	public static void saveKit(Inventory inv, File file){
        if(inv == null || file == null) return;
        if(file.exists()) file.delete();
        
        FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
 
        ItemStack[] contents = inv.getContents();
 
        for(int i = 0; i < contents.length; i++){
            ItemStack item = contents[i];
            if(item != null) if(item.getType() != Material.AIR) conf.set("Slot." + i, item);
        }
 
        try {
            conf.save(file);
        }
        catch(IOException e){
            return;
        }
    }
	
	@SuppressWarnings("static-access")
	public static List<ItemStack> getKit(String uuid){
        if(uuid == null) return null;
        ItemStack[] items = null;
        
        YamlConfiguration conf = new YamlConfiguration().loadConfiguration(new File("plugins//BFFA//Inventories//" + uuid + ".yml"));
 
        if(conf.contains("Slot") && conf.isConfigurationSection("Slot")){
            int size = conf.getInt("Slot", 27);
 
            items = new ItemStack[size];
 
            for(int i = 0; i < size; i++){
                if(conf.contains("Slot." + i)) items[i] = conf.getItemStack("Slot." + i);
                else items[i] = new ItemStack(Material.AIR);
            }
        }
        List<ItemStack> kit = new ArrayList<ItemStack>();
        for(ItemStack i : items) {
        	if(i != null) {
        		kit.add(i);
        	}
        }
        kit.removeAll(Collections.singleton(null));
        return kit;
    }
	

}
