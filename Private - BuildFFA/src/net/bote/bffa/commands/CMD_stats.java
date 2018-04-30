package net.bote.bffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bote.bffa.main.Main;
import net.bote.bffa.stats.ConfigStats;
import net.bote.bffa.stats.MySQL;
import net.bote.bffa.stats.Stats;
import net.bote.bffa.utils.Config;

public class CMD_stats implements CommandExecutor {
	
	private Main plugin;

	public CMD_stats(Main main) {
		// TODO Auto-generated constructor stub
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(sender instanceof Player) {
			Player p = (Player) sender;
				if(Main.isMySQLEnabled()) {
					try {
						if(args.length == 0) {
							p.sendMessage("§7-----[§6Stats » §e" + p.getName() + "§7]-----");
							p.sendMessage("§7Kills: §e" + Stats.getKills(p.getUniqueId().toString()));
							p.sendMessage("§7Tode: §e" + Stats.getDeaths(p.getUniqueId().toString()));
							p.sendMessage("§7K/D: §e" + Stats.getKD(p.getUniqueId().toString()));
							p.sendMessage("§7-----[§6Stats » §e" + p.getName() + "§7]-----");
						} else if(args.length == 1) {
							Player target = Bukkit.getPlayer(args[0]);
							if(target != null) {
								p.sendMessage("§7-----[§6Stats » §e" + target.getName() + "§7]-----");
								p.sendMessage("§7Kills: §e" + Stats.getKills(target.getUniqueId().toString()));
								p.sendMessage("§7Tode: §e" + Stats.getDeaths(target.getUniqueId().toString()));
								p.sendMessage("§7K/D: §e" + Stats.getKD(target.getUniqueId().toString()));
								p.sendMessage("§7-----[§6Stats » §e" + target.getName() + "§7]-----");
							} else {
								OfflinePlayer off = Bukkit.getOfflinePlayer(args[0]);
								if(off.isOnline()) {
									p.sendMessage("§7-----[§6Stats » §e" + off.getName() + "§7]-----");
									p.sendMessage("§7Kills: §e" + Stats.getKills(off.getUniqueId().toString()));
									p.sendMessage("§7Tode: §e" + Stats.getDeaths(off.getUniqueId().toString()));
									p.sendMessage("§7K/D: §e" + Stats.getKD(off.getUniqueId().toString()));
									p.sendMessage("§7-----[§6Stats » §e" + off.getName() + "§7]-----");
								} else {
									if(off.hasPlayedBefore()) {
										p.sendMessage("§7-----[§6Stats » §e" + off.getName() + "§7]-----");
										p.sendMessage("§7Kills: §e" + Stats.getKills(off.getUniqueId().toString()));
										p.sendMessage("§7Tode: §e" + Stats.getDeaths(off.getUniqueId().toString()));
										p.sendMessage("§7K/D: §e" + Stats.getKD(off.getUniqueId().toString()));
										p.sendMessage("§7-----[§6Stats » §e" + off.getName() + "§7]-----");
									} else {
										p.sendMessage(Main.prefix+"§cDieser Spieler hat §4noch nie §cBuildFFA gespielt!");
									}
								}
							}
						} else {
							p.sendMessage(Main.prefix+"§7Nutze: §e/stats <Name>");
						}
					} catch (NullPointerException error) {
						if(p.isOp()) {
							p.sendMessage(Main.prefix+"§cVerbinde das Plugin mit MySQL, um Stats zu nutzen!");
							System.err.println("Fehler: " + error.getMessage());
						} else {
							p.sendMessage(Main.prefix+"§cDie Stats sind zur Zeit nicht verfügbar!");
						}
					}
				} else {
					if(args.length == 0) {
						p.sendMessage("§7-----[§6Stats » §e" + p.getName() + "§7]-----");
						p.sendMessage("§7Kills: §e" + ConfigStats.getKills(p.getUniqueId().toString()));
						p.sendMessage("§7Tode: §e" + ConfigStats.getDeaths(p.getUniqueId().toString()));
						p.sendMessage("§7K/D: §e" + ConfigStats.getKD(p.getUniqueId().toString()));
						p.sendMessage("§7-----[§6Stats » §e" + p.getName() + "§7]-----");
					} else if(args.length == 1) {
						Player target = Bukkit.getPlayer(args[0]);
						if(target != null) {
							p.sendMessage("§7-----[§6Stats » §e" + target.getName() + "§7]-----");
							p.sendMessage("§7Kills: §e" + ConfigStats.getKills(target.getUniqueId().toString()));
							p.sendMessage("§7Tode: §e" + ConfigStats.getDeaths(target.getUniqueId().toString()));
							p.sendMessage("§7K/D: §e" + ConfigStats.getKD(target.getUniqueId().toString()));
							p.sendMessage("§7-----[§6Stats » §e" + target.getName() + "§7]-----");
						} else {
							OfflinePlayer off = Bukkit.getOfflinePlayer(args[0]);
							if(off.isOnline()) {
								p.sendMessage("§7-----[§6Stats » §e" + off.getName() + "§7]-----");
								p.sendMessage("§7Kills: §e" + ConfigStats.getKills(off.getUniqueId().toString()));
								p.sendMessage("§7Tode: §e" + ConfigStats.getDeaths(off.getUniqueId().toString()));
								p.sendMessage("§7K/D: §e" + ConfigStats.getKD(off.getUniqueId().toString()));
								p.sendMessage("§7-----[§6Stats » §e" + off.getName() + "§7]-----");
							} else {
								if(off.hasPlayedBefore()) {
									p.sendMessage("§7-----[§6Stats » §e" + off.getName() + "§7]-----");
									p.sendMessage("§7Kills: §e" + ConfigStats.getKills(off.getUniqueId().toString()));
									p.sendMessage("§7Tode: §e" + ConfigStats.getDeaths(off.getUniqueId().toString()));
									p.sendMessage("§7K/D: §e" + ConfigStats.getKD(off.getUniqueId().toString()));
									p.sendMessage("§7-----[§6Stats » §e" + off.getName() + "§7]-----");
								} else {
									p.sendMessage(Main.prefix+"§cDieser Spieler hat §4noch nie §cBuildFFA gespielt!");
								}
							}
						}
					} else {
						p.sendMessage(Main.prefix+"§7Nutze: §e/stats <Name>");
					}
				}
		}
		return true;
	}

}
