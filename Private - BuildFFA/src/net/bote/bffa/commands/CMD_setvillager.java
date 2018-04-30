package net.bote.bffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
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

@SuppressWarnings("deprecation")
public class CMD_setvillager implements CommandExecutor {
	
	private Main plugin;

	public CMD_setvillager(Main main) {
		// TODO Auto-generated constructor stub
		this.plugin = main;
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("bffa.admin.villager")) {
				if(args.length == 0) {
					String name = "Spawn";
					Utils.villager = true;
					
					Villager v = (Villager) Bukkit.getWorld(p.getLocation().getWorld().getName()).spawnCreature(p.getLocation(), CreatureType.VILLAGER);
					v.setCustomName("§a§l➜Inventarsortierung");
					v.setCustomNameVisible(true);
					v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 356000, 356000));
					v.setAdult();
					v.setRemoveWhenFarAway(false);
					
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						
						@Override
						public void run() {
							
							EntityModifier em = new EntityModifier(v, Main.getInstance());
							em.modify().setNoAI(true);
							
						}
					}, 20*2);
					
					for(Player all : Bukkit.getOnlinePlayers()) {
						try {
							all.teleport(LocationManager.getLocation("Spawn"));
						} catch (NullPointerException error) {
							error.printStackTrace();
							Bukkit.broadcastMessage(Main.prefix+"§4Bitte einen Admin, den Spawn zu setzen!");
						}
					}
					
//					for(Location blocks : Main.blocks) {
//						Block b = blocks.getBlock();
//						b.setType(Material.AIR);
//					}
//					
//					for(Player all : Bukkit.getOnlinePlayers()) {
//						try {
//							all.teleport(LocationManager.getLocation("Spawn"));
//						} catch (NullPointerException error) {
//							p.sendMessage(Main.prefix+"§cSetzte bitte mit /setspawn den Spawn");
//						}
//					}
//					
//					WorldResetter.deleteWorldSave(LocationManager.cfg.getString("Locations.Spawn.worldname"));
//					
//					ArmorStand as = p.getLocation().getWorld().spawn(p.getLocation(), ArmorStand.class);
//					
//					as.setBoots(createColorItem(Material.LEATHER_BOOTS, 244, 238, 66));
//					as.setChestplate(createColorItem(Material.LEATHER_CHESTPLATE, 244, 238, 66));
//					as.setLeggings(createColorItem(Material.LEATHER_LEGGINGS, 244, 238, 66));
//					as.setHelmet(getPlayerSkull(args[0]));
//					
//					double x = p.getLocation().getX();
//					double y = p.getLocation().getY();
//					double z = p.getLocation().getZ();
//					double yaw = p.getLocation().getYaw();
//					double pitch = p.getLocation().getPitch();
//					
//					Location loc = new Location(p.getWorld(), x, y, z);
//					loc.setYaw((float) yaw);
//					loc.setPitch((float) pitch);
//					
//					EulerAngle rightarm = new EulerAngle(243, 25, 0);
//					EulerAngle leftarm = new EulerAngle(311, 321, 241);
//					
//					EulerAngle rightleg = new EulerAngle(46, 0, 0);
//					EulerAngle leftleg = new EulerAngle(11, 0, 0);
//					
//					as.setArms(true);
//					as.setBasePlate(false);
//					
//					// TODO Turn right direction  + code
//					
//					as.setRightArmPose(rightarm);
//					as.setLeftArmPose(leftarm);
//					
//					as.setLeftLegPose(leftleg);
//					as.setRightLegPose(rightleg);
//					
//					as.setCustomName("§a§lInventarsortierung");
//					as.setCustomNameVisible(true);
//					
//					WorldResetter.saveWorld(Bukkit.getWorld(LocationManager.cfg.getString("Locations.Spawn.worldname")));
					
				} else {
					p.sendMessage("§7Nutze: §e/setvillager");
				}
			}
		}
		return true;
	}
	
	public ItemStack createColorItem(Material mat, int r, int g, int b) {
		 ItemStack h1 = new ItemStack(Material.LEATHER_BOOTS);
	     LeatherArmorMeta im = (LeatherArmorMeta) h1.getItemMeta();
	     im.setColor(Color.fromRGB(r, g, b));
	     im.setDisplayName("§2§lKing Boots");
	     h1.setItemMeta(im);
	     return h1;
	}
	
	public static ItemStack getPlayerSkull(String name) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
	

}
