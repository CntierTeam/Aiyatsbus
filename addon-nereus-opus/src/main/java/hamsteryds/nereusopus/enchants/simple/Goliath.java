package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Goliath extends EventExecutor {
   public Goliath(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity) {
         LivingEntity creatureA = (LivingEntity)event.getDamager();
         LivingEntity creatureB = (LivingEntity)event.getDamager();
         if (creatureA.getHealth() >= creatureB.getHealth()) {
            event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
         }
      }
   }
}
