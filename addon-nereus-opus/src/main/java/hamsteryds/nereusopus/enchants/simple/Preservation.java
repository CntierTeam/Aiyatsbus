package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.entity.EntityDamageEvent;

public class Preservation extends EventExecutor {
   public Preservation(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      double multiplier = this.getValue("damage-multiplier", level);
      double damage = event.getDamage();
      event.setDamage(damage * multiplier);
   }
}
