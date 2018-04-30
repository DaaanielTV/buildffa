package net.bote.bffa.commands;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bote.bffa.main.Main;
import net.bote.bffa.utils.Utils;

public class CMD_build implements CommandExecutor {
	
	private Main plugin;

	public CMD_build(Main main) {
		// TODO Auto-generated constructor stub
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("bffa.admin.build")) {
				
				if(args.length == 0) {
					if(Utils.build.contains(p)) {
						Utils.build.remove(p);
						p.sendMessage(Main.prefix+"§7Du bist nun nicht mehr im §eBuild");
						p.setGameMode(GameMode.SURVIVAL);
						p.playSound(p.getLocation(), Sound.FIREWORK_BLAST, 2, 3);
					} else {
						Utils.build.add(p);
						p.sendMessage(Main.prefix+"§7Du bist nun im §eBuild");
						p.playSound(p.getLocation(), Sound.FIREWORK_BLAST, 2, 3);
						p.setGameMode(GameMode.CREATIVE);
					}
				} else {
					p.sendMessage(Main.prefix+"§7Nutze: §e/build");
				}
				
			} else {
				p.sendMessage(Main.prefix+"§cKein Recht!");
			}
		}
		return true;
	}

}
