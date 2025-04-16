package net.bote.bffa.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.EntityType;

/**
 * Modifies an Entity using modern Paper API
 */
public class EntityModifier {

    static Entity entity;
    static int scheduler;
    static Plugin plugin;
    static Player player = null;
    static float Speed;

    public EntityModifier(Entity entity, Plugin plugin) {
        this.entity = entity;
        this.plugin = plugin;
    }

    /**
     * Modify an Entity with the Builder
     */
    public static Builder modify() {
        return new Builder();
    }

    public static final class Builder {

        public Builder setDisplayName(String display) {
            entity.setCustomName(display);
            entity.setCustomNameVisible(true);
            return this;
        }

        public Builder setDisplayNameVisible(Boolean visible) {
            entity.setCustomNameVisible(visible);
            return this;
        }

        public Builder playEffect(EntityType entityeffect) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).playEffect(entityeffect);
            }
            return this;
        }

        public Builder remove() {
            entity.remove();
            return this;
        }

        public Builder setPassenger(Entity passenger) {
            entity.addPassenger(passenger);
            return this;
        }

        public Builder setFireTicks(int ticks) {
            entity.setFireTicks(ticks);
            return this;
        }

        public Builder setLocation(Location location) {
            teleport(location);
            return this;
        }

        public Builder setYawPitch(float yaw, float pitch) {
            Location loc = entity.getLocation();
            loc.setYaw(yaw);
            loc.setPitch(pitch);
            teleport(loc);
            return this;
        }

        public Builder teleport(Location location) {
            entity.teleport(location);
            return this;
        }

        public Builder die() {
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).setHealth(0);
            }
            return this;
        }

        public Builder setInvisible(boolean invisible) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).setInvisible(invisible);
            }
            return this;
        }

        public Builder setNoAI(boolean noAI) {
            if (entity instanceof Mob) {
                ((Mob)entity).setAI(!noAI);
            }
            return this;
        }

        public Builder setCanPickUpLoot(boolean canPickUpLoot) {
            if (entity instanceof Mob) {
                ((Mob)entity).setCanPickupItems(canPickUpLoot);
            }
            return this;
        }

        public Builder setHealth(float health) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).setHealth(health);
            }
            return this;
        }

        public Builder setCanDespawn(boolean canDespawn) {
            if (entity instanceof Mob) {
                ((Mob)entity).setRemoveWhenFarAway(canDespawn);
            }
            return this;
        }

        public Builder walkToLocation(Location location, float speed) {
            if (entity instanceof Mob) {
                ((Mob)entity).getPathfinder().moveTo(location, speed);
            }
            return this;
        }

        public Builder followPlayer(Player target, float speed) {
            if (!(entity instanceof Mob)) {
                return this;
            }
            
            player = target;
            Speed = speed;
            scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
                () -> {
                    if (entity.isDead() || !player.isOnline()) {
                        stopFollowingPlayer();
                        return;
                    }
                    
                    double distance = entity.getLocation().distance(player.getLocation());
                    if (distance < 11) {
                        float currentSpeed = Speed;
                        if (distance < 3) {
                            currentSpeed = 0;
                        }
                        ((Mob)entity).getPathfinder().moveTo(player.getLocation(), currentSpeed);
                    } else {
                        if (player.isOnGround()) {
                            entity.teleport(player);
                        }
                    }
                }, 0L, 1L);
            return this;
        }

        public Builder stopFollowingPlayer() {
            if (scheduler > 0) {
                Bukkit.getScheduler().cancelTask(scheduler);
                scheduler = 0;
            }
            return this;
        }

        public Entity build() {
            return entity;
        }
    }
}
