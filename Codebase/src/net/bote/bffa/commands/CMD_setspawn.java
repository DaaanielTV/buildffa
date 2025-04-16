package net.bote.bffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bote.bffa.main.Main;
import net.bote.bffa.utils.LocationManager;

public class CMD_setspawn implements CommandExecutor {
	
	private Main plugin;

	public CMD_setspawn(Main main) {
		// TODO Auto-generated constructor stub
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("bffa.admin.setspawn")) {
				if(args.length == 0) {
					
					LocationManager.setLocation("Spawn", p.getLocation());
					
					LocationManager.saveLocations();
					
					p.sendMessage(Main.prefix+"§7Du hast den §eSpawn §7gesetzt!");
				} else {
					p.sendMessage(Main.prefix+"§7Nutze: §e/setspawn");
				}
			} else {
				p.sendMessage(Main.prefix+"§cKein Recht!");
			}
		}
		return true;
	}

}
