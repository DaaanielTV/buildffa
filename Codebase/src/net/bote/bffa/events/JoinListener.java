package net.bote.bffa.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import net.bote.bffa.main.Main;
import net.bote.bffa.stats.ConfigStats;
import net.bote.bffa.stats.MySQL;
import net.bote.bffa.utils.Config;
import net.bote.bffa.utils.LocationManager;
import net.bote.bffa.utils.Utils;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Location loc;

        // Set world settings
        p.getWorld().setTime(1000);
        p.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        
        try {
            loc = LocationManager.getLocation("Spawn");
            double height = LocationManager.cfg.getDouble("Height");
        } catch (NullPointerException | IllegalArgumentException e1) {
            e.joinMessage(null);
            sendSetupMessage(p);
            return;
        }
        
        // Handle join message
        String joinMessage;
        Plugin nickPlugin = Bukkit.getPluginManager().getPlugin("NickSystem");
        
        if(nickPlugin != null && nickPlugin.isEnabled()) {
            try {
                Class<?> nickSystemClass = Class.forName("net.bote.nickserver.api.NickSystem");
                if((boolean) nickSystemClass.getMethod("isNicked", Player.class).invoke(null, p)) {
                    String nickName = (String) nickSystemClass.getMethod("getNickName", Player.class).invoke(null, p);
                    joinMessage = Main.prefix + "§a" + nickName + " §8hat das Spiel betreten.";
                } else {
                    joinMessage = Main.prefix + "§a" + p.getName() + " §8hat das Spiel betreten.";
                }
            } catch (Exception ex) {
                // Fallback if NickSystem API changes
                joinMessage = getDefaultJoinMessage(p);
            }
        } else {
            joinMessage = getDefaultJoinMessage(p);
        }
        e.joinMessage(net.kyori.adventure.text.Component.text(joinMessage));
        
        // Handle stats
        if(!Main.isMySQLEnabled()) {
            if(ConfigStats.isRegistered(p.getUniqueId().toString())) {
                if(!ConfigStats.getName(p.getUniqueId().toString()).equals(p.getName())) {
                    ConfigStats.setName(p.getUniqueId().toString(), p.getName());
                }
            } else {
                ConfigStats.register(p.getUniqueId().toString(), p.getName());
            }
        } else {
            if(!MySQL.playerExists(p.getUniqueId().toString())) {
                MySQL.createPlayer(p.getUniqueId().toString());
            }
        }
        
        // Set player state
        p.setGameMode(GameMode.SURVIVAL);
        Utils.setJoinInv(p);
        
        // Update scoreboards
        Bukkit.getOnlinePlayers().forEach(Utils::updateScoreboard);
        
        // Show teaming message
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            if(Main.cfg.getBoolean("Config.Teaming")) {
                p.sendMessage(Main.prefix + "§aTeaming ist §lerlaubt§r§a!");
            } else {
                p.sendMessage(Main.prefix + translateTeamingMessage());
            }
        }, 5);
        
        // Teleport player to spawn
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            try {
                Location spawnLoc = LocationManager.getLocation("Spawn");
                p.teleport(spawnLoc);
            } catch (IllegalArgumentException ex) {
                if(p.hasPermission("bffa.admin.setspawn")) {
                    p.sendMessage(Main.prefix + "§cBitte setze den Spawn mit /setspawn");
                } else {
                    p.sendMessage(Main.prefix + "§cBitte einen Admin, den Spawn zu setzen.");
                }
            }
        }, p.hasPlayedBefore() ? 1 : 10);
    }
    
    private void sendSetupMessage(Player p) {
        p.sendMessage("§8------§7[§eSetup Manager§7]§8------");
        p.sendMessage("§7- §eSpawn setzen");
        if(!LocationManager.locationIsExisting("Height")) {
            p.sendMessage("§7- §eHöhe setzen");
        }
        if(Config.getConfig().getString("Mapname") == null) {
            p.sendMessage("§7- §eMapnamen setzen §8(optional)");
        }
        p.sendMessage("§7- §eInventar-Villager erstellen §8(optional)");
        p.sendMessage("");
        p.sendMessage("§eAlle Befehle mit /bffa help");
        p.sendMessage("§8------§7[§eSetup Manager§7]§8------");
    }
    
    private String getDefaultJoinMessage(Player p) {
        if(Config.getConfig().getBoolean("Config.ShowDisplayname")) {
            return Main.prefix + p.getDisplayName() + " §r§8hat das Spiel betreten.";
        } else {
            return Main.prefix + "§a" + p.getName() + " §8hat das Spiel betreten.";
        }
    }
    
    private String translateTeamingMessage() {
        return ChatColor.translateAlternateColorCodes('&', Main.cfg.getString("Config.TeamingForbidden"));
    }
}
