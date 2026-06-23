package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FirstStrike extends EventExecutor {
   public FirstStrike(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         if (creature.getHealth() >= creature.getMaxHealth() - 0.1) {
            event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
         }
      }
   }
}
