package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Defender extends EventExecutor {
   public Defender(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Tameable tameable && tameable.isTamed() && tameable.getOwnerUniqueId() == this.getDamagerSource(event).getUniqueId()) {
         event.setCancelled(true);
      }
   }

   public Entity getDamagerSource(EntityDamageByEntityEvent event) {
      Entity damager = event.getDamager();
      return damager instanceof Projectile projectile ? (Entity)projectile.getShooter() : damager;
   }
}
