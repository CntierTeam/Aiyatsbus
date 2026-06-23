package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.entity.EntityDamageEvent;

public class Evasion extends EventExecutor {
   public Evasion(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      event.setCancelled(true);
   }
}
