package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Lesion extends EventExecutor {
   public Lesion(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         if (!PermissionUtils.hasDamagePermission(event.getDamager(), creature)) {
            return;
         }

         double duration = this.getValue("duration", level);
         int repeatTicks = (int)this.getValue("repeat-ticks", level);
         double damage = this.getValue("damage", level);
         AtomicInteger counter = new AtomicInteger();
         FoliaUtils.runAtFixedRate(creature, task -> {
            if (counter.incrementAndGet() * repeatTicks >= duration) {
               task.cancel();
            } else {
               creature.damage(damage);
            }
         }, 0L, (long)repeatTicks);
      }
   }
}
