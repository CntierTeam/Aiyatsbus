package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.MechanismUtils;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Carve extends EventExecutor {
   public Carve(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (MechanismUtils.checkCritical(event.getDamager())) {
         double damageMultiplier = this.getValue("damage-multiplier", level);
         double damageSplash = this.getValue("damage-splash-multiplier", level);
         double range = this.getValue("range", level);
         event.setDamage(event.getDamage() * damageMultiplier);
         FoliaUtils.runTask(event.getEntity(), task -> {
            for (Entity nearby : event.getEntity().getNearbyEntities(range, range, range)) {
               if (nearby instanceof LivingEntity && nearby.getUniqueId() != event.getDamager().getUniqueId()) {
                  LivingEntity creature = (LivingEntity)nearby;
                  if (!creature.isDead() && PermissionUtils.hasDamagePermission(event.getDamager(), creature)) {
                     creature.damage(damageSplash / 100.0 * event.getDamage());
                  }
               }
            }
         });
      }
   }
}
