package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Grapple extends EventExecutor implements FoliaNeeded {
   public Grapple(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         Vector direction = event.getDamager().getLocation().subtract(creature.getLocation()).toVector().normalize();
         direction = direction.multiply(this.getValue("velocity", level));
         WorldUtils.addVelocity(creature, direction, true);
      }
   }
}
