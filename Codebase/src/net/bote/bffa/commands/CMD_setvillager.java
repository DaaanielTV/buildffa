package net.bote.bffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.bote.bffa.main.Main;
import net.bote.bffa.utils.EntityModifier;
import net.bote.bffa.utils.LocationManager;
import net.bote.bffa.utils.Utils;

public class CMD_setvillager implements CommandExecutor {
    
    private Main plugin;

    public CMD_setvillager(Main main) {
        this.plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player)sender;
        if(!p.hasPermission("bffa.admin.villager")) {
            p.sendMessage(Main.noperm);
            return true;
        }

        if(args.length != 0) {
            p.sendMessage(Main.prefix + "§7Use: §e/setvillager");
            return true;
        }

        Utils.villager = true;
        
        // Spawn villager using modern API
        Villager v = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
        v.setCustomName("§a§l➜Inventarsortierung");
        v.setCustomNameVisible(true);
        v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 356000));
        v.setAdult();
        v.setRemoveWhenFarAway(false);
        
        // Apply AI settings after a short delay to ensure entity is properly spawned
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            EntityModifier em = new EntityModifier(v, Main.getInstance());
            em.modify().setNoAI(true);
        }, 40L);
        
        // Teleport all players to spawn
        for(Player all : Bukkit.getOnlinePlayers()) {
            try {
                all.teleport(LocationManager.getLocation("Spawn"));
            } catch (NullPointerException error) {
                error.printStackTrace();
                Bukkit.broadcastMessage(Main.prefix + "§4Please ask an admin to set the spawn!");
            }
        }

        return true;
    }
    
    private ItemStack createColorItem(Material mat, int r, int g, int b) {
        ItemStack item = new ItemStack(mat);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(Color.fromRGB(r, g, b));
        meta.setDisplayName("§2§lKing Boots");
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack getPlayerSkull(String name) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        item.setItemMeta(meta);
        return item;
    }
}
