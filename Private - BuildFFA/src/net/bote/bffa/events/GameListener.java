package net.bote.bffa.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.bote.bffa.main.Main;
import net.bote.bffa.stats.ConfigStats;
import net.bote.bffa.stats.MySQL;
import net.bote.bffa.stats.Stats;
import net.bote.bffa.utils.LocationManager;
import net.bote.bffa.utils.Utils;

public class GameListener implements Listener {
	
	private static ArrayList<Player> died = new ArrayList<Player>();
	
	private static ArrayList<Player> death = new ArrayList<Player>();
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(Utils.build.contains(p)) {
			e.setCancelled(false);
			return;
		}
		if(p.getLocation().getBlockY() <= LocationManager.cfg.getDouble("Locations.Height")) {
			
			/*
			 *  -200 | 0
			 */
			
			if(!died.contains(p)) {
				e.setCancelled(true);
				p.setHealth(0);
				
				Main.lastDamage.remove(e.getPlayer());
				
				/*
				 *  Debugg for death message
				 */
				
				
				died.add(p);
				
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						
						died.remove(p);
						
					}
				}, 20);
				
			} else {
				e.setCancelled(true);
			}
			
		} else {
			e.setCancelled(false);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setDroppedExp(0);
		e.getDrops().clear();
		Player p = (Player) e.getEntity();
		Player killer = p.getKiller();
		
		if(killer instanceof Player && killer != null) {
		
			e.setDeathMessage(null);
			
			if(!death.contains(p)) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					if(Main.cfg.getBoolean("Config.ShowDisplayname") == true) {
						all.sendMessage(Main.prefix + p.getDisplayName() + " §7wurde von §r" + killer.getDisplayName() + " §r§7getötet.");
					} else {
						all.sendMessage(Main.prefix+"§a" + p.getName() + " §7wurde von §a" + killer.getName() + " §7getötet.");
					}
				}
				death.add(p);
				
				Utils.updateScoreboard(killer);
				Utils.updateScoreboard(p);
				
				if(Main.isMySQLEnabled()) {
					if(!MySQL.playerExists(p.getUniqueId().toString())) {
						MySQL.createPlayer(p.getUniqueId().toString());
					}
					if(!MySQL.playerExists(killer.getUniqueId().toString())) {
						MySQL.createPlayer(killer.getUniqueId().toString());
					}
					if(killer != null) {
						Stats.addKill(killer.getUniqueId().toString());
					}
					Stats.addDeath(p.getUniqueId().toString());
					
				} else {
					
					String playeruuid = p.getUniqueId().toString();
					String killeruuid = killer.getUniqueId().toString();
					
					if(!ConfigStats.isRegistered(playeruuid)) {
						ConfigStats.register(playeruuid, p.getName());
					}
					if(!ConfigStats.isRegistered(killeruuid)) {
						ConfigStats.register(killeruuid, killer.getName());
					}
					if(killer != null) {
						ConfigStats.addKill(killeruuid);
					}
					ConfigStats.addDeath(playeruuid);
				}
				
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						death.remove(p);
						
					}
				}, 20);
			}
			
			killer.setHealth(20);
			
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					p.spigot().respawn();
					
				}
			}, 2);
			
		} else {
			
			e.setDeathMessage(null);
			
			if(Main.isMySQLEnabled()) {
				
				Stats.addDeath(p.getUniqueId().toString());
				
			} else {
				ConfigStats.addDeath(p.getUniqueId().toString());
			}
			
			
			if(!death.contains(p)) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					if(Main.cfg.getBoolean("Config.ShowDisplayname") == true) {
						all.sendMessage(Main.prefix + p.getDisplayName() + " §r§7ist gestorben.");
					} else {
						all.sendMessage(Main.prefix+"§a" + p.getName() + " §7ist gestorben.");
					}
				}
				death.add(p);
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						death.remove(p);
						
					}
				}, 20);
			}
			
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					p.spigot().respawn();
					
				}
			}, 2);
		}
		
		/*
		 * Place for stuff :3
		 */
	}
	
	@EventHandler
	public void onRswn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				try {
					p.teleport(LocationManager.getLocation("Spawn"));
				} catch (IllegalArgumentException e1) {
					if(p.hasPermission("bffa.admin.setspawn")) {
						p.sendMessage(Main.prefix+"§cBitte setze den Spawn!");
					} else {
						Bukkit.broadcastMessage("§4Der Spawn muss noch mit /setspawn gesetzt werden!");
					}
				}
				
			}
		}, 2);
		
		p.setHealth(20);
		p.setFoodLevel(20);
		Utils.setJoinInv(p);
		Utils.updateScoreboard(p, Bukkit.getOnlinePlayers().size() + 1);
	}

}
