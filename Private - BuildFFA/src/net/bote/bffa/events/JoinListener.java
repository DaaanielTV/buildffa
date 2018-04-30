package net.bote.bffa.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import net.bote.bffa.main.Main;
import net.bote.bffa.stats.ConfigStats;
import net.bote.bffa.stats.MySQL;
import net.bote.bffa.utils.Config;
import net.bote.bffa.utils.LocationManager;
import net.bote.bffa.utils.Utils;
import net.bote.nickserver.api.NickSystem;
import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Plugin nick = Bukkit.getPluginManager().getPlugin("NickSystem");
		Player p = e.getPlayer();
		Location loc = null;
		double high;
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set day");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle false");
		
		try {
			loc = LocationManager.getLocation("Spawn");
			high = LocationManager.cfg.getDouble("Height");
		} catch (NullPointerException | IllegalArgumentException e1) {
			e.setJoinMessage(null);
			p.sendMessage("§8------§7[§eSetup Manager§7]§8------");
			p.sendMessage("§7- §eSpawn setzen");
			if(!LocationManager.locationIsExisting("Height")) {
				p.sendMessage("§7- §eHöhe setzen");
			}
			if(Config.getConfig().getString("Mapname") == null) {
				p.sendMessage("§7- §eMapnamen setzen §8(optional)");
			}
			p.sendMessage("§7- §eInventar-Villager erstellen §8(optional)");
			p.sendMessage("");
			p.sendMessage("§eAlle Befehle mit /bffa help");
			p.sendMessage("§8------§7[§eSetup Manager§7]§8------");
			return;
		}
		
				if(nick != null) {
					if(NickSystem.isNicked(p)) {
						e.setJoinMessage(Main.prefix+"§a" + NickSystem.getNickName(p) + " §8hat das Spiel betreten.");
						
					} else {
						e.setJoinMessage(Main.prefix+"§a" + p.getName() + " §8hat das Spiel betreten.");
					}
				} else {
					if(Config.getConfig().getBoolean("Config.ShowDisplayname") == true) {
						e.setJoinMessage(Main.prefix + p.getDisplayName() + " §r§8hat das Spiel betreten.");
					} else {
						e.setJoinMessage(Main.prefix+"§a" + p.getName() + " §8hat das Spiel betreten.");
					}
				}
				
				if(!Main.isMySQLEnabled()) {
					if(ConfigStats.isRegistered(p.getUniqueId().toString())) {
						if(ConfigStats.getName(p.getUniqueId().toString()) != p.getName()) {
							ConfigStats.setName(p.getUniqueId().toString(), p.getName());
						}
					} else {
						ConfigStats.register(p.getUniqueId().toString(), p.getName());
					}
				} else {
					if(!MySQL.playerExists(p.getUniqueId().toString())) {
						MySQL.createPlayer(p.getUniqueId().toString());
					}
				}
				
				p.setGameMode(GameMode.SURVIVAL);
				
				Utils.setJoinInv(p);
				
				for(Player all : Bukkit.getOnlinePlayers()) {
					Utils.updateScoreboard(all);
				}
				
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						
						if(Main.cfg.getBoolean("Config.Teaming") == true) {
							p.sendMessage(Main.prefix+"§aTeaming ist §lerlaubt§r§a!");
						} else {
							p.sendMessage(Main.prefix + ChatColor.translateAlternateColorCodes('&', Main.cfg.getString("Config.TeamingForbidden")));
						}
						
					}
				}, 5);
				
				if(!p.hasPlayedBefore()) {
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							
							try {
								Location loc1 = LocationManager.getLocation("Spawn");
								p.teleport(loc1);
							} catch (IllegalArgumentException e) {
								if(p.isOp()) {
									p.sendMessage(Main.prefix+"§cBitte setze den Spawn mit /setspawn");
								} else {
									p.sendMessage(Main.prefix+"§cBitte einen Admin, den Spawn zu setzen.");
								}
							}
							
						}
					}, 10);
				} else {
					
					try {
						Location loc1 = LocationManager.getLocation("Spawn");
						p.teleport(loc1);
					} catch (IllegalArgumentException ex) {
						if(p.isOp()) {
							p.sendMessage(Main.prefix+"§cBitte setze den Spawn mit /setspawn");
						} else {
							p.sendMessage(Main.prefix+"§cBitte einen Admin, den Spawn zu setzen.");
						}
					}
				}
		
	}
	
}
