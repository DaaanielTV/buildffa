package net.bote.bffa.commands;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bote.bffa.main.Main;
import net.bote.bffa.utils.Utils;

public class CMD_setmap implements CommandExecutor {
	
	private Main plugin;

	public CMD_setmap(Main main) {
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("bffa.admin.setname")) {
				if(args.length == 1) {
					String name = args[0];
					if(Main.cfg.getString("Config.Mapname") != null) {
						p.sendMessage(Main.prefix+"§7Du hast die Map erfolgreich zu: §e" + name + " §7umbenannt!");
					} else {
						p.sendMessage(Main.prefix+"§7Du hast den Mapname §e" + name + " §7gesetzt!");
					}
					
					Main.cfg.set("Config.Mapname", name);
					try {
						Main.cfg.save(Main.f);
						
						for(Player all : Bukkit.getOnlinePlayers()) {
							Utils.updateScoreboard(all);
						}
						
					} catch (IOException e) {
						e.printStackTrace();
						p.sendMessage(Main.error);
						return true;
					}
			} else {
				p.sendMessage(Main.prefix+"§7Nutze: §e/setname <Name>");
			}
		} else {
			p.sendMessage(Main.prefix+"§cKein Recht!");
		}
	}
		return true;
	}

}
