package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.stats.MathUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Proximity extends EventExecutor {
   public Proximity(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         LivingEntity damager = (LivingEntity)event.getDamager();
         if (damager.getWorld().equals(creature.getWorld())) {
            double dist = damager.getLocation().distance(creature.getLocation());
            event.setDamage(event.getDamage() * MathUtils.calculate(this.getText("damage-multiplier"), "level", level, "dist", dist));
         }
      }
   }
}
