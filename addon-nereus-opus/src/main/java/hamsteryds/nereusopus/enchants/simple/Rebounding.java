package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Rebounding extends EventExecutor implements FoliaNeeded {
   public Rebounding(File file) {
      super(file);
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         LivingEntity damager = (LivingEntity)event.getDamager();
         Vector direction = damager.getLocation().subtract(creature.getLocation()).toVector().normalize().multiply(this.getValue("velocity", level));

         try {
            direction.checkFinite();
            WorldUtils.addVelocity(damager, direction, true);
         } catch (Exception var7) {
         }
      }
   }
}
