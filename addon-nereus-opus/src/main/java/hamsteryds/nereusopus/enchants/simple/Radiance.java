package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffectType;

public class Radiance extends EventExecutor implements FoliaNeeded {
   public Radiance(File file) {
      super(file);
   }

   @Override
   public void projectileHitBlock(int level, ProjectileHitEvent event) {
      if (event.getEntity() instanceof AbstractArrow) {
         AbstractArrow arrow = (AbstractArrow)event.getEntity();
         double range = this.getValue("range", level);
         double duration = this.getValue("duration", level);
         FoliaUtils.runTask(arrow, task -> {
            for (Entity entity : arrow.getNearbyEntities(range, range, range)) {
               if (entity instanceof LivingEntity creature) {
                  creature.addPotionEffect(PotionEffectType.GLOWING.createEffect((int)duration, 0));
               }
            }
         });
      }
   }
}
