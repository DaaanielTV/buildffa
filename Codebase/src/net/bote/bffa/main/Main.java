package net.bote.bffa.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.bote.bffa.commands.CMD_bffa;
import net.bote.bffa.commands.CMD_build;
import net.bote.bffa.commands.CMD_setheight;
import net.bote.bffa.commands.CMD_setmap;
import net.bote.bffa.commands.CMD_setspawn;
import net.bote.bffa.commands.CMD_setvillager;
import net.bote.bffa.commands.CMD_stats;
import net.bote.bffa.commands.CMD_teaming;
import net.bote.bffa.events.BasicEvents;
import net.bote.bffa.events.ChatListener;
import net.bote.bffa.events.DamageListener;
import net.bote.bffa.events.GameListener;
import net.bote.bffa.events.InvSortListener;
import net.bote.bffa.events.JoinListener;
import net.bote.bffa.events.PlaceListener;
import net.bote.bffa.events.QuitListener;
import net.bote.bffa.stats.MySQL;
import net.bote.bffa.utils.Config;
import net.bote.bffa.utils.InventorySort;
import net.bote.bffa.utils.LocationManager;
import net.bote.bffa.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	
	public static MySQL mysql;
	
	public static String noperm = Main.prefix+"§cKein Recht!";
	
	public static HashMap<Player, Player> lastDamage = new HashMap<Player, Player>();
	
	public static ArrayList<Location> blocks = new ArrayList<Location>();
	
	public static String error = Main.prefix+"§4Die Daten konnten nicht gespeichert werden!";
	
	public static File f = new File("plugins/BFFA/config", "configuration.yml");
	public static YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);
	
	public static String prefix;
	
	private static Main instance;
	
	// TODO: Displayname looking for debugging, configure stats, inventory sorter
	
	@Override
	public void onEnable() {
		
		Config.setupConfig();
		
		Utils.villager = false;
		
		prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("Config.Prefix"));
		
		if(cfg.getBoolean("Config.MySQL") == true) {
			connctMySQL();
		}
		
		instance = this;
		
		registerEvents();
		
		startInvManager();
		
		this.getCommand("setname").setExecutor(new CMD_setmap(this));
		this.getCommand("setspawn").setExecutor(new CMD_setspawn(this));
		this.getCommand("build").setExecutor(new CMD_build(this));
		this.getCommand("setheight").setExecutor(new CMD_setheight(this));
		this.getCommand("stats").setExecutor(new CMD_stats(this));
		this.getCommand("setvillager").setExecutor(new CMD_setvillager(this));
		this.getCommand("teaming").setExecutor(new CMD_teaming(this));
		this.getCommand("bffa").setExecutor(new CMD_bffa(this));
		
		startTime();
		
		Utils u = new Utils();
		u.startAnimation();
		
		System.out.println("[BFFA] Das Plugin wurde erfolgreich gestartet!");
	}
	
	@Override
	public void onDisable() {
		
		for(String s : InventorySort.banks.keySet()) {
			InventorySort.saveBank(s, InventorySort.banks.get(s));
		}
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.kickPlayer(prefix + "§cDieser Server wird kurz neugestartet.");
		}
		
	}
	
	private void registerEvents() {
		
		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlaceListener(), this);
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new BasicEvents(), this);
		Bukkit.getPluginManager().registerEvents(new GameListener(), this);
		Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
		Bukkit.getPluginManager().registerEvents(new InvSortListener(), this);
		System.out.println("[BFFA] Events wurden registriert!");
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public void connctMySQL() {
	    String ip = cfg.getString("MySQL.ip");
	    String database = cfg.getString("MySQL.database");
	    String name = cfg.getString("MySQL.name");
	    String passwort = cfg.getString("MySQL.password");
	    try {
	      mysql = new MySQL(ip, database, name, passwort);
	      mysql.update("CREATE TABLE IF NOT EXISTS BFFAStats(UUID varchar(64), KILLS int, DEATHS int);");
	      Bukkit.getConsoleSender().sendMessage("Stats wurden geladen!");
	      
	    } catch(Exception ex) {
	      Bukkit.getConsoleSender().sendMessage("Es konnte keine Verbindung zur MySQL hergestellt werden!");
	      
	    }
	    
	  }
	
	public static boolean isMySQLEnabled() {
		return Main.cfg.getBoolean("Config.MySQL");
	}
	
	public void startTime() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {

				try {
					
					World w = Bukkit.getWorld(LocationManager.cfg.getString("Locations.Spawn.worldname"));
					w.setTime(1000);
					
				} catch (NullPointerException | IllegalArgumentException ignored) {
					
				}
				
			}
		}, 20L, 20*60);
	}
	
	public void startInvManager() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				
				List<String> onlineUUID = new ArrayList<String>();
				
				for(String s : InventorySort.banks.keySet()) {
					InventorySort.saveBank(s, InventorySort.banks.get(s));
				}
				
				
				InventorySort.banks.clear();
				
				for(Player all : Bukkit.getOnlinePlayers()) {
					InventorySort.getBank(all.getUniqueId().toString());
				}
				
			}
		}, 20 * 60 * 15, 20 * 60 * 15);
	}

}
