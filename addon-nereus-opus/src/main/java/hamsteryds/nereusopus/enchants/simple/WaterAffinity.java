package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class WaterAffinity extends EventExecutor {
   public WaterAffinity(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager().isInWater()) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
