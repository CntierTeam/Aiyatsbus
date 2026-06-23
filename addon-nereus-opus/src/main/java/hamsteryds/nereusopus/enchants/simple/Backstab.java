package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Backstab extends EventExecutor {
   public Backstab(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity) {
         LivingEntity entity_1 = (LivingEntity)event.getDamager();
         LivingEntity entity_2 = (LivingEntity)event.getEntity();
         if (this.isBackToEye(entity_1, entity_2)) {
            double damageMultiplier = this.getValue("damage-multiplier", level);
            event.setDamage(event.getDamage() * damageMultiplier);
         }
      }
   }

   public boolean isBackToEye(LivingEntity entity_1, LivingEntity entity_2) {
      Location loc_1 = entity_1.getEyeLocation();
      Location loc_2 = entity_2.getEyeLocation();
      if (!loc_1.getWorld().getName().equals(loc_2.getWorld().getName())) {
         return false;
      } else {
         Vector direction_1 = loc_2.clone().subtract(loc_1).toVector();
         direction_1.setY(0);
         Vector direction_2 = loc_2.getDirection();
         direction_2.setY(0);
         return !(direction_1.angle(direction_2) >= Math.PI / 3);
      }
   }
}
