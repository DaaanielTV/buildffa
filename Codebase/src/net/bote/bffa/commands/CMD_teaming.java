package net.bote.bffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bote.bffa.main.Main;
import net.bote.bffa.utils.Config;
import net.bote.bffa.utils.Utils;

public class CMD_teaming implements CommandExecutor {
	
	private Main plugin;

	public CMD_teaming(Main main) {
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("buildffa.admin.teaming")) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("allow")) {
						Main.cfg.set("Config.Teaming", true);
						Config.save();
						for(Player all : Bukkit.getOnlinePlayers()) {
							Utils.updateScoreboard(all);
						}
						p.sendMessage(Main.prefix+"§7Du hast Teaming auf §aERLAUBT §7gestellt.");
					} else if(args[0].equalsIgnoreCase("deny")) {
						Main.cfg.set("Config.Teaming", false);
						p.sendMessage(Main.prefix+"§7Du hast Teaming auf §cVERBOTEN §7gestellt.");
						Config.save();
						for(Player all : Bukkit.getOnlinePlayers()) {
							Utils.updateScoreboard(all);
						}
					} else {
						p.sendMessage(Main.prefix+"§7Nutze: §e/teaming <§aallow §e| §cdeny§e>");
					}
				} else {
					p.sendMessage(Main.prefix+"§7Nutze: §e/teaming <§aallow §e| §cdeny§e>");
				}
			} else {
				p.sendMessage(Main.prefix + "§cKein Recht!");
			}
		}
		return true;
	}

}
