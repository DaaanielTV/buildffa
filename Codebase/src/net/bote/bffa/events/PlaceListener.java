package net.bote.bffa.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.bote.bffa.main.Main;
import net.bote.bffa.utils.LocationManager;
import net.bote.bffa.utils.Utils;

public class PlaceListener implements Listener {
    
    private ArrayList<Player> msg = new ArrayList<>();
    
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        Location blockLoc = block.getLocation();
        
        // Allow building for players in build mode
        if(Utils.build.contains(p)) {
            return;
        }
        
        // Check if block is placed near spawn protection
        double spawnProtection = LocationManager.cfg.getDouble("Locations.Spawn.Y") - Main.cfg.getInt("Config.BuildprotectionUnderSpawn");
        if(blockLoc.getY() > spawnProtection) {
            if(!msg.contains(p)) {
                p.sendMessage(Main.prefix + "§cHier kannst du nichts platzieren!");
                msg.add(p);
                
                // Remove message cooldown after 2 seconds
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> msg.remove(p), 40);
            }
            e.setCancelled(true);
            return;
        }
        
        // Handle sandstone block placement
        if(block.getType() == Material.SANDSTONE) {
            Main.blocks.add(blockLoc);
            
            // Schedule block transformation
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                if(!block.getType().equals(Material.SANDSTONE)) {
                    return;
                }
                
                block.setType(Material.REDSTONE_BLOCK);
                
                // Schedule block removal
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    block.setType(Material.AIR);
                    Main.blocks.remove(blockLoc);
                }, 60);
            }, 60);
        } else {
            // Only allow admins to place non-sandstone blocks in build mode
            if(p.hasPermission("bffa.admin.build")) {
                p.sendMessage("§7Gehe in den §eBuild§7, um zu bauen.");
            }
            e.setCancelled(true);
        }
    }
    
    public static Location getLocation(String name) {
        double x = LocationManager.cfg.getDouble("Locations." + name + ".X");
        double y = LocationManager.cfg.getDouble("Locations." + name + ".Y") - Main.cfg.getInt("Config.BuildprotectionUnderSpawn");
        double z = LocationManager.cfg.getDouble("Locations." + name + ".Z");
        double yaw = LocationManager.cfg.getDouble("Locations." + name + ".Yaw");
        double pitch = LocationManager.cfg.getDouble("Locations." + name + ".Pitch");
        String worldName = LocationManager.cfg.getString("Locations." + name + ".worldName");
        
        Location loc = new Location(Bukkit.getWorld(worldName), x, y, z);
        loc.setYaw((float) yaw);
        loc.setPitch((float) pitch);
        
        return loc;
    }
}
