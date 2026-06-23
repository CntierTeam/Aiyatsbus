package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;

public class Shockwave extends EventExecutor {
   public Shockwave(File file) {
      super(file);
   }

   @Override
   public void shootBow(int level, EntityShootBowEvent event) {
      double range = this.getValue("range", level);
      double damage = this.getValue("damage-splash", level);
      AbstractArrow arrow = (AbstractArrow)event.getProjectile();
      Entity who = event.getEntity();
      arrow.setGlowing(true);
      FoliaUtils.runAtFixedRate(arrow, task -> (new Runnable() {
         int counter = 0;

         @Override
         public void run() {
            if (arrow.isDead()) {
               task.cancel();
            } else if (arrow.isInBlock()) {
               task.cancel();
            } else if (this.counter++ >= 50) {
               arrow.remove();
               task.cancel();
            } else {
               for (Entity entity : arrow.getNearbyEntities(range, range, range)) {
                  if (!PermissionUtils.hasDamagePermission(event.getEntity(), entity)) {
                     return;
                  }

                  if (entity.getUniqueId() != who.getUniqueId() && entity instanceof LivingEntity creature) {
                     creature.damage(damage);
                  }
               }
            }
         }
      }).run(), 0L, 2L);
   }
}
