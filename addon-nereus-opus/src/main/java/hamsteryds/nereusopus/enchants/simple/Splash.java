package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Splash extends EventExecutor implements FoliaNeeded {
   public Splash(File file) {
      super(file);
   }

   @Override
   public void projectileHitBlock(int level, ProjectileHitEvent event) {
      if (event.getEntity() instanceof Trident) {
         Trident trident = (Trident)event.getEntity();
         double range = this.getValue("range", level);
         FoliaUtils.runTask(trident, task -> {
            for (Entity entity : trident.getNearbyEntities(range, range, range)) {
               if (entity instanceof LivingEntity creature) {
                  if (!PermissionUtils.hasDamagePermission(trident, creature)) {
                     return;
                  }

                  creature.damage(this.getValue("damage-splash", level));
               }
            }
         });
      }
   }
}
