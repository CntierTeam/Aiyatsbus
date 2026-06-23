package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Kinetic extends EventExecutor {
   public Kinetic(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      if (event.getCause() == DamageCause.FLY_INTO_WALL) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
