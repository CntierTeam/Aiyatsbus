package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Arcanic extends EventExecutor {
   public Arcanic(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      if (event.getCause() == DamageCause.POISON
         || event.getCause() == DamageCause.WITHER
         || event.getCause() == DamageCause.STARVATION
         || event.getCause() == DamageCause.DRAGON_BREATH) {
         event.setCancelled(true);
      }
   }
}
