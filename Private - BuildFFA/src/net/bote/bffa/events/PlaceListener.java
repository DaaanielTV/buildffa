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
	
	private Main plugin;
	
	ArrayList<Player> msg = new ArrayList<Player>();
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		
		if(Utils.build.contains(e.getPlayer())) {
			e.setCancelled(false);
			return;
		}
		
		if(e.getBlock().getLocation().getY() > LocationManager.cfg.getDouble("Locations." + "Spawn" + ".Y") - Main.cfg.getInt("Config.BuildprotectionUnderSpawn")) {
			if(Utils.build.contains(p)) {
				e.setCancelled(false);
			} else {
				e.setCancelled(true);
				if(!msg.contains(p)) {
					p.sendMessage(Main.prefix+"§cHier kannst du nichts platzieren!");
					msg.add(p);
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							msg.remove(p);
							
						}
					}, 40);
				} 
				return;
			}
		}
			
		if (e.getBlock().getType() == Material.SANDSTONE) {
	        final Block b = e.getBlock();
	        Main.blocks.add(e.getBlock().getLocation());
	        e.setCancelled(false);
	        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {

					b.setType(Material.REDSTONE_BLOCK);
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							
							b.setType(Material.AIR);
							Main.blocks.remove(b.getLocation());
							
						}
					}, 60L);
					
				}
			}, 60L);
	      } else {
	    	  
	        if(p.hasPermission("bffa.admin.build")) {
	        	p.sendMessage("§7Gehe in den §eBuild§7, um zu bauen.");
	        	e.setCancelled(true);
	        } else {
	        	e.setCancelled(true);
	        }
	        
	      }
		
	}
	
//	plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//        public void run() {
//          b.setType(Material.REDSTONE_BLOCK);
//          plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//            public void run() {
//              b.setType(Material.AIR);
//              Main.blocks.remove(b.getLocation());
//            }
//          }, 40L);
//        }
//      }, 40L);
	
	public static Location getLocation(String name){
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
