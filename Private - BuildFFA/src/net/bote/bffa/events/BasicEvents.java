package net.bote.bffa.events;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import net.bote.bffa.utils.Utils;

public class BasicEvents implements Listener {
	
	@EventHandler
	public void onWeather(WeatherChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onContact(EntityInteractEvent e) {
		if(e.getEntity() instanceof Villager) {
			Villager v = (Villager) e.getEntity();
			if(v.getCustomName().equals("§a§l➜Inventarsortierung")) {
				e.setCancelled(true);
			} else {
				e.setCancelled(false);
			}
		}
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		e.setExpToDrop(0);
		if(Utils.build.contains(e.getPlayer())) {
			e.setCancelled(false);
		} else {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onXP(PlayerLevelChangeEvent e) {
		e.getPlayer().setLevel(0);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		try {
			Material mat = e.getCurrentItem().getType();
			if(mat == Material.LEATHER_BOOTS || mat == Material.LEATHER_CHESTPLATE || mat == Material.LEATHER_HELMET || mat == Material.LEATHER_LEGGINGS || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§b·٠§9● Pfeil")) {
				e.setCancelled(true);
			} else {
				e.setCancelled(false);
			}
		} catch (Exception ignored) {
			
		}
	}

}
