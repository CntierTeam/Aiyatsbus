package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Tectonic extends EventExecutor implements FoliaNeeded {
   public Tectonic(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      if (event.getCause() == DamageCause.FALL) {
         double range = this.getValue("range", level);
         double damage = this.getValue("damage-splash", level);
         FoliaUtils.runTask(event.getEntity(), task -> {
            for (Entity entity : event.getEntity().getNearbyEntities(range, range, range)) {
               if (entity instanceof LivingEntity creature) {
                  if (!PermissionUtils.hasDamagePermission(event.getEntity(), creature)) {
                     return;
                  }

                  creature.damage(damage);
               }
            }
         });
      }
   }
}
