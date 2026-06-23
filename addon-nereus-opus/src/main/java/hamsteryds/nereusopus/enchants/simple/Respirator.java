package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Respirator extends EventExecutor {
   public Respirator(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      if (event.getCause() == DamageCause.DRAGON_BREATH) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
