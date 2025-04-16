package net.bote.bffa.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.bote.bffa.main.Main;
import net.bote.bffa.utils.Utils;

public class QuitListener implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			Utils.updateScoreboard(all, Bukkit.getOnlinePlayers().size() - 1);
		}
		
		e.setQuitMessage(Main.prefix+"§a" + p.getName() + " §7hat das Spiel verlassen.");
		
	}

}
