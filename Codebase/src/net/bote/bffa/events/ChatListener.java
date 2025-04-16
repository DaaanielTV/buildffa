package net.bote.bffa.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.bote.bffa.main.Main;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		if(Main.cfg.getBoolean("Config.ShowDisplayname") == true) {
			Bukkit.broadcastMessage(Main.prefix + e.getPlayer().getDisplayName() + " §8● §f" + e.getMessage());
		} else {
			Bukkit.broadcastMessage(Main.prefix+"§a" + e.getPlayer().getName() + " §8● §f" + e.getMessage());
		}
	}
	
}
