package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Accumulation extends EventExecutor {
   public static HashMap<UUID, Long> stamps = new HashMap<>();

   public Accumulation(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      Entity damager = event.getDamager();
      if (damager instanceof Projectile projectile && projectile.getShooter() instanceof LivingEntity creature) {
         damager = creature;
      }

      if (damager instanceof LivingEntity) {
         double damageMultiplier = this.getValue("damage-multiplier", level);
         double duration = this.getValue("duration", level);
         if (stamps.containsKey(damager.getUniqueId()) && System.currentTimeMillis() - stamps.get(damager.getUniqueId()) >= duration * 50.0) {
            event.setDamage(event.getDamage() * damageMultiplier);
         }

         stamps.put(damager.getUniqueId(), System.currentTimeMillis());
      }
   }
}
