package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Animals;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Slaughter extends EventExecutor {
   public Slaughter(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Animals) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
