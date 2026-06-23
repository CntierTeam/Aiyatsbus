package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Cerebral extends EventExecutor {
   public Cerebral(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof Arrow && event.getEntity() instanceof LivingEntity) {
         Arrow arrow = (Arrow)event.getDamager();
         LivingEntity creature = (LivingEntity)event.getEntity();
         if (arrow.getLocation().getY() < creature.getLocation().getY() + creature.getEyeHeight() - 0.22) {
            return;
         }

         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
