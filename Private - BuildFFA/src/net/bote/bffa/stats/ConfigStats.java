package net.bote.bffa.stats;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import net.bote.bffa.main.Main;

public class ConfigStats {
	
	public static File f = new File("plugins/BFFA", "stats.yml");
	public static YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(f);
	
	public static boolean isRegistered(String uuid) {
		return cfg.contains(uuid);
	}
	
	public static void saveStats() {
		try {
			cfg.save(f);
		} catch (IOException e) {
			System.err.println(Main.error);
			e.printStackTrace();
		}
	}
	
	public static void register(String uuid, String name) {
		if(!isRegistered(uuid)) {
			cfg.set(uuid + ".Name", name);
			cfg.set(uuid + ".Kills", 0);
			cfg.set(uuid + ".Deaths", 0);
			saveStats();
		}
	}
	
	public static void setName(String uuid, String newname) {
		cfg.set(uuid + ".Name", newname);
	}
	
	public static void addKill(String uuid) {
		int kills = cfg.getInt(uuid + ".Kills");
		kills = kills + 1;
		cfg.set(uuid + ".Kills", kills);
		saveStats();
	}
	
	public static Integer getKills(String uuid) {
		return cfg.getInt(uuid + ".Kills");
	}
	
	public static Integer getDeaths(String uuid) {
		return cfg.getInt(uuid + ".Deaths");
	}
	
	public static String getName(String uuid) {
		return cfg.getString(uuid + ".Name");
	}
	
	public static void addDeath(String uuid) {
		int deaths = getDeaths(uuid);
		deaths = deaths + 1;
		cfg.set(uuid + ".Deaths", deaths);
		saveStats();
	}
	
	public static Double getKD(String uuid) {
		if(getDeaths(uuid) == 0) {
			return Double.valueOf(getDeaths(uuid).intValue());
		} else {
			int kills = getKills(uuid);
			int deahts = getDeaths(uuid);
			double kd; 
			kd = kills / deahts;
			return kd;
		}
	}

}
