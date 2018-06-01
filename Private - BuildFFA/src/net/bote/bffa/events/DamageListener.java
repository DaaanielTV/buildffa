package net.bote.bffa.events;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.bote.bffa.utils.LocationManager;
import net.bote.bffa.utils.Utils;

public class DamageListener implements Listener {
	
	@EventHandler
	public void onDMG(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			if(e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
			} else {
				e.setCancelled(false);
			}
		}
	}
	
	@EventHandler
	public void onDMGbyEnt(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {

			/*
			 *  OPFER -> TÄTER
			 */
			
			/*
			 * 
			 * 
			 *  --- Spawn
			 * 
			 * 
			 */
			
			Player p = (Player) e.getEntity();
			Player k = (Player) e.getDamager();
			
			double yp = p.getLocation().getY();
			double yk = k.getLocation().getY();
			
			double h = LocationManager.getSpawnHeight() - 3;
			
			if(yp >= h && yk >= h) {
				e.setCancelled(true);
				return;
			} else if(yp <= h && yk >= h) {
				e.setCancelled(true);
				return;
			} else if(yp >= h && yk <= h) {
				e.setCancelled(true);
				return;
			} else {
				e.setCancelled(false);
			}
		} else if(e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			Arrow a = (Arrow) e.getDamager();
			Entity shoot = (Entity) a.getShooter();
			if(shoot instanceof Player) {
				String name = "Spawn";
				if(p.getLocation().getY() < LocationManager.cfg.getDouble("Locations." + name + ".Y") - 3 && shoot.getLocation().getY() < LocationManager.cfg.getDouble("Locations." + name + ".Y") - 3) {
					e.setCancelled(true);
				} else if(p.getLocation().getY() < LocationManager.cfg.getDouble("Locations." + name + ".Y") - 3 && !(shoot.getLocation().getY() < LocationManager.cfg.getDouble("Locations." + name + ".Y") - 3)) {
					e.setCancelled(true);
				} else if(p.getLocation().getY() < LocationManager.cfg.getDouble("Locations." + name + ".Y") - 3 && shoot.getLocation().getY() > LocationManager.cfg.getDouble("Locations." + name + ".Y") - 3) {
					e.setCancelled(true);
				} else if(p.getLocation().getY() > LocationManager.cfg.getDouble("Locations." + name + ".Y") - 3 && shoot.getLocation().getY() < LocationManager.cfg.getDouble("Locations." + name + ".Y") - 3) {
					e.setCancelled(true);					
				} else {
					e.setCancelled(false);
				}
			}
		} else if(e.getEntity() instanceof Village) {
			Villager v = (Villager) e.getEntity();
			Player dmg = (Player)e.getDamager();
			if(v.getCustomName().equals("§a§l➜Inventarsortierung")) {
				if(Utils.build.contains(dmg)) {
					e.setCancelled(false);
				} else {
					e.setCancelled(true);
				}
			} else {
				e.setCancelled(false);
			}
		}
		
	}
	
	
	
//	if(p.getLocation().getY() - 2 > LocationManager.cfg.getDouble("Locations.Spawn.Y") && shoot.getLocation().getY() - 2 < LocationManager.cfg.getDouble("Locations.Spawn.Y")) {
//		e.setCancelled(true);
//	}
//	else if(p.getLocation().getY() - 2 > LocationManager.cfg.getDouble("Locations.Spawn.Y") && shoot.getLocation().getY() - 2 > LocationManager.cfg.getDouble("Locations.Spawn.Y")) {
//		e.setCancelled(true);
//	}
//	else {
//		e.setCancelled(false);
//	}

}
