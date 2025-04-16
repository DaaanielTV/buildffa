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
    
    private static ArrayList<Player> died = new ArrayList<>();
    private static ArrayList<Player> death = new ArrayList<>();
    
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(Utils.build.contains(p)) {
            return;
        }

        if(p.getLocation().getBlockY() <= LocationManager.cfg.getDouble("Locations.Height")) {
            if(!died.contains(p)) {
                e.setCancelled(true);
                p.setHealth(0);
                Main.lastDamage.remove(p);
                
                died.add(p);
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> died.remove(p), 20);
            } else {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDroppedExp(0);
        e.getDrops().clear();
        e.deathMessage(null);

        Player victim = e.getPlayer();
        Player killer = victim.getKiller();
        
        if(killer != null) {
            if(!death.contains(victim)) {
                String deathMessage;
                if(Main.cfg.getBoolean("Config.ShowDisplayname")) {
                    deathMessage = Main.prefix + victim.getDisplayName() + " §7wurde von §r" + killer.getDisplayName() + " §r§7getötet.";
                } else {
                    deathMessage = Main.prefix + "§a" + victim.getName() + " §7wurde von §a" + killer.getName() + " §7getötet.";
                }
                
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(deathMessage));
                death.add(victim);
                
                Utils.updateScoreboard(killer);
                Utils.updateScoreboard(victim);
                
                if(Main.isMySQLEnabled()) {
                    MySQL.createPlayer(victim.getUniqueId().toString());
                    MySQL.createPlayer(killer.getUniqueId().toString());
                    Stats.addKill(killer.getUniqueId().toString());
                    Stats.addDeath(victim.getUniqueId().toString());
                } else {
                    String victimUUID = victim.getUniqueId().toString();
                    String killerUUID = killer.getUniqueId().toString();
                    
                    if(!ConfigStats.isRegistered(victimUUID)) {
                        ConfigStats.register(victimUUID, victim.getName());
                    }
                    if(!ConfigStats.isRegistered(killerUUID)) {
                        ConfigStats.register(killerUUID, killer.getName());
                    }
                    ConfigStats.addKill(killerUUID);
                    ConfigStats.addDeath(victimUUID);
                }
                
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> death.remove(victim), 20);
            }
            
            killer.setHealth(20);
            
            // Schedule respawn
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                victim.spigot().respawn();
            }, 2);
        } else {
            if(!death.contains(victim)) {
                if(Main.isMySQLEnabled()) {
                    Stats.addDeath(victim.getUniqueId().toString());
                } else {
                    ConfigStats.addDeath(victim.getUniqueId().toString());
                }
                
                String deathMessage;
                if(Main.cfg.getBoolean("Config.ShowDisplayname")) {
                    deathMessage = Main.prefix + victim.getDisplayName() + " §r§7ist gestorben.";
                } else {
                    deathMessage = Main.prefix + "§a" + victim.getName() + " §7ist gestorben.";
                }
                
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(deathMessage));
                death.add(victim);
                
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> death.remove(victim), 20);
            }
            
            // Schedule respawn
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                victim.spigot().respawn();
            }, 2);
        }
    }
    
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        
        // Set respawn location
        try {
            e.setRespawnLocation(LocationManager.getLocation("Spawn"));
        } catch (IllegalArgumentException ex) {
            if(p.hasPermission("bffa.admin.setspawn")) {
                p.sendMessage(Main.prefix + "§cBitte setze den Spawn!");
            } else {
                Bukkit.broadcastMessage("§4Der Spawn muss noch mit /setspawn gesetzt werden!");
            }
        }
        
        // Reset player state
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            p.setHealth(20);
            p.setFoodLevel(20);
            Utils.setJoinInv(p);
            Utils.updateScoreboard(p, Bukkit.getOnlinePlayers().size() + 1);
        }, 2);
    }
}
