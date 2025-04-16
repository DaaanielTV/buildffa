package net.bote.bffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bote.bffa.main.Main;
import net.bote.bffa.utils.LocationManager;

public class CMD_setheight implements CommandExecutor {
	
	private Main plugin;

	public CMD_setheight(Main main) {
		// TODO Auto-generated constructor stub
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("bffa.admin.setheight")) {
				
				if(args.length == 0) {
					
					LocationManager.setHeight("Height", p.getLocation().getBlockY());
					p.sendMessage(Main.prefix + "§7Du hast die erfolreich die §eHöhe §7auf §eY:" + p.getLocation().getBlockY() + " §7gesetzt!");
					
				} else {
					p.sendMessage(Main.prefix+"§7Nutze: §e/setheight"); 
				}
				
			} else {
				p.sendMessage(Main.prefix+"§cKein Recht!");
			}
		}
		return true;
	}

}
