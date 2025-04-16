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
            }
        }
    }
    
    @EventHandler
    public void onDMGbyEnt(EntityDamageByEntityEvent e) {
        // Player vs Player combat
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            Player attacker = (Player) e.getDamager();
            
            double victimHeight = victim.getLocation().getY();
            double attackerHeight = attacker.getLocation().getY();
            double spawnProtection = LocationManager.getSpawnHeight() - 3;
            
            // Cancel damage if either player is in spawn protection area
            if(victimHeight >= spawnProtection || attackerHeight >= spawnProtection) {
                e.setCancelled(true);
                return;
            }
        } 
        // Arrow damage
        else if(e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            Arrow arrow = (Arrow) e.getDamager();
            
            if(!(arrow.getShooter() instanceof Player)) {
                return;
            }
            
            Entity shooter = (Entity) arrow.getShooter();
            double spawnHeight = LocationManager.getSpawnHeight() - 3;
            
            // Cancel arrow damage if either player is in spawn protection
            if(victim.getLocation().getY() >= spawnHeight || shooter.getLocation().getY() >= spawnHeight) {
                e.setCancelled(true);
            }
        }
        // Villager protection
        else if(e.getEntity() instanceof Villager) {
            Villager villager = (Villager) e.getEntity();
            if(!(e.getDamager() instanceof Player)) {
                return;
            }
            
            Player damager = (Player) e.getDamager();
            
            if(villager.getCustomName() != null && 
               villager.getCustomName().equals("§a§l➜Inventarsortierung")) {
                e.setCancelled(!Utils.build.contains(damager));
            }
        }
    }
}
