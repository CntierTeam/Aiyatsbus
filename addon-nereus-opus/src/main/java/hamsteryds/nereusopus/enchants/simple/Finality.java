package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.PermissionUtils;
import hamsteryds.nereusopus.utils.stats.MathUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Finality extends EventExecutor {
   public Finality(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         double minHealth = MathUtils.calculate(this.getText("min-health"), "level", level, "maxBlood", creature.getMaxHealth());
         if (minHealth >= creature.getHealth()) {
            if (!PermissionUtils.hasDamagePermission(event.getDamager(), creature)) {
               return;
            }

            event.setCancelled(true);
            FoliaUtils.runTask(creature, t -> creature.damage(creature.getHealth() + 1.0));
         }
      }
   }
}
